package com.xhm.hangzhoubike.rest;

import com.alibaba.fastjson.JSON;
import com.xhm.hangzhoubike.service.BikeStationService;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RequestMapping
@Component("dispatcher")
public class Dispatcher {

    Logger logger = Logger.getLogger("webLogger");
    @Resource
    BikeStationService bikeStationService;

    @PostConstruct
    public void init() {

    }
    
    @RequestMapping(value = "/index.htm", method = RequestMethod.GET)
    public @ResponseBody
    String index(HttpServletRequest request, HttpServletResponse response) {
        return bikeStationService.hello();
    }


    @RequestMapping(value = "/queryBikeStationByLocation.json",
            method = {RequestMethod.GET},
            produces= "application/json;charset=UTF-8")
    public @ResponseBody
    String queryBikeStationByLocation(HttpServletRequest request, HttpServletResponse response) {
        String range = request.getParameter("range");
        String x = request.getParameter("x");
        String y = request.getParameter("y");
        logger.info("query by location:range="+range+",x="+x+"y="+y);
        return JSON.toJSONString(bikeStationService.queryBikeStationByLocation(range,x,y));
    }

    @RequestMapping(value = "/queryBikeStationByName.json",
            method = {RequestMethod.GET},
            produces= "application/json;charset=UTF-8")
    public @ResponseBody
    String queryBikeStationByName(HttpServletRequest request, HttpServletResponse response) {
        String name = request.getParameter("name");
        logger.info("query by name:name="+name);
        return JSON.toJSONString(bikeStationService.queryBikeStationByName(name));
    }
}
