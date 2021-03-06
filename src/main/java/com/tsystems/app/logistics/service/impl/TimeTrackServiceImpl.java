package com.tsystems.app.logistics.service.impl;

import com.tsystems.app.logistics.converter.TimeTrackConverter;
import com.tsystems.app.logistics.dao.impl.OrderDao;
import com.tsystems.app.logistics.dao.impl.TimeTrackDao;
import com.tsystems.app.logistics.dao.impl.UserDao;
import com.tsystems.app.logistics.dto.SuitableTruckDto;
import com.tsystems.app.logistics.dto.TimeTrackDto;
import com.tsystems.app.logistics.entity.Order;
import com.tsystems.app.logistics.entity.TimeTrack;
import com.tsystems.app.logistics.entity.User;
import com.tsystems.app.logistics.entity.enums.DriverAction;
import com.tsystems.app.logistics.service.api.OrderService;
import com.tsystems.app.logistics.service.api.PathPointService;
import com.tsystems.app.logistics.service.api.TimeTrackService;
import com.tsystems.app.logistics.service.api.TruckService;
import com.tsystems.app.logisticscommon.enums.OrderStatus;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by ksenia on 20.10.2017.
 */
@Service
@Transactional
public class TimeTrackServiceImpl implements TimeTrackService {
    private static final Logger LOG = LogManager.getLogger(TimeTrackServiceImpl.class);

    private TimeTrackDao trackDao;
    private UserDao userDao;
    private OrderDao orderDao;

    @Autowired
    private TimeTrackConverter trackConverter;
    @Autowired
    private OrderService orderService;
    @Autowired
    private PathPointService pointService;
    @Autowired
    private TruckService truckService;

    @Autowired
    public void setTrackDao(TimeTrackDao trackDao) {
        this.trackDao = trackDao;
        trackDao.setEntityClass(TimeTrack.class);
    }
    @Autowired
    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
        userDao.setEntityClass(User.class);
    }
    @Autowired
    public void setOrderDao(OrderDao orderDao) {
        this.orderDao = orderDao;
        orderDao.setEntityClass(Order.class);
    }


    @Override
    public void addNewTimeTrack(String login, TimeTrackDto trackDto) {

        User driver = userDao.getUserByLogin(login).get(0);
        Order order = orderDao.findOneById(trackDto.getOrder().getId());

        if (!order.getCrew().getTruck().getFunctioning()) {
            return;
        }
        //check if the order is new
        //if yes, change status to IN_PROCESS
        if (trackDto.getDriverAction().equals(DriverAction.START_WORKING_SHIFT)) {
            List<TimeTrack> orderTrackList = trackDao.getTracksForOrder(trackDto.getOrder().getId());
            if (orderTrackList.isEmpty()) {

                boolean hasCargoToUnload = pointService.hasCargoToUnload(order.getId());
                if (hasCargoToUnload) {
                    return;
                }

                SuitableTruckDto suitableTrucks = truckService.getSuitableTruckByOrderId(order.getId());
                if (!suitableTrucks.getIsCurrentTruckSuitable()) {
                    throw new RuntimeException("error.logical.notSuitableTruck");
                }

                LOG.debug("Order {} is new - set status 'IN_PROCESS'", order.getId());
                order.setStatus(OrderStatus.IN_PROCESS);
                order = orderDao.update(order);
                orderService.updateBoardUpdateOrder(order);
            }
        }

        if (trackDto.getDriverAction().equals(DriverAction.END_WORKING_SHIFT)) {
            boolean isFinishedOrder = orderService.isAllPointsDoneByOrder(order);
            if (!isFinishedOrder) {
                return;
            }
        }

        List<TimeTrack> trackList = trackDao.getLastDriverTrack(driver.getId());
        if (!trackList.isEmpty()) {
            TimeTrack lastDriverTrack = trackList.get(0);
            long duration = (Duration.between(lastDriverTrack.getDate().toLocalDateTime(), LocalDateTime.now())).toMillis();
            lastDriverTrack.setDuration(duration);
            LOG.debug("Set duration for last time track for driver {}", driver.getPersonalNumber());
            trackDao.update(lastDriverTrack);
        }

        TimeTrack timeTrack = new TimeTrack();
        timeTrack.setUser(driver);
        timeTrack.setDriverAction(trackDto.getDriverAction());
        timeTrack.setDate(new Timestamp(System.currentTimeMillis()));
        timeTrack.setOrder(order);
        trackDao.create(timeTrack);
    }

    @Override
    public TimeTrackDto getLastActionForOrder(String login, Long orderId) {
        List<TimeTrack> timeTrackList = trackDao.getLastActionForOrder(login, orderId);
        if (timeTrackList.isEmpty()) {
            return null;
        }
        return trackConverter.toTimeTrackDto(timeTrackList.get(0));
    }

    @Override
    public void addTimeTrackRepair(Order order, User driver) {
        TimeTrack timeTrack = new TimeTrack();
        timeTrack.setUser(driver);
        timeTrack.setDriverAction(DriverAction.START_TRUCK_REPAIRING);
        timeTrack.setDate(new Timestamp(System.currentTimeMillis()));
        timeTrack.setOrder(order);
        trackDao.create(timeTrack);
    }
}
