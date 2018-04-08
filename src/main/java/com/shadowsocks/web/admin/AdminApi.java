package com.shadowsocks.web.admin;

import com.shadowsocks.dto.ResponseMessageDto;
import com.shadowsocks.dto.request.ServerRequestDto;
import com.shadowsocks.dto.request.UserBalanceRequestDto;
import com.shadowsocks.dto.response.PaymentOrderDto;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;


@RequestMapping("admin")
public interface AdminApi {

    @ApiOperation(value = "增加节点", tags = "admin")
    @RequestMapping(path = "/addNewServer", method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
    ResponseMessageDto purchaseServer(@RequestBody ServerRequestDto serverRequestDto);

    @ApiOperation(value = "入款", tags = "admin")
    @RequestMapping(path = "/addUserBalance", method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
    void userBalabceServer(@RequestBody UserBalanceRequestDto userBalanceRequestDto);

    @ApiOperation(value = "根据订单号查看充值订单", tags = "admin")
    @RequestMapping(path = "/getUserOrder", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
    PaymentOrderDto findOrdersByOrderId(String orderId);


    @ApiOperation(value = "根据用户名查看充值订单", tags = "admin")
    @RequestMapping(path = "/getOrderLit", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
    List<PaymentOrderDto> findOrdersList(String userName);
}
