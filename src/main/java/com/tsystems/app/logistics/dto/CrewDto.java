package com.tsystems.app.logistics.dto;

import java.util.List;

/**
 * Created by ksenia on 14.10.2017.
 */
public class CrewDto {
    private Long id;
    private TruckDto truck;
    private List<DriverDto> users;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TruckDto getTruck() {
        return truck;
    }

    public void setTruck(TruckDto truck) {
        this.truck = truck;
    }

    public List<DriverDto> getUsers() {
        return users;
    }

    public void setUsers(List<DriverDto> users) {
        this.users = users;
    }
}
