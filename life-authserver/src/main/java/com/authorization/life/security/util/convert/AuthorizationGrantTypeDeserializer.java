package com.authorization.life.security.util.convert;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.core.AuthorizationGrantType;

import java.io.IOException;

@Slf4j
public class AuthorizationGrantTypeDeserializer extends JsonDeserializer<AuthorizationGrantType> {

    @Override
    public AuthorizationGrantType deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        return new AuthorizationGrantType(jsonParser.getValueAsString());
    }

}
