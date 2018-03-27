package com.shadowsocks.dto.entity;

import lombok.Data;

@Data
public class PayOrder {
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
