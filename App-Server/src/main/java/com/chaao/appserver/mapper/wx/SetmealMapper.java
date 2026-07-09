package com.chaao.appserver.mapper.wx;

import dto.wx.SetmealDTO;
import org.apache.ibatis.annotations.Mapper;
import vo.wx.SetemalDishVO;
import vo.wx.SetmealVO;

import java.util.List;

@Mapper
public interface SetmealMapper {

    // 根据分类id查询套餐
    List<SetmealVO> getSetmealById(SetmealDTO setmealDTO);



    //根据套餐ID查询包含的菜品（在UI界面就是点击这个套餐出现的页面）
    List<SetemalDishVO> getSetmealDishById(Long id);


}
