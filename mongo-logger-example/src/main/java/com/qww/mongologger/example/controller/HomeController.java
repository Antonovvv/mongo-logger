package com.qww.mongologger.example.controller;

import com.qww.mongologger.core.MongoLogger;
import com.qww.mongologger.core.annotation.MLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Controller
public class HomeController {
    @Autowired
    private MongoLogger mongoLogger;

    @ResponseBody
    @RequestMapping("/custom-log")
    public String custom() {
        mongoLogger.add("custom", true);
        mongoLogger.add("test", 1);
        mongoLogger.commit("custom");
        mongoLogger.getSelf(HomeController.class).test();
        return "oh";
    }

    @MLog()
    public String test() {
        mongoLogger.add("test method", true);
        return "";
    }

    @MLog(collectionName = "param")
    @ResponseBody
    @RequestMapping("/param-log")
    public String home(HttpServletRequest request, HttpServletResponse response) {
        Map<String, String[]> params = request.getParameterMap();
        for (Map.Entry<String, String[]> param : params.entrySet()) {
            mongoLogger.add(param.getKey(), param.getValue());
        }
        return "hello";
    }

    @MLog(collectionName = "param")
    @ResponseBody
    @RequestMapping("/log")
    public String log(HttpServletRequest request, HttpServletResponse response) {
        Map<String, String[]> params = request.getParameterMap();
        for (Map.Entry<String, String[]> param : params.entrySet()) {
            mongoLogger.add(param.getKey(), param.getValue());
        }
        return "hello";
    }
}
