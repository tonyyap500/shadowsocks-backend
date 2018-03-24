package com.shadowsocks.dto.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class RegisterDto {
    @ApiModelProperty(value = "用户名")
    @NotNull
    private String username;

    @ApiModelProperty(value = "邮箱")
    @NotNull
    private String email;

    @ApiModelProperty(value = "密码")
    @NotNull
    private String password;

    @ApiModelProperty(value = "邀请码")
    private Integer inviter;

    @ApiModelProperty(value = "验证码")
    @NotNull
    private String captcha;
}
