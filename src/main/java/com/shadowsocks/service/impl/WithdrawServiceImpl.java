package com.shadowsocks.service.impl;


import com.shadowsocks.dao.WithdrawDao;
import com.shadowsocks.dto.entity.Balance;
import com.shadowsocks.dto.entity.User;
import com.shadowsocks.dto.entity.Withdraw;
import com.shadowsocks.dto.enums.WithdrawStatusEnum;
import com.shadowsocks.service.BalanceService;
import com.shadowsocks.service.WithdrawService;
import com.shadowsocks.utils.RandomStringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Service
@Slf4j
public class WithdrawServiceImpl implements WithdrawService {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Resource
    private WithdrawDao withdrawDao;

    @Resource
    private BalanceService balanceService;

    //TODO transaction manage
    @Override
    public boolean withdraw(User user, double transactionAmount) {
        Optional<Balance> balanceOptional = balanceService.findBalanceByUserId(user.getId());
        if(balanceOptional.isPresent()) {
            Balance balanceData = balanceOptional.get();
            if(balanceData.getCurrentBalance() <= transactionAmount) {
                log.error("用户 {} 提现失败，余额 {}, 提现金额 {}", user.getUsername(), balanceData.getCurrentBalance(), transactionAmount);
                return false;
            }

            balanceService.minusBalanceByUserId(user.getId(), transactionAmount);
            String currentTime = LocalDateTime.now().format(formatter);
            Withdraw withdraw = Withdraw.builder()
                    .userId(user.getId())
                    .transactionId(RandomStringUtils.generateRandomStringWithMD5())
                    .amount(transactionAmount)
                    .channel("UNION_PAY")
                    .status(WithdrawStatusEnum.PENDING.name())
                    .remark(user.getRealName() + "|" + user.getBankCardNo())
                    .operator("")
                    .createTime(currentTime)
                    .updateTime(currentTime)
                    .build();
            int result = withdrawDao.createOrder(withdraw);
            return result == 1;
        }
        return false;
    }
}
