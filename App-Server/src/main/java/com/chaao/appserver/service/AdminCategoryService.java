package com.chaao.appserver.service;

import dto.admin.category.CategoryPageQueryDTO;
import entity.admin.category.Category;
import vo.PageResult;

import java.util.List;

public interface  AdminCategoryService {
    PageResult<Category> getCategoryPage(CategoryPageQueryDTO dto);
    List<Category> getEnableCategoryList(Integer type);
    void addCategory(Category category);
    void editCategory(Category category);
    void changeStatus(Long id, Integer status);
    void removeCategory(Long id);
    Category getById(Long id);
}
