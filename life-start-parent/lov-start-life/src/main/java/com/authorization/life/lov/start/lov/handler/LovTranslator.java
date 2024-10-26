package com.authorization.life.lov.start.lov.handler;

import com.authorization.life.lov.start.lov.entity.LovDetail;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Map;

/**
 * lov翻译器
 */
public interface LovTranslator {

    /**
     * 翻译对象目标字段
     * @param targetField 目标字段
     * @param object 对象
     * @return 翻译后的对象
     */
    Object translateObject(String[] targetField, Object object);

    LovDetail getLov(String lovCode);

    Map<String, String> getLovValue(String lovCode);

    @Getter
    @Setter
    @ToString
    @EqualsAndHashCode
    class LocalCacheKey{
        private Long tenantId;
        private String lovCode;

        private LocalCacheKey(Long tenantId, String lovCode) {
            this.tenantId = tenantId;
            this.lovCode = lovCode;
        }
        public static LocalCacheKey of(Long tenantId, String lovCode){
            return new LocalCacheKey(tenantId, lovCode);
        }
    }
}
