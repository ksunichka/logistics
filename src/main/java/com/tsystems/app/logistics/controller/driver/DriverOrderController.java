package com.tsystems.app.logistics.controller.driver;

import com.tsystems.app.logistics.dto.OrderInfoDto;
import com.tsystems.app.logistics.dto.TimeTrackDto;
import com.tsystems.app.logistics.service.api.OrderService;
import com.tsystems.app.logistics.service.api.PathPointService;
import com.tsystems.app.logistics.service.api.TimeTrackService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;

/**
 * Created by ksenia on 18.10.2017.
 */
@Controller
@RequestMapping({"/driver", "/driver/order"})
public class DriverOrderController {
    private static final Logger LOG = LogManager.getLogger(DriverOrderController.class);

    private String typeOfCenterAttribute = "typeOfCenter";

    @Autowired
    private OrderService orderService;
    @Autowired
    private PathPointService pointService;
    @Autowired
    private TimeTrackService trackService;

    @RequestMapping
    public String getDriverOrder(@AuthenticationPrincipal User user, Model model) {
        LOG.trace("GET /driver/order");
        model.addAttribute(typeOfCenterAttribute, "driver/driver-order.jsp");
        OrderInfoDto currentOrder = orderService.getCurrentOrderByDriverLogin(user.getUsername(), OrderInfoDto.class);
        if (currentOrder != null) {
            model.addAttribute("scheduleIsNotCompleted", pointService.hasCargoToUnload(currentOrder.getId()));
            model.addAttribute("allPointsDone", orderService.isAllPointsDoneByOrderId(currentOrder.getId()));
            model.addAttribute("currentOrder", currentOrder);
            model.addAttribute("lastAction", trackService.getLastActionForOrder(user.getUsername(), currentOrder.getId()));
        }

        return "page";
    }

    @RequestMapping(value = "/close-point/{pathPointId}", method = RequestMethod.GET)
    public String closePathPoint(@PathVariable(value = "pathPointId") Long pathPointId, Model model) {
        LOG.trace("GET /driver/close-point/{}", pathPointId);
        pointService.closePathPoint(pathPointId);
        return "redirect:/driver/order";
    }

    @RequestMapping(value = "/add-action", method = RequestMethod.POST)
    public String addDriverAction(@AuthenticationPrincipal User user, @Valid TimeTrackDto trackDto, Model model) {
        LOG.trace("POST /driver/add-action {}", trackDto.getDriverAction().name());

        try {
            trackService.addNewTimeTrack(user.getUsername(), trackDto);
        } catch (Exception e) {
            LOG.trace("Current truck is not suitable. Cannot start order {}.  {}", trackDto.getOrder().getId(), e.getMessage());
            return "redirect:/driver/order?error=" + e.getMessage();
        }

        return "redirect:/driver/order";
    }
}
