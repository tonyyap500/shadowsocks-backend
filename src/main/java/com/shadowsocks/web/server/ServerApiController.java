package com.shadowsocks.web.server;


import com.google.common.collect.Lists;
import com.shadowsocks.dto.ResponseMessageDto;
import com.shadowsocks.dto.entity.*;
import com.shadowsocks.dto.enums.ResultEnum;
import com.shadowsocks.dto.response.CityDto;
import com.shadowsocks.dto.response.CountryDto;
import com.shadowsocks.dto.response.ServerDto;
import com.shadowsocks.service.BalanceService;
import com.shadowsocks.service.EmailService;
import com.shadowsocks.service.ServerService;
import com.shadowsocks.utils.EmailUtils;
import com.shadowsocks.utils.HtmlUtils;
import com.shadowsocks.utils.SessionKeyUtils;
import com.shadowsocks.web.BaseController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.text.MessageFormat;
import java.util.List;
import java.util.Optional;

@RestController
@Slf4j
public class ServerApiController extends BaseController implements ServerApi{

    private HttpSession session;
    private ServerService serverService;
    private EmailService emailService;
    private BalanceService balanceService;

    public ServerApiController(HttpSession session, ServerService serverService, EmailService emailService, BalanceService balanceService) {
        this.session = session;
        this.serverService = serverService;
        this.emailService = emailService;
        this.balanceService = balanceService;
    }

    @Override
    public List<CountryDto> getCountryList() {
        return serverService.findCountryList();
    }

    @Override
    public List<CityDto> getCityList(@PathVariable("country") String country) {
        return serverService.findCityList(country);
    }

    private Optional<ServerDto> applyServer(int userId, int serverId) {
        boolean result = serverService.applyServer(serverId, userId);
        if(!result) {
            return Optional.empty();
        }
        Optional<Server> serverOptional = serverService.findServerById(serverId);
        if(serverOptional.isPresent()) {
            Server server = serverOptional.get();
            ServerDto serverDto = ServerDto.builder()
                    .domain(server.getDomain())
                    .port(server.getPort())
                    .password(server.getPassword())
                    .encryption("aes-256-cfb")
                    .country(server.getCountryInChinese())
                    .city(server.getCityInChinese())
                    .build();
            return Optional.ofNullable(serverDto);
        }
        return Optional.empty();
    }

    @Override
    public ResponseMessageDto purchaseServer(@PathVariable("id") String id) {
        User user = (User) session.getAttribute(SessionKeyUtils.getKeyForUser());
        int serverId = Integer.parseInt(id);
        Optional<Balance> balanceOptional = balanceService.findBalanceByUserId(user.getId());
        if(balanceOptional.isPresent() && balanceOptional.get().getCurrentBalance() > 10.0) {
            Optional<ServerDto> serverDtoOptional = applyServer(user.getId(), serverId);
            if(serverDtoOptional.isPresent()) {
                ServerDto dto = serverDtoOptional.get();
                List<EmailConfig> emailConfigList = emailService.findEmailConfigs();
                String contentPattern = HtmlUtils.getPurchaseHtmlPattern();
                String content = MessageFormat.format(contentPattern, dto.getDomain(), dto.getPort(), dto.getPassword(), dto.getEncryption(), dto.getCountry(), dto.getCity());

                EmailObject emailObject = EmailObject.builder().toList(Lists.newArrayList(user.getEmail())).subject("OceanHere 服务购买成功").content(content).build();
                EmailUtils.sendEmailAsyc(emailConfigList, emailObject);
                log.info("用户购买服务器，国家{}， 城市{}, 域名{}, 端口{}", dto.getCountry(), dto.getCity(), dto.getDomain(), dto.getPort());
                return ResponseMessageDto.builder().result(ResultEnum.SUCCESS).message("购买成功，购买结果请查看邮件").build();
            }
        }

        return ResponseMessageDto.builder().result(ResultEnum.FAIL).message("购买失败，当前余额低于10.0元, 请即时充值").build();
    }
}
