package dto;

import io.swagger.annotations.ApiModelProperty;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class WxLoginDTO {

    /**
     * 微信授权临时code
     */
    @Schema(description = "微信授权临时code(必须)")
    private String code;
    /**
     * 非必传：用户昵称、头像（前端回显/自动注册用）
     */
    @Schema(description = "用户昵称（非必须）")
    private String name;
}