package vo.wx;

import cn.hutool.json.JSON;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
//@Builder
public class DishVO {
//    返回给前端的
    private Long id; // 菜品id,
    private String name; // 菜品名,
    private Double price; // 价格,
    private String image; // 图片地址,
    // 菜品其他业务字段

//    介绍
    private String description;
    private Integer status; // 状态
    private Long categoryId;


//    ---重点：有可有可选的规格 ------
// 用List<Flavor>接收JSON数组，前端是用flavors，而数据库字段是flavor_json，所以记得映射
    private List<Flavor> flavors;

    // 内部类，对应口味规格结构
    @Data
    public static class Flavor {
        private String name; // 规格名，如"辣度"
        private List<String> values; // 选项，如["微辣", "中辣"]
    }


    //SQL 查询结果无法自动映射到 List<Flavor>
    //你现在的代码存在一个类型不匹配的问题：
    //数据库：flavor_json 字段存的是 字符串（JSON 格式的文本）。
    //Java 对象：DishVO 里的 flavorJson 字段定义的是 List<Flavor> 对象。
    //MyBatis 的原生能力无法自动把数据库里的“JSON 字符串”转换成 Java 的“List 对象”。 它只会尝试把字符串赋值给 List，结果就是 null 或者报错。


    // --------- 所以我们要自定义<resultMap>Mybaits映射返回值 ---------
}
