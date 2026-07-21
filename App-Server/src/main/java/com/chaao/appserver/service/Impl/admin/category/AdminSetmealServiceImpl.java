package com.chaao.appserver.service.Impl.admin.category;

import com.chaao.appserver.mapper.admin.category.AdminSetmealMapper;
import com.chaao.appserver.service.AdminSetmealService;
import dto.admin.category.CategoryPageQueryDTO;
import entity.admin.category.Setmeal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vo.PageResult;


import java.time.LocalDateTime;

@Service
public class AdminSetmealServiceImpl implements AdminSetmealService {

    @Autowired
    private AdminSetmealMapper adminSetmealMapper;

    @Override
    public PageResult<Setmeal> getSetmealPage(CategoryPageQueryDTO dto) {
        return new PageResult<>(adminSetmealMapper.selectSetmealCount(dto), adminSetmealMapper.selectSetmealPage(dto));
    }

    @Override
    public void addSetmeal(Setmeal setmeal) {
        setmeal.setCreateTime(LocalDateTime.now());
        setmeal.setUpdateTime(LocalDateTime.now());
        setmeal.setVersion(1L);
        setmeal.setDeleted(0);
        adminSetmealMapper.insertSetmeal(setmeal);
    }

    @Override
    public void editSetmeal(Setmeal setmeal) {
        setmeal.setUpdateTime(LocalDateTime.now());
        adminSetmealMapper.updateSetmealByVersion(setmeal);
    }

    @Override
    public void removeSetmeal(Long id) {
        adminSetmealMapper.deleteSetmeal(id);
    }

    @Override
    public Setmeal getSetmealById(Long id) {
        return adminSetmealMapper.selectSetmealById(id);
    }
}