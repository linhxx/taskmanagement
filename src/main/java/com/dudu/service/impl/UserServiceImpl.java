package com.dudu.service.impl;

import com.dudu.dao.UserMapper;
import com.dudu.domain.User;
import com.dudu.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService{
    @Autowired
    UserMapper userMapper;

    @Override
    public User queryUserByNameAndPwd(String username, String password) {
        return this.userMapper.queryUserByNameAndPwd(username, password);
    }

    @Override
    public List<User> queryUserList(Map<String, Object> map) {
        return this.userMapper.queryUserList(map);
    }

    @Override
    public User getUserById(Integer id) {
        return this.userMapper.getUserById(id);
    }

    @Override
    public int addUser(Integer id, String username) {
        return this.userMapper.addUser(id, username);
    }

    @Override
    public int updateUser(Integer id, String username, String password) {
        return this.userMapper.updateUser(id, username, password);
    }

    @Override
    public int deleteUser(Integer[] ids) {
        return this.userMapper.deleteUser(ids);
    }

    @Override
    public int resetUserPassword(Integer id) {
        return this.userMapper.resetUserPassword(id);
    }
}
