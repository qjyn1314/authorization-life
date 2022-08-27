package com.authorization.start.util.json;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JsonObjectMapperTest {


    public static void main(String[] args) {

        String lovJson = JsonHelper.writeValueAsString(JsonObjectMapperTest.lovJson);
        log.info("转换后的json是：-{}",lovJson);

        LovJson lovJson1 = JsonHelper.readValue(lovJson, LovJson.class);

        log.info("转换后的对象是：-{}",lovJson1);

    }

    static LovJson lovJson = new LovJson();
    static {
        lovJson.setLovCode("sex");
        lovJson.setLovName("性别");
        lovJson.setLovTypeCode("fixed");
        lovJson.setTenantId(0L);
    }

    @Getter
    @Setter
    static class LovJson {

        /**
         * 值集代码
         */
        private String lovCode;
        /**
         * 值集名称
         */
        private String lovName;
        /**
         * 值集类型代码
         */
        private String lovTypeCode;
        /**
         * 租户ID
         */
        private Long tenantId;

        @Override
        public String toString() {
            return "LovJson{" +
                    "lovCode='" + lovCode + '\'' +
                    ", lovName='" + lovName + '\'' +
                    ", lovTypeCode='" + lovTypeCode + '\'' +
                    ", tenantId=" + tenantId +
                    '}';
        }
    }

}
