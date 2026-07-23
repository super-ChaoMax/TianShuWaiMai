package entity.admin.category;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class Dish {
    private Long id;
    private String name;
    private Long categoryId;
    private Double price;
    private String image;
    private String description;
    private Integer status;
    private String flavorJson;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private Long version;
    private Integer deleted;
}