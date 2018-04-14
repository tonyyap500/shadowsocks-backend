package com.shadowsocks.web.admin;

import com.shadowsocks.dto.PaymentOrderResponse;
import com.shadowsocks.dto.ResponseMessageDto;
import com.shadowsocks.dto.request.LoginDto;
import com.shadowsocks.dto.request.ServerRequestDto;
import com.shadowsocks.dto.response.LoginResponse;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;



@RequestMapping("admin")
public interface AdminApi {

    @ApiOperation(value = "登录", tags = "admin")
    @RequestMapping(path = "/login", method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
    LoginResponse login(@RequestBody LoginDto loginDto);

    @ApiOperation(value = "增加节点", tags = "admin")
    @RequestMapping(path = "/addNewServer", method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
    ResponseMessageDto addNewServer(@RequestBody ServerRequestDto serverRequestDto);

    @ApiOperation(value = "检查邮箱是否正常", tags = "admin")
    @RequestMapping(path = "/checkEmail", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
    ResponseMessageDto checkEmail(String email, String password, String receiver);

    @ApiOperation(value = "查询充值订单", tags = "admin")
    @RequestMapping(path = "/findOrders", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
    PaymentOrderResponse findPayOrders(String token, int start, int pageSize);

    enum OrderStatus {
        CANCELLED,
        FINISHED
    }
    @ApiOperation(value = "标记为完成状态", tags = "admin")
    @RequestMapping(path = "/updateOrder", method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
    ResponseMessageDto updateOrder(String token, String transactionId, OrderStatus orderStatus);
}
