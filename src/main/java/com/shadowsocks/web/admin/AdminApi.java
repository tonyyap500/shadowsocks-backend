package com.shadowsocks.web.admin;

import com.shadowsocks.dto.ResponseMessageDto;
import com.shadowsocks.dto.request.ServerRequestDto;
import com.shadowsocks.dto.request.UserBalanceRequestDto;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


@RequestMapping("admin")
public interface AdminApi {

    @ApiOperation(value = "增加节点", tags = "admin")
    @RequestMapping(path = "/addNewServer", method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
    ResponseMessageDto purchaseServer(@RequestBody ServerRequestDto serverRequestDto);

    @ApiOperation(value = "入款", tags = "admin")
    @RequestMapping(path = "/addUserBalance", method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
    void userBalabceServer(@RequestBody UserBalanceRequestDto userBalanceRequestDto);
}
