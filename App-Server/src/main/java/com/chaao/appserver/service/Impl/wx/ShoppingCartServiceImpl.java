package com.chaao.appserver.service.Impl.wx;


import com.chaao.appserver.mapper.wx.ShoppingCartMapper;
import com.chaao.appserver.service.ShoppingCartService;
import com.chaao.appserver.service.SpringSecurity.LoginUser;
import com.chaao.appserver.util.SecurityUtils;
import dto.wx.ShoppingCartDTO;
import entity.wx.ShoppingCart;
import entity.wx.WxUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vo.Result;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {

    @Autowired
    private ShoppingCartMapper shoppingCartMapper;

    @Override
    public List<ShoppingCart> getShoppingCartList(Long userId )  {
            return shoppingCartMapper.getShoppingCartList(userId);
    }

    /*
     * 添加购物车
     * 注意：在 Service 层我们会先判断是否存在，存在则走 update，不存在则走 insert
     */
    @Override
    public void add(Long userId,ShoppingCartDTO shoppingCartDTO) {


        // 1. 获取当前登录用户ID
//        Long currentUserId = userId;

        // 2. 【核心防坑操作】构造一个专门用于查重查询的对象
        ShoppingCart queryCart = new ShoppingCart();
        queryCart.setUserId(userId);
        queryCart.setDishId(shoppingCartDTO.getDishId());
        queryCart.setSetmealId(shoppingCartDTO.getSetmealId());
        queryCart.setDishFlavor(shoppingCartDTO.getDishFlavor());

        // 2. 【防御性校验】如果既不是菜品也不是套餐，直接抛出异常
        if (shoppingCartDTO.getDishId() == null && shoppingCartDTO.getSetmealId() == null) {
            throw new RuntimeException("添加购物车失败：缺少菜品或套餐ID");
        }



        // 3. 查询购物车中是否已存在该商品
        Long cartId = shoppingCartMapper.selectIdByCondition(userId,shoppingCartDTO);

        if (cartId != null) {
            log.info("购物车已存在该商品，数量 +1");
            // 【情况A】已存在 -> 数量 +1
            // 这里需要执行 UPDATE 操作
            // 你可以单独写一个 updateNumberById 方法，或者复用 list 查出实体再更新
            ShoppingCart updateCart = new ShoppingCart();
            updateCart.setId(cartId);
            updateCart.setNumber(1); // 这里传1，在XML里写 number = number + #{number}
            updateCart.setUpdateTime(LocalDateTime.now());
            shoppingCartMapper.updateNumberById(updateCart);

        } else {
            log.info("购物车不存在该商品，添加购物车");
            // 【情况B】不存在 -> 插入新数据
            ShoppingCart newCart = new ShoppingCart();
            newCart.setUserId(userId);
            newCart.setName(shoppingCartDTO.getName());
            newCart.setImage(shoppingCartDTO.getImage());
            newCart.setDishId(shoppingCartDTO.getDishId());
            newCart.setSetmealId(shoppingCartDTO.getSetmealId());
            newCart.setDishFlavor(shoppingCartDTO.getDishFlavor());
            newCart.setAmount(shoppingCartDTO.getAmount());
            newCart.setNumber(1);

            // 【关键】补全冗余字段（名称、图片、单价），避免后续菜品下架或改价影响购物车
            // 这里需要根据 dishId 或 setmealId 去查数据库，然后赋值
            // newCart.setName(...);
            // newCart.setImage(...);
            // newCart.setAmount(...);

            newCart.setCreateTime(LocalDateTime.now());
            newCart.setUpdateTime(LocalDateTime.now());

            shoppingCartMapper.addToShoppingCart(newCart);
        }

    }


    /*
     * 减购物车
     */
    @Override
    public void subNumberById(Long userId, ShoppingCartDTO shoppingCartDTO) {


        // 2. 【防御性校验】如果既不是菜品也不是套餐，直接抛出异常
        if (shoppingCartDTO.getDishId() == null && shoppingCartDTO.getSetmealId() == null) {
            throw new RuntimeException("添加购物车失败：缺少菜品或套餐ID");
        }

        // 1. 设置查询条件，查出当前登录用户的这条购物车数据
        List<ShoppingCart> list = shoppingCartMapper.getShoppingCartList(userId);

        if (list != null && !list.isEmpty()) {
            for (ShoppingCart cart : list) {
                // 【修复 1】使用 Objects.equals 解决空指针异常
                boolean isMatch = Objects.equals(cart.getId(), shoppingCartDTO.getId()) ||
                        Objects.equals(cart.getDishId(), shoppingCartDTO.getDishId()) ||
                        Objects.equals(cart.getSetmealId(), shoppingCartDTO.getSetmealId());

                if (isMatch) {
                    if (cart.getNumber() == 1) {
                        log.info("购物车数量为1，删除该商品");
                        // 数量为1，再次减就直接删除整条记录
                        shoppingCartMapper.deleteById(cart.getId());
                    } else {
                        log.info("购物车数量>1，数量-1");
                        // 【修复 2】数量应该是 原数量 - 1，而不是写死为 1
                        ShoppingCart newCart = new ShoppingCart();
                        newCart.setUserId(userId);
                        newCart.setId(cart.getId()); // 注意：这里应该用查出来的 cart.getId()
                        newCart.setDishId(cart.getDishId());
                        newCart.setSetmealId(cart.getSetmealId());
                        newCart.setNumber(1); // 正确减 1
                        newCart.setUpdateTime(LocalDateTime.now());

                        // 只需要 update 即可，不需要再调用 subNumberById
                        shoppingCartMapper.subNumberById(newCart);
                    }
                    // 找到匹配的数据并处理后，直接跳出循环，避免继续遍历浪费性能
                    break;
                }
            }
        }




    }

    // 清空
    @Override
    public void deleteByUserId(Long userId) {
        shoppingCartMapper.deleteByUserId(userId);
    }


}
