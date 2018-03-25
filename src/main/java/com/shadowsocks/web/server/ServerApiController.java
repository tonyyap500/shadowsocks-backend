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
import org.apache.catalina.manager.util.SessionUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Optional;

@RestController
@Slf4j
public class ServerApiController extends BaseController implements ServerApi{

    //TODO 验证用户余额信息
    private HttpServletRequest request;
    private HttpServletResponse response;
    private HttpSession session;
    private ServerService serverService;

    public ServerApiController(HttpServletRequest request, HttpServletResponse response,
                               HttpSession session, ServerService serverService) {
        this.request = request;
        this.response = response;
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

    @Override
    public ServerDto applyServer(@PathVariable("id") int id) {
        User user = (User) session.getAttribute(SessionKeyUtils.getKeyForUser());
        boolean result = serverService.applyServer(id, user.getId());
        if(!result) {
            return ServerDto.builder().build();
        }
        Optional<Server> serverOptional = serverService.findServerById(id);
        if(serverOptional.isPresent()) {
            Server server = serverOptional.get();
            return ServerDto.builder()
                    .domain(server.getDomain())
                    .port(server.getPort())
                    .password(server.getPassword())
                    .build();
        }
        return ServerDto.builder().build();
    }
}
