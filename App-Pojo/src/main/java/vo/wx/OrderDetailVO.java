package vo.wx;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class OrderDetailVO {
    private String name;    // 菜品名
    private Integer number; // 份数
    private BigDecimal amount;// 单价
}