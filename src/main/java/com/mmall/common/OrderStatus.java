package com.mmall.common;

/**
 * @author liliang
 */

public enum OrderStatus {

    ORDER_CANCELED(0,"已取消"),
    ORDER_NON_PAYMENT(10,"未付款"),
    ORDER_PAY_UP(20,"已付款"),
    ORDER_HAS_SHIPPING(40,"已发货"),
    ORDER_TRADE_SUCCESS(50,"交易成功"),
    ORDER_TRADE_CLOSED(60,"交易关闭");

    private final int status;
    private final String desc;

    OrderStatus(int status, String desc) {
        this.status = status;
        this.desc = desc;
    }

    public int getStatus() {
        return status;
    }

    public String getDesc() {
        return desc;
    }
}
