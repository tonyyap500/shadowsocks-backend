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
     * 申请使用服务器
     * */
    boolean applyServer(int id, int userId);

    /**
     * 释放服务器
     * */
    boolean releaseServer(int userId, String newPassword);

    /**
     * 根据id查询服务器信息
     * */
    Optional<Server> findServerById(int id);

    /**
     * 分页查询服务器使用
     * */
    List<Server> findServers(int start, int pageSize);

    /**
     * 增加服务器
     * */
    boolean addNewServer(Server server);

    /**
     * 根据域名查询服务器
     * */
    List<Server> findServersByDomain(String domain);

    /**
     * 查看我的节点
     * */
    List<Server> findMyServers(int userId);
}
