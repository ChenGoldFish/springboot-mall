package com.chengoldfish.springbootmall.dao;

import com.chengoldfish.springbootmall.dto.UserRegisterRequest;
import com.chengoldfish.springbootmall.model.User;

public interface UserDAO {

    User getUserById(Integer uesrId);

    Integer createUser(UserRegisterRequest userRegisterRequest);
}
