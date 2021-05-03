package com.qww.mongologger.admin.controller;

import com.qww.mongologger.admin.service.MapReduceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class APIController {
    @Autowired
    MapReduceService mapReduceService;

    @ResponseBody
    @RequestMapping("/")
    public String test() throws Exception {
        mapReduceService.runRouteCount();
        return "done";
    }
}
