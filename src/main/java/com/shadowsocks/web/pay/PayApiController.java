package com.shadowsocks.web.pay;

import com.shadowsocks.config.GlobalConfig;
import com.shadowsocks.dto.PaymentDto;
import com.shadowsocks.dto.entity.PayOrder;
import com.shadowsocks.dto.entity.User;
import com.shadowsocks.dto.enums.PaymentEnum;
import com.shadowsocks.dto.enums.ResultEnum;
import com.shadowsocks.dto.response.ThirdPartyPayDto;
import com.shadowsocks.service.BalanceService;
import com.shadowsocks.service.PayService;
import com.shadowsocks.utils.RandomStringUtils;
import com.shadowsocks.utils.SessionKeyUtils;
import com.shadowsocks.web.BaseController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.Optional;

@RestController
@Slf4j
public class PayApiController extends BaseController implements PayApi{

    private HttpSession session;
    private PayService payService;
    private GlobalConfig globalConfig;
    private BalanceService balanceService;

    public PayApiController(HttpSession session, PayService payService,
                            BalanceService balanceService, GlobalConfig globalConfig) {
        this.session = session;
        this.payService = payService;
        this.balanceService = balanceService;
        this.globalConfig = globalConfig;
    }

    @Override
    public ThirdPartyPayDto reload(@PathVariable("channel") PaymentEnum channel, @PathVariable("amount") double amount) {
        User user = (User) session.getAttribute(SessionKeyUtils.getKeyForUser());
        String transactionId = RandomStringUtils.generateRandomString();
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
                if(balanceUpdate) log.info("用户 {} 余额更新成功，余额变动 {} 元", payOrder.getUserId(), payOrder.getAmount());
            });
            return true;
        }
        return false;
    }
}
