package com.shadowsocks.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentDto {
    private int userId;
    private double amount;
    private String channel;
    private String status;
    private String remark;
    private String createTime;
}
