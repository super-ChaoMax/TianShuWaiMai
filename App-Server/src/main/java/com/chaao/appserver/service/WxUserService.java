package com.chaao.appserver.service;

import entity.wx.WxUser;

public interface WxUserService {

    /**
     * 根据openid查询用户
     */
    WxUser getUserByOpenId(String openId);

    /**
     * 微信用户自动注册
     */
    WxUser registerWxUser(String openId);
}