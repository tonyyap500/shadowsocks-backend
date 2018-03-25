package com.shadowsocks.service.impl;

import com.shadowsocks.dao.ServerDao;
import com.shadowsocks.dto.entity.Server;
import com.shadowsocks.dto.response.CountryDto;
import com.shadowsocks.dto.response.CityDto;
import com.shadowsocks.service.ServerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class ServerServiceImpl implements ServerService{

    @Resource
    private ServerDao serverDao;

    @Override
    public List<CountryDto> findCountryList() {
        return serverDao.findCountryList();
    }

    @Override
    public List<CityDto> findCityList(String country) {
        return serverDao.findCityList(country);
    }

    @Override
    public Optional<Server> findServerById(int id) {
        Server server = serverDao.findById(id);
        return Optional.ofNullable(server);
    }
}
