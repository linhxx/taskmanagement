package com.lin.service;

import com.lin.domain.User;

import java.util.List;
import java.util.Map;

public interface UserService {
    User queryUserByNameAndPwd(String username, String password);
    List<User> queryUserList(Map<String, Object> map);
    User getUserById(Integer id);
    User getUserByName(String username);
    int addUser(Integer id, String username);
    int updateUser(Integer id, String username, String password);
    int deleteUser(Integer[] ids);
    int resetUserPassword(Integer id);
}
