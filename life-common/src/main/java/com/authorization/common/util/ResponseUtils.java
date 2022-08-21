package com.authorization.common.util;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.URLUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpHeaders;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;

/**
 * http响应流操作工具类
 */
@Slf4j
public class ResponseUtils {

    public static final String CONTENT_TYPE = "%s;charset=" + "UTF-8";

    /**
     * 往response写入xlsx
     *
     * @param response response
     * @param fileName 文件名称
     * @param bytes    字迹流
     */
    public static void downloadXlsx(HttpServletResponse response, String fileName, byte[] bytes) {
        download(response, fileName, String.format(CONTENT_TYPE, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"), bytes);
    }

    /**
     * 往response写入xls
     *
     * @param response response
     * @param fileName 文件名称
     * @param bytes    字迹流
     */
    public static void downloadXls(HttpServletResponse response, String fileName, byte[] bytes) {
        download(response, fileName, String.format(CONTENT_TYPE, "application/vnd.ms-excel"), bytes);
    }

    /**
     * 往response写入pdf
     *
     * @param response response
     * @param fileName 文件名称
     * @param bytes    字迹流
     */
    public static void downloadPdf(HttpServletResponse response, String fileName, byte[] bytes) {
        download(response, fileName, String.format(CONTENT_TYPE, "application/pdf"), bytes);
    }

    /**
     * 往response写入字节流
     *
     * @param response response
     * @param fileName 带后缀的文件名称，若不带后缀默认octet-stream
     * @param bytes    字迹流
     */
    public static void download(HttpServletResponse response, String fileName, String contentType, byte[] bytes) {
        Assert.notNull(bytes);
        try (BufferedOutputStream out = new BufferedOutputStream(response.getOutputStream())) {
            response.reset();
            response.setHeader(HttpHeaders.CONTENT_DISPOSITION,
                    String.format("attachment;filename=%s", URLUtil.encode(fileName, Charset.defaultCharset())));
            response.setContentType(contentType);
            response.addHeader(HttpHeaders.CONTENT_LENGTH, String.valueOf(bytes.length));
            response.setHeader(HttpHeaders.CACHE_CONTROL, "must-revalidate, post-check=0, pre-check=0");
            response.setHeader(HttpHeaders.PRAGMA, "public");
            response.setHeader(HttpHeaders.SET_COOKIE, "fileDownload=true; path=/");
            response.setDateHeader(HttpHeaders.EXPIRES, (System.currentTimeMillis() + 1000));
            IOUtils.write(bytes, out);
            out.flush();
        } catch (IOException e) {
            log.error("文件流写入失败", e);
        }
    }
}
