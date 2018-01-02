package com.mmall.dao;

import com.mmall.pojo.User;
import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface UserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(User record) throws MySQLIntegrityConstraintViolationException;

    int insertSelective(User record);

    User selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    User selectUser(@Param("username") String username, @Param("password") String password);

    Integer selectByType(@Param("str") String str,String type);

    Map<String,String> selectByUsername(String username);

    int checkAnswer(@Param("username") String username,@Param("question") String question,@Param("answer") String answer);

    int updateByUsername(@Param("username") String username, @Param("passwordNew") String passwordNew);

    List<User> selectAll();
}