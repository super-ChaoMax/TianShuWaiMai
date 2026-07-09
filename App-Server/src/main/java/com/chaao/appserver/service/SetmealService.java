package com.chaao.appserver.service;

import dto.wx.SetmealDTO;
import vo.wx.SetmealVO;

import java.util.List;

public interface SetmealService {

    List<SetmealVO> getSetmealById(SetmealDTO setmealDTO);

}
