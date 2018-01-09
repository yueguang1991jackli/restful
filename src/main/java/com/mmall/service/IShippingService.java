package com.mmall.service;

import com.github.pagehelper.PageInfo;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.Shipping;

import java.util.Map;

/**
 * @author liliang
 */
public interface IShippingService{

    ServerResponse<Map<String,Integer>> createAddress(Integer userId,
                                                     String receiverName,
                                                     String  receiverPhone,
                                                     String  receiverMobile,
                                                     String receiverProvince,
                                                     String receiverCity,
                                                     String receiverAddress,
                                                     String  receiverZip);

    ServerResponse del(Integer shippingId);

    ServerResponse update(Shipping shipping);

    ServerResponse<Shipping> select(Integer shippingId);

    ServerResponse<PageInfo<Shipping>> list(Integer id, Integer pageNum, Integer pageSize);
}
