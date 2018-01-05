package com.mmall.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mmall.common.Const;
import com.mmall.common.ServerResponse;
import com.mmall.common.TokenCache;
import com.mmall.dao.UserMapper;
import com.mmall.pojo.User;
import com.mmall.service.IUserService;
import com.mmall.utils.Md5Encode;
import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.sql.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author liliang
 */
@Service(value = "iUserService")
@Scope("")
public class UserServiceImpl implements IUserService {
    @Autowired
    private UserMapper userMapper;

    /**
     * 用户登陆
     * @param username
     * @param password
     * @return
     * @throws UnsupportedEncodingException
     * @throws NoSuchAlgorithmException
     */
    @Override
    public ServerResponse<User> login(String username, String password)
            throws UnsupportedEncodingException, NoSuchAlgorithmException {

        String decodePassword = Md5Encode.encodeByMd5(password);
        User user = userMapper.selectUser(username, decodePassword);
        if (null == user) {
            return ServerResponse.createByErrorMessage("密码错误");
        }
        return ServerResponse.createBySuccess(user);
    }

    /**
     * 注册用户
     *
     * @param user
     * @return
     * @throws UnsupportedEncodingException
     * @throws NoSuchAlgorithmException
     */
    @Override
    public ServerResponse register(User user) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        String encodePassword = Md5Encode.encodeByMd5(user.getPassword());
        user.setPassword(encodePassword);
        user.setRole(Const.Role.ROLE_CUSTOMER);
        user.setCreateTime(new Date(System.currentTimeMillis()));
        user.setUpdateTime(new Date(System.currentTimeMillis()));
        try {
            int insert = userMapper.insert(user);
            if (insert == 1) {
                return ServerResponse.createBySuccessMessage("校验成功");
            }
            return ServerResponse.createByErrorMessage("用户已存在");
            //todo 未完成异常情况代码
        } catch (MySQLIntegrityConstraintViolationException mysql) {
            mysql.printStackTrace();
            return ServerResponse.createByErrorMessage("用户已存在");
        }
    }

    /**
     * 用户数据校验
     * @param str
     * @param type
     * @return
     */
    @Override
    public ServerResponse checkValid(String str, String type) {
        Integer i = userMapper.selectByType(str, type);
        if (i == null) {
            return ServerResponse.createBySuccessMessage("校验成功");

        }
        return ServerResponse.createByErrorMessage("用户已存在");
    }

    /**
     * 忘记密码
     * @param username
     * @return
     */
    @Override
    public ServerResponse<String> forgetGetQuestion(String username) {
        Map<String, String> map = userMapper.selectByUsername(username);
        //判断是否为空
        if (null == username) {
            return ServerResponse.createByErrorMessage("用户名为空，请重新输入");
        } else if (null == map) {
            return ServerResponse.createByErrorMessage("用户名不存在");
        }
        return ServerResponse.createBySuccess(map.get("question"));
    }

    /**
     * 用户密码判断
     * @param username
     * @param question
     * @param answer
     * @return
     */
    @Override
    public ServerResponse<String> forgetCheckAnswer(String username, String question, String answer) {
        //判断答案是否正确
        int resultCount = userMapper.checkAnswer(username, question, answer);
        if (resultCount > 0) {
            //生成token
            String uuid = UUID.randomUUID().toString();
            TokenCache.setKey(TokenCache.TOKEN_PREFIX + username, uuid);
            return ServerResponse.createBySuccess(uuid);
            //返回
        }
        return ServerResponse.createByErrorMessage("问题答案错误");
    }

    /**
     * 重设密码
     * @param username
     * @param passwordNew
     * @param forgetToken
     * @return
     */
    @Override
    public ServerResponse resetPassword(String username, String passwordNew, String forgetToken) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        String token = TokenCache.getKey(TokenCache.TOKEN_PREFIX + username);
        //判断token是否正确
        if (forgetToken.equals(token)){
            //正确,根据用户更新用户密码
            String passwordNewEncode = Md5Encode.encodeByMd5(passwordNew);
            int result = userMapper.updateByUsername(username, passwordNewEncode);
            if (result == 1){
                //成功,返回更新成功
                return ServerResponse.createBySuccessMessage("更新成功");
            }
                //失败,返回更新失败
            return ServerResponse.createByErrorMessage("更新失败");
        }
        //不正确,返回token错误
        return ServerResponse.createByErrorMessage("token错误");
    }

    /**
     * 登录中的更新密码
     *
     * @param user
     * @param passwordOld
     * @param passwordNew
     * @return
     */
    @Override
    public ServerResponse resetPasswordOnLogin(User user, String passwordOld, String passwordNew) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        //判断旧密码是否正确
        String passwordOldEncode = Md5Encode.encodeByMd5(passwordOld);
        User user1 = userMapper.selectByPrimaryKey(user.getId());
        if (user1.getPassword().equals(passwordOldEncode)){
            //是,更新密码
            String passwordNewEncode = Md5Encode.encodeByMd5(passwordNew);
            int result = userMapper.updateByUsername(user.getUsername(), passwordNewEncode);
            //更新成功,返回更新成功
            if(result ==1 ){
                return ServerResponse.createBySuccessMessage("更新成功");
            }
            //更新失败,返回更新失败
            return ServerResponse.createByErrorCodeMessage(1, "更新失败");
        }
        //否,返回旧的密码错误
        return ServerResponse.createByErrorCodeMessage(1, "旧密码输入错误");
    }

    @Override
    public ServerResponse updateInformation(User user) {
        int result = userMapper.updateByPrimaryKeySelective(user);
        if (result == 1){
            return ServerResponse.createBySuccessMessage("更新成功");
        }
        return ServerResponse.createByErrorCodeMessage(1,"更新失败");
    }

    @Override
    public ServerResponse<PageInfo<User>> listUsers(Integer pageSize, Integer pageNum) {
        //pageHelper将所有用户进行分页
        PageHelper.startPage(1, 10);
        List<User> list = userMapper.selectAll();
        PageInfo<User> page = new PageInfo<>(list);
        //返回分页信息
        return ServerResponse.createBySuccess(page);
    }


}
