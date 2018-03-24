package com.shadowsocks.dto.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class LoginDto {

    @ApiModelProperty(value = "用户名或邮箱")
    @NotNull
    private String username;

    @ApiModelProperty(value = "密码")
    @NotNull
    private String password;

    @ApiModelProperty(value = "ipf地址", hidden = true)
    private String ip;
}
