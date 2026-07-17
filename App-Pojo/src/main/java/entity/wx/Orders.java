package entity.wx;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 订单主表实体类
 * 对应数据库表：orders
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Orders implements Serializable {

    private static final long serialVersionUID = 1L;

    //

    /** 雪花全局主键 */
    private Long id;

    /** 外部订单流水号 */
    private String number;

    /** 订单状态：1待付款，2待商家接单，3待配送，4已完成，5已取消，6退款中 */
    private Integer status;

    /** 下单C端用户ID */
    private Long userId;

    /** 收货地址ID (关联 address_book 表) */
    private Long addressBookId;

    /** 【核心快照】收货人姓名 */
    private String consignee;

    /** 【核心快照】联系电话 */
    private String phone;

    /** 【核心快照】详细地址 */
    private String address;

    /** 订单原价总额（菜品+打包费） */
    private BigDecimal amount;

    /** 优惠金额（红包、满减等） */
    private BigDecimal discountAmount;

    /** 用户实付金额 */
    private BigDecimal payAmount;

    /** 累计退款金额 */
    private BigDecimal refundAmount;

    /** 支付方式：1微信支付，2支付宝支付 */
    private Integer payMethod;

    /** 用户下单备注 */
    private String remark;

    /** 【新增】餐具状态：1需要，0不需要 */
    private Integer tablewareStatus;

    /** 【新增】餐具数量 */
    private Integer tablewareNumber;

    /* 配送状态：1立即送出，0预约时间 */
//    private Integer deliveryStatus;

    /* 预计送达时间（用户预约的时间段） */
//    private String estimatedDeliveryTime;

    private LocalDateTime deliveryTime;

    /** 用户提交下单时间 */
    private LocalDateTime orderTime;

    /** 支付完成时间 */
    private LocalDateTime payTime;

    /** 实际出餐时间 */
    private LocalDateTime outTime;

    /** 订单完成/送达时间 */
    private LocalDateTime finishTime;

    /** 订单取消时间 */
    private LocalDateTime cancelTime;

    /** 记录创建时间 */
    private LocalDateTime createTime;

    /** 记录更新时间 */
    private LocalDateTime updateTime;

    /** 【核心】乐观锁版本号，防止并发状态错乱 */
    private Integer version;

    /** 逻辑删除标识：0正常，1删除 */
    private Integer deleted;
}