package com.mmall.controller.portal;

import com.mmall.common.Const;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
import com.mmall.service.ICartService;
import com.mmall.utils.CookieUtil;
import com.mmall.utils.JsonUtil;
import com.mmall.vo.CartVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * 前台购物车模块
 * 可以不用登陆,但是结算的时候必须强制登陆
 * 将购物车商品信息加入到缓存中
 * @author liliang
 */
@RestController
@RequestMapping("/cart")
public class CartController {

    @Autowired
    ICartService iCartService;

    /**
     * 获取cookie中购物车
     * @param request
     * @return
     */
    @GetMapping("/getCart.do")
    public ServerResponse<CartVo> getCartInCookie(HttpServletRequest request){
        String cartJson = CookieUtil.getUid(request, Const.CARTCOOKIENAME);
        if (cartJson == null){
            return ServerResponse.createBySuccessMessage("购物车为空");
        }
        User user = (User) request.getSession().getAttribute(Const.CURRENT_USER);
        if (user ==null){
            return iCartService.getCart(cartJson,null);
        }
        return iCartService.getCart(cartJson, user.getId());
    }


    /**
     * 添加购物车
     * @param productId
     * @param count
     * @return
     */
    @GetMapping("/add.do")
    public ServerResponse<CartVo> add(HttpServletResponse response, HttpServletRequest request, Integer productId, Integer count){
        //cookie存放map(count,productId)
        String cartJson = CookieUtil.getUid(request, Const.CARTCOOKIENAME);
        if (cartJson==null){
            cartJson="{}";
        }
        Map<String, Integer> map = iCartService.add(cartJson, productId, count);
        if (map == null){
            return ServerResponse.createByErrorMessage("参数异常");
        }else {
            String jsonString = JsonUtil.toJSONString(map);
            CookieUtil.addCookie(response,Const.CARTCOOKIENAME,jsonString,600000);
            User user = (User) request.getSession().getAttribute(Const.CURRENT_USER);
            if (user ==null){
                return iCartService.getCart(jsonString,null);
            }
            return iCartService.getCart(jsonString, user.getId());
        }
    }

}
