package com.dudu.controller;

import com.dudu.domain.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** 主页
 * Created by tengj on 2017/4/10.
 */


@Controller
public class IndexController {

    @RequestMapping("/main")
    public String main(){
        return "main";
    }

    @RequestMapping("/index")
    public ModelAndView index(HttpServletRequest request, HttpServletResponse response){
        User user = (User)request.getSession().getAttribute("user");
        ModelAndView mv = new ModelAndView("/index");
        mv.addObject("username", user.getUsername());
        mv.addObject("userurl", "/setuser/"+user.getId());
        return mv;
    }
}
