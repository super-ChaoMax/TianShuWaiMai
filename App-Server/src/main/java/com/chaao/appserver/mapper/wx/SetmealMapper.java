package com.chaao.appserver.mapper.wx;

import dto.wx.SetmealDTO;
import org.apache.ibatis.annotations.Mapper;
import vo.wx.SetmealVO;

import java.util.List;

@Mapper
public interface SetmealMapper {

    // 根据id查询套餐详情
    List<SetmealVO> getSetmealById(SetmealDTO setmealDTO);

}
