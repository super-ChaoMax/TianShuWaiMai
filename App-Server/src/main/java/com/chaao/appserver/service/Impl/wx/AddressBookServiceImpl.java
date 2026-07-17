package com.chaao.appserver.service.Impl.wx;


import com.chaao.appserver.mapper.wx.AddressBookMapper;
import com.chaao.appserver.service.AddressBookService;
import com.chaao.appserver.service.SpringSecurity.LoginUser;
import com.chaao.appserver.util.SecurityUtils;
import com.yourcompany.common.util.XueHuaiID;
import entity.wx.AddressBook;
import entity.wx.WxUser;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vo.Result;

import java.util.List;

@Service
@Slf4j
public class AddressBookServiceImpl implements AddressBookService {
    @Autowired
    private AddressBookMapper addressBookMapper;

    /**
     * 条件查询
     *
     * @param addressBook
     * @return
     */
    public List<AddressBook> list(@Param("addressBook") AddressBook addressBook) {
        return addressBookMapper.list(addressBook);
    }

    /**
     * 新增地址
     *
     * @param addressBook
     */
    public void save(@Param("addressBook") AddressBook addressBook) {

        LoginUser loginUser = SecurityUtils.getCurrentUser();

        // 强制转换为微信用户逻辑
        if (loginUser != null && loginUser.isWxUser()) {
            WxUser wxUser = loginUser.getWxUser();
            Long userId = wxUser.getId(); // 拿到微信用户的数据库主键
            // 执行 SQL 删除...

            // 1. 如果前端没传 id，使用雪花算法生成一个
            if (addressBook.getId() == null) {
                // IdWorker 或 SnowflakeIdGenerator 是你项目里的雪花算法工具类
                addressBook.setId(XueHuaiID.generateUserId());
            }

            addressBook.setUserId(userId);
            addressBook.setIsDefault(0);
            addressBookMapper.insert(addressBook);
        }


    }

    /**
     * 根据id查询
     *
     * @param id
     * @return
     */
    public AddressBook getById(@Param("id") Long id) {
        AddressBook addressBook = addressBookMapper.getById(id);
        return addressBook;
    }

    /**
     * 根据id修改地址
     *
     * @param addressBook
     */
    public void update(@Param("addressBook") AddressBook addressBook) {
        addressBookMapper.update(addressBook);
    }

    /**
     * 设置默认地址
     *
     * @param addressBook
     */
    @Transactional
    public void setDefault(@Param("addressBook") AddressBook addressBook) {

        LoginUser loginUser = SecurityUtils.getCurrentUser();
        // 强制转换为微信用户逻辑
        if (loginUser != null && loginUser.isWxUser()) {
            WxUser wxUser = loginUser.getWxUser();
            Long userId = wxUser.getId(); // 拿到微信用户的数据库主键
            // 执行 SQL 删除...
            //1、将当前用户的所有地址修改为非默认地址 update address_book set is_default = ? where user_id = ?
            addressBook.setIsDefault(0);
            addressBook.setUserId(userId);
            addressBookMapper.updateIsDefaultByUserId(addressBook);

            //2、将当前地址改为默认地址 update address_book set is_default = ? where id = ?
            addressBook.setIsDefault(1);
            addressBookMapper.update(addressBook);
        }



    }

    /**
     * 根据id删除地址
     *
     * @param id
     */
    public void deleteById(@Param("id") Long id) {
        addressBookMapper.deleteById(id);
    }

}
