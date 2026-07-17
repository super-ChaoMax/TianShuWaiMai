package entity.wx;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 订单商品明细表实体类
 * 对应数据库表：order_detail
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class    OrderDetail implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 雪花主键 */
    private Long id;

    /** 关联的订单主表ID */
    private Long orderId;

    /** 单品菜品ID (如果是套餐，此字段为NULL) */
    private Long dishId;

    /** 套餐ID (如果是单品，此字段为NULL) */
    private Long setmealId;

    /** 【核心快照】下单时的商品名称 */
    private String name;

    /** 【核心快照】下单时的商品图片 */
    private String image;

    /** 购买份数 */
    private Integer number;

    /** 当前商品小计金额 (数量 × 单价) */
    private BigDecimal amount;

    /** 记录创建时间 */
    private LocalDateTime createTime;

    /** 逻辑删除标识：0正常，1删除 */
    private Integer deleted;
}