package com.shadowsocks.dto.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WithdrawRecord {
    private int id;
    private int userId;
    private String transactionId;
    private double amount;
    private String channel;
    private String status;
    private String remark;
    private String createTime;
    private String updateTime;
}
