package com.chengoldfish.springbootmall.service.Impl;

import com.chengoldfish.springbootmall.dao.UserDao;
import com.chengoldfish.springbootmall.dto.UserLoginRequest;
import com.chengoldfish.springbootmall.dto.UserRegisterRequest;
import com.chengoldfish.springbootmall.model.User;
import com.chengoldfish.springbootmall.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;
import org.springframework.web.server.ResponseStatusException;

@Component
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDAO;

    private final static Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Override
    public User getUserById(Integer userId) {
        return userDAO.getUserById(userId);
    }

    //註冊 命名原因:不單只是創建帳號，還包含了檢查email
    @Override
    public Integer register(UserRegisterRequest userRegisterRequest) {

        //檢查email是否被註冊過
        User user = userDAO.getUserByEmail(userRegisterRequest.getEmail());

        //如果user存在 回傳400
        if(user != null){
            logger.warn("該 email {}  已經被註冊",userRegisterRequest.getEmail());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        //使用 MD5 生成密碼的雜湊值
        String hashedPassword = DigestUtils.md5DigestAsHex(userRegisterRequest.getPassword().getBytes());
        userRegisterRequest.setPassword(hashedPassword);

        //創建帳號
        return userDAO.createUser(userRegisterRequest);
    }

    @Override
    public User login(UserLoginRequest userLoginRequest) {
        User user = userDAO.getUserByEmail(userLoginRequest.getEmail());

        //檢查user是否存在
        if(user == null){
            logger.warn("該email {} 尚未被註冊",userLoginRequest.getEmail());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        //使用 MD5 生成密碼雜湊值
        String hashedPassword = DigestUtils.md5DigestAsHex(userLoginRequest.getPassword().getBytes());

        //比較密碼
        if(user.getPassword().equals(hashedPassword)){
            return user;
        }else{
            logger.warn("email {} 的密碼不正確",userLoginRequest.getEmail());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }
}
