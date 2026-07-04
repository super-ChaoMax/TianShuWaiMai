package com.chaao.appserver.util;

import com.alibaba.fastjson2.JSON;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson2.JSON;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.util.Map;


// 用于调用微信 code2session 接口
@Component
public class WxApiUtil {

    @Value("${wx.miniapp.appid}")
    private String appId;

    @Value("${wx.miniapp.secret}")
    private String appSecret;

    /**
     * 用临时code换取 openid、session_key
     */
    public Map<String, Object> code2Session(String code) {
        String url = String.format(
                "https://api.weixin.qq.com/sns/jscode2session?appid=%s&secret=%s&js_code=%s&grant_type=authorization_code",
                appId, appSecret, code
        );
        String response = HttpUtil.get(url);
        return JSON.parseObject(response, Map.class);
    }

    /**
     * 提取openid
     */
    public String getOpenId(String code) {
        Map<String, Object> result = code2Session(code);
        if (result.containsKey("errcode") && !"0".equals(result.get("errcode").toString())) {
            throw new RuntimeException("微信授权失败：" + result.get("errmsg"));
        }
        return (String) result.get("openid");
    }
}