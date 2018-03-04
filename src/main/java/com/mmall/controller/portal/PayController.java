package com.mmall.controller.portal;

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.google.common.collect.Maps;
import com.mmall.common.ServerResponse;
import com.mmall.service.IPayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

import static com.alipay.api.AlipayConstants.CHARSET;
import static com.alipay.api.AlipayConstants.SIGN_TYPE;

/**
 * 使用支付宝的支付模块
 * @author liliang
 */
@RestController
@RequestMapping("/order")
public class PayController {

    @Autowired
    private IPayService payService;

    /**
     * 发起支付宝预支付
     * @param orderNo 订单号
     * @param request
     * @return 返回支付宝二维码地址
     */
    @GetMapping("/pay.do")
    public ServerResponse<Map<String,String>> pay(long orderNo, HttpServletRequest request){
        String path = request.getSession().getServletContext().getRealPath("qrcode");
        return payService.pay(orderNo,path);
    }

    /**
     * 支付宝回调
     * @param request
     * @return 返回success否则支付宝会一直尝试
     */
    @PostMapping("/alipay_callback.do")
    public String alipayCallBack(HttpServletRequest request) throws AlipayApiException {
        Map<String,String> paramsMap = Maps.newLinkedHashMap();
        //将异步通知中收到的所有参数都存放到map中
        Map<String, String[]> params =  request.getParameterMap();
        for (String param : params.keySet()){
            paramsMap.put(param,params.get(param).toString());
        }
        ServerResponse serverResponse = payService.checkSign(paramsMap);
        if (serverResponse.isSuccess()){
            return "success";
        }
        return "failure";
    }

    /**
     * 获取订单状态
     * @param orderNo
     * @return
     */
    @GetMapping("/query_order_pay_status.do")
    public ServerResponse queryOrderPayStatus(long orderNo){
        //利用支付宝提供查询订单接口查询订单支付情况
        return payService.queryOrderPayStatus(orderNo);
    }


}
