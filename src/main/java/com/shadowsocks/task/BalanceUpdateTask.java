package com.shadowsocks.task;

import com.google.common.collect.Lists;
import com.shadowsocks.dto.entity.Balance;
import com.shadowsocks.dto.entity.Server;
import com.shadowsocks.service.BalanceService;
import com.shadowsocks.service.ServerService;
import com.shadowsocks.utils.RandomStringUtils;
import com.shadowsocks.utils.SleepUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@Slf4j
public class BalanceUpdateTask {

    private ServerService serverService;
    private BalanceService balanceService;

    public BalanceUpdateTask(ServerService serverService, BalanceService balanceService) {
        this.serverService = serverService;
        this.balanceService = balanceService;
    }

    private List<Server> getAvailableServerList() {
        List<Server> serverList = Lists.newLinkedList();
        int pageSize = 1;
        int start = 0;
        List<Server> list = serverService.findServers(start, pageSize);
        while (!CollectionUtils.isEmpty(list)) {
            serverList.addAll(list);
            start = start + pageSize;
            list = serverService.findServers(start, pageSize);
        }
        return serverList;
    }

    /**
     * 每天凌晨3点刷新余额
     * */
    @Scheduled(cron = "0 0 3 * * *")
    public void updateBalanceTask() {
        List<Server> serverList = getAvailableServerList();
        List<Integer> userIdList = serverList.stream().map(Server::getCurrentOwner).collect(Collectors.toList());
        userIdList.forEach(userId -> {
            log.info("扣除用户 [userId={}] 余额 {} 元", userId, 0.3);
            balanceService.minusBalanceByUserId(userId, 0.3);
            SleepUtils.sleep(500);
            Optional<Balance> balanceOptional = balanceService.findBalanceByUserId(userId);
            balanceOptional.ifPresent(balance -> {
                if(balance.getCurrentBalance() < 0.3) {
                    String newPassword = RandomStringUtils.generatePassword();
                    serverService.releaseServer(balance.getUserId(), newPassword);
                    log.info("回收节点 [userId={}], 新密码 {}", balance.getUserId(), newPassword);
                    SleepUtils.sleep(500);
                }
            });
        });
    }
}
