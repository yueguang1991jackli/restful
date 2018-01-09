package com.mmall.Interceptor;

import com.mmall.common.Const;
import com.mmall.pojo.User;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
* @author liliang
*/
public class LoginInterceptor extends HandlerInterceptorAdapter{

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//        HttpSession session = request.getSession();
//        User user = (User) session.getAttribute(Const.CURRENT_USER);
//        if (user == null){
//            response.sendError(404,"用户未登录,请登录");
//            return false;
//        }
        return true;
    }

}