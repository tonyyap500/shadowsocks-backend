package com.shadowsocks.service;

import com.shadowsocks.dto.entity.User;

public interface WithdrawService {

    /**
     * 申请提现
     * */
    boolean withdraw(User user, double amount);
}
