package com.shadowsocks.web.internal;

import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@RequestMapping("internal")
public interface InternalApi {

    @ApiOperation(value = "获取本机ip", tags = "internal")
    @RequestMapping(path = "/getIP", method = RequestMethod.GET)
    String getLatestConfig();

    @ApiOperation(value = "获取最新配置", tags = "internal")
    @RequestMapping(path = "/getLatestConfig", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
    String getLatestConfig(String domain);
}
