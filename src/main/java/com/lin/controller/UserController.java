package com.lin.controller;

import com.alibaba.fastjson.JSONObject;
import com.lin.domain.User;
import com.lin.service.UserService;
import com.lin.tools.ServletUtil;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @RequestMapping("")
    public String User(){
        return "user-list";
    }

    /**
     * 根据用户名获取用户列表，用户名空时返回整个列表
     * @param request
     * @param response
     */
    @ResponseBody
    @RequestMapping(value = "/queryUserList", method = RequestMethod.POST, produces="application/json;charset=UTF-8")
    public void queryUserList(HttpServletRequest request , HttpServletResponse response){
        String page = request.getParameter("page");
        String rows = request.getParameter("rows");
        String username = request.getParameter("username");
        Map<String, Object> map = new HashMap<>();
        map.put("page", page);
        map.put("rows", rows);
        map.put("username", username);
        List<User> userList = userService.queryUserList(map);
        PageInfo<User> pageInfo = new PageInfo<>(userList);
        JSONObject jo=new JSONObject();
        jo.put("rows", userList);
        jo.put("total", pageInfo.getPages());//总页数
        jo.put("records",pageInfo.getTotal());//查询出的总记录数
        ServletUtil.createSuccessResponse(200, jo, response);
    }

    /**
     * 新增用户 其中用户名不能重复也不能空，密码默认和用户名一样
     * @param request
     * @param response
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public void addUser(HttpServletRequest request , HttpServletResponse response){
        String username = request.getParameter("username");
        JSONObject result = this.checkUsernameExist(username, 0);
        if(!result.getBoolean("flag")){
            ServletUtil.createSuccessResponse(200, result, response);
            return;
        }
        Integer newId = 1;
        Map<String, Object> mapForId = new HashMap<>();
        List<User> userListForId = userService.queryUserList(mapForId);
        if (null != userListForId && userListForId.size() > 0) {
            User latestUser = userListForId.get(0);
            newId = latestUser.getId() + 1;
        }
        Integer resId = userService.addUser(newId, username);
        if(resId > 0){
            result.put("message","用户添加成功!");
            result.put("flag",true);
        }else{
            result.put("message","用户添加失败!");
            result.put("flag",false);
        }
        ServletUtil.createSuccessResponse(200, result, response);
    }

    /**
     * 根据id来修改用户名，用户名不能空，不能重复
     * @param request
     * @param response
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public void updateUser(HttpServletRequest request, HttpServletResponse response){
        Integer id = Integer.parseInt(request.getParameter("id"));
        String username = request.getParameter("username");
        JSONObject result = this.checkUsernameExist(username, id);
        if(!result.getBoolean("flag")){
            ServletUtil.createSuccessResponse(200, result, response);
            return;
        }
        Integer resId = userService.updateUser(id, username, "");//不允许修改密码
        if(resId > 0){
            result.put("message","用户名修改成功!");
            result.put("flag",true);
        }else{
            result.put("message","用户名修改失败!");
            result.put("flag",false);
        }
        ServletUtil.createSuccessResponse(200, result, response);
    }

    /**
     * 删除用户
     * @param request
     * @param response
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public void deleteUser(HttpServletRequest request, HttpServletResponse response){
        JSONObject result = new JSONObject();
        String[] stringIds = request.getParameter("ids").split(",");
        Integer[] ids = new Integer[stringIds.length];
        int i = 0;
        for (String stringId: stringIds){
            Integer id = Integer.parseInt(stringId);
            User user = this.getUserById(id);
            if(null != user){
                ids[i++] = id;
            }
        }
        if(0 >= ids.length){
            result.put("message","没有有效id!");
            result.put("flag",false);
            ServletUtil.createSuccessResponse(200, result, response);
            return;
        }
        Integer resId = this.userService.deleteUser(ids);
        if(resId > 0){
            result.put("message","删除用户成功!");
            result.put("flag",true);
        }else{
            result.put("message","删除用户失败!");
            result.put("flag",false);
        }
        ServletUtil.createSuccessResponse(200, result, response);
    }

    @ResponseBody
    @RequestMapping(value = "/resetPassword", method = RequestMethod.POST)
    public void resetPassword(HttpServletRequest request, HttpServletResponse response){
        JSONObject result=new JSONObject();
        Integer id = Integer.parseInt(request.getParameter("id"));
        if(0 >= id){
            result.put("isSuccess",false);
            ServletUtil.createSuccessResponse(200, result, response);
            return;
        }
        Integer resId = this.userService.resetUserPassword(id);
        if(resId > 0){
            result.put("isSuccess",true);
            User user = (User)request.getSession().getAttribute("user");
            Integer userId = user.getId();
            if(userId == id) {
                //重置自己的密码
                request.getSession().removeAttribute("user");
                result.put("isSelf",true);
            }else{
                //重置其他人的密码
                result.put("isSelf",false);
            }
        }else{
            result.put("isSuccess",false);
        }
        ServletUtil.createSuccessResponse(200, result, response);
    }

    private JSONObject checkUsernameExist(String username, Integer id){
        JSONObject result=new JSONObject();
        if(username.isEmpty()){
            result.put("message","用户名不能为空!");
            result.put("flag",false);
            return result;
        }
        List<User> userList = this.getUserByName(username);
        if(this.checkRealDuplicated(userList, id)){
            result.put("message","用户名已存在，请更换用户名!");
            result.put("flag",false);
            return result;
        }else {
            result.put("flag", true);
            return result;
        }
    }

    private Boolean checkRealDuplicated(List<User> userList, Integer id){
        if(0 == id && !userList.isEmpty()){
            return true;
        }else if(id > 0 && !userList.isEmpty() && id != userList.get(0).getId()){
            return true;
        }else{
            return false;
        }
    }

    private List<User> getUserByName(String username){
        Map<String, Object> map = new HashMap<>();
        map.put("username", username);
        List<User> userList = userService.queryUserList(map);
        return userList;
    }

    private User getUserById(Integer id){
        return this.userService.getUserById(id);
    }

}
