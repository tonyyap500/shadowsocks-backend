package com.shadowsocks.service.impl;

import com.google.common.collect.Lists;
import com.shadowsocks.dao.ServerDao;
import com.shadowsocks.dto.entity.Server;
import com.shadowsocks.dto.response.CountryDto;
import com.shadowsocks.dto.response.CityDto;
import com.shadowsocks.service.ServerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class ServerServiceImpl implements ServerService{

    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

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
    public boolean applyServer(int id, int userId) {
        String updateTime = LocalDateTime.now().format(formatter);
        int result = serverDao.applyServer(id, userId, updateTime);
        return result == 1;
    }

    @Override
    public boolean releaseServer(int userId, String newPassword) {
        String updateTime = LocalDateTime.now().format(formatter);
        int result = serverDao.releaseServer(userId, newPassword, updateTime);
        return result == 1;
    }

    @Override
    public List<Server> findServers(int start, int pageSize) {
        List<Server> serverList = serverDao.findServers(start, pageSize);
        if(!CollectionUtils.isEmpty(serverList)) {
            return serverList;
        }
        return Lists.newArrayList();
    }

    @Override
    public Optional<Server> findServerById(int id) {
        Server server = serverDao.findById(id);
        return Optional.ofNullable(server);
    }

    @Override
    public boolean addNewServer(Server server) {
        int result = serverDao.addNewServer(server);
        return result == 1;
    }

    @Override
    public List<Server> findServersByDomain(String domain) {
        List<Server> serverList = serverDao.findServersByDomain(domain);
        if(!CollectionUtils.isEmpty(serverList)) {
            return serverList;
        }
        return Lists.newArrayList();
    }

    @Override
    public List<Server> findMyServers(int userId) {
        return serverDao.findMyServers(userId);
    }
}
