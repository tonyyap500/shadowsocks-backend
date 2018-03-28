package com.shadowsocks.service.impl;

import com.shadowsocks.dao.BalanceDao;
import com.shadowsocks.dto.entity.Balance;
import com.shadowsocks.service.BalanceService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Service
public class BalanceServiceImpl implements BalanceService {

    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Resource
    private BalanceDao balanceDao;

    @Override
    public boolean createItem(int userId) {
        String time = LocalDateTime.now().format(formatter);
        int result = balanceDao.createItem(userId, time);
        return result == 1;
    }

    @Override
    public boolean addBalanceByUserId(int userId, double amount) {
        String updateTime = LocalDateTime.now().format(formatter);
        int result = balanceDao.addBalanceByUserId(userId, amount, updateTime);
        return result == 1;
    }

    @Override
    public Optional<Balance> findBalanceByUserId(int userId) {
        Balance balance = balanceDao.findBalanceByUserId(userId);
        return Optional.ofNullable(balance);
    }

    @Override
    public boolean minusBalanceByUserId(int userId, double amount) {
        String updateTime = LocalDateTime.now().format(formatter);
        int result = balanceDao.minusBalanceByUserId(userId, amount, updateTime);
        return result == 1;
    }
}
