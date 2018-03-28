package com.shadowsocks.web.internal;

import com.google.common.collect.Maps;
import com.shadowsocks.dto.entity.Server;
import com.shadowsocks.service.ServerService;
import com.shadowsocks.utils.ShadowsocksUtils;
import com.shadowsocks.web.BaseController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@Slf4j
public class InternalApiController extends BaseController implements InternalApi {

    private ServerService serverService;

    public InternalApiController(ServerService serverService) {
        this.serverService = serverService;
    }

    @Override
    public String getLatestConfig(String domain) {
        List<Server> serverList = serverService.findServersByDomain(domain);
        if(CollectionUtils.isEmpty(serverList)) {
            log.error("服务器 {} 节点已经用尽，请增加新的节点!", domain);
            return ShadowsocksUtils.buildShadowsocksConfig(Maps.newLinkedHashMap());
        }
        Map<String, String> portAndPassMap = serverList.stream().collect(Collectors.toMap(Server::getPort, Server::getPassword));
        return ShadowsocksUtils.buildShadowsocksConfig(portAndPassMap);
    }
}
