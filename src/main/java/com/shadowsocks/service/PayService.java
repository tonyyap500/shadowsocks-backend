package com.shadowsocks.service;

import com.shadowsocks.dto.PaymentDto;
import com.shadowsocks.dto.PaymentOrderResponse;
import com.shadowsocks.dto.entity.PayOrder;

import java.util.List;
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

    /**
     * 根据用户id查询充值订单
     * */
    List<PayOrder> findOrdersByUserId(int userId);

    /**
     * 分页查询订单
     * */
    PaymentOrderResponse findOrders(int start, int pageSize);

    /**
     * 标记订单完成
     * */
    boolean finishOrder(String transactionId);
}
