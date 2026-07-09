package com.chaao.appserver.service;
import dto.wx.DishDTO;
import org.apache.ibatis.annotations.Param;
import vo.wx.DishVO;

import java.util.List;

public interface DishService {

    // 根据分类ID查菜品列表
    List<DishVO> dishListByCategoryId(@Param("dto") DishDTO dishDTO);

}
