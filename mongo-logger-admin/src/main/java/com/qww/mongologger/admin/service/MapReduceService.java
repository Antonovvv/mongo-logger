package com.qww.mongologger.admin.service;

import com.qww.mongologger.mapreduce.routecount.RouteCount;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class MapReduceService {
    public boolean runRouteCount() throws InterruptedException, IOException, ClassNotFoundException {
        String inputURI = "mongodb://mongologger:mongo-logger@122.51.139.75:27777/test.param?authSource=admin";
        String outputURI = "mongodb://mongologger:mongo-logger@122.51.139.75:27777/test.routeCount?authSource=admin";
        return RouteCount.run(inputURI, outputURI);
    }

}
