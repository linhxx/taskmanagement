package com.lin.controller;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.lin.domain.Task;
import com.lin.domain.User;
import com.lin.service.TaskService;
import com.lin.service.UserService;
import com.lin.tools.ServletUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping("/task")
public class TaskController {
    @Autowired
    private TaskService taskService;
    @Autowired
    private UserService userService;
    @Value("${lin.task.source.notcomplete}")
    private String notCompleteStatus;
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @RequestMapping("")
    public String learn(){
        return "task-list";
    }

    @RequestMapping(value = "/queryTaskList",method = RequestMethod.POST,produces="application/json;charset=UTF-8")
    @ResponseBody
    public void queryTaskList(HttpServletRequest request , HttpServletResponse response){
        String page = request.getParameter("page"); // 取得当前页数,注意这是jqgrid自身的参数
        String rows = request.getParameter("rows"); // 取得每页显示行数，,注意这是jqgrid自身的参数
        String user = request.getParameter("user");
        String task = request.getParameter("task");
        String taskStatus = request.getParameter("taskStatus");
        Map<String,Object> params = new HashMap<>();
        params.put("page", page);
        params.put("rows", rows);
        params.put("username", user);
        params.put("task", task);
        if(null != taskStatus){
            Integer taskInt = Integer.parseInt(taskStatus);
            if(-1 <= taskInt && 2 >= taskInt){
                params.put("taskStatus", taskInt);
            }
        }
        List<Task> taskList = taskService.queryTaskList(params);
        PageInfo<Task> pageInfo =new PageInfo<>(taskList);

        JSONObject jo=new JSONObject();
        jo.put("rows", taskList);
        jo.put("total", pageInfo.getPages());//总页数
        jo.put("records",pageInfo.getTotal());//查询出的总记录数
        ServletUtil.createSuccessResponse(200, jo, response);
    }

    @RequestMapping(value = "/addTask",method = RequestMethod.POST,produces="application/json;charset=UTF-8")
    @ResponseBody
    public void addTask(HttpServletRequest request , HttpServletResponse response){
        String userName = request.getParameter("username");
        String task = request.getParameter("task");
        String lastDayString = request.getParameter("lastday");
        Integer lastDay = 0;
        if(null != lastDayString && !lastDayString.isEmpty()){
            lastDay = Integer.parseInt(lastDayString);//TODO::未判断是否是整数
        }
        Map<String,Object> params = new HashMap<>();
        params.put("username", userName);
        params.put("task", task);
        params.put("lastday", lastDay);
        params.put("status", Integer.parseInt(this.notCompleteStatus));
        params.put("startdate", plusDay(0));
        params.put("enddate", plusDay(lastDay));
        JSONObject jo = this.checkParam(params);
        if(!jo.getBoolean("flag")){
            ServletUtil.createSuccessResponse(200, jo, response);
            return;
        }
        Integer resId = this.taskService.add(params);
        if(0 >= resId){
            jo.put("flag", false);
            jo.put("message", "新增任务异常");
        }else{
            jo.put("flag", true);
            jo.put("message", "新增任务成功");
        }
        ServletUtil.createSuccessResponse(200, jo, response);
    }

    @RequestMapping(value = "/updateTask",method = RequestMethod.POST,produces="application/json;charset=UTF-8")
    @ResponseBody
    public void updateTask(HttpServletRequest request , HttpServletResponse response){
        Integer id = Integer.parseInt(request.getParameter("id"));
        Integer status = Integer.parseInt(request.getParameter("status"));
        JSONObject jo = new JSONObject();
        Integer resId = this.taskService.update(id, status);
        if(0 >= resId){
            jo.put("flag", false);
            jo.put("message", "编辑任务失败");
        }else{
            jo.put("flag", true);
            jo.put("message", "编辑任务成功");
        }
        ServletUtil.createSuccessResponse(200, jo, response);
    }

    @RequestMapping(value = "/deleteTask",method = RequestMethod.POST,produces="application/json;charset=UTF-8")
    @ResponseBody
    public void deleteTask(HttpServletRequest request , HttpServletResponse response){
        String ids = request.getParameter("ids");
        JSONObject jo = new JSONObject();
        if(ids.isEmpty()){
            jo.put("flag", false);
            jo.put("message", "请至少选择一个任务");
            ServletUtil.createSuccessResponse(200, jo, response);
            return;
        }
        Set<Integer> set = this.getIds(ids);
        Integer resId = this.taskService.deleteByIds(set);
        if(0 >= resId){
            jo.put("flag", false);
            jo.put("message", "删除失败");
        }else{
            jo.put("flag", true);
            jo.put("message", "删除成功");
        }
        ServletUtil.createSuccessResponse(200, jo, response);
    }

    private Set<Integer> getIds(String ids){
        Set<Integer> set = new HashSet();
        String[] allIds = ids.split(",");
        for(String strId: allIds){
            if(!isNumeric(strId)){
                continue;
            }
            Task task = this.taskService.queryTaskById(Integer.parseInt(strId));
            if(null == task || !(task instanceof Task)){
                continue;
            }
            set.add(Integer.parseInt(strId));
        }
        return set;
    }

    private static boolean isNumeric(String str){
        for (int i = 0; i < str.length(); i++){
            if (!Character.isDigit(str.charAt(i))){
                return false;
            }
        }
        return true;
    }
    private JSONObject checkParam(Map<String,Object> params){
        JSONObject jo = new JSONObject();
        jo.put("flag", false);
        if(params.get("username").toString() == ""){
            jo.put("message", "用户名不能为空");
        }else if(params.get("task").toString() == ""){
            jo.put("message", "任务不能为空");
        }else if(0 > Integer.parseInt(params.get("status").toString())){
            jo.put("message", "任务天数应大于等于0");
        }else if(0 > Integer.parseInt(params.get("status").toString())){
            jo.put("message", "任务天数应大于等于0");
        }else{
            //TODO::判断username是否存在于user表中
            User user = this.userService.getUserByName(params.get("username").toString());
            if(null == user || !(user instanceof User)){
                jo.put("message", "用户不存在，请先到用户管理页面创建用户");
            }else {
                jo.put("flag", true);
            }
        }
        return jo;
    }
    private static String plusDay(int num){
        Date d = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String currDate = format.format(d);//当前日期
        if(0 == num){
            return currDate;
        }
        Calendar ca = Calendar.getInstance();
        ca.add(Calendar.DATE, num);// num为增加的天数，可以改变的
        d = ca.getTime();
        return format.format(d);
    }
}
