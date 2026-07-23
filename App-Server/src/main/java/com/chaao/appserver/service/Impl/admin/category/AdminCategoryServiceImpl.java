package com.chaao.appserver.service.Impl.admin.category;

import com.chaao.appserver.mapper.admin.category.AdminCategoryMapper;
import com.chaao.appserver.service.AdminCategoryService;
import com.yourcompany.common.util.XueHuaiID;
import dto.admin.category.CategoryPageQueryDTO;
import entity.admin.category.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vo.PageResult;


import java.time.LocalDateTime;
import java.util.List;

@Service
public class AdminCategoryServiceImpl implements AdminCategoryService {

    @Autowired
    //------- 重点记得引入的是admincategoryMapper
    private AdminCategoryMapper adminCategoryMapper;

    @Override
    public PageResult<Category> getCategoryPage(CategoryPageQueryDTO dto) {
        // 提前计算偏移量
        dto.setOffset((dto.getPage() - 1) * dto.getPageSize());

        List<Category> list = adminCategoryMapper.selectCategoryPage(dto);
        Long total = adminCategoryMapper.selectCategoryCount(dto);
        return new PageResult<>(total, list);
    }

    @Override
    public List<Category> getEnableCategoryList(Integer type) {
        return adminCategoryMapper.selectEnableCategory(type);
    }

    @Override
    public void addCategory(Category category) {
        category.setStatus(1);
        category.setCreateTime(LocalDateTime.now());
        category.setUpdateTime(LocalDateTime.now());
        category.setCreateUser(1L);
        category.setUpdateUser(1L);
        category.setId(XueHuaiID.generateUserId());
        adminCategoryMapper.insertCategory(category);
    }

    @Override
    public void editCategory(Category category) {
        category.setUpdateTime(LocalDateTime.now());
        category.setUpdateUser(1L);
        adminCategoryMapper.updateCategory(category);
    }

    @Override
    public void changeStatus(Long id, Integer status) {
        adminCategoryMapper.updateStatus(id, status);
    }

    @Override
    public void removeCategory(Long id) {
        adminCategoryMapper.deleteCategory(id);
    }

    @Override
    public Category getById(Long id) {
        return adminCategoryMapper.selectById(id);
    }
}