package com.lin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class AlgorithmController {

    @RequestMapping("/algorithm")
    public String main(){
        return "algorithm";
    }

}
