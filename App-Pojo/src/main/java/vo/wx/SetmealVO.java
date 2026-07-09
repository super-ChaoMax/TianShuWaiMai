package vo.wx;

import lombok.Data;

@Data
public class SetmealVO {
    private Long id;
    private String name;
    private Long categoryId;
    private Double price;
    private String image;
    private String description;
    private Integer status;
}
