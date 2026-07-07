package com.chaao.appserver.controller.wx;


import com.chaao.appserver.service.Impl.wx.DishServiceImpl;
import dto.wx.DishDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vo.Result;
import vo.wx.DishVO;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/wx/dish")
@Tag(name = "C端-菜品接口")
public class DishController {

    @Autowired
    private DishServiceImpl dishService;

    @PostMapping("/list")
    @Operation(summary = "根据分类分页查询菜品")
    public Result<List<DishVO>> dishListByCategoryId(@RequestBody DishDTO dto) {
        List<DishVO> list = dishService.dishListByCategoryId(dto);
        return Result.success(list);

    }
}
