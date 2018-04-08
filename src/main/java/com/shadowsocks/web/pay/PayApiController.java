package com.shadowsocks.web.pay;

import com.shadowsocks.config.GaotongConfig;
import com.shadowsocks.dto.GaotongCallbackDto;
import com.shadowsocks.dto.GaotongPayDto;
import com.shadowsocks.dto.PaymentDto;
import com.shadowsocks.dto.entity.PayOrder;
import com.shadowsocks.dto.entity.User;
import com.shadowsocks.dto.enums.GaotongEnum;
import com.shadowsocks.dto.enums.PaymentEnum;
import com.shadowsocks.dto.enums.ResultEnum;
import com.shadowsocks.dto.response.PaymentOrderDto;
import com.shadowsocks.dto.response.ThirdPartyPayDto;
import com.shadowsocks.service.BalanceService;
import com.shadowsocks.service.PayService;
import com.shadowsocks.utils.DecimalUtils;
import com.shadowsocks.utils.GaotongPayUtils;
import com.shadowsocks.utils.RandomStringUtils;
import com.shadowsocks.web.BaseController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

@RestController
@Slf4j
public class PayApiController extends BaseController implements PayApi{

    private PayService payService;
    private BalanceService balanceService;
    private GaotongConfig gaotongConfig;
    private HttpServletRequest request;

    public PayApiController(PayService payService,GaotongConfig gaotongConfig,
                            BalanceService balanceService, HttpServletRequest request) {
        this.payService = payService;
        this.balanceService = balanceService;
        this.gaotongConfig = gaotongConfig;
        this.request = request;
    }

    @Override
    public ThirdPartyPayDto reload(@PathVariable("channel") PaymentEnum channel, @PathVariable("amount") double amount, String token,String remark) {
        User user = getUser(token);
        String transactionId = RandomStringUtils.generateRandomStringWithMD5();
        PaymentDto paymentDto = PaymentDto.builder()
                .transactionId(transactionId)
                .userId(user.getId())
                .channel(channel.name())
                .amount(amount)
                .remark(user.getUsername())
                .build();
        boolean result = payService.createOrder(paymentDto);
        if(result) {
            log.info("用户 {} 创建充值订单, 金额 {}, 通道 {}", user.getUsername(), amount, channel);
            double randomValue = BigDecimal.valueOf(new Random().nextFloat()).setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
            GaotongPayDto gaotongPayDto = GaotongPayDto.builder()
                    .callbackURL(gaotongConfig.getCallback())
                    .merchantNo(gaotongConfig.getMerchantNo())
                    .money(amount + randomValue)
                    .remark("")
                    .paymentType(GaotongEnum.WEIXIN)
                    .publicKey(gaotongConfig.getPublicKey())
                    .transactionNo(transactionId)
                    .build();
            String url = GaotongPayUtils.buildPaymentURL(gaotongPayDto);
            return ThirdPartyPayDto.builder().result(ResultEnum.SUCCESS).message("生成订单成功，请支付").url(url).build();
        }
        return ThirdPartyPayDto.builder().result(ResultEnum.FAIL).message("生成订单失败").url("").build();
    }

    @Override
    public void callback() {
        Map<String, String> parameterMap = request.getParameterMap().entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, map -> map.getValue()[0]));

        String partner = parameterMap.get("partner");
        String transactionId = parameterMap.get("ordernumber");
        String orderStatus = parameterMap.get("orderstatus");
        String amount = parameterMap.get("paymoney");
        String remark = parameterMap.get("attach");
        String sign = parameterMap.get("sign");

        log.info("收到高通回调信息 订单号{}, 金额{}, 签名{}, 备注{}", partner, amount, sign, remark);
        GaotongCallbackDto callbackDto = GaotongCallbackDto.builder()
                .partner(partner)
                .orderNumber(transactionId)
                .orderStatus(orderStatus)
                .payMoney(amount)
                .key(gaotongConfig.getPublicKey())
                .sign(sign)
                .build();

        boolean checkStatus = GaotongPayUtils.checkSign(callbackDto);
        if(!checkStatus) {
            log.error("高通支付签名校验失败");
            return;
        }

        boolean updateResult = payService.updateStatus(transactionId);
        if(updateResult) {
            log.info("更新订单状态成功， 订单号 {}", transactionId);
            Optional<PayOrder> payOrderOptional = payService.findOrderByTransactionId(transactionId);
            payOrderOptional.ifPresent(payOrder -> {
                boolean balanceUpdate = balanceService.addBalanceByUserId(payOrder.getUserId(), payOrder.getAmount());
                if(balanceUpdate) log.info("用户 [userId={}] 余额更新成功，余额变动 {} 元", payOrder.getUserId(), payOrder.getAmount());
            });
        }
    }

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
