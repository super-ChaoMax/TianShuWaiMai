package com.chaao.appserver.service.Impl.wx;

import com.chaao.appserver.mapper.wx.CategoryMapper;
import com.chaao.appserver.service.CategoryService;
import vo.wx.CategoryVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

//    引入数据库mapper
    @Autowired
    private CategoryMapper categoryMapper;


    @Override
    public List<CategoryVO> selectAllCategory() {
        return categoryMapper.selectAllCategory();
    }
}
