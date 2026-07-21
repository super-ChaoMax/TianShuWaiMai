package entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class OrderStatusLog {
    /**
     * 雪花主键
     */
    private Long id;

    /**
     * 关联订单主表ID
     */
    private Long orderId;

    /**
     * 变更前状态
     */
    private Integer fromStatus;

    /**
     * 变更后状态
     */
    private Integer toStatus;

    /**
     * 操作人类型：1-用户,2-商家,3-骑手,4-系统
     */
    private Integer operatorType;

    /**
     * 操作人ID
     */
    private Long operatorId;

    /**
     * 操作备注
     */
    private String remark;

    /**
     * 状态变更时间
     */
    private LocalDateTime createTime;
}