package com.chaao.appserver.service;

import dto.wx.CategoryDTO;

import java.util.List;

public interface CategoryService {

//    查询全部分类
    public List<CategoryDTO> selectAllCategory();
}
