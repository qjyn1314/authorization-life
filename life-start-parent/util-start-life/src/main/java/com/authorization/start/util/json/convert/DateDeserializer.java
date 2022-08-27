package com.authorization.start.util.json.convert;

import cn.hutool.core.date.format.FastDateFormat;
import com.authorization.start.util.json.JsonDateUtil;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;

/**
 * date类型反序列化解析器
 */
@Slf4j
public class DateDeserializer extends JsonDeserializer<Date> {

    private static final FastDateFormat DATE_FORMAT = FastDateFormat.getInstance(JsonDateUtil.DATETIME);

    @Override
    public Date deserialize(JsonParser jsonParser, DeserializationContext ctxt) throws IOException {
        try {
            return DATE_FORMAT.parse(jsonParser.getValueAsString());
        } catch (ParseException e) {
            log.warn("date format error", e);
            return null;
        }
    }
}
