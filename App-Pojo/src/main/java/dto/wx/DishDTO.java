package dto.wx;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class DishDTO {


    // 1. 菜品查询参数
    @Schema(description = "分类id(必须)")
    private Long categoryId;
    @Schema(description = "类型（必须） 1菜品 2套餐")
    private Integer type;

    @Schema(description = "页码")
    private Integer page = 1;  // 默认第一页;
    @Schema(description = "每页数量")
    private Integer pageSize = 1000; // 默认1000条;

    @Schema(description = "状态")
    private Integer status = 1; // 给默认值1，杜绝null


    @Schema(description = "名称")
    private String name;

    private Integer start;



}
