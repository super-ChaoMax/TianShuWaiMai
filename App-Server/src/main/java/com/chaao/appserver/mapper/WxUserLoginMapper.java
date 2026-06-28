package com.chaao.appserver.mapper;

import entity.wx.WxUser;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface WxUserLoginMapper {

    // 根据微信用户唯一标识查询微信用户
    WxUser findByOpenid(String openid);

    // 插入微信用户
    int insert(WxUser wxUser);
}
