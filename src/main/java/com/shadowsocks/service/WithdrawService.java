package com.shadowsocks.service;

import com.shadowsocks.dto.entity.User;
import com.shadowsocks.dto.entity.Withdraw;
import com.shadowsocks.dto.response.WithdrawRecord;

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


    int getTotal();


    /**
     * 管理员查询提现记录
     * */
    List<WithdrawRecord> findWithdrawOrders(int start, int pageSize);

    /**
     * 标记订单完成
     * */
    boolean finishOrder(String transactionId);

    /**
     * 取消订单
     * */
    boolean cancelOrder(String transactionId);
}
