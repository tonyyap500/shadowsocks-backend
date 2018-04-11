package com.shadowsocks.web;

import com.shadowsocks.dto.entity.Admin;
import com.shadowsocks.dto.entity.User;
import com.shadowsocks.utils.CacheUtils;
import com.shadowsocks.utils.IpUtils;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;


@Slf4j
public class BaseController {

    @Resource
    private HttpServletRequest request;

    protected String getCurrentIpAddress() {
        return IpUtils.getCurrentIp(request);
    }

    public User getUser(String token) {
        return CacheUtils.get(token, User.class);
    }

    public Admin getAdmin(String token) {
        return CacheUtils.get(token, Admin.class);
    }
}
