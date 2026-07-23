package com.chaao.appserver.controller.admin;

import com.chaao.appserver.service.AdminDishService;
import com.chaao.appserver.util.DishRedisUtil;
import dto.admin.category.CategoryPageQueryDTO;
import entity.admin.category.Dish;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import vo.PageResult;
import vo.Result;
import vo.wx.DishVO;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@RestController
@RequestMapping("/admin/dish")
@Slf4j
public class AdminDishController {

    @Autowired
    private AdminDishService dishService;


    //引入封装好的redis
    @Autowired
    private DishRedisUtil dishRedisUtil;

    @GetMapping("/page")
    public Result<PageResult<Dish>> page(CategoryPageQueryDTO dto) {
        return Result.success(dishService.getDishPage(dto));
    }


    @PostMapping
    public Result<String> save(@RequestBody Dish dish) {
        dishService.addDish(dish);

        // 同步存入Redis
        DishVO vo = new  DishVO();
        vo.setId(dish.getId());
        vo.setName(dish.getName());
        vo.setCategoryId(dish.getCategoryId());
        vo.setPrice( dish.getPrice());
        vo.setImage(dish.getImage());
        vo.setDescription(dish.getDescription());
        vo.setStatus(dish.getStatus());
        log.info("菜品同步存入Redis");
        dishRedisUtil.setDish(vo);

        return Result.success("菜品新增成功");
    }

    @PutMapping
    public Result<String> update(@RequestBody Dish dish) {
        dishService.editDish(dish);

        //先删旧缓存 再存新缓存
        dishRedisUtil.deleteGoodsCache(dish.getId(),dish.getCategoryId());
        log.info("同步修改菜品——先删旧缓存");
        DishVO vo = new  DishVO();
        vo.setId(dish.getId());
        vo.setName(dish.getName());
        vo.setCategoryId(dish.getCategoryId());
        vo.setPrice( dish.getPrice());
         vo.setImage(dish.getImage());
         vo.setDescription(dish.getDescription());
         vo.setStatus(dish.getStatus());


         dishRedisUtil.setDish(vo);
        log.info("同步修改菜品——再存新缓存");

        return Result.success("菜品修改成功");
    }

    @DeleteMapping("/{id}")
    public Result<String> delete(@PathVariable Long id) {
        dishService.removeDish(id);

        dishRedisUtil.deleteGoodsCache(id,dishService.getDishById(id).getCategoryId());
        log.info("同步删除菜品——删除缓存");
        return Result.success("删除成功");
    }

    @GetMapping("/{id}")
    public Result<Dish> getInfo(@PathVariable Long id) {
        return Result.success(dishService.getDishById(id));
    }
}