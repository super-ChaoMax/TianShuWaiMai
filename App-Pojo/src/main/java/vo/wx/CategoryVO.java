package vo.wx;

import lombok.Data;

@Data
public class CategoryVO {
//    给前端返回的菜单列表数据

    private Long id;
    private int type;
    private String name;
    private int status;     // 0: 禁用（隐藏）  1: 启用

}
