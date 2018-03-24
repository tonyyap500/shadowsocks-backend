package com.shadowsocks.web;

import com.shadowsocks.utils.IpUtils;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;


@Slf4j
public class BaseController {

    protected String getCurrentIpAddress(HttpServletRequest request) {
        return IpUtils.getCurrentIp(request);
    }
}
