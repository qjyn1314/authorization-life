package com.authorization.gateway.entity;

import com.authorization.utils.security.UserDetail;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * 请求上下文，记录请求上下文信息
 */
@Getter
@Setter
@Accessors(chain = true)
public class RequestContext {

    public static final String CTX_KEY = "requestContext";

    private String serviceCode;
    private String serviceName;
    private String httpMethod;
    private String uri;
    private UserDetail userDetail;
    private boolean isAuthed;

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("{");
        sb.append("\"serviceCode\":\"")
                .append(serviceCode).append('\"');
        sb.append(",\"serviceName\":\"")
                .append(serviceName).append('\"');
        sb.append(",\"httpMethod\":\"")
                .append(httpMethod).append('\"');
        sb.append(",\"uri\":\"")
                .append(uri).append('\"');
        sb.append(",\"isAuthed\":")
                .append(isAuthed);
        sb.append('}');
        return sb.toString();
    }
}
