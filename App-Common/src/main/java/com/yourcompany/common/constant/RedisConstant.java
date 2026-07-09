package com.yourcompany.common.constant;

public class RedisConstant {

    private RedisConstant(){}

    // 菜品详情key( dish_{id})
    public static final String Dish_KEY = "dish_%s";
    // 分类菜品集合key( category_{id})
    public static final String CATEGORY_Dish_KEY = "category_%s";
    // 缓存过期时间 1小时
    public static final long Dish_CACHE_TIME = 1;


}
