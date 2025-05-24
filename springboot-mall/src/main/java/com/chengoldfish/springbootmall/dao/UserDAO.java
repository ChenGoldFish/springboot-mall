package com.chengoldfish.springbootmall.dao;

import com.chengoldfish.springbootmall.dto.UserRegisterRequest;
import com.chengoldfish.springbootmall.model.User;

public interface UserDAO {

    User getUserById(Integer uesrId);

    User getUserByEmail(String email);

    Integer createUser(UserRegisterRequest userRegisterRequest);
}
