package vo.wx;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderUserVO {
    private Long id;
    private Integer status;
    private LocalDateTime checkoutTime; // 展示时间
    private BigDecimal payAmount;      // 实付金额
    private List<OrderDetailVO> orderDetails; // 订单商品列表

    //加一个订单创建时间
    private LocalDateTime createTime;
}