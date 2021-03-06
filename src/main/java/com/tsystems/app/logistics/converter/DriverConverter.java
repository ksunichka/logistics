package com.tsystems.app.logistics.converter;

import com.tsystems.app.logistics.dto.CrewDriverProfileDto;
import com.tsystems.app.logistics.dto.DriverDto;
import com.tsystems.app.logistics.dto.DriverProfileDto;
import com.tsystems.app.logistics.entity.Crew;
import com.tsystems.app.logistics.entity.Order;
import com.tsystems.app.logistics.entity.User;
import com.tsystems.app.logisticscommon.DriverInfoBoardDto;
import com.tsystems.app.logisticscommon.DriverShortDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by ksenia on 14.10.2017.
 */
@Component
public class DriverConverter {

    @Autowired
    private CityConverter cityConverter;
    @Autowired
    private CrewConverter crewConverter;

    public DriverDto toDriverDto(User user) {
        DriverDto driverDto = new DriverDto();
        driverDto.setId(user.getId());
        driverDto.setFirstName(user.getFirstName());
        driverDto.setLastName(user.getLastName());
        driverDto.setPersonalNumber(user.getPersonalNumber());
        driverDto.setLogin(user.getLogin());
        driverDto.setPassword(user.getPassword());
        driverDto.setOnOrder(user.getOnOrder());
        if (user.getCurrentCity() != null) {
            driverDto.setCurrentCity(cityConverter.toCityDto(user.getCurrentCity()));
        }
        return driverDto;
    }

    public List<DriverDto> toDriverDtoList(List<User> userList) {
        return userList
                .stream()
                .map(user ->
                        toDriverDto(user))
                .collect(Collectors.toList());
    }

    public DriverShortDto toDriverShortDto(User user) {
        DriverShortDto driverShortDto = new DriverShortDto();
        driverShortDto.setId(user.getId());
        driverShortDto.setLogin(user.getLogin());
        driverShortDto.setFirstName(user.getFirstName());
        driverShortDto.setLastName(user.getLastName());
        driverShortDto.setPersonalNumber(user.getPersonalNumber());
        return driverShortDto;
    }

    public List<DriverShortDto> toDriverShortDtoList(List<User> userList) {
        return userList
                .stream()
                .map(user ->
                        toDriverShortDto(user))
                .collect(Collectors.toList());
    }

    public DriverProfileDto orderToDriverProfileDto(String login, Order order) {
        DriverProfileDto dto = new DriverProfileDto();
        if (order.getCrew() != null) {
            Crew crew = order.getCrew();
            CrewDriverProfileDto crewDriverProfileDto = crewConverter.toCrewDriverProfileDto(crew);
            crewDriverProfileDto.getUsers().removeIf(driver -> driver.getLogin().equals(login));
            dto.setCrew(crewDriverProfileDto);
        }
        return dto;
    }

    public DriverProfileDto driverToDriverProfileDto(DriverProfileDto dto, User driver) {
        if (driver == null || dto == null) {
            return null;
        }
        dto.setId(driver.getId());
        dto.setPersonalNumber(driver.getPersonalNumber());
        dto.setFirstName(driver.getFirstName());
        dto.setLastName(driver.getLastName());
        return dto;
    }


    public DriverInfoBoardDto toDriverInfoBoardDto(User driver) {
        DriverInfoBoardDto dto = new DriverInfoBoardDto();
        dto.setId(driver.getId());
        dto.setFirstName(driver.getFirstName());
        dto.setLastName(driver.getLastName());
        dto.setPersonalNumber(driver.getPersonalNumber());
        dto.setOnOrder(driver.getOnOrder());
        dto.setCurrentCity(cityConverter.toCityDto(driver.getCurrentCity()));
        return dto;
    }

    public List<DriverInfoBoardDto> toDriverInfoBoardDtoList(List<User> drivers) {
        return drivers
                .stream()
                .map(driver ->
                        toDriverInfoBoardDto(driver))
                .collect(Collectors.toList());
    }
}
