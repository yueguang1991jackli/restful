package com.mmall.controller.portal;

import com.mmall.service.ICartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 前台购物车模块
 * 可以不用登陆,但是结算的时候必须强制登陆
 * @author liliang
 */
@RestController
@RequestMapping("/cart")
public class CartController {

    @Autowired
    ICartService iCartService;



}
