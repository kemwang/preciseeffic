package com.ruqimobility.precisiontest.utils;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class CustomAccessObjectUtils {

    private static final Logger log = LoggerFactory.getLogger(CustomAccessObjectUtils.class);
    private static final String LOCAL_HOST = "127.0.0.1";
    private static final String INVALID_IP = "0:0:0:0:0:0:0:1";

    public CustomAccessObjectUtils() {
    }

    public static String getIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (!isValid(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }

        if (!isValid(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }

        if (!isValid(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }

        if (!isValid(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }

        if (!isValid(ip)) {
            ip = request.getRemoteAddr();
            if ("127.0.0.1".equals(ip) || "0:0:0:0:0:0:0:1".equals(ip)) {
                InetAddress inetAddress = null;

                try {
                    inetAddress = InetAddress.getLocalHost();
                } catch (UnknownHostException var4) {
                    var4.printStackTrace();
                }

                ip = inetAddress.getHostAddress();
            }
        }

        if (ip != null && ip.indexOf(",") > 0) {
            ip = ip.substring(0, ip.indexOf(","));
        }

        return ip;
    }

    private static boolean isValid(String ip) {
        return !StringUtils.isEmpty(ip) && !"unknown".equalsIgnoreCase(ip);
    }
}
