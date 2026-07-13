package com.chaao.appserver.mapper.wx;

import dto.wx.ShoppingCartDTO;
import entity.wx.ShoppingCart;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ShoppingCartMapper {

    /**
     *  获取当前用户的购物车
     */
    List<ShoppingCart> getShoppingCartList( Long userId);


    /*
     *  动态条件查询购物车列表是否存在（通常只查当前用户的）
     *  返回数量存在走 update ，不存在走 insert
     *  根据条件查询购物车中是否存在该商品
         如果存在，返回购物车记录的 id；如果不存在，返回 null
     */

    // 多参数了,这是最标准、最清晰的做法。通过 @Param 为每个参数显式命名，XML 中直接使用这些名称即可。
    Long  selectIdByCondition(@Param("userId") Long userId,
                              @Param("cart") ShoppingCartDTO shoppingCartDTO);


    //添加到购物车
    /**
     * 添加购物车
     * 注意：在 Service 层我们会先判断是否存在，存在则走 update，不存在走 insert
     */
    void addToShoppingCart(ShoppingCart shoppingCart);


    /**
     * 更新购物车数量
     */
    void updateNumberById(ShoppingCart shoppingCart);



    /**
     *      减少购物车数量
     */
    void subNumberById(ShoppingCart shoppingCart);


    /**
     * 删除购物车中的某一项
     */
    void deleteById(Long id);



    /**
     * 清空当前用户的购物车
     */
    void deleteByUserId(Long userId);




}
