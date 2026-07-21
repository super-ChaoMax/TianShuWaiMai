package com.chaao.appserver.controller.admin;

import com.chaao.appserver.service.AdminCategoryService;
import dto.admin.category.CategoryPageQueryDTO;
import entity.admin.category.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import vo.PageResult;
import vo.Result;


import java.util.List;

@RestController
@RequestMapping("/admin/category")
public class AdminCategoryController {

    @Autowired
    private AdminCategoryService categoryService;

    @GetMapping("/page")
    public Result<PageResult<Category>> page(CategoryPageQueryDTO dto) {
        return Result.success(categoryService.getCategoryPage(dto));
    }

    @GetMapping("/list")
    public Result<List<Category>> list(Integer type) {
        return Result.success(categoryService.getEnableCategoryList(type));
    }

    @PostMapping
    public Result<String> add(@RequestBody Category category) {
        categoryService.addCategory(category);
        return Result.success("新增成功");
    }

    @PutMapping
    public Result<String> update(@RequestBody Category category) {
        categoryService.editCategory(category);
        return Result.success("修改成功");
    }

    @PostMapping("/status/{status}")
    public Result<String> status(@PathVariable Integer status, Long id) {
        categoryService.changeStatus(id, status);
        return Result.success("状态修改成功");
    }

    @DeleteMapping("/{id}")
    public Result<String> delete(@PathVariable Long id) {
        categoryService.removeCategory(id);
        return Result.success("删除成功");
    }

    @GetMapping("/{id}")
    public Result<Category> info(@PathVariable Long id) {
        return Result.success(categoryService.getById(id));
    }
}