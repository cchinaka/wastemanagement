package com.milcomsolutions.web.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
@RequestMapping("/guest/homely")
public class Home extends BaseController {

    @RequestMapping
    public String getHomePage() {
        return "home";
    }


    @RequestMapping("area")
    public @ResponseBody Map<String, Object> getAddress(@RequestParam String entityName, @RequestParam Long id) throws ClassNotFoundException {
        Map<String, Object> result = new HashMap<>();
        result.put("result", genericService.findEntityById(Class.forName(entityName), id));
        System.out.println("result: " + result);
        return result;
    }


    @RequestMapping(value = "/infomaps", method = RequestMethod.GET)
    public @ResponseBody Map<String, Object> findInfo(HttpServletRequest request) {
        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("one", request.getRequestURI());
        responseMap.put("two", request.getRequestURL());
        responseMap.put("three", request.getParameterMap());
        responseMap.put("fur", request.getQueryString());
        return responseMap;
    }
}
