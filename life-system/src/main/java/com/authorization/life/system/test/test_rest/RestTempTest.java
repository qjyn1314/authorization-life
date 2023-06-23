package com.authorization.life.system.test.test_rest;

import cn.hutool.json.JSONUtil;
import com.authorization.life.system.infra.dto.EmpDTO;
import com.authorization.life.system.infra.entity.Emp;
import com.authorization.life.system.infra.util.RestTempUtil;
import com.authorization.utils.json.JsonHelper;
import com.authorization.utils.result.Result;
import com.fasterxml.jackson.core.type.TypeReference;
import com.github.pagehelper.PageInfo;
import groovy.util.logging.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 测试 restTemplate请求 http接口
 *
 * @author wangjunming
 * @date 2023/6/23 14:58
 */
@Slf4j
public class RestTempTest {

    public static void main(String[] args) {

    }


    private static void testShardingJdbcQuery() {
        RestTemplate restTemplate = RestTempUtil.restTemplate();
        String url = "http://127.0.0.1:9050/lemd/emp/page";
        // 设置请求头
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-Type", "application/json;charset=utf-8");
        // 请求参数
        Map<String, Object> params = new HashMap<>();
        params.put("pageNum", 1);
        params.put("pageSize", 2000);
        //将请求头和请求参数设置到HttpEntity中
        HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<>(params, httpHeaders);
        String jsonResult = restTemplate.postForObject(url, httpEntity, String.class);
        TypeReference<Result<PageInfo<Emp>>> empPage = new TypeReference<Result<PageInfo<Emp>>>() {
        };
        Result<PageInfo<Emp>> infoJsonResult = JsonHelper.readValue(jsonResult, empPage);
        log.info("查询用户信息成功--->{}", JSONUtil.toJsonStr(infoJsonResult));
    }

    private static void testShardingJdbcInsert() {
        List<EmpDTO> empDTOList = new ArrayList<>();
        int start = 1;
        int end = 200;
        for (int i = start; i < end; i++) {
            if (i % 2 == 0) {
                empDTOList.add(new EmpDTO().setEmpName("张三" + i).setPhone("1532135571" + i).setEmail("qjyn131" + i + "@163.com").setEnabledFlag(false).setTenantId(0L));
                log.info("执行到了->>i % 2 == 0-->>{}", i);
            } else if (i % 3 == 0) {
                empDTOList.add(new EmpDTO().setEmpName("李四" + i).setPhone("1551501402" + i).setEmail("qjyn131" + i + "@gmail.com").setEnabledFlag(false).setTenantId(1L));
                log.info("执行到了->>i % 3 == 0-->>{}", i);
            } else if (i % 5 == 0) {
                empDTOList.add(new EmpDTO().setEmpName("赵六" + i).setPhone("1661601603" + i).setEmail("1602423365" + i + "@qq.com").setEnabledFlag(false).setTenantId(5L));
                log.info("执行到了->>i % 5 == 0-->>{}", i);
            } else {
                empDTOList.add(new EmpDTO().setEmpName("王五" + i).setPhone("1602423365" + i).setEmail("qjyn131" + i + "@foxmail.com").setEnabledFlag(false).setTenantId(3L));
                log.info("执行到了->>default-->>{}", i);
            }
        }
        Map<Long, List<EmpDTO>> countMap = empDTOList.stream().collect(Collectors.groupingBy(EmpDTO::getTenantId));
        countMap.forEach((k, v) -> {
            log.info("租户id-->{}-共多少数据->{}", k, v.size());
        });
        RestTemplate restTemplate = RestTempUtil.me().restTemplate();
        String url = "http://127.0.0.1:9050/lemd/emp/batchSave";
        // 设置请求头
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-Type", "application/json;charset=utf-8");
        //将请求头和请求参数设置到HttpEntity中
        HttpEntity<List<EmpDTO>> httpEntity = new HttpEntity<>(empDTOList, httpHeaders);
        String jsonResult = restTemplate.postForObject(url, httpEntity, String.class);
        TypeReference<Result<Boolean>> saveEmp = new TypeReference<Result<Boolean>>() {
        };
        Result<Boolean> booleanJsonResult = JsonHelper.readValue(jsonResult, saveEmp);

    }


}