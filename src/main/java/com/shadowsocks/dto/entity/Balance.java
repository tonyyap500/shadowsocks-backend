package com.shadowsocks.dto.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Balance {
    private int id;
    private int userId;
    private double currentBalance;
    private String createTime;
    private String updateTime;
}
