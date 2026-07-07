package com.chaao.appserver.controller.wx;

import com.chaao.appserver.service.CategoryService;
import vo.wx.CategoryVO;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vo.Result;

import java.util.List;

@Slf4j
@RestController
@Tag(name = "C端-菜品分类接口")
@RequestMapping("/wx/category")
public class CategoryController {

//    导入业务类
    @Autowired
    private CategoryService categoryService;


    @GetMapping()
    public Result<List<CategoryVO>> selectAllCategory() {
        List<CategoryVO> list = categoryService.selectAllCategory();
        log.info("查询所有菜品");
        return Result.success("操作成功", list);
    }


}
