package com.chengoldfish.springbootmall.service;

import com.chengoldfish.springbootmall.dto.UserRegisterRequest;
import com.chengoldfish.springbootmall.model.User;

public interface UserService {

    User getUserById(Integer userId);

    Integer register(UserRegisterRequest userRegisterRequest);
}
