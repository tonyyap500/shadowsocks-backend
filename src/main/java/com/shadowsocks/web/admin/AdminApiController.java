package com.shadowsocks.web.admin;

import com.shadowsocks.dto.ResponseMessageDto;
import com.shadowsocks.dto.entity.Server;
import com.shadowsocks.dto.enums.ResultEnum;
import com.shadowsocks.dto.request.ServerRequestDto;
import com.shadowsocks.service.ServerService;
import com.shadowsocks.utils.RandomStringUtils;
import com.shadowsocks.utils.SleepUtils;
import com.shadowsocks.web.BaseController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

@RestController
@Slf4j
public class AdminApiController extends BaseController implements AdminApi {

    @Resource
    private ServerService serverService;

    private List<Server> transferServerDtoToServerList(ServerRequestDto serverRequestDto) {
        List<Long> portList = LongStream.rangeClosed(serverRequestDto.getMinPort(), serverRequestDto.getMaxPort()).boxed().collect(Collectors.toList());
        return portList.stream().map(port ->
                Server.builder()
                        .country(serverRequestDto.getCountry())
                        .countryInChinese(serverRequestDto.getCountryInChinese())
                        .city(serverRequestDto.getCity())
                        .cityInChinese(serverRequestDto.getCityInChinese())
                        .domain(serverRequestDto.getDomain())
                        .port(port.toString())
                        .currentOwner(0)
                        .password(RandomStringUtils.generatePassword())
                        .build()
        ).collect(Collectors.toList());
    }


    @Override
    public ResponseMessageDto purchaseServer(@RequestBody ServerRequestDto serverRequestDto) {
        List<Server> serverList = transferServerDtoToServerList(serverRequestDto);
        serverList.forEach(server -> {
            boolean result = serverService.addNewServer(server);
            if(result) {
                log.info("新增节点，国家{}, 城市{}, 域名{}, 端口{}", serverRequestDto.getCountryInChinese(), serverRequestDto.getCityInChinese(), serverRequestDto.getDomain(), server.getPort());
            }else {
                log.error("新增节点，国家{}, 城市{}, 域名{}, 端口{}", serverRequestDto.getCountryInChinese(), serverRequestDto.getCityInChinese(), serverRequestDto.getDomain(), server.getPort());
            }
            SleepUtils.sleep(500);
        });
        return ResponseMessageDto.builder().result(ResultEnum.SUCCESS).message("添加成功").build();
    }
}
