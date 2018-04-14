package com.shadowsocks.web.admin;

import com.google.common.collect.Lists;
import com.shadowsocks.dto.PaymentOrderResponse;
import com.shadowsocks.dto.ResponseMessageDto;
import com.shadowsocks.dto.entity.*;
import com.shadowsocks.dto.enums.ResultEnum;
import com.shadowsocks.dto.request.LoginDto;
import com.shadowsocks.dto.request.ServerRequestDto;
import com.shadowsocks.dto.response.LoginResponse;
import com.shadowsocks.service.*;
import com.shadowsocks.utils.CacheUtils;
import com.shadowsocks.utils.EmailUtils;
import com.shadowsocks.utils.RandomStringUtils;
import com.shadowsocks.utils.SleepUtils;
import com.shadowsocks.web.BaseController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

@RestController
@Slf4j
public class AdminApiController extends BaseController implements AdminApi {

    private ServerService serverService;
    private PayService payService;
    private BalanceService balanceService;
    private AdminService adminService;

    public AdminApiController(ServerService serverService, PayService payService, BalanceService balanceService, AdminService adminService) {
        this.serverService = serverService;
        this.payService = payService;
        this.balanceService = balanceService;
        this.adminService = adminService;
    }

    private List<Server> transferServerDtoToServerList(ServerRequestDto serverRequestDto) {
        List<Long> portList = LongStream.rangeClosed(serverRequestDto.getMinPort(), serverRequestDto.getMaxPort()).boxed().collect(Collectors.toList());
        return portList.stream().map(port ->
                Server.builder()
                        .country(serverRequestDto.getCountry())
                        .countryInChinese(serverRequestDto.getCountryInChinese())
                        .city(serverRequestDto.getCity())
                        .cityInChinese(serverRequestDto.getCityInChinese())
                        .domain(serverRequestDto.getDomain())
                        .port(port.toString())
                        .currentOwner(0)
                        .password(RandomStringUtils.generatePassword())
                        .build()
        ).collect(Collectors.toList());
    }


    @Override
    public ResponseMessageDto addNewServer(@RequestBody ServerRequestDto serverRequestDto) {
        List<Server> serverList = transferServerDtoToServerList(serverRequestDto);
        serverList.forEach(server -> {
            boolean result = serverService.addNewServer(server);
            if(result) {
                log.info("新增节点，国家{}, 城市{}, 域名{}, 端口{}", serverRequestDto.getCountryInChinese(), serverRequestDto.getCityInChinese(), serverRequestDto.getDomain(), server.getPort());
            }else {
                log.error("新增节点，国家{}, 城市{}, 域名{}, 端口{}", serverRequestDto.getCountryInChinese(), serverRequestDto.getCityInChinese(), serverRequestDto.getDomain(), server.getPort());
            }
            SleepUtils.sleep(500);
        });
        return ResponseMessageDto.builder().result(ResultEnum.SUCCESS).message("添加成功").build();
    }

    @Override
    public ResponseMessageDto checkEmail(String email, String password, String receiver) {
        if(email.contains("@") && email.contains(".")) {
            String username = email.split("@")[0];
            String smtp = "smtp." + email.split("@")[1];

            EmailConfig emailConfig = EmailConfig.builder()
                    .username(username)
                    .password(password)
                    .smtpServer(smtp)
                    .build();

            EmailObject emailObject = EmailObject.builder().toList(Lists.newArrayList(receiver)).subject("测试邮件").content("测试邮件").build();
            EmailUtils.sendEmailAsyc(Lists.newArrayList(emailConfig), emailObject);
            return ResponseMessageDto.builder().result(ResultEnum.SUCCESS).message("请查看日志").build();
        }else {
            return ResponseMessageDto.builder().result(ResultEnum.FAIL).message("邮箱格式不正确").build();
        }
    }

    @Override
    public LoginResponse login(@RequestBody LoginDto loginDto) {
        Optional<Admin> adminOptional = adminService.login(loginDto.getUsername(), loginDto.getPassword());
        log.info("管理员 {} 登录，来源 IP: {}", loginDto.getUsername(), getCurrentIpAddress());
        if(adminOptional.isPresent()) {
            Admin admin = adminOptional.get();
            admin.setAdmin(true);
            String token = RandomStringUtils.generateRandomStringWithMD5();
            CacheUtils.put(token, admin, 3600);
            adminService.updateAdminInfo(admin.getId(), getCurrentIpAddress());
            return LoginResponse.builder().result(ResultEnum.SUCCESS).token(token).message("登录成功").build();
        }
        return LoginResponse.builder().result(ResultEnum.SUCCESS).message("登录失败").build();
    }

    @Override
    public PaymentOrderResponse findPayOrders(String token, int start, int pageSize) {
        Admin admin = getAdmin(token);
        if(!admin.isAdmin()) {
            return PaymentOrderResponse.builder().total(0).payOrderList(Lists.newArrayList()).build();
        }
        int size = pageSize < 50 ? pageSize : 50;
        return payService.findOrders(start, size);
    }

    @Override
    public ResponseMessageDto updateOrder(String token, String transactionId) {
        Admin admin = getAdmin(token);
        if(!admin.isAdmin()) {
            return ResponseMessageDto.builder().result(ResultEnum.FAIL).message("更新失败").build();
        }
        //TODO 用MyBatis事务
        Optional<PayOrder> payOrderOptional = payService.findOrderByTransactionId(transactionId);
        if(payOrderOptional.isPresent()) {
            log.info("{} 标记充值订单 {} 为完成状态", admin.getUsername(), transactionId);
            boolean result = payService.finishOrder(transactionId);
            if(result) {
                PayOrder payOrder = payOrderOptional.get();
                log.info("{} 增加用户 [userId={}] 余额 {} 元", admin.getUsername(), payOrder.getUserId(), payOrder.getAmount());
                balanceService.addBalanceByUserId(payOrder.getUserId(), payOrder.getAmount());
            }
        }
        return ResponseMessageDto.builder().result(ResultEnum.SUCCESS).message("订单状态更新成功").build();
    }
}
