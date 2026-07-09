package com.chaao.appserver.mapper.wx;

import dto.wx.DishDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import vo.wx.DishVO;

import java.util.List;

@Mapper
public interface DishMapper {
    // 必须在这里加 @Param("dto")
    List<DishVO> dishListByCategoryId(@Param("dto") DishDTO dishDTO);
}
