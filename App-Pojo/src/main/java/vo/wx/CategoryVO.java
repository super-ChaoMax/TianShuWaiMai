package vo.wx;

import lombok.Data;

@Data
public class CategoryVO {
//    给前端返回的菜单列表数据

    // 改成String类型，接收数据库里的字符串格式id
//    private Long  id;
    // 原来 private Long id;
    private String id; // 改成String，前端就不会丢失精度了

    private int type;
    private String name;
    private int status;     // 0: 禁用（隐藏）  1: 启用

}
