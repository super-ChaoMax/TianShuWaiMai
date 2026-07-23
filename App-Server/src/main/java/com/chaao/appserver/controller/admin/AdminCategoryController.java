package com.chaao.appserver.controller.admin;

import com.chaao.appserver.service.AdminCategoryService;
import com.chaao.appserver.util.DishRedisUtil;
import dto.admin.category.CategoryPageQueryDTO;
import entity.admin.category.Category;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import vo.PageResult;
import vo.Result;


import java.util.List;

@Slf4j
@RestController
@RequestMapping("/admin/category")
public class AdminCategoryController {

    @Autowired
    private AdminCategoryService categoryService;



    @GetMapping("/page")
    @Operation(summary = "分类的分页查询")
    public Result<PageResult<Category>> page(CategoryPageQueryDTO dto) {
        return Result.success(categoryService.getCategoryPage(dto));
    }

    @GetMapping("/list")
    @Operation(summary = "分类的列表")
    public Result<List<Category>> list(Integer type) {
        return Result.success(categoryService.getEnableCategoryList(type));
    }

    @PostMapping
    @Operation(summary = "新增分类")
    public Result<String> add(@RequestBody Category category) {
        categoryService.addCategory(category);
        return Result.success("新增成功");
    }

    @PutMapping
    @Operation(summary = "修改分类")
    public Result<String> update(@RequestBody Category category) {
        categoryService.editCategory(category);
        return Result.success("修改成功");
    }

    @PostMapping("/status")
    @Operation(summary = "分类状态修改")
    public Result<String> status(@RequestBody Category dto) {
        categoryService.changeStatus(dto.getId(), dto.getStatus());
        return Result.success("状态修改成功");
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除分类")
    public Result<String> delete(@PathVariable Long id) {
        categoryService.removeCategory(id);
        return Result.success("删除成功");
    }

    @GetMapping("/{id}")
    public Result<Category> info(@PathVariable Long id) {
        return Result.success(categoryService.getById(id));
    }
}