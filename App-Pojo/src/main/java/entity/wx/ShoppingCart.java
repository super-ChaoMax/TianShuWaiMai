package entity.wx;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class ShoppingCart  {

    private static final long serialVersionUID = 1L;

    private Long id;

    // 名称、图片、单价作为冗余字段直接存入
    private String name;
    private String image;
    
    private Long userId;
    private Long dishId;
    private Long setmealId;
    
    // 口味可能是 "微辣,少冰" 这样的字符串，或者是JSON格式
    private String dishFlavor; 
    
    private Integer number;
    private BigDecimal amount;
    
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}