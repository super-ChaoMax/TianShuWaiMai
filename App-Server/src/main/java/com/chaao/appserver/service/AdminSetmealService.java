package com.chaao.appserver.service;

import dto.admin.category.CategoryPageQueryDTO;
import entity.admin.category.Setmeal;
import vo.PageResult;

public interface AdminSetmealService {
    PageResult<Setmeal> getSetmealPage(CategoryPageQueryDTO dto);
    void addSetmeal(Setmeal setmeal);
    void editSetmeal(Setmeal setmeal);
    void removeSetmeal(Long id);
    Setmeal getSetmealById(Long id);
}
