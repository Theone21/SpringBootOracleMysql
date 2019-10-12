package com.example.demo.controller;

import com.example.demo.model.RevisitInfo;
import com.example.demo.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @Autowired
    private TestService ts;

    @GetMapping("/length")
    public Long length(){
        return ts.getLength();
    }

    @GetMapping("/revisitLength")
    public Long revisitLen(){
        return ts.getReVisitLen();
    }

    @GetMapping("/init")
    public String init(){
        return "init";
    }

    @GetMapping("/get")
    public RevisitInfo get(){
        return ts.get();
    }

    @GetMapping("/twoDataBase")
    public String two(){
        return ts.two();
    }

}
