package com.chaao.appserver.service;

import dto.wx.ShoppingCartDTO;
import entity.wx.ShoppingCart;

import java.util.List;

public interface ShoppingCartService {

    /**
     *  获取当前用户的购物车
     */
    List<ShoppingCart> getShoppingCartList( Long userId );



    void add(Long userId,ShoppingCartDTO shoppingCartDTO);




    void subNumberById(Long userId,ShoppingCartDTO shoppingCartDTO);




    /**
     * 清空当前用户的购物车
     */
    void deleteByUserId(Long userId);

//    /**
//     * 删除购物车中的某一项
//     */
//    void deleteById(Long id);
//


}
