package entity.admin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * WebSocket 统一推送消息实体
 * 前后端约定固定格式，方便解析扩展
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WsOrderMsg {

    /**
     * 消息类型标识
     * NEW_ORDER：新订单提醒
     * ORDER_STATUS：订单状态变更
     * 可自行扩展更多业务类型
     */
    private String type;

    /**
     * 新订单数量
     */
    private Integer orderCount;

    /**
     * 最新订单编号
     */
    private String orderNo;

    /**
     * 自定义消息内容（预留扩展字段）
     */
    private String msg;
}