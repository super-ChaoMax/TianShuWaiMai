package com.chaao.appserver.mapper.admin.category;

import dto.admin.category.CategoryPageQueryDTO;
import entity.admin.category.Category;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface AdminCategoryMapper {

    //分页查询
    List<Category> selectCategoryPage(@Param("dto") CategoryPageQueryDTO dto);

    //统计总数
    Long selectCategoryCount(@Param("dto") CategoryPageQueryDTO dto);

    //查询启用分类下拉列表
    List<Category> selectEnableCategory(@Param("type") Integer type);

    //新增
    int insertCategory(Category category);

    //修改
    int updateCategory(Category category);

    //修改状态
    int updateStatus(@Param("id") Long id, @Param("status") Integer status);

    //逻辑删除
    int deleteCategory(@Param("id") Long id);

    //根据id查询
    Category selectById(@Param("id") Long id);
}