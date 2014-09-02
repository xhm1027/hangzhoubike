package com.xhm.hangzhoubike.rest;

import com.alibaba.pt.commons.persistence.page.Page;
import com.xhm.hangzhoubike.model.dataobject.BikeStationDO;
import com.xhm.hangzhoubike.service.BikeStationService;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/")
public class VmDispatcher {
    @Resource
    BikeStationService bikeStationService;

    @RequestMapping("/index.html")
    public String showIndex(HttpServletRequest request, Model model){
        model.addAttribute("param",System.currentTimeMillis());

        String stationId = request.getParameter("stationId");
        if(StringUtils.isBlank(stationId)){
            String page = request.getParameter("page");
            int index = 0;
            if(StringUtils.isNotBlank(page)){
                index = Integer.valueOf(page);
            }
            Page<BikeStationDO> stationPage = bikeStationService.queryBikeStationPageInDB(index,20);
            model.addAttribute("stationPage",stationPage);
            return "list";
        }else{
            return "history";
        }
    }
}
