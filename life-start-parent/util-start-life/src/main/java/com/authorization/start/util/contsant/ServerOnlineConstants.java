package com.authorization.start.util.contsant;

/**
 * 服务的上线的订阅常量信息
 *
 * @author wangjunming
 */
public class ServerOnlineConstants {

    public static final String INSTANCE_CHANNEL = "auth-server.service.instance.*";

    public static final String INSTANCE_UP_TOPIC = "auth-server.service.instance.up";

    public static final String INSTANCE_DOWN_TOPIC = "auth-server.service.instance.down";

    public static final String KEY_SERVICE_CODE = "serviceName";

}
