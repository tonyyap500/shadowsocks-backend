package com.shadowsocks.web.pay;

import com.shadowsocks.dto.enums.PaymentEnum;
import com.shadowsocks.dto.response.ThirdPartyPayDto;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;



@RequestMapping("pay")
public interface PayApi {

    @ApiOperation(value = "充值", tags = "pay")
    @RequestMapping(path = "/reload/{channel}/{amount}", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
    ThirdPartyPayDto reload(@PathVariable("channel") PaymentEnum paymentEnum, @PathVariable("amount") double amount);

    @ApiOperation(value = "异步回调", tags = "pay")
    @RequestMapping(path = "/callback", method = RequestMethod.GET)
    boolean callback(String transactionId);
}
