package com.chaao.appserver.DishRedisTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

// 填写你的项目启动类
@SpringBootTest
public class DishRedisTest {

    @Autowired
    private DishRedisUtilTest dishRedisUtilTest;

    @Autowired
    private ObjectMapper objectMapper;

    // 测试存入菜品对象缓存
    @Test
    void testSaveGoods(){
        //创建一个菜品对象
        DishVOTest dishVO = new DishVOTest(
                1620110000000000001L,
                "辣椒炒肉",
                28.80,
                "https://oss.test.com/dish/001.jpg",
                "经典湘式下饭菜，肥瘦相间五花肉",
                1,
                1620100000000000001L,
                null
        );
        dishRedisUtilTest.saveGoodsCache(dishVO);
        System.out.println("商品缓存存入成功");
    }

    // 测试根据分类查询所有商品
    //Redis 里存的是 JSON，反序列化后变成了 LinkedHashMap，不能直接强转为你的 DishVOTest 对象。
    @Test
    void testGetGoodsByCategory(){
        // 查询分类id为3的所有商品

        // 取出时，用 ObjectMapper 转换
        List<DishVOTest> goodsList = dishRedisUtilTest.getDishByCategoryId(1620100000000000001L);
        goodsList.forEach(System.out::println);
    }

    // 测试删除商品缓存
    @Test
    void testDelGoods(){
        dishRedisUtilTest.deleteGoodsCache(1L,3L);
        System.out.println("商品缓存删除成功");
    }

}