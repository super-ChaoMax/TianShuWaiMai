package com.chaao.appserver.service;

import dto.admin.category.CategoryPageQueryDTO;
import entity.admin.category.Dish;
import vo.PageResult;

public interface  AdminDishService {
    PageResult<Dish> getDishPage(CategoryPageQueryDTO dto);
    void addDish(Dish dish);
    void editDish(Dish dish);
    void removeDish(Long id);
    Dish getDishById(Long id);
}
