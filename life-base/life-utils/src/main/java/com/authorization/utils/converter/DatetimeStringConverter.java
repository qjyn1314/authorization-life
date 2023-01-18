package com.authorization.utils.converter;

import com.authorization.utils.json.JsonDateUtil;
import ma.glasnost.orika.MappingContext;
import ma.glasnost.orika.converter.BidirectionalConverter;
import ma.glasnost.orika.metadata.Type;

import java.util.Date;

/**
 * 时间转换器
 */
public class DatetimeStringConverter extends BidirectionalConverter<Date, String> {

    public static final String NAME = "datetimeStringConverter";

    public static DatetimeStringConverter getInstance() {
        return SingletonEnum.INSTANCE.getInstance();
    }

    @Override
    public String convertTo(Date date, Type<String> type, MappingContext mappingContext) {
        return JsonDateUtil.format(date, JsonDateUtil.DATETIME);
    }

    @Override
    public Date convertFrom(String s, Type<Date> type, MappingContext mappingContext) {
        return JsonDateUtil.parseDateTime(s);
    }

    enum SingletonEnum {

        //创建一个枚举对象
        INSTANCE;
        private final DatetimeStringConverter datetimeStringConverter;

        /**
         * 私有化枚举的构造函数
         */
        SingletonEnum() {
            datetimeStringConverter = new DatetimeStringConverter();
        }

        public DatetimeStringConverter getInstance() {
            return datetimeStringConverter;
        }
    }
}
