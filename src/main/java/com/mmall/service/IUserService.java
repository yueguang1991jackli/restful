package com.mmall.service;

import com.github.pagehelper.PageInfo;
import com.mmall.common.ServerResponse;
import com.mmall.common.UserList;
import com.mmall.pojo.User;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

public interface IUserService {
    ServerResponse<User> login(String username, String password)
            throws UnsupportedEncodingException, NoSuchAlgorithmException;

    ServerResponse register(User user) throws UnsupportedEncodingException, NoSuchAlgorithmException;

    ServerResponse checkValid(String str, String type);

    ServerResponse<String> forgetGetQuestion(String username);

    ServerResponse<String> forgetCheckAnswer(String username, String question, String answer);

    ServerResponse resetPassword(String username, String passwordNew, String forgotToken) throws UnsupportedEncodingException, NoSuchAlgorithmException;

    ServerResponse resetPasswordOnLogin(User user, String passwordOld, String passwordNew) throws UnsupportedEncodingException, NoSuchAlgorithmException;

    ServerResponse updateInformation(User user);

    ServerResponse<PageInfo<User>> listUsers(Integer pageSize, Integer pageNum);
}
