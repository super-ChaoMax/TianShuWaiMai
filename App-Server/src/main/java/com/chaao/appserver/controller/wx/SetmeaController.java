package com.chaao.appserver.controller.wx;

import com.chaao.appserver.service.Impl.wx.DishServiceImpl;
import com.chaao.appserver.service.Impl.wx.SetmealServiceImpl;
import dto.wx.DishDTO;
import dto.wx.SetmealDTO;
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
import vo.wx.SetmealVO;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/wx/setmeal")
@Tag(name = "C端-套餐接口")
public class SetmeaController {

    @Autowired
    private SetmealServiceImpl setmealService;

    // 前端调用  对应这个接口
    @PostMapping("/list")
    @Operation(summary = "根据分类分页查询菜品")
    public Result<List<SetmealVO>> setmealListByCategoryId(@RequestBody SetmealDTO dto) {
        List<SetmealVO> list = setmealService.getSetmealById(dto);
        return Result.success(list);

    }

}
