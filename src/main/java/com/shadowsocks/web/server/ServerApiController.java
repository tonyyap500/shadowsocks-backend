package com.shadowsocks.web.server;


import com.shadowsocks.dto.entity.Server;
import com.shadowsocks.dto.entity.User;
import com.shadowsocks.dto.response.CityDto;
import com.shadowsocks.dto.response.CountryDto;
import com.shadowsocks.dto.response.ServerDto;
import com.shadowsocks.service.ServerService;
import com.shadowsocks.utils.SessionKeyUtils;
import com.shadowsocks.web.BaseController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Optional;

@RestController
@Slf4j
public class ServerApiController extends BaseController implements ServerApi{

    private HttpSession session;
    private ServerService serverService;

    public ServerApiController(HttpSession session, ServerService serverService) {
        this.session = session;
        this.serverService = serverService;
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
                    .build();
            return Optional.ofNullable(serverDto);
        }
        return Optional.empty();
    }

    @Override
    public ServerDto purchaseServer(@PathVariable("id") int serverId) {
        User user = (User) session.getAttribute(SessionKeyUtils.getKeyForUser());
        Optional<ServerDto> serverDtoOptional = applyServer(user.getId(), serverId);
        serverDtoOptional.ifPresent(serverDto -> {
            //TODO 扣除余额
            //TODO 生成订单
            //TODO 发送邮件
        });
        return serverDtoOptional.orElseGet(() -> ServerDto.builder().build());
    }
}
