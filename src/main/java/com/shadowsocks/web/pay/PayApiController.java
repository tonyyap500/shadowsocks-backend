package com.shadowsocks.web.pay;

import com.shadowsocks.config.GlobalConfig;
import com.shadowsocks.dto.PaymentDto;
import com.shadowsocks.dto.entity.PayOrder;
import com.shadowsocks.dto.entity.User;
import com.shadowsocks.dto.enums.PaymentEnum;
import com.shadowsocks.dto.enums.ResultEnum;
import com.shadowsocks.dto.response.PaymentOrderDto;
import com.shadowsocks.dto.response.ThirdPartyPayDto;
import com.shadowsocks.service.BalanceService;
import com.shadowsocks.service.PayService;
import com.shadowsocks.utils.DecimalUtils;
import com.shadowsocks.utils.RandomStringUtils;
import com.shadowsocks.web.BaseController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@Slf4j
public class PayApiController extends BaseController implements PayApi{

    private PayService payService;
    private GlobalConfig globalConfig;
    private BalanceService balanceService;

    public PayApiController(PayService payService,
                            BalanceService balanceService, GlobalConfig globalConfig) {
        this.payService = payService;
        this.balanceService = balanceService;
        this.globalConfig = globalConfig;
    }

    @Override
    public ThirdPartyPayDto reload(@PathVariable("channel") PaymentEnum channel, @PathVariable("amount") double amount, String token) {
        User user = getUser(token);
        String transactionId = RandomStringUtils.generateRandomStringWithMD5();
        PaymentDto paymentDto = PaymentDto.builder()
                .transactionId(transactionId)
                .userId(user.getId())
                .channel(channel.name())
                .amount(amount)
                .remark("测试")
                .build();
        boolean result = payService.createOrder(paymentDto);
        if(result) {
            log.info("用户 {} 创建充值订单, 金额 {}, 通道 {}", user.getUsername(), amount, channel);
            String urlPrefix = globalConfig.getUrl();
            String url = urlPrefix + "/shadowsocks/pay/callback?transactionId=" + transactionId;
            return ThirdPartyPayDto.builder().result(ResultEnum.SUCCESS).message("生成订单成功，请支付").url(url).build();
        }
        return ThirdPartyPayDto.builder().result(ResultEnum.FAIL).message("生成订单失败").url("").build();
    }

    @Override
    public boolean callback(String transactionId) {
        boolean updateResult = payService.updateStatus(transactionId);
        if(updateResult) {
            log.info("更新订单状态成功， 订单号 {}", transactionId);
            Optional<PayOrder> payOrderOptional = payService.findOrderByTransactionId(transactionId);
            payOrderOptional.ifPresent(payOrder -> {
                boolean balanceUpdate = balanceService.addBalanceByUserId(payOrder.getUserId(), payOrder.getAmount());
                if(balanceUpdate) log.info("用户 [userId={}] 余额更新成功，余额变动 {} 元", payOrder.getUserId(), payOrder.getAmount());
            });
            return true;
        }
        return false;
    }

    //TODO channel和status显示为中文
    @Override
    public List<PaymentOrderDto> findOrdersByUserId(String token) {
        User user = getUser(token);
        List<PayOrder> payOrderList = payService.findOrdersByUserId(user.getId());
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
}
