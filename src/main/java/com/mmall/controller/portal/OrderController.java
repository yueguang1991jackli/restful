package com.mmall.controller.portal;

import com.mmall.common.ServerResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author liliang
 */
@RestController
@RequestMapping("/order")
public class OrderController {
    /**
     * 创建订单
     * @param shippingId
     * @return
     */
    @GetMapping("/create.do")
    public ServerResponse createOrder(Integer shippingId){
        return null;
    }
}
