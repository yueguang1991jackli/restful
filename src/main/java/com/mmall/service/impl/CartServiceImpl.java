package com.mmall.service.impl;

import com.mmall.common.Const;
import com.mmall.common.ServerResponse;
import com.mmall.dao.CartMapper;
import com.mmall.dao.ProductMapper;
import com.mmall.pojo.Product;
import com.mmall.service.ICartService;
import com.mmall.utils.JsonUtil;
import com.mmall.vo.CartVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author liliang
 */
@Service("iCartService")
public class CartServiceImpl implements ICartService {
    @Autowired
    ProductMapper productMapper;
    @Autowired
    CartMapper cartMapper;
    @Override
    public ServerResponse<CartVo> getCart(String cartJson) {
        //转换为map对象
        Map<String,Integer> map = new HashMap<>(16);
        Map<String,Integer> parseObject = JsonUtil.parseObject(cartJson, map.getClass());
        List<CartVo.CartProductVo> productList = new LinkedList<>();
        //遍历map中key
        int i =1;
        BigDecimal totalPrice = new BigDecimal("0");
        for (String key: parseObject.keySet()) {
            //查询对应商品的信息
            Product product = productMapper.selectByProductId(Integer.parseInt(key));
            CartVo.CartProductVo cartProductVo = new CartVo.CartProductVo();
            cartProductVo.setId(i);i++;
            cartProductVo.setProductId(product.getId());
            cartProductVo.setQuantity(parseObject.get(key));
            cartProductVo.setProductName(product.getName());
            cartProductVo.setProductSubtitle(product.getSubtitle());
            cartProductVo.setProductMainImage(product.getMainImage());
            cartProductVo.setProductPrice(product.getPrice());
            cartProductVo.setProductStatus(product.getStatus());
            BigDecimal total = new BigDecimal(parseObject.get(key).toString());
            totalPrice = total.multiply(product.getPrice()).add(totalPrice);
            cartProductVo.setProductTotalPrice(product.getPrice().multiply(total));
            cartProductVo.setProductStock(product.getStock());
            cartProductVo.setProductChecked(Const.Cart.CHECKED);
            if (parseObject.get(key)>product.getStock()){
                return ServerResponse.createByErrorMessage("cookie异常");
            }cartProductVo.setLimitQuantity(Const.Cart.LIMIT_NUM_SUCCESS);
            productList.add(cartProductVo);
        }
        //将product赋值到cartVo中的内部类中,返回cartVo
        CartVo cartVo = new CartVo();
        cartVo.setAllChecked(true);
        cartVo.setCartProductVoList(productList);
        cartVo.setCartTotalPrice(totalPrice);
        //包装返回
        return ServerResponse.createBySuccess(cartVo);
    }

    @Override
    public Map<String, Integer> add(String cartJson, Integer productId, Integer count) {
        //获取对用productId商品的已存在数量,并相加
        Map<String,Integer> map = new HashMap<>(16);
        Map<String,Integer> parseObject = JsonUtil.parseObject(cartJson, map.getClass());
        //购物车中已存在商品
        if (parseObject != null) {
            Integer hasCount = parseObject.get(productId.toString());
            count=count+hasCount;
            Product product = productMapper.selectByProductId(productId);
            // 判断相加后的数量小于库存
            if (count<=product.getStock()){
                //放到map中,并解析为json string
                map.put(productId.toString(),count);
                return map;
            }
            return null;
        }
        //购物车中不存在商品
        map.put(productId.toString(),count);
        return map;
    }
}
