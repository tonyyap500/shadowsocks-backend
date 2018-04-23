package com.shadowsocks.web.withdraw;

import com.shadowsocks.dto.ResponseMessageDto;
import com.shadowsocks.dto.entity.User;
import com.shadowsocks.dto.enums.ResultEnum;
import com.shadowsocks.dto.request.BindBankCardRequestDto;
import com.shadowsocks.dto.request.WithdrawDto;
import com.shadowsocks.service.UserService;
import com.shadowsocks.service.WithdrawService;
import com.shadowsocks.web.BaseController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;


@RestController
@Slf4j
public class WithdrawApiController extends BaseController implements WithdrawApi {

    @Resource
    private UserService userService;

    @Resource
    private WithdrawService withdrawService;

    @Override
    public ResponseMessageDto bindBankCard(@RequestBody BindBankCardRequestDto bindBankCardRequestDto, String token) {
        if(StringUtils.isEmpty(bindBankCardRequestDto.getWithdrawPassword())
                || bindBankCardRequestDto.getWithdrawPassword().length() != 6) {
            return ResponseMessageDto.builder().result(ResultEnum.FAIL).message("密码必须是6位数字").build();
        }

        User user = getUser(token);
        if(!StringUtils.isEmpty(user.getBankCardNo())) {
            return ResponseMessageDto.builder().result(ResultEnum.FAIL).message("您已绑定银行卡，请不要重复绑定").build();
        }

        userService.bindBankCard(user, bindBankCardRequestDto);
        log.info("用户 {} 姓名 {} 绑定银行卡 {}", user.getUsername(), bindBankCardRequestDto.getRealName(), bindBankCardRequestDto.getBankCardNo());
        return ResponseMessageDto.builder().result(ResultEnum.SUCCESS).message("银行卡绑定成功").build();
    }

    @Override
    public ResponseMessageDto withdraw(@RequestBody WithdrawDto withdrawDto, String token) {
        User user = getUser(token);
        if(!withdrawDto.getWithdrawPassword().equals(user.getWithdrawPassword())) {
            return ResponseMessageDto.builder().result(ResultEnum.FAIL).message("提现密码错误").build();
        }

        boolean result = withdrawService.withdraw(user, withdrawDto.getAmount());
        if(!result) {
            return ResponseMessageDto.builder().result(ResultEnum.FAIL).message("申请提现失败，请检查余额是否足够").build();
        }
        return ResponseMessageDto.builder().result(ResultEnum.FAIL).message("申请提现成功，客服将尽快处理您的提现申请").build();
    }
}
