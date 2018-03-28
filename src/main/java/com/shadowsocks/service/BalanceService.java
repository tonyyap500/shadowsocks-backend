package com.shadowsocks.service;

import com.shadowsocks.dto.entity.Balance;

import java.util.Optional;

public interface BalanceService {

    /**
     * 创建空数据
     * */
    boolean createItem(int userId);

    /**
     * 根据用户id增加余额
     * */
    boolean addBalanceByUserId(int userId, double amount);

    /**
     * 根据用户id扣除余额
     * */
    boolean minusBalanceByUserId(int userId, double amount);

    /**
     * 根据用户ID查询余额信息
     * */
    Optional<Balance> findBalanceByUserId(int userId);
}
