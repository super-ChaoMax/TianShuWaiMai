package com.chaao.appserver.controller.admin;

import com.chaao.appserver.service.AdminDishService;
import dto.admin.category.CategoryPageQueryDTO;
import entity.admin.category.Dish;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import vo.PageResult;
import vo.Result;



@RestController
@RequestMapping("/admin/dish")
public class AdminDishController {

    @Autowired
    private AdminDishService dishService;

    @GetMapping("/page")
    public Result<PageResult<Dish>> page(CategoryPageQueryDTO dto) {
        return Result.success(dishService.getDishPage(dto));
    }

    @PostMapping
    public Result<String> save(@RequestBody Dish dish) {
        dishService.addDish(dish);
        return Result.success("菜品新增成功");
    }

    @PutMapping
    public Result<String> update(@RequestBody Dish dish) {
        dishService.editDish(dish);
        return Result.success("菜品修改成功");
    }

    @DeleteMapping("/{id}")
    public Result<String> delete(@PathVariable Long id) {
        dishService.removeDish(id);
        return Result.success("删除成功");
    }

    @GetMapping("/{id}")
    public Result<Dish> getInfo(@PathVariable Long id) {
        return Result.success(dishService.getDishById(id));
    }
}