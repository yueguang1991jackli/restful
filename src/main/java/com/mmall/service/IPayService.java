package com.mmall.service;

import com.alipay.api.AlipayApiException;
import com.mmall.common.ServerResponse;

import java.util.Map;

/**
 * @author liliang
 */
public interface IPayService {

    ServerResponse<Map<String,String>> pay(long orderNo,String path);

    ServerResponse queryOrderPayStatus(long orderNo);

    ServerResponse checkSign(Map<String, String> paramsMap) throws AlipayApiException;
}
