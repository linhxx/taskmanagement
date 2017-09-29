package com.lin.service.impl;

import com.lin.dao.TaskMapper;
import com.lin.domain.Task;
import com.lin.service.TaskService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class TaskServiceImpl implements TaskService{
    @Autowired
    TaskMapper taskMapper;
    @Override
    public int add(Map<String, Object> params) {
        return this.taskMapper.addTask(params);
    }

    @Override
    public int update(Integer id, Integer status) {
        return this.taskMapper.updateTask(id, status);
    }

    @Override
    public int deleteByIds(Set<Integer> ids) {
        return this.taskMapper.deleteTask(ids);
    }

    @Override
    public Task queryTaskById(Integer id) {
        return this.taskMapper.getTaskById(id);
    }

    @Override
    public List<Task> queryTaskList(Map<String, Object> params) {
        return this.taskMapper.queryTaskList(params);
    }
}
