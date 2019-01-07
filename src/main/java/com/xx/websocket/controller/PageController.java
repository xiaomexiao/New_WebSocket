package com.xx.websocket.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class PageController {

    @RequestMapping(value = "toindex")
    public String  toindex(){
        return "index";
    }
}
