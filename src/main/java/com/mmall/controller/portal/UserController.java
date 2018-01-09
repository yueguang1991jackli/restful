package com.mmall.controller.portal;

import com.mmall.common.Const;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
import com.mmall.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

/**
 * 前台用户模块
 * @author liliang
 */
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private IUserService iUserService;

    /**
     * 用户登陆
     *
     * @param username
     * @param password
     * @return
     * @throws UnsupportedEncodingException
     * @throws NoSuchAlgorithmException
     */
    @PostMapping(value = "/login.do")
    public ServerResponse<User> login(String username, String password, HttpServletRequest request) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        ServerResponse<User> response = iUserService.login(username, password);
        request.getSession().setAttribute(Const.CURRENT_USER, response.getData());
        return response;
    }

    /**
     * 用户退出
     *
     * @param session
     * @return
     */
    @GetMapping(value = "/logout.do")
    public ServerResponse logout(HttpSession session) {
        session.removeAttribute(Const.CURRENT_USER);
        if (session.getAttribute(Const.CURRENT_USER) == null) {
            return ServerResponse.createBySuccessMessage("退出成功");
        }
        return ServerResponse.createByErrorMessage("服务器异常");
    }

    /**
     * 用户注册
     *
     * @param user
     * @return
     * @throws UnsupportedEncodingException
     * @throws NoSuchAlgorithmException
     */
    @PostMapping("/register.do")
    public ServerResponse register(User user) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        return iUserService.register(user);
    }

    /**
     * 用户数据校验
     *
     * @param str
     * @param type
     * @return
     */
    @PostMapping("/check_valid.do")
    public ServerResponse checkValid(String str, String type) {
        return iUserService.checkValid(str, type);
    }

    /**
     * 获取用户信息
     *
     * @param request
     * @return
     */
    @GetMapping("/get_user_info.do")
    public ServerResponse<User> getUserInfo(HttpServletRequest request) {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorMessage("用户未登录,无法获取当前用户信息");
        }
        return ServerResponse.createBySuccess(user);
    }

    /**
     * 忘记密码
     *
     * @param username
     * @return
     */
    @PostMapping("/forget_get_question.do")
    public ServerResponse<String> forgetGetQuestion(String username) {
        return iUserService.forgetGetQuestion(username);
    }

    /**
     * 校验密码
     * 需要生成一个token用来保证验证校验密码操作与修改密码操作连贯性
     * 此token保存到redis之类缓存中，并设置过期时间
     * 与验证码的有效性相识
     *
     * @param username
     * @param question
     * @param answer
     * @return
     */
    @PostMapping("/forget_check_answer.do")
    public ServerResponse<String> forgetCheckAnswer(String username, String question, String answer) {
        return iUserService.forgetCheckAnswer(username, question, answer);
    }

    /**
     * 重设密码
     * @param username
     * @param passwordNew
     * @param forgetToken
     * @return
     */
    @PostMapping("/forget_reset_password.do")
    public ServerResponse resetPassword(String username,String passwordNew,String forgetToken) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        return iUserService.resetPassword(username, passwordNew, forgetToken);
    }

    /**
     * 登录状态更新密码
     * @param passwordOld
     * @param passwordNew
     * @param session
     * @return
     */
    @PostMapping("/reset_password.do")
    public ServerResponse resetPasswordOnLogin(String passwordOld,String passwordNew,HttpSession session) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        //判断用户是否登录
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        //未登录,返回未登录
        if (user == null) {
            return ServerResponse.createByErrorMessage("用户未登录");
        }
        //已登录,执行更新操作
        return iUserService.resetPasswordOnLogin(user,passwordOld, passwordNew);
    }

    /**
     * 登录中更新用户信息
     * @param user
     * @param session
     * @return
     */
    @PostMapping("/update_information.do")
    public ServerResponse updateInformation(User user, HttpSession session){
        //判断用户是否登录
        User currentUser = (User) session.getAttribute(Const.CURRENT_USER);
        //未登录,返回未登录
        if (currentUser == null) {
            return ServerResponse.createByErrorMessage("用户未登录");
        }else {
            //更新用户信息
            user.setId(currentUser.getId());
            return iUserService.updateInformation(user);
        }
    }

    /**
     * 获取当前登录用户的详细信息，并强制登录
     * @param session
     * @return
     */
    @PostMapping("/user/get_information.do")
    public ServerResponse<User> getInformation(HttpSession session){
        //判断用户是否登录
        User currentUser = (User) session.getAttribute(Const.CURRENT_USER);
        //未登录,返回未登录
        if (currentUser == null) {
            return ServerResponse.createByErrorMessage("用户未登录");
        }else {
            //获取用户信息
            return ServerResponse.createBySuccess(currentUser);
        }
    }
}
