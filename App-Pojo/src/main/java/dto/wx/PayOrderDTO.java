package dto.wx;

import java.math.BigDecimal;
import lombok.Data;

//通用支付入参 DTO（业务层统一入参，不管什么渠道都用这个）
@Data
public class PayOrderDTO {
    // 系统内部订单号（唯一）
    private String orderNo;
    // 支付金额
    private BigDecimal amount;
    // 订单标题
    private String subject;
    // 用户端跳转地址（支付成功后返回页面）
    private String returnUrl;
    // 异步回调地址（公网接口）
    private String notifyUrl;
    // 支付渠道编码：ALI_SANDBOX / ALI_PROD / WX_JSAPI / MOCK
    private String payChannel;
    // 用户id
    private Long userId;
}