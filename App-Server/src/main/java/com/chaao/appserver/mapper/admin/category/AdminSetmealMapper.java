package com.chaao.appserver.mapper.admin.category;

import dto.admin.category.CategoryPageQueryDTO;
import entity.admin.category.Setmeal;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;
@Mapper
public interface AdminSetmealMapper {

    List<Setmeal> selectSetmealPage(@Param("dto") CategoryPageQueryDTO dto);
    Long selectSetmealCount(@Param("dto") CategoryPageQueryDTO dto);

    int insertSetmeal(Setmeal setmeal);
    int updateSetmeal(Setmeal setmeal);
    int deleteSetmeal(@Param("id") Long id);
    Setmeal selectSetmealById(@Param("id") Long id);
    int updateSetmealByVersion(Setmeal setmeal);
}