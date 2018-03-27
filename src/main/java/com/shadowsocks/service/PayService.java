package com.shadowsocks.service;

import com.shadowsocks.dto.PaymentDto;
import com.shadowsocks.dto.entity.PayOrder;

import java.util.Optional;

public interface PayService {

    /**
     * 创建充值订单
     * */
    boolean createOrder(PaymentDto paymentDto);

    /**
     * 更新订单状态
     * */
    boolean updateStatus(String id);

    /**
     * 根据订单号查询订单
     * */
    Optional<PayOrder> findOrderByTransactionId(String transactionId);
}
