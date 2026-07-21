package com.chaao.appserver.controller.admin;

import com.chaao.appserver.service.AdminSetmealService;
import dto.admin.category.CategoryPageQueryDTO;
import entity.admin.category.Setmeal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import vo.PageResult;
import vo.Result;


@RestController
@RequestMapping("/admin/setmeal")
public class AdminSetmealController {

    @Autowired
    private AdminSetmealService setmealService;

    @GetMapping("/page")
    public Result<PageResult<Setmeal>> page(CategoryPageQueryDTO dto) {
        return Result.success(setmealService.getSetmealPage(dto));
    }

    @PostMapping
    public Result<String> save(@RequestBody Setmeal setmeal) {
        setmealService.addSetmeal(setmeal);
        return Result.success("套餐新增成功");
    }

    @PutMapping
    public Result<String> update(@RequestBody Setmeal setmeal) {
        setmealService.editSetmeal(setmeal);
        return Result.success("套餐修改成功");
    }

    @DeleteMapping("/{id}")
    public Result<String> delete(@PathVariable Long id) {
        setmealService.removeSetmeal(id);
        return Result.success("删除成功");
    }

    @GetMapping("/{id}")
    public Result<Setmeal> getInfo(@PathVariable Long id) {
        return Result.success(setmealService.getSetmealById(id));
    }
}