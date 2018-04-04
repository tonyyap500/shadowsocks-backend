package com.shadowsocks.dto.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentOrderDto {
    private String transactionId;
    private double amount;
    private String channel;
    private String status;
    private String createTime;
    private String updateTime;
}
