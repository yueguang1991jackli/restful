package com.mmall.controller.manage;

import com.github.pagehelper.PageInfo;
import com.mmall.common.Const;
import com.mmall.common.ServerResponse;
import com.mmall.controller.portal.UserController;
import com.mmall.pojo.User;
import com.mmall.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

/**
 * 后台用户管理
 * @author liliang
 */
@RestController
@RequestMapping("/manage/user")
public class ManageUserController {

    @Autowired
    private IUserService iUserService;

    @PostMapping("/login.do")
    public ServerResponse<User> login(String username, String password, HttpServletRequest request)
            throws UnsupportedEncodingException, NoSuchAlgorithmException {
        ServerResponse<User> response = iUserService.login(username, password);
        request.getSession().setAttribute(Const.CURRENT_USER, response.getData());
        return response;
    }

    @GetMapping("/list.do")
    public ServerResponse<PageInfo<User>> listUsers(@RequestParam(value = "pageSize",defaultValue = "10") Integer pageSize, @RequestParam(value = "pageNum",defaultValue = "1") Integer pageNum){
        return iUserService.listUsers(pageSize, pageNum);
    }
}
