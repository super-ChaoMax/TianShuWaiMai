package com.chaao.appserver.service.Impl.admin.category;

import com.chaao.appserver.mapper.admin.category.AdminDishMapper;
import com.chaao.appserver.service.AdminDishService;
import dto.admin.category.CategoryPageQueryDTO;
import entity.admin.category.Dish;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vo.PageResult;


import java.time.LocalDateTime;

@Service
public class AdminDishServiceImpl implements AdminDishService {

    @Autowired
    private AdminDishMapper adminDishMapper;

    @Override
    public PageResult<Dish> getDishPage(CategoryPageQueryDTO dto) {
        return new PageResult<>(adminDishMapper.selectDishCount(dto), adminDishMapper.selectDishPage(dto));
    }

    @Override
    public void addDish(Dish dish) {
        dish.setCreateTime(LocalDateTime.now());
        dish.setUpdateTime(LocalDateTime.now());
        dish.setVersion(1L);
        dish.setDeleted(0);
        adminDishMapper.insertDish(dish);
    }

    @Override
    public void editDish(Dish dish) {
        dish.setUpdateTime(LocalDateTime.now());
        //乐观锁修改
        adminDishMapper.updateDishByVersion(dish);
    }

    @Override
    public void removeDish(Long id) {
        adminDishMapper.deleteDish(id);
    }

    @Override
    public Dish getDishById(Long id) {
        return adminDishMapper.selectDishById(id);
    }
}