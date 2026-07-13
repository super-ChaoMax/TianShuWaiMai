package dto.wx;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ShoppingCartDTO {

    // 前端传参通常不需要传 user_id (从Token获取)，也不需要传 name/image (后端查库填充)。

    private Long dishId;      // 菜品ID
    private Long setmealId;   // 套餐ID
    private String dishFlavor;// 口味

    private BigDecimal amount;
    private Integer number;
    private String name;
    private String image;
    private Long id;
    
    // 注意：这里不需要 number，通常点击一次就是+1，或者前端传具体数量
}