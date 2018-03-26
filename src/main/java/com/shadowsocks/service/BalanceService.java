package com.shadowsocks.service;

public interface BalanceService {

    /**
     * 创建空数据
     * */
    boolean createItem(int userId);

    /**
     * 根据用户id增加余额
     * */
    boolean addBalanceByUserId(int userId, double amount);
}
