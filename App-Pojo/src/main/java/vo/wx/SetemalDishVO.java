package vo.wx;

import lombok.Data;

@Data
public class SetemalDishVO {
    // 套餐菜品（返回给前端的）
    private String name;
    private String image;
    private int copies;
    private String description;
}
