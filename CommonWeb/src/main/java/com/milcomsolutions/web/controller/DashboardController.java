package com.milcomsolutions.web.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping({ "/", "/admin/dashboard", "/user/dashboard" })
public class DashboardController extends BaseController {

    @Autowired
    HttpServletRequest request;

    private static final Log LOG = LogFactory.getLog(DashboardController.class);


    @RequestMapping({ "/home", "" })
    public String home() throws Exception {
        LOG.info("go home...");
        return "home-page";
    }
}
