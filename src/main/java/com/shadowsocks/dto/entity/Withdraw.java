package com.shadowsocks.dto.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Withdraw {
    private int id;
    private int userId;
    private String transactionId;
    private double amount;
    private String channel;
    private String status;
    private String remark;
    private String operator;
    private String createTime;
    private String updateTime;
}
