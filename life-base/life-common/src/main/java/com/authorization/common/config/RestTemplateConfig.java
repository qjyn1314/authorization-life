package com.authorization.common.config;

import com.authorization.utils.json.JsonHelper;
import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.client5.http.io.HttpClientConnectionManager;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;

/**
 * RestTemplate 配置类
 * <p>
 * 参考: https://www.jianshu.com/p/95680c1eb6e0
 *
 * @author wangjunming
 * @date 2022/12/29 14:27
 */
@Configuration
public class RestTemplateConfig {

    /**
     * http连接管理器
     *
     * @return HttpClientConnectionManager
     */
    @Bean
    public HttpClientConnectionManager poolingHttpClientConnectionManager() {
        /**
         // 注册http和https请求
         Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
         .register("http", PlainConnectionSocketFactory.getSocketFactory())
         .register("https", SSLConnectionSocketFactory.getSocketFactory())
         .build();
         PoolingHttpClientConnectionManager poolingHttpClientConnectionManager = new PoolingHttpClientConnectionManager(registry);
         */

        PoolingHttpClientConnectionManager poolingHttpClientConnectionManager = new PoolingHttpClientConnectionManager();
        // 最大连接数
        poolingHttpClientConnectionManager.setMaxTotal(500);
        // 同路由并发数（每个主机的并发）
        poolingHttpClientConnectionManager.setDefaultMaxPerRoute(100);
        return poolingHttpClientConnectionManager;
    }

    /**
     * HttpClient
     *
     * @param poolingHttpClientConnectionManager
     * @return HttpClient
     */
    @Bean
    public HttpClient httpClient(HttpClientConnectionManager poolingHttpClientConnectionManager) {
        HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
        // 设置http连接管理器
        httpClientBuilder.setConnectionManager(poolingHttpClientConnectionManager);

        /*// 设置重试次数
        httpClientBuilder.setRetryHandler(new DefaultHttpRequestRetryHandler(3, true));*/

        // 设置默认请求头
        /*List<Header> headers = new ArrayList<>();
        headers.add(new BasicHeader("Connection", "Keep-Alive"));
        httpClientBuilder.setDefaultHeaders(headers);*/

        return httpClientBuilder.build();
    }

    /**
     * 请求连接池配置
     *
     * @param httpClient
     * @return ClientHttpRequestFactory
     */
    @Bean
    public ClientHttpRequestFactory clientHttpRequestFactory(HttpClient httpClient) {
        HttpComponentsClientHttpRequestFactory clientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory();
        // httpClient创建器
        clientHttpRequestFactory.setHttpClient(httpClient);
        // 连接超时时间/毫秒（连接上服务器(握手成功)的时间，超出抛出connect timeout）
        clientHttpRequestFactory.setConnectTimeout(5 * 1000);
        // 数据读取超时时间(socketTimeout)/毫秒（务器返回数据(response)的时间，超过抛出read timeout）
//        clientHttpRequestFactory.setReadTimeout(10 * 1000);
        // 连接池获取请求连接的超时时间，不宜过长，必须设置/毫秒（超时间未拿到可用连接，会抛出org.apache.http.conn.ConnectionPoolTimeoutException: Timeout waiting for connection from pool）
        clientHttpRequestFactory.setConnectionRequestTimeout(10 * 1000);
        return clientHttpRequestFactory;
    }

    /**
     * rest模板
     *
     * @return RestTemplate
     */
    @Bean
    public RestTemplate restTemplate(ClientHttpRequestFactory clientHttpRequestFactory) {
        //添加额外的HttpMessageConverter，在构建器上配置的任何转换器都将替换RestTemplate的默认转换器。
        MappingJackson2HttpMessageConverter jackson2HttpMessageConverter = new MappingJackson2HttpMessageConverter();
        jackson2HttpMessageConverter.setObjectMapper(JsonHelper.getObjectMapper());
        RestTemplate restTemplate = new RestTemplateBuilder()
                //解决乱码问题，遍历寻找，进行覆盖
                .additionalMessageConverters(new StringHttpMessageConverter(StandardCharsets.UTF_8))
                .additionalMessageConverters(new FormHttpMessageConverter())
                .additionalMessageConverters(jackson2HttpMessageConverter)
                .build();
        restTemplate.setRequestFactory(clientHttpRequestFactory);
        return restTemplate;
    }

    private static volatile RestTemplate restTemplate = null;

    /**
     * 获取静态的 RestTemplate
     *
     * @return RestTemplate
     */
    public static RestTemplate getStaticRest() {
        RestTemplateConfig restConfig = new RestTemplateConfig();
        if (restTemplate != null) {
            return restTemplate;
        }
        synchronized (RestTemplateConfig.class) {
            if (restTemplate != null) {
                return restTemplate;
            }
            restTemplate = restConfig
                    .restTemplate(restConfig
                            .clientHttpRequestFactory(restConfig
                                    .httpClient(restConfig
                                            .poolingHttpClientConnectionManager())));
        }
        return restTemplate;
    }

}
