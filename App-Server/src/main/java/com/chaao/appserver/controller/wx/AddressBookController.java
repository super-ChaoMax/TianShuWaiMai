package com.chaao.appserver.controller.wx;


import com.chaao.appserver.service.AddressBookService;
import com.chaao.appserver.service.SpringSecurity.LoginUser;
import com.chaao.appserver.util.SecurityUtils;
import entity.wx.AddressBook;
import entity.wx.WxUser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import vo.Result;

import java.util.List;

@RestController
@RequestMapping("/wx/addressBook")
@Api(tags = "C端地址簿接口")
@Slf4j
public class AddressBookController {

    @Autowired
    private AddressBookService addressBookService;

    /**
     * 查询当前登录用户的所有地址信息
     *
     * @return
     */
    @GetMapping("/list")
    @ApiOperation("查询当前登录用户的所有地址信息")
    public Result<List<AddressBook>> list() throws Exception {

        System.out.println("------------- 查询当前登录用户的所有地址信息 ----------");
        LoginUser loginUser = SecurityUtils.getCurrentUser();

        // 强制转换为微信用户逻辑
        if (loginUser != null && loginUser.isWxUser()) {
            WxUser wxUser = loginUser.getWxUser();
            Long userId = wxUser.getId(); // 拿到微信用户的数据库主键
            AddressBook addressBook = new AddressBook();
            addressBook.setUserId(userId);
            List<AddressBook> list = addressBookService.list(addressBook);
            return Result.success(list);
        }
        throw new Exception("非微信用户无法清空购物车");

    }

    /**
     * 新增地址
     *
     * @param addressBook
     * @return
     */
    @PostMapping
    @ApiOperation("新增地址")
    public Result save(@RequestBody AddressBook addressBook) {
        log.info("新增地址前端传递的参数" + addressBook);
        System.out.println("------------- 新增地址 ----------");
        addressBookService.save(addressBook);
        return Result.success();
    }

    @GetMapping("/{id}")
    @ApiOperation("根据id查询地址")
    public Result<AddressBook> getById(@PathVariable Long id) {
        log.info("根据id查询地址前端传递的参数" + id);
        System.out.println("------------- 根据id查询地址 ----------");
        AddressBook addressBook = addressBookService.getById(id);
        return Result.success(addressBook);
    }

    /**
     * 根据id修改地址
     *
     * @param addressBook
     * @return
     */
    @PutMapping
    @ApiOperation("根据id修改地址")
    public Result update(@RequestBody AddressBook addressBook) {
        log.info("根据id修改地址前端传递的参数" + addressBook);

        addressBookService.update(addressBook);
        return Result.success();
    }

    /**
     * 设置默认地址
     *
     * @param addressBook
     * @return
     */
    @PutMapping("/default")
    @ApiOperation("设置默认地址")
    public Result setDefault(@RequestBody AddressBook addressBook) {
        log.info("设置默认地址前端传递的参数" + addressBook);
        addressBookService.setDefault(addressBook);
        return Result.success();
    }

    /**
     * 根据id删除地址
     *
     * @param id
     * @return
     */
    @DeleteMapping
    @ApiOperation("根据id删除地址")
    public Result deleteById(Long id) {

        log.info("根据id删除地址前端传递的参数" + id);
        addressBookService.deleteById(id);
        return Result.success();
    }

    /**
     * 查询默认地址
     */
    @GetMapping("default")
    @ApiOperation("查询默认地址")
    public Result<AddressBook> getDefault() throws Exception {

        System.out.println("------------- 查询默认地址 ----------");
        LoginUser loginUser = SecurityUtils.getCurrentUser();

        // 强制转换为微信用户逻辑
        if (loginUser != null && loginUser.isWxUser()) {
            WxUser wxUser = loginUser.getWxUser();
            Long userId = wxUser.getId(); // 拿到微信用户的数据库主键

            //SQL:select * from address_book where user_id = ? and is_default = 1
            AddressBook addressBook = new AddressBook();
            addressBook.setIsDefault(1);
            addressBook.setUserId(userId);
            List<AddressBook> list = addressBookService.list(addressBook);

            if (list != null && list.size() == 1) {
                return Result.success(list.get(0));
            }

            return Result.error("没有查询到默认地址");
        }
        throw new Exception("非微信用户无法清空购物车");


    }



}
