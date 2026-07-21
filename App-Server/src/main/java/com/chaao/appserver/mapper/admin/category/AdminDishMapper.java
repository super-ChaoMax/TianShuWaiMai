package com.chaao.appserver.mapper.admin.category;

import dto.admin.category.CategoryPageQueryDTO;
import entity.admin.category.Dish;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface AdminDishMapper {

    List<Dish> selectDishPage(@Param("dto") CategoryPageQueryDTO dto);
    Long selectDishCount(@Param("dto") CategoryPageQueryDTO dto);

    int insertDish(Dish dish);
    int updateDish(Dish dish);
    int deleteDish(@Param("id") Long id);
    Dish selectDishById(@Param("id") Long id);

    //乐观锁更新
    int updateDishByVersion(Dish dish);
}