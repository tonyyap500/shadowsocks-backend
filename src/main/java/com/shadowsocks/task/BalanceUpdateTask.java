package com.shadowsocks.task;

import com.google.common.collect.Lists;
import com.shadowsocks.dto.entity.*;
import com.shadowsocks.service.BalanceService;
import com.shadowsocks.service.EmailService;
import com.shadowsocks.service.ServerService;
import com.shadowsocks.service.UserService;
import com.shadowsocks.utils.EmailUtils;
import com.shadowsocks.utils.HtmlUtils;
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
    private UserService userService;
    private EmailService emailService;

    public BalanceUpdateTask(ServerService serverService, BalanceService balanceService, UserService userService, EmailService emailService) {
        this.serverService = serverService;
        this.balanceService = balanceService;
        this.userService = userService;
        this.emailService = emailService;
    }

    private List<Server> getAvailableServerList() {
        List<Server> serverList = Lists.newLinkedList();
        int pageSize = 100;
        int start = 0;
        List<Server> list = serverService.findServers(start, pageSize);
        while (!CollectionUtils.isEmpty(list)) {
            serverList.addAll(list);
            start = start + pageSize;
            list = serverService.findServers(start, pageSize);
            SleepUtils.sleep(500);
        }
        return serverList;
    }

    /**
     * 每天凌晨3点刷新余额
     * */
    @Scheduled(cron = "0 0 3 * * *")
    public void updateBalanceTask() {
        //TODO 优化这里
        List<Server> serverList = getAvailableServerList();
        List<Integer> userIdList = serverList.stream().map(Server::getCurrentOwner).collect(Collectors.toList());
        userIdList.forEach(userId -> {
            log.info("扣除用户 [userId={}] 余额 {} 元", userId, 0.3);
            balanceService.minusBalanceByUserId(userId, 0.3);
            SleepUtils.sleep(200);
            Optional<Balance> balanceOptional = balanceService.findBalanceByUserId(userId);
            balanceOptional.ifPresent(balance -> {
                if(balance.getCurrentBalance() < 0.3) {
                    String newPassword = RandomStringUtils.generatePassword();
                    serverService.releaseServer(balance.getUserId(), newPassword);
                    log.info("回收节点 [userId={}], 新密码 {}", balance.getUserId(), newPassword);
                    SleepUtils.sleep(200);
                }
            });
        });
    }

    private void notifyUserToReload(String email) {
        List<EmailConfig> emailConfigList = emailService.findEmailConfigs();
        String content = HtmlUtils.getNotifyToReloadHtml();
        EmailObject emailObject = EmailObject.builder().toList(Lists.newArrayList(email)).subject("404Here 充值提醒").content(content).build();
        EmailUtils.sendEmailAsyc(emailConfigList, emailObject);
    }

    /**
     * 邮件充值提醒服务
     * */
    @Scheduled(cron = "0 30 4 * * *")
    public void balanceNotify() {
        int pageSize = 100;
        int start = 0;
        List<Server> list = serverService.findServers(start, pageSize);
        while (!CollectionUtils.isEmpty(list)) {
            list.forEach(server -> {
                Optional<Balance> balanceOptional = balanceService.findBalanceByUserId(server.getCurrentOwner());
                Balance balance = balanceOptional.get();
                if(balance.getCurrentBalance() <= 0.6) {
                    Optional<User> userOptional = userService.findUserById(balance.getUserId());
                    String email = userOptional.get().getEmail();
                    log.info("正在向用户 {} 发送充值提醒", userOptional.get().getUsername());
                    notifyUserToReload(email);
                    SleepUtils.sleep(10000);
                }
                SleepUtils.sleep(200);
            });
            start = start + pageSize;
            list = serverService.findServers(start, pageSize);
        }
    }
}
