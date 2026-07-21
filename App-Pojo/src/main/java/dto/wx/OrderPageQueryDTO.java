package dto.wx;

import lombok.Data;

@Data
public class OrderPageQueryDTO {
    // 页码
    private Integer page;
    // 每页条数
    private Integer pageSize;
    // 订单状态 可选
    private Integer status;
}