package com.authorization.life.security.util.convert;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.core.AuthorizationGrantType;

import java.io.IOException;

@Slf4j
public class AuthorizationGrantTypeSerializer extends JsonSerializer<AuthorizationGrantType> {

    @Override
    public void serialize(AuthorizationGrantType authorizationGrantType, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeString(authorizationGrantType.getValue());
    }
}
