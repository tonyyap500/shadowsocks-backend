package com.shadowsocks.web.withdraw;

import com.shadowsocks.dto.ResponseMessageDto;
import com.shadowsocks.dto.entity.User;
import com.shadowsocks.dto.enums.ResultEnum;
import com.shadowsocks.dto.request.BindBankCardRequestDto;
import com.shadowsocks.service.UserService;
import com.shadowsocks.utils.CacheUtils;
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
        user.setRealName(bindBankCardRequestDto.getRealName());
        user.setBankCardNo(bindBankCardRequestDto.getBankCardNo());
        user.setWithdrawPassword(bindBankCardRequestDto.getWithdrawPassword());

        CacheUtils.put(token, user, 3600);
        userService.bindBankCard(user);
        log.info("用户 {} 姓名 {} 绑定银行卡 {}", user.getUsername(), user.getRealName(), user.getBankCardNo());
        return ResponseMessageDto.builder().result(ResultEnum.SUCCESS).message("银行卡绑定成功").build();
    }
}
