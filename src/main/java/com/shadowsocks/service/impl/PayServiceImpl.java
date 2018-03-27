package com.shadowsocks.service.impl;

import com.shadowsocks.dao.PayDao;
import com.shadowsocks.dto.PaymentDto;
import com.shadowsocks.dto.entity.PayOrder;
import com.shadowsocks.dto.enums.PayStatusEnum;
import com.shadowsocks.service.PayService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Service
public class PayServiceImpl implements PayService{

    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Resource
    private PayDao payDao;

    @Override
    public boolean createOrder(PaymentDto paymentDto) {
        String time = LocalDateTime.now().format(formatter);
        paymentDto.setStatus(PayStatusEnum.PENDING.name());
        paymentDto.setCreateTime(time);
        int result = payDao.createOrder(paymentDto);
        return result == 1;
    }

    @Override
    public boolean updateStatus(String transactionId) {
        String time = LocalDateTime.now().format(formatter);
        int result = payDao.updateStatus(transactionId, time);
        return result == 1;
    }

    @Override
    public Optional<PayOrder> findOrderByTransactionId(String transactionId) {
        PayOrder payOrder = payDao.findOrderByTransactionId(transactionId);
        return Optional.ofNullable(payOrder);
    }
}
