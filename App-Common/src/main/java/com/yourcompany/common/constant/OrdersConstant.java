package com.yourcompany.common.constant;

public class OrdersConstant {
    private OrdersConstant(){}


    //生成6个状态
    //1.待付款
    public static final int PENDING_PAYMENT = 1;
    //2.待商家接单
    public static final int PENDING_DELIVERY = 2;
    //3.待配送
    public static final int PENDING_DELIVERED = 3;
    //4.已完成
    public static final int COMPLETED = 4;
    //5.已取消
    public static final int CANCELED = 5;
    //6.退款中
    public static final int REFUNDING = 6;



}
