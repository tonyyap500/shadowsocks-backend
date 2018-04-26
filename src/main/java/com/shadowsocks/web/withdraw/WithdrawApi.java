package com.shadowsocks.web.withdraw;

import com.shadowsocks.dto.ResponseMessageDto;
import com.shadowsocks.dto.request.BindBankCardRequestDto;
import com.shadowsocks.dto.request.WithdrawDto;
import com.shadowsocks.dto.response.BankAccountDto;
import com.shadowsocks.dto.response.WithdrawRecord;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@RequestMapping("withdraw")
public interface WithdrawApi {

    /**
     * 绑定银行卡
     * */
    @ApiOperation(value = "绑定银行卡", tags = "withdraw")
    @RequestMapping(path = "/bind", method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
    ResponseMessageDto bindBankCard(@RequestBody BindBankCardRequestDto bindBankCardRequestDto, String token);


    /**
     * 查看提现账户
     * */
    @ApiOperation(value = "查看提现账户", tags = "withdraw")
    @RequestMapping(path = "/bankInfo", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
    BankAccountDto bankInfo(String token);


    /**
     * 申请提现
     * */
    @ApiOperation(value = "申请提现", tags = "withdraw")
    @RequestMapping(path = "/withdraw", method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
    ResponseMessageDto withdraw(@RequestBody WithdrawDto withdrawDto, String token);


    /**
     * 查看提现记录
     * */
    @ApiOperation(value = "提现记录", tags = "withdraw")
    @RequestMapping(path = "/withdrawRecord", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
    List<WithdrawRecord> withdrawRecord(String token);


    /**
     * 查看余额
     * */
    @ApiOperation(value = "查看余额", tags = "withdraw")
    @RequestMapping(path = "/checkBalance", method = RequestMethod.GET)
    double checkBalance(String token);
}
