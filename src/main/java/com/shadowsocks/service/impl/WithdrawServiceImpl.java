package com.shadowsocks.service.impl;


import com.google.common.collect.Lists;
import com.shadowsocks.dao.WithdrawDao;
import com.shadowsocks.dto.entity.Balance;
import com.shadowsocks.dto.entity.User;
import com.shadowsocks.dto.entity.Withdraw;
import com.shadowsocks.dto.enums.WithdrawStatusEnum;
import com.shadowsocks.dto.response.WithdrawRecord;
import com.shadowsocks.service.BalanceService;
import com.shadowsocks.service.WithdrawService;
import com.shadowsocks.utils.RandomStringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    @Override
    public List<Withdraw> findWithdrawHistory(int userId) {
        List<Withdraw> withdrawList = withdrawDao.findWithdrawHistory(userId);
        if(!CollectionUtils.isEmpty(withdrawList)) {
            return withdrawList;
        }
        return Lists.newArrayList();
    }

    private WithdrawRecord buildWithdrawRecord(Withdraw withdraw) {
        return WithdrawRecord.builder()
                .id(withdraw.getId())
                .userId(withdraw.getUserId())
                .transactionId(withdraw.getTransactionId())
                .amount(withdraw.getAmount())
                .channel(withdraw.getChannel())
                .status(withdraw.getStatus())
                .remark(withdraw.getRemark())
                .createTime(withdraw.getCreateTime())
                .updateTime(withdraw.getUpdateTime())
                .build();
    }

    @Override
    public int getTotal() {
        return withdrawDao.getTotal();
    }

    @Override
    public List<WithdrawRecord> findWithdrawOrders(int start, int pageSize) {
        List<Withdraw> withdrawList = withdrawDao.findWithdrawOrders(start, pageSize);
        return withdrawList.stream().map(this::buildWithdrawRecord).collect(Collectors.toList());
    }

    @Override
    public boolean finishOrder(String transactionId) {
        String now = LocalDateTime.now().format(formatter);
        int result = withdrawDao.finishOrder(now, transactionId);
        return result == 1;
    }

    @Override
    public boolean cancelOrder(String transactionId) {
        //TODO 考虑是否要将修改余额
        String now = LocalDateTime.now().format(formatter);
        int result = withdrawDao.cancelOrder(now, transactionId);
        return result == 1;
    }
}
