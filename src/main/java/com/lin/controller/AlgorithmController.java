package com.lin.controller;

import com.alibaba.fastjson.JSONObject;
import com.lin.service.algorithm.CalculateService;
import com.lin.service.algorithm.FindUnionService;
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
        CalculateService calculateService = new CalculateService(calculateString);
        Double res = calculateService.dealCalculate();
        jo.put("flag", true);
        jo.put("message", calculateString + "=" + res);
        ServletUtil.createSuccessResponse(200, jo, response);
    }

    @RequestMapping(value = "/findUnion",method = RequestMethod.POST,produces="application/json;charset=UTF-8")
    public void findUnion(HttpServletRequest request , HttpServletResponse response){
        Integer nodeNum = Integer.parseInt(request.getParameter("nodeNum"));
        String connectPairs = request.getParameter("connectPairs");
        JSONObject jo = new JSONObject();
        if(connectPairs.isEmpty() || 1 >= nodeNum || !connectPairs.contains("|")){
            jo.put("flag", false);
            jo.put("message", "节点要大于1个，连接内容不能是空，以|隔开");
            ServletUtil.createSuccessResponse(200, jo, response);
            return;
        }
        FindUnionService fs = new FindUnionService(nodeNum, connectPairs);
        jo.put("flag", true);
        Integer[] id = fs.getId();
        String msg = "";
        for(Integer i:id){
            msg += "节点" + i + "的父节点是-->" + id[i] + "\n";
        }
        jo.put("message", "集合数量："+fs.getCount()+"，\n每个节点的父节点："+msg);
        ServletUtil.createSuccessResponse(200, jo, response);
    }
}
