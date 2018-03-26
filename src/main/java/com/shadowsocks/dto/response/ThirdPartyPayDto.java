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
public class ThirdPartyPayDto {
    private ResultEnum result;
    private String message;
    private String url;
}
