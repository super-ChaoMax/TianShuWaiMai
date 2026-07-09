package dto.wx;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class SetmealDTO {
//    前端请求参数
    @Schema(description = "分类id(必须)")
    private Long categoryId;
    @Schema(description = "类型（非必须默认2） 1菜品 2套餐")
    private Integer type=2;
    @Schema(description = "页码")
    private Integer page = 1;
    @Schema(description = "每页数量")
    private Integer pageSize = 100;
    @Schema(description = "状态")
    private Integer status = 1;

    // 查询参数偏移量
    private Integer start;
}
