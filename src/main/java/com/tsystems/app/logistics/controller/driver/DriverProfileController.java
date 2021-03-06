package com.tsystems.app.logistics.controller.driver;

import com.tsystems.app.logistics.service.api.DriverService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by ksenia on 18.10.2017.
 */
@Controller
@RequestMapping("/driver/profile")
public class DriverProfileController {
    private static final Logger LOG = LogManager.getLogger(DriverProfileController.class);

    private String typeOfCenterAttribute = "typeOfCenter";

    @Autowired
    private DriverService driverService;

    @RequestMapping
    public String getDriverProfile(@AuthenticationPrincipal User user, Model model) {
        LOG.trace("GET /driver/profile {}", user.getUsername());
        model.addAttribute(typeOfCenterAttribute, "driver/driver-profile.jsp");
        model.addAttribute("driverProfile", driverService.getDriverProfileByLogin(user.getUsername()));
        return "page";
    }
}
