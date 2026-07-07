package com.chaao.appserver.mapper.wx;

import dto.wx.DishDTO;
import org.apache.ibatis.annotations.Mapper;
import vo.wx.DishVO;

import java.util.List;

@Mapper
public interface DishMapper {

    List<DishVO> dishListByCategoryId(DishDTO dishDTO);
}
