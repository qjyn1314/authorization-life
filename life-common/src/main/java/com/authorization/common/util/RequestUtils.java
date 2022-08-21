package com.authorization.common.util;

import com.google.common.base.Objects;
import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Slf4j
public class RequestUtils {

    /**
     * 工具类防止被初始化
     *
     * @since 1.0 Created by lipangeng on 2017/11/20 下午5:42. Email:lipg@outlook.com
     */
    private RequestUtils() {
    }

    /**
     * 从当前线程中获取Request
     *
     * @since 1.0 Created by lipangeng on 2017/11/20 下午5:42. Email:lipg@outlook.com
     */
    public static HttpServletRequest getRequest() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    }

    /**
     * 获取ContextPath路径
     *
     * @since 1.0 Created by lipangeng on 2017/12/5 下午12:13. Email:lipg@outlook.com
     */
    public static String getContextPath() {
        return getRequest().getContextPath();
    }

    /**
     * 获取服务器地址
     *
     * @since 1.0 Created by lipangeng on 2017/12/19 下午12:12. Email:lipg@outlook.com
     */
    public static String getServerUrl() {
        return getScheme() + "://" + getHost() + (getPort() == null ? "" : (":" + getPort()));
    }

    /**
     * 获取服务器地址
     *
     * @since 1.0 Created by lipangeng on 2017/12/19 下午12:12. Email:lipg@outlook.com
     */
    public static String getServerAndContextUrl() {
        HttpServletRequest request = getRequest();
        return getScheme() + "://" + getHost() + (getPort() == null ? "" : (":" + getPort())) +
                request.getContextPath();
    }

    /**
     * 获取争取的Scheme
     *
     * @since 1.0 Created by lipangeng on 2018/1/11 下午9:02. Email:lipg@outlook.com
     */
    public static String getScheme() {
        HttpServletRequest request = getRequest();
        String scheme = request.getHeader("X-Forwarded-Proto");
        if (Strings.isNullOrEmpty(scheme)) {
            scheme = request.getHeader("X-Forward-Proto");
        }
        if (Strings.isNullOrEmpty(scheme)) {
            scheme = request.getScheme();
        }
        return scheme;
    }

    /**
     * 获取请求的真实的Port
     *
     * @since 1.0 Created by lipangeng on 2018/1/18 下午5:54. Email:lipg@outlook.com
     */
    public static String getPort() {
        HttpServletRequest request = getRequest();
        String port = request.getHeader("X-Forwarded-Port");
        if (Strings.isNullOrEmpty(port)) {
            port = request.getHeader("X-Forward-Port");
        }
        if (Strings.isNullOrEmpty(port)) {
            port = String.valueOf(request.getServerPort());
        }
        String scheme = getScheme();
        // 需不需要增加端口
        if ((Objects.equal(scheme, "http") && Objects.equal(port, "80")) ||
                (Objects.equal(scheme, "https") && Objects.equal(port, "443"))) {
            port = null;
        }
        return port;
    }

    /**
     * 获取请求的真实的serverName
     *
     * @since 1.0 Created by lipangeng on 2018/1/18 下午5:54. Email:lipg@outlook.com
     */
    public static String getHost() {
        HttpServletRequest request = getRequest();
        String host = request.getHeader("X-Forwarded-Host");
        if (Strings.isNullOrEmpty(host)) {
            host = request.getHeader("X-Forward-Host");
        }
        if (Strings.isNullOrEmpty(host)) {
            host = request.getServerName();
        }
        return host;
    }


    public static String getIp() {
        HttpServletRequest request = getRequest();
        String ip = request.getHeader("X-Forwarded-for");
        if (Strings.isNullOrEmpty(ip)) {
            ip = request.getHeader("X-Forward-for");
        }
        if (Strings.isNullOrEmpty(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
}

