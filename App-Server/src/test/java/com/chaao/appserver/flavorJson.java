package com.chaao.appserver;

//import cn.hutool.core.lang.TypeReference;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference; // <--- 【关键修改】这里必须用 Jackson 的 TypeReference
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import vo.wx.DishVO;

import java.util.List;

@SpringBootTest
public class flavorJson {

//    测试菜品规格JSON 反序列化

    @Test
    void testFlavorJson() throws JsonProcessingException {

        String jsonStr = "[{\"name\":\"辣度\",\"values\":[\"微辣\",\"中辣\"]}]";

        ObjectMapper mapper = new ObjectMapper();

        // 现在 readValue 可以识别这个 TypeReference 了
        List<DishVO.Flavor> flavors = mapper.readValue(
                jsonStr,
                new TypeReference<List<DishVO.Flavor>>() {}
        );

        System.out.println(flavors);// 应输出 [{name='辣度', values=[微辣, 中辣]}]

    }

}
