package com.chaao.appserver.mapper.wx;

import vo.wx.CategoryVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CategoryMapper {

//    返回全部分类（无参）
    List<CategoryVO> selectAllCategory();


}
