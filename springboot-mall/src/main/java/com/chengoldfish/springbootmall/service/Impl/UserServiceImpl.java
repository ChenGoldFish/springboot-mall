package com.chengoldfish.springbootmall.service.Impl;

import com.chengoldfish.springbootmall.dao.UserDAO;
import com.chengoldfish.springbootmall.dto.UserRegisterRequest;
import com.chengoldfish.springbootmall.model.User;
import com.chengoldfish.springbootmall.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDAO userDAO;

    @Override
    public User getUserById(Integer userId) {
        return userDAO.getUserById(userId);
    }

    @Override
    public Integer register(UserRegisterRequest userRegisterRequest) {
        return userDAO.createUser(userRegisterRequest);
    }
}
