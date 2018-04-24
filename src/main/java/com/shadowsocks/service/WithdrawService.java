package com.shadowsocks.service;

import com.shadowsocks.dto.entity.User;
import com.shadowsocks.dto.entity.Withdraw;

import java.util.List;

public interface WithdrawService {

    /**
     * 申请提现
     * */
    boolean withdraw(User user, double amount);

    /**
     * 查看提现记录
     * */
    List<Withdraw> findWithdrawHistory(int userId);
}
