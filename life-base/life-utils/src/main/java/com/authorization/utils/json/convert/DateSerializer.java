package com.authorization.utils.json.convert;

import cn.hutool.core.date.format.FastDateFormat;
import com.authorization.utils.json.JsonDateUtil;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.util.Date;

/**
 * date类型日期序列化解析器
 */
public class DateSerializer extends JsonSerializer<Date> {

    private static final FastDateFormat DATE_FORMAT = FastDateFormat.getInstance(JsonDateUtil.DATETIME);

    @Override
    public void serialize(Date date, JsonGenerator jsonGenerator, SerializerProvider serializers) throws IOException {
        jsonGenerator.writeString(DATE_FORMAT.format(date));
    }
}
