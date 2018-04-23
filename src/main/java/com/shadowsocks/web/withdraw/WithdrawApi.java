package com.shadowsocks.web.withdraw;

import com.shadowsocks.dto.ResponseMessageDto;
import com.shadowsocks.dto.request.BindBankCardRequestDto;
import com.shadowsocks.dto.request.WithdrawDto;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@RequestMapping("withdraw")
public interface WithdrawApi {

    /**
     * 绑定银行卡
     * */
    @ApiOperation(value = "绑定银行卡", tags = "withdraw")
    @RequestMapping(path = "/bind", method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
    ResponseMessageDto bindBankCard(@RequestBody BindBankCardRequestDto bindBankCardRequestDto, String token);


    /**
     * 申请提现
     * */
    @ApiOperation(value = "申请提现", tags = "withdraw")
    @RequestMapping(path = "/withdraw", method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
    ResponseMessageDto withdraw(@RequestBody WithdrawDto withdrawDto, String token);
}
