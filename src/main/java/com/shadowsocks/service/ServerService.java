package com.shadowsocks.service;

import com.shadowsocks.dto.entity.Server;
import com.shadowsocks.dto.response.CountryDto;
import com.shadowsocks.dto.response.CityDto;

import java.util.List;
import java.util.Optional;

public interface ServerService {

    /**
     * 查找可用国家
     * */
    List<CountryDto> findCountryList();

    /**
     * 查找可用城市
     * */
    List<CityDto> findCityList(String country);

    /**
     * 根据id查询服务器信息
     * */
    Optional<Server> findServerById(int id);
}
