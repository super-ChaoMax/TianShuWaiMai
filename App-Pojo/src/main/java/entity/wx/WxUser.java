package entity.wx;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WxUser {

    //雪花id
    private Long id;

    /**
     * 微信用户唯一标识
     */
    private String openid;

    /**
     * 微信昵称
     */
    private String name;


    /**
     * 微信手机号
     */
    private String phone;

    /**
     * 版本号
     */
    private Integer version;

    /**
     *  是否删除
     */
    private Integer deleted;

    /**
     * 状态
     */
    private Integer status;

    /**
     * 创建时间
     */
    private LocalDateTime createdTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

}
