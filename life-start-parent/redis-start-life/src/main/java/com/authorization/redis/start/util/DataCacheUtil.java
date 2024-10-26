package com.authorization.redis.start.util;

import cn.hutool.core.util.StrUtil;
import com.authorization.redis.start.service.DataCacheInterface;
import com.authorization.utils.contsant.BaseConstants;
import com.authorization.utils.json.JsonHelper;
import com.authorization.utils.message.StrForm;
import com.fasterxml.jackson.core.type.TypeReference;

import java.util.Objects;

public class DataCacheUtil {

    /**
     * 快速失败标识
     */
    public static final String FAST_FAIL = "FAST_FAIL";


    public static <T> T getDataCache(DataCacheInterface<T> dataCacheInterface,
                                     RedisUtil redisUtil,
                                     String key,
                                     String tenantId,
                                     TypeReference<T> clazz) {
        if (Objects.isNull(dataCacheInterface) || Objects.isNull(redisUtil)) {
            return null;
        }
        key = StrForm.of(key).add("tenantId", tenantId).format();
        String dataJson = redisUtil.strGet(key);
        if (StrUtil.isNotBlank(dataJson)) {
            return cachedMessageGroup(dataJson, clazz);
        }
        T data = dataCacheInterface.getDbData(tenantId);
        if (!Objects.isNull(data)) {
            redisUtil.strSet(key, data);
            return data;
        }
        String opsKey = StrForm.of(key).add("tenantId", BaseConstants.DEFAULT_TENANT_ID).format();
        dataJson = redisUtil.strGet(opsKey);
        if (StrUtil.isNotBlank(dataJson)) {
            redisUtil.strSet(key, dataJson);
            return cachedMessageGroup(dataJson, clazz);
        }
        data = dataCacheInterface.getDbData(BaseConstants.DEFAULT_TENANT_ID);
        if (!Objects.isNull(data)) {
            redisUtil.strSet(key, data);
            redisUtil.strSet(opsKey, data);
            return data;
        }
        redisUtil.strSet(key, FAST_FAIL);
        redisUtil.strSet(opsKey, FAST_FAIL);
        return null;
    }


    private static <T> T cachedMessageGroup(String dataJson, TypeReference<T> clazz) {
        if (FAST_FAIL.equals(dataJson)) {
            return null;
        }
        return JsonHelper.readValue(dataJson, clazz);
    }


}
