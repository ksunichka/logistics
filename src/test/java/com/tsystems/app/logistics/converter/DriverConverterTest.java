package com.tsystems.app.logistics.converter;

import com.tsystems.app.logistics.entity.User;
import com.tsystems.app.logisticscommon.DriverShortDto;
import org.junit.Assert;
import org.junit.Test;

public class DriverConverterTest {
    @Test
    public void toDriverShortDto() throws Exception {
        User user = new User();
        user.setId(1L);
        user.setFirstName("Ivan");
        user.setLastName("Ivanov");
        user.setPersonalNumber("personal-n-1");

        DriverConverter driverConverter = new DriverConverter();
        DriverShortDto driverShortDto = driverConverter.toDriverShortDto(user);

        Assert.assertEquals(driverShortDto.getId(), user.getId());
        Assert.assertEquals(driverShortDto.getFirstName(), user.getFirstName());
        Assert.assertEquals(driverShortDto.getLastName(), user.getLastName());
        Assert.assertEquals(driverShortDto.getPersonalNumber(), user.getPersonalNumber());
    }

}