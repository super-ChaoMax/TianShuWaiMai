package com.chaao.appserver.service.Impl;

import com.chaao.appserver.mapper.WxUserLoginMapper;
import com.chaao.appserver.service.WxUserService;
import com.yourcompany.common.util.XueHuaiID;
import entity.wx.WxUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
public class WxUserServiceImpl implements WxUserService {

    @Autowired
    private WxUserLoginMapper wxUserLoginMapper;

    @Override
    public WxUser getUserByOpenId(String openid) {
        return wxUserLoginMapper.findByOpenid(openid);
    }

    @Override
    public WxUser registerWxUser(String openid) {
        WxUser wxUser = new WxUser();

        // 雪花id自行替换，这里临时UUID转long演示
        long uid = XueHuaiID.generateUserId();
        wxUser.setId(uid);


        wxUser.setOpenid(openid);
        wxUser.setName("微信用户");
        // 为必填字段设置默认值
        wxUser.setStatus(1);      // 假设 1 代表正常状态
        wxUser.setDeleted(0);     // 假设 0 代表未删除
        wxUser.setVersion(1);     // 初始化版本号
        wxUserLoginMapper.insert(wxUser);
        return wxUser;
    }
}