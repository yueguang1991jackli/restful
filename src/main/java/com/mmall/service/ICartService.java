package com.mmall.service;

import com.mmall.common.ServerResponse;
import com.mmall.vo.CartVo;

import java.util.Map;

/**
 * @author liliang
 */
public interface ICartService {

    ServerResponse<CartVo> getCart(String cartJson);

    /**cookie中购物车数量进行处理,获取最新购物车信息,已map形式返回
     * @param cartJson
     * @param productId
     * @param count
     * @return
     */
    Map<String, Integer> add(String cartJson, Integer productId, Integer count);
}
