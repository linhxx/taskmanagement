package com.lin.controller;

import com.alibaba.fastjson.JSONObject;
import com.lin.tools.ServletUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/algorithm")
public class AlgorithmController {

    @RequestMapping("")
    public String main(){
        return "algorithm";
    }

    @RequestMapping(value = "/calculate",method = RequestMethod.POST,produces="application/json;charset=UTF-8")
    public void calculate(HttpServletRequest request , HttpServletResponse response){
        String calculateString = request.getParameter("calculateString");
        JSONObject jo = new JSONObject();
        if(calculateString.isEmpty()){
            jo.put("flag", false);
            jo.put("message", "请输入+-*/()及0-9数字");
            ServletUtil.createSuccessResponse(200, jo, response);
            return;
        }

    }
}
