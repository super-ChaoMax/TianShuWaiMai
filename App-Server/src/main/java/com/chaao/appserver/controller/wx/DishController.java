package com.chaao.appserver.controller.wx;


import com.chaao.appserver.service.Impl.wx.DishServiceImpl;
import com.chaao.appserver.util.DishRedisUtil;
import dto.wx.DishDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vo.Result;
import vo.wx.DishVO;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/wx/dish")
@Tag(name = "C端-菜品接口")
public class DishController {

    @Autowired
    private DishServiceImpl dishService;

    @Autowired
    private DishRedisUtil dishRedisUtil;

    @PostMapping("/list")
    @Operation(summary = "根据分类分页查询菜品")
    public Result<List<DishVO>> dishListByCategoryId(@RequestBody DishDTO dto) {

/*        第一版本：没有缓存

        // 打印整个dto，看字段值
        log.info("前端传进来的DTO：{}", dto);
        log.info("前端传进来的categoryId：{}", dto.getCategoryId());

        List<DishVO> list = dishService.dishListByCategoryId(dto);
        return Result.success(list);
*/

        log.info("前端传进来的DTO：{}", dto);
        log.info("前端传进来的categoryId：{}", dto.getCategoryId());
        Long categoryId = dto.getCategoryId();

        // 1. 先从Redis拿缓存
        List<DishVO> voList = dishRedisUtil.getDishByCategoryId(categoryId);
        if (!CollectionUtils.isEmpty(voList)) {
            log.info("从Redis中获取的菜品数据：{}", voList);
            return Result.success(voList);
        }

        // 2. 缓存为空，查询数据库
        List<DishVO> dbList = dishService.dishListByCategoryId(dto);
        if (CollectionUtils.isEmpty(dbList)) {
            log.info("数据库中没有数据");
            return Result.success(new ArrayList<>());
        }

        // 3. 数据库数据批量存入Redis
        for (DishVO dishVO : dbList) {
            log.info("存入Redis的菜品数据：{}", dishVO);
            dishRedisUtil.setDish(dishVO);
        }

        return Result.success(dbList);


    }
}
