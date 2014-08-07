package com.xhm.hangzhoubike.rest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class VmDispatcher {

    @RequestMapping("/index.html")
    public String showIndex(Model model){
        model.addAttribute("param",System.currentTimeMillis());
        return "index";
    }
}
