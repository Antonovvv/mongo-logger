package com.qww.mongologger.admin.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.qww.mongologger.admin.entity.DBInfo;
import com.qww.mongologger.admin.entity.QueryLogParam;
import com.qww.mongologger.admin.service.MapReduceService;
import com.qww.mongologger.admin.service.QueryService;
import com.qww.mongologger.admin.utils.TableResult;
import com.qww.mongologger.core.entity.WebLog;
import com.qww.mongologger.mapreduce.document.RouteCountDocument;
import com.qww.mongologger.mapreduce.document.RouteTimelineDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class APIController {
    @Autowired
    MapReduceService mapReduceService;
    @Autowired
    QueryService queryService;

    @RequestMapping("/count")
    public String count() throws Exception {
        mapReduceService.runRouteCount();
        return "done";
    }

    @RequestMapping("/timeline")
    public String timeline() throws Exception {
        mapReduceService.runRouteTimeline();
        return "done";
    }

    @RequestMapping("/admin-api/queryLog")
    public TableResult<WebLog> queryLog(@ModelAttribute QueryLogParam queryLogParam) throws JsonProcessingException {
        System.out.println(queryLogParam);
        return queryService.queryLog(queryLogParam);
    }

    @RequestMapping("/admin-api/defaultDB")
    public DBInfo defaultDB() {
        return new DBInfo("122.51.139.75:27777", "test", "param");
    }

    @RequestMapping("/admin-api/analysis/count")
    public TableResult<RouteCountDocument> getRouteCount() throws JsonProcessingException {
        return mapReduceService.getRouteCount();
    }

    @RequestMapping("admin-api/analysis/timeline")
    public TableResult<RouteTimelineDocument> getRouteTimeline() throws JsonProcessingException {
        return mapReduceService.getRouteTimeline();
    }
}
