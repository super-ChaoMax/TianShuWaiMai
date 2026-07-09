package com.chaao.appserver.DishRedisTest;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class DishVOTest {
//    返回给前端的
    private Long id; // 菜品id,
    private String name; // 菜品名,
    private Double price; // 价格,
    private String image; // 图片地址,
    // 菜品其他业务字段

//    介绍
    private String description;
    private Integer status; // 状态
    private Long categoryId;

//    ---重点：有可有可选的规格 ------
    private List<Flavor> flavorJson; // 用List<Flavor>接收JSON数组

    // 内部类，对应口味规格结构
    @Data
    public static class Flavor {
        private String name; // 规格名，如"辣度"
        private List<String> values; // 选项，如["微辣", "中辣"]
    }

}
