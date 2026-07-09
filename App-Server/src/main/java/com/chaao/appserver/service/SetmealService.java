package com.chaao.appserver.service;

import dto.wx.SetmealDTO;
import vo.wx.SetemalDishVO;
import vo.wx.SetmealVO;

import java.util.List;

public interface SetmealService {

    List<SetmealVO> getSetmealById(SetmealDTO setmealDTO);

    //根据套餐ID查询包含的菜品（在UI界面就是点击这个套餐出现的页面）
    List<SetemalDishVO> getSetmealDishById(Long id);

}
