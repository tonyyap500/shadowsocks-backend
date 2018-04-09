package com.shadowsocks.dto.response;


import com.shadowsocks.dto.enums.ResultEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginResponse {

    /**
     * 是否成功
     * */
    private ResultEnum result;

    /**
     * token
     * */
    private String token;

    /**
     * message
     * */
    private String message;
}
