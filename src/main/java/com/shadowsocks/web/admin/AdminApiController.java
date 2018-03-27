package com.shadowsocks.web.admin;

import com.shadowsocks.dto.ResponseMessageDto;
import com.shadowsocks.dto.entity.Server;
import com.shadowsocks.dto.enums.ResultEnum;
import com.shadowsocks.dto.request.ServerRequestDto;
import com.shadowsocks.service.ServerService;
import com.shadowsocks.utils.RandomStringUtils;
import com.shadowsocks.web.BaseController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@Slf4j
public class AdminApiController extends BaseController implements AdminApi {

    @Resource
    private ServerService serverService;

    private Server transferServerDtoToServer(ServerRequestDto serverRequestDto) {
        return Server.builder()
                .country(serverRequestDto.getCountry())
                .countryInChinese(serverRequestDto.getCountryInChinese())
                .city(serverRequestDto.getCity())
                .cityInChinese(serverRequestDto.getCityInChinese())
                .domain(serverRequestDto.getDomain())
                .port(serverRequestDto.getPort())
                .currentOwner(0)
                .password(RandomStringUtils.generatePassword())
                .build();
    }

    @Override
    public ResponseMessageDto purchaseServer(@RequestBody ServerRequestDto serverRequestDto) {
        Server server = transferServerDtoToServer(serverRequestDto);
        boolean result = serverService.addNewServer(server);
        if(result) {
            //TODO 做unique key，防止重复添加
            log.info("新增节点，国家{}, 城市{}, 域名{}, 端口{}", serverRequestDto.getCountryInChinese(), serverRequestDto.getCityInChinese(), serverRequestDto.getDomain(), serverRequestDto.getPort());
            return ResponseMessageDto.builder().result(ResultEnum.SUCCESS).message("添加成功").build();
        }
        return ResponseMessageDto.builder().result(ResultEnum.FAIL).message("添加失败").build();
    }
}
