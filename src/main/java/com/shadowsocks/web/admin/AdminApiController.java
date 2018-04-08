package com.shadowsocks.web.admin;

import com.google.common.collect.Lists;
import com.shadowsocks.dto.ResponseMessageDto;
import com.shadowsocks.dto.entity.EmailConfig;
import com.shadowsocks.dto.entity.EmailObject;
import com.shadowsocks.dto.entity.PayOrder;
import com.shadowsocks.dto.entity.Server;
import com.shadowsocks.dto.enums.ResultEnum;
import com.shadowsocks.dto.request.ServerRequestDto;
import com.shadowsocks.dto.request.UserBalanceRequestDto;
import com.shadowsocks.dto.response.PaymentOrderDto;
import com.shadowsocks.exception.BusinessException;
import com.shadowsocks.service.BalanceService;
import com.shadowsocks.service.PayService;
import com.shadowsocks.service.ServerService;
import com.shadowsocks.service.UserService;
import com.shadowsocks.utils.DecimalUtils;
import com.shadowsocks.utils.EmailUtils;
import com.shadowsocks.utils.RandomStringUtils;
import com.shadowsocks.utils.SleepUtils;
import com.shadowsocks.web.BaseController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

@RestController
@Slf4j
public class AdminApiController extends BaseController implements AdminApi {

    @Resource
    private ServerService serverService;

    @Resource
    private PayService payService;

    @Resource
    private BalanceService balanceService;

    @Resource
    private UserService userService;



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
    public ResponseMessageDto purchaseServer(@RequestBody ServerRequestDto serverRequestDto) {
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
    public void userBalabceServer(UserBalanceRequestDto userBalanceRequestDto) {
        Optional<PayOrder> order= payService.findOrderByTransactionId(userBalanceRequestDto.getOrderId());
        if(order.isPresent()){
            if(!order.get().getStatus().equals("PENDING")){
                throw new BusinessException("订单已经处理，订单号："+userBalanceRequestDto.getOrderId()+"金额："+ userBalanceRequestDto.getAmount());

            }
            if(userBalanceRequestDto.getAmount()==order.get().getAmount()){
            payService.updateStatus(userBalanceRequestDto.getOrderId());

            balanceService.addBalanceByUserId(order.get().getUserId(),order.get().getAmount());
            }else {
                log.error("订单号与入款金额不符，订单号{}, 金额{},", userBalanceRequestDto.getOrderId(), userBalanceRequestDto.getAmount());
                throw new BusinessException("订单号与入款金额不符，订单号："+ userBalanceRequestDto.getOrderId()+ "金额："+userBalanceRequestDto.getAmount());
            }

        }else {
            log.error("没有查询到订单，订单号{}, 金额{},", userBalanceRequestDto.getOrderId(), userBalanceRequestDto.getAmount());

            throw new BusinessException("没有查询到订单，订单号："+userBalanceRequestDto.getOrderId()+"金额："+ userBalanceRequestDto.getAmount());

        }

    }

    @Override
    public List<PaymentOrderDto> findOrdersList(String userName) {
        int id=userService.findUserIdByUsername(userName);
        List<PayOrder> payOrderList = payService.findOrdersByUserId(id);
        return payOrderList.stream().map(payOrder ->
                PaymentOrderDto.builder()
                        .transactionId(payOrder.getTransactionId())
                        .amount(DecimalUtils.halfRoundUp(payOrder.getAmount()))
                        .channel(payOrder.getChannel())
                        .status(payOrder.getStatus())
                        .createTime(payOrder.getCreateTime())
                        .updateTime(payOrder.getUpdateTime())
                        .build()
        ).collect(Collectors.toList());
    }

    @Override
    public PaymentOrderDto findOrdersByOrderId(String orderId) {

        Optional<PayOrder> payOrder=payService.findOrderByTransactionId(orderId);

        return  PaymentOrderDto.builder()
                .transactionId(payOrder.get().getTransactionId())
                .amount(DecimalUtils.halfRoundUp(payOrder.get().getAmount()))
                .channel(payOrder.get().getChannel())
                .status(payOrder.get().getStatus())
                .createTime(payOrder.get().getCreateTime())
                .updateTime(payOrder.get().getUpdateTime())
                .build();
    }

    @Override
    public ResponseMessageDto checkEmail(String email, String password) {
        if(email.contains("@") && email.contains(".")) {
            String username = email.split("@")[0];
            String smtp = "smtp." + email.split("@")[1];

            EmailConfig emailConfig = EmailConfig.builder()
                    .username(username)
                    .password(password)
                    .smtpServer(smtp)
                    .build();

            String to = "renjie373270@gmail.com";
            EmailObject emailObject = EmailObject.builder().toList(Lists.newArrayList(to)).subject("测试邮件").content("测试邮件").build();
            EmailUtils.sendEmailAsyc(Lists.newArrayList(emailConfig), emailObject);
            return ResponseMessageDto.builder().result(ResultEnum.SUCCESS).message("请查看日志").build();
        }else {
            return ResponseMessageDto.builder().result(ResultEnum.FAIL).message("邮箱格式不正确").build();
        }
    }
}
