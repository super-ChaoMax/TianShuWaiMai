package dto.wx;

import lombok.Data;
import java.math.BigDecimal;

/**
 * 统一退款通用DTO
 * 支付宝/微信/Mock/银联 退款共用这一套参数
 */
@Data
public class RefundDTO {

    /**
     * 系统内部订单号
     */
    private String orderNo;

    /**
     * 第三方支付交易号（支付成功返回的outTradeNo）
     */
    private String outTradeNo;

    /**
     * 退款金额
     */
    private BigDecimal refundAmount;

    /**
     * 退款原因
     */
    private String refundReason;

    /**
     * 支付渠道编码 MOCK/ALI_SANDBOX/WX_JSAPI
     */
    private String payChannel;

    /**
     * 商户退款单号（自己系统生成唯一退款号）
     */
    private String refundNo;
}