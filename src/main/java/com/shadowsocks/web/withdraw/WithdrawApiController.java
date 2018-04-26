package com.shadowsocks.web.withdraw;

import com.shadowsocks.dto.ResponseMessageDto;
import com.shadowsocks.dto.entity.Balance;
import com.shadowsocks.dto.entity.User;
import com.shadowsocks.dto.entity.Withdraw;
import com.shadowsocks.dto.enums.ResultEnum;
import com.shadowsocks.dto.request.BindBankCardRequestDto;
import com.shadowsocks.dto.request.WithdrawDto;
import com.shadowsocks.dto.response.BankAccountDto;
import com.shadowsocks.dto.response.WithdrawRecord;
import com.shadowsocks.service.BalanceService;
import com.shadowsocks.service.UserService;
import com.shadowsocks.service.WithdrawService;
import com.shadowsocks.utils.DecimalUtils;
import com.shadowsocks.web.BaseController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@RestController
@Slf4j
public class WithdrawApiController extends BaseController implements WithdrawApi {

    private UserService userService;
    private WithdrawService withdrawService;
    private BalanceService balanceService;

    public WithdrawApiController(UserService userService, WithdrawService withdrawService, BalanceService balanceService) {
        this.userService = userService;
        this.withdrawService = withdrawService;
        this.balanceService = balanceService;
    }

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
    public BankAccountDto bankInfo(String token) {
        User user = getUser(token);
        BankAccountDto bankAccountDto = new BankAccountDto();
        bankAccountDto.setName(user.getRealName());
        bankAccountDto.setBankCardNo(user.getBankCardNo());
        return bankAccountDto;
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


    private WithdrawRecord buildWithdrawRecord(Withdraw withdraw) {
        return WithdrawRecord.builder()
                .id(withdraw.getId())
                .transactionId(withdraw.getTransactionId())
                .amount(DecimalUtils.roundDown(withdraw.getAmount()))
                .channel(withdraw.getChannel())
                .status(withdraw.getStatus())
                .remark(withdraw.getRemark())
                .createTime(withdraw.getCreateTime())
                .updateTime(StringUtils.isEmpty(withdraw.getUpdateTime()) ? "" : withdraw.getUpdateTime())
                .build();
    }

    @Override
    public List<WithdrawRecord> withdrawRecord(String token) {
        User user = getUser(token);
        List<Withdraw> withdrawList = withdrawService.findWithdrawHistory(user.getId());
        return withdrawList.stream().map(this::buildWithdrawRecord).collect(Collectors.toList());
    }

    @Override
    public double checkBalance(String token) {
        User user = getUser(token);
        Optional<Balance> balanceOptional = balanceService.findBalanceByUserId(user.getId());
        if(balanceOptional.isPresent()) {
            return DecimalUtils.roundDown(balanceOptional.get().getCurrentBalance());
        }
        return 0;
    }
}
