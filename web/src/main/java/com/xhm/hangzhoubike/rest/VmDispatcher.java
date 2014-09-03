package com.xhm.hangzhoubike.rest;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.pt.commons.persistence.page.Page;
import com.xhm.hangzhoubike.model.dataobject.BikeStationDO;
import com.xhm.hangzhoubike.model.dataobject.BikeStationHistoryDO;
import com.xhm.hangzhoubike.service.BikeStationService;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/")
public class VmDispatcher {
    @Resource
    BikeStationService bikeStationService;

    @RequestMapping("/")
    public String shwoDefault(HttpServletRequest request, Model model){
        return showPage(request, model);
    }
    
    @RequestMapping("/index.html")
    public String showPage(HttpServletRequest request, Model model){
        String page = request.getParameter("page");
        int index = 0;
        if(StringUtils.isNotBlank(page)){
            index = Integer.valueOf(page);
        }
        Page<BikeStationDO> stationPage = getPage(index);
        model.addAttribute("stationPage",stationPage);
        model.addAttribute("page",index);
        return "page";
    }
    private Page<BikeStationDO> getPage(int index){
        return bikeStationService.queryBikeStationPageInDB(index,12);
    }

    @RequestMapping(value = "/page.json", method = {RequestMethod.GET},
            produces= "application/json;charset=UTF-8")
    public @ResponseBody String showPageJson(HttpServletRequest request){
        String page = request.getParameter("page");
        int index = 0;
        if(StringUtils.isNotBlank(page)){
            index = Integer.valueOf(page);
        }
        Page<BikeStationDO> stationPage = getPage(index);
        return JSONObject.toJSONString(stationPage);
    }
    

    @RequestMapping("/realTimeQuery.html")
    public String realTimeQuery(HttpServletRequest request, Model model){
        String name = request.getParameter("name");
        List<BikeStationDO> stationList = bikeStationService.queryBikeStationByName(name);
        model.addAttribute("stationList",stationList);
        return "list";
    }

    @RequestMapping(value="/realTimeQuery.json", method = {RequestMethod.GET},
            produces= "application/json;charset=UTF-8")
    public @ResponseBody String realTimeQueryJson(HttpServletRequest request){
        String name = request.getParameter("name");
        List<BikeStationDO> stationList = bikeStationService.queryBikeStationByName(name);
        return JSONObject.toJSONString(stationList);
    }

    @RequestMapping("/history.html")
    public String history(HttpServletRequest request, Model model){
        String stationId = request.getParameter("stationId");
        if(StringUtils.isBlank(stationId)){

        }
        BikeStationDO stationDO = bikeStationService.queryBikeStationByStationId(Long.valueOf(stationId));
        model.addAttribute("station",stationDO);
        BikeStationHistoryDO query = new BikeStationHistoryDO();
        Date end = new Date();
        Date start = new Date(end.getTime()-7*24*3600000l);
        query.setGmtModified(end);
        query.setGmtCreate(start);
        query.setStationId(Long.valueOf(stationId));
        List<BikeStationHistoryDO> list = bikeStationService.queryHistory(query);
        List<String> timeList = new ArrayList<String>();
        List<Integer> rentList = new ArrayList<Integer>();
        List<Integer> returnList = new ArrayList<Integer>();
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm");
        for(BikeStationHistoryDO history : list){
            timeList.add(sdf.format(history.getLogTime()));
            rentList.add(history.getCanBeRent());
            returnList.add(history.getCanBeReturn());
        }
        model.addAttribute("timeList", JSONObject.toJSONString(timeList));
        model.addAttribute("rentList", JSONObject.toJSONString(rentList));
        model.addAttribute("returnList", JSONObject.toJSONString(returnList));
        return "history";
    }

    @RequestMapping(value="/history.json", method = {RequestMethod.GET},
            produces= "application/json;charset=UTF-8")
    public @ResponseBody String historyJson(HttpServletRequest request){
        String stationId = request.getParameter("stationId");
        if(StringUtils.isBlank(stationId)){

        }
        BikeStationHistoryDO query = new BikeStationHistoryDO();
        Date end = new Date();
        Date start = new Date(end.getTime()-7*24*3600000l);
        query.setGmtModified(end);
        query.setGmtCreate(start);
        query.setStationId(Long.valueOf(stationId));
        List<BikeStationHistoryDO> list = bikeStationService.queryHistory(query);
        return JSONObject.toJSONString(list);
    }
}
