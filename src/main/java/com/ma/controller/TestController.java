package com.ma.controller;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by mh on 2019/1/4.
 */
@RestController
@Slf4j
public class TestController {
    //Logger logger = LoggerFactory.getLogger(TestController.class);
    @GetMapping("/test")
    public String test(){
        log.info("有访客到达");
        //log.error("error test");
        return "test";
    }
}
