package vo.wx;

import lombok.Data;

//统一返回结果 VO（屏蔽渠道差异）
@Data
public class PayRespVO {
    // 支付跳转链接 / 二维码地址
    private String payUrl;
    // 第三方支付单号（支付宝/微信交易号）
    private String outTradeNo;
    // 渠道编码
    private String payChannel;
    // 状态：CREATE-已创建 PAYING-支付中 SUCCESS-成功 FAIL-失败
    private String payStatus;

    // 新增：判断是否成功
    public boolean isSuccess(){
        return "SUCCESS".equals(this.payStatus);
    }

    // 新增：存放第三方原始返回报文
    private String body;
}