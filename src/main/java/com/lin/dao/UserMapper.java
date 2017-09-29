package com.lin.dao;

import com.lin.domain.User;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public interface UserMapper {
    User queryUserByNameAndPwd(@Param("username") String username, @Param("password") String password);
    List<User> queryUserList(Map<String, Object> map);
    User getUserById(@Param("id") Integer id);
    User getUserByName(@Param("username")String username);
    int addUser(@Param("id") Integer id, @Param("username") String username);
    int updateUser(@Param("id") Integer id, @Param("username") String username, @Param("password") String password);
    int deleteUser(@Param("ids") Integer[] ids);
    int resetUserPassword(@Param("id") Integer id);
}
