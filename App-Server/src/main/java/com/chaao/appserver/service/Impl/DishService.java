package com.chaao.appserver.service.Impl;
import dto.wx.DishDTO;
import vo.wx.DishVO;

import java.util.List;

public interface DishService {

    // 根据分类ID查菜品列表
    List<DishVO> dishListByCategoryId(DishDTO dishDTO);

}
