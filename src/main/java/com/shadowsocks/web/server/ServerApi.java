package com.shadowsocks.web.server;


import com.shadowsocks.dto.response.CityDto;
import com.shadowsocks.dto.response.CountryDto;
import com.shadowsocks.dto.response.ServerDto;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@RequestMapping("server")
public interface ServerApi {

    @ApiOperation(value = "获取可用国家", tags = "server")
    @RequestMapping(path = "/countryList", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
    List<CountryDto> getCountryList();

    @ApiOperation(value = "获取可用城市", tags = "server")
    @RequestMapping(path = "/cityList/{country}", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
    List<CityDto> getCityList(@PathVariable("country") String country);

    @ApiOperation(value = "购买服务器", tags = "server")
    @RequestMapping(path = "/purchase/{serverId}", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
    ServerDto purchaseServer(@PathVariable("serverId") int serverId);
}
