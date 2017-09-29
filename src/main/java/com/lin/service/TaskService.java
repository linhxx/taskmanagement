package com.lin.service;

import com.lin.domain.Task;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface TaskService {
    int add(Map<String, Object> params);
    int update(Integer id, Integer status);
    int deleteByIds(Set<Integer> ids);
    Task queryTaskById(Integer id);
    List<Task> queryTaskList(Map<String, Object> params);
}
