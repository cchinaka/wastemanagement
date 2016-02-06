package com.milcomsolutions.wastemanagement.controller;

import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
@RequestMapping("/dash")
public class WasteDashboardController {

    @Autowired
    HttpServletRequest request;

    private static final Log LOG = LogFactory.getLog(WasteDashboardController.class);


    @RequestMapping({ "/index", "" })
    public String home() throws Exception {
        return "receiver-dash";
    }


    @RequestMapping("system-numbers")
    public @ResponseBody ModelMap getSystemNumbers() {
        LOG.info("loading system numbers...");
        ModelMap modelMap = new ModelMap();
        modelMap.addAttribute("pending", 115);
        modelMap.addAttribute("approved", 300);
        modelMap.addAttribute("errors", 85);
        modelMap.addAttribute("services", 2030);
        return modelMap;
    }


    @RequestMapping("pending-transactions")
    public @ResponseBody ModelMap getPendingTrxns() {
        ModelMap modelMap = new ModelMap();
        modelMap.addAttribute("data", Collections.singletonList("Chuka"));
        return modelMap;
    }


    @RequestMapping("notifications")
    public @ResponseBody List<String> getNotifications() {
        return Collections.singletonList("Nothing Happening in the capital");
    }
}
