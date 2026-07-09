package com.chaao.appserver.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import vo.wx.DishVO;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static com.yourcompany.common.constant.RedisConstant.*;

@Component
public class DishRedisUtil {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;


    // 注入转换工具
    @Autowired
    private ObjectMapper objectMapper;

    /**
     * 设置：存入菜品缓存（给前端的）
     */
    public void saveGoodsCache(DishVO dishVO){
        // 1.拼接商品详情key
        String DishKey = String.format(Dish_KEY, dishVO.getId());
        // 2.存入商品JSON，设置1小时过期
        redisTemplate.opsForValue().set(DishKey,dishVO,Dish_CACHE_TIME, TimeUnit.HOURS);
        // 3.把商品id加入对应分类集合
        String categoryKey = String.format(CATEGORY_Dish_KEY,dishVO.getCategoryId());
        redisTemplate.opsForSet().add(categoryKey,dishVO.getId());
    }

    /**
     * 查询： 根据菜品id  获取菜品详情
     */
    public DishVO getGoodsById(Long goodsId){
        String key = String.format(Dish_KEY,goodsId);
        //Redis 里存的是 JSON，反序列化后变成了 LinkedHashMap，不能直接强转为你的 DishVOTest 对象。
//        return (DishVOTest) redisTemplate.opsForValue().get(key);


        Object obj = redisTemplate.opsForValue().get(key);
        if(obj == null){
            return null;
        }
        // 核心：map转对象，不再强转
        return objectMapper.convertValue(obj, DishVO.class);
    }

    /**
     * 查询：根据分类id查询该分类下所有商品
     */
    public List<DishVO> getDishByCategoryId(Long categoryId){
        List<DishVO> dishVOList = new ArrayList<>();
        // 拼接分类商品集合key
        String categoryKey = String.format(CATEGORY_Dish_KEY,categoryId);
        // 获取该分类下所有商品id
        Set<Object> goodsIdSet = redisTemplate.opsForSet().members(categoryKey);
        if(goodsIdSet == null || goodsIdSet.isEmpty()){
            return dishVOList;
        }
        // 遍历id查询商品详情
        for (Object idObj : goodsIdSet) {
            Long goodsId = Long.valueOf(idObj.toString());
            DishVO dishVO = getGoodsById(goodsId);
            if(dishVO != null){
                dishVOList.add(dishVO);
            }
        }
        return dishVOList;
    }

    /**
     * 删除商品缓存
     */
    public void deleteGoodsCache(Long DishId,Long categoryId) {
        // 删除商品详情
        String DishKey = String.format(Dish_KEY, DishId);
        redisTemplate.delete(DishKey);
        // 从分类集合移除商品id
        String categoryKey = String.format(CATEGORY_Dish_KEY, categoryId);
    }


}