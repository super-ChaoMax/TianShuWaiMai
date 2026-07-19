package log.wx;

import lombok.Data;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class PayLog {
    private Long id;
    /** 系统订单号 */
    private String orderNo;
    /** 第三方交易号 */
    private String outTradeNo;
    /** 退款单号 */
    private String refundNo;
    /** 金额 */
    private BigDecimal amount;
    /** 支付渠道 */
    private String payChannel;
    /** 日志类型 1支付 2退款 */
    private Integer logType;
    /** 状态 0失败 1成功 */
    private Integer status;
    /** 第三方返回报文 */
    private String resultContent;
    /** 创建时间 */
    private Date createTime;
}