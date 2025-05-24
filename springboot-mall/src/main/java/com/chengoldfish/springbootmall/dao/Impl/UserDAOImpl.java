package com.chengoldfish.springbootmall.dao.Impl;

import com.chengoldfish.springbootmall.dao.UserDAO;
import com.chengoldfish.springbootmall.dto.UserRegisterRequest;
import com.chengoldfish.springbootmall.model.User;
import com.chengoldfish.springbootmall.rowmapper.UserRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class UserDAOImpl implements UserDAO {

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;
    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public User getUserById(Integer uesrId) {
        String sql = "SELECT user_id, email, password, created_date,last_modified_date FROM user WHERE user_id=:user_id";

        Map<String,Object> map = new HashMap<>();
        map.put("user_id",uesrId);

        List<User> userList = namedParameterJdbcTemplate.query(sql, map, new UserRowMapper());

        if(userList.size()>0){
            return userList.get(0);
        }else {
            return null;
        }
    }

    @Override
    public Integer createUser(UserRegisterRequest userRegisterRequest) {
       String sql="INSERT INTO user(email, password, created_date,last_modified_date) " +
               "VALUES (:email, :password, :createdDate, :lastModifiedDate)";

       Map<String,Object> map = new HashMap<>();
       map.put("email",userRegisterRequest.getEmail());
       map.put("password",userRegisterRequest.getPassword());

       Date now = new Date();
       map.put("createdDate",now);
       map.put("lastModifiedDate",now);

       KeyHolder keyHolder = new GeneratedKeyHolder();

       namedParameterJdbcTemplate.update(sql, new MapSqlParameterSource(map), keyHolder);

       int userId = keyHolder.getKey().intValue();

       return userId;
    }
}
