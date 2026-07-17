package dto.wx;

import entity.wx.OrderDetail;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Data
public class OrderSubmitDTO  implements Serializable {
    private Long userId;             // 用户ID (通常从Token/ThreadLocal获取)
    private List<OrderDetail> orderDetails;      // 【核心】前端勾选的购物车记录ID列表
    private Long addressBookId;      // 收货地址ID
    private Integer payMethod;       // 支付方式 (1微信, 2支付宝)
    private String remark;           // 备注
    private Integer deliveryStatus;  // 配送状态 (1立即, 0预约)
    private String estimatedDeliveryTime; // 预计送达时间

    //是否需要餐具的状态
    private Integer tablewareStatus; // 餐具状态 (1需要, 0不需要)
    private Integer tablewareNumber; // 餐具数量

    //新增一个总金额
    private BigDecimal amount;



}