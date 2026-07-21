package dto.admin.category;

import lombok.Data;

@Data
public class CategoryPageQueryDTO {
    private Integer page = 1;
    private Integer pageSize = 10;
    private Integer type;
    private String name;
    private Long categoryId;

    // 偏移量，手动计算
    private Integer offset;
}