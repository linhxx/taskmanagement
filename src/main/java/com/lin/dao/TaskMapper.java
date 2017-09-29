package com.lin.dao;

import com.lin.domain.Task;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
public interface TaskMapper {
    List<Task> queryTaskList(Map<String, Object> map);
    Task getTaskById(Integer id);
    int addTask(Map<String, Object> map);
    int updateTask(@Param("id") Integer id, @Param("status") Integer status);
    int deleteTask(@Param("ids") Set<Integer> ids);
}
