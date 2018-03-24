package com.shadowsocks.utils;

import com.shadowsocks.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.UnknownHostException;

@Slf4j
public class IpUtils {

    public static String getCurrentIp(HttpServletRequest request) {
        String ip = null;

        for (String source : new String[]{"X-Forwarded-For", "X-Real-IP", "Proxy-Client-IP", "WL-Proxy-Client-IP", "request.getRemoteAddr"}) {
            if ("request.getRemoteAddr".equals(source)) {
                ip = request.getRemoteAddr();
            } else {
                ip = request.getHeader(source);
            }
            if (!isIpInvalid(ip)) {
                log.info("get IP {} from {}", ip, source);
                ip = ip.split(",")[0];
                break;
            }
        }

        return ip;
    }

    private static boolean isIpInvalid(String ip) {
        return ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip);
    }

    private static IpEnum getIpType(String ipStr) {
        try {
            InetAddress ip = InetAddress.getByName(ipStr);
            if (ip instanceof Inet4Address) {
                return IpEnum.IPV4;
            }
            if (ip instanceof Inet6Address) {
                return IpEnum.IPV6;
            }
        } catch (UnknownHostException e) {
            log.error("ip[{}] 格式错误", ipStr);
            throw new BusinessException("ip[" + ipStr + "] 格式错误");
        }
        throw new BusinessException("ip[" + ipStr + "] 格式错误");
    }

    public static boolean isIpv4(String ipStr) {
        IpEnum ipEnum = getIpType(ipStr);
        return ipEnum == IpEnum.IPV4;
    }

    enum IpEnum {
        IPV4,
        IPV6
    }
}
