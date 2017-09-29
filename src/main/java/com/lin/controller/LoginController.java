package com.lin.controller;

import com.alibaba.fastjson.JSONObject;
import com.lin.domain.User;
import com.lin.service.UserService;
import com.lin.tools.ServletUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;


@Controller
public class LoginController {
    @Autowired
    private UserService userService;
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @RequestMapping(value = "/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response){
        request.getSession().removeAttribute("user");
        return "/toLogin";
    }

    @RequestMapping(value = "/login",method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> login(HttpServletRequest request, HttpServletResponse response){
        Map<String,Object> map =new HashMap<String,Object>();
        String username=request.getParameter("username");
        String password=request.getParameter("password");
        if(!username.equals("") && password!=""){
            User user = userService.queryUserByNameAndPwd(username, password);
            if(user != null){
                request.getSession().setAttribute("user",user);
                map.put("result","1");
            }
            else{
                map.put("result","2");
            }
        }else{
            map.put("result","0");
        }
        return map;
    }

    @RequestMapping(value = "/setuser/{id}", method = RequestMethod.GET)
    public ModelAndView setUser(@PathVariable("id") Integer id, HttpServletResponse response){
        User user = userService.getUserById(id);
        ModelAndView mv = new ModelAndView("/userinfo");
        if(null != user){
            mv.addObject("username", user.getUsername());
            mv.addObject("id", user.getId());
        }else{
            mv.addObject("username", "获取用户失败");
            mv.addObject("id", "-1");
        }
        mv.addObject("hidden", true);
        return mv;
    }

    @ResponseBody
    @RequestMapping(value = "/checkPwd", method = RequestMethod.POST)
    public void checkUserPassword(HttpServletRequest request , HttpServletResponse response){
        String oldPwd = request.getParameter("oldPwd");
        JSONObject result = new JSONObject();
        User user = (User)request.getSession().getAttribute("user");
        if(user.getPassword().equals(oldPwd)){
            result.put("validMsg", true);
        }else{
            result.put("validMsg", false);
        }
        ServletUtil.createSuccessResponse(200, result, response);
    }

    @ResponseBody
    @RequestMapping(value = "/resetUserInfo", method = RequestMethod.POST)
    public void resetUserInfo(HttpServletRequest request , HttpServletResponse response){
        JSONObject result = new JSONObject();
        String newUsername = request.getParameter("newUsername");
        if(newUsername.isEmpty()){
            result.put("msg", 0);//操作失败
            ServletUtil.createSuccessResponse(200, result, response);
            return;
        }
        String newPwd = request.getParameter("newPwd");
        User user = (User)request.getSession().getAttribute("user");
        Integer id = user.getId();
        if(newPwd.isEmpty()){
            //不重置密码的情况
            result = this.updateUserInfo(id, newUsername, "");
            user.setUsername(newUsername);
            request.getSession().removeAttribute("user");
            request.getSession().setAttribute("user", user);
        }else{
            //重置密码的情况
            result = this.updateUserInfo(id, newUsername, newPwd);
            request.getSession().removeAttribute("user");
        }
        ServletUtil.createSuccessResponse(200, result, response);
    }

    private JSONObject updateUserInfo(Integer id, String newUsername, String newPwd){
        Integer resId = this.userService.updateUser(id, newUsername, newPwd);
        JSONObject result = new JSONObject();
        if(0 >= resId){
            result.put("msg", 0);//操作失败
        }else if(!newPwd.isEmpty()){
            result.put("msg", 2);//修改名字和密码
        }else{
            result.put("msg", 1);//仅修改名字
        }
        return result;
    }
}
