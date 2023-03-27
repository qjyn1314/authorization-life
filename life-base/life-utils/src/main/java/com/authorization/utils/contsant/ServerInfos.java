package com.authorization.utils.contsant;

/**
 * 各个服务的常量定义
 * <p>
 * 每个服务之间的端口号间隔 20 个正整数
 *
 * @author wangjunming
 */
public class ServerInfos {

    public static class GateWayLife {
        public static final String SERVER_NAME = "gway-life";
        public static final String SERVER_PORT = "9000";
    }

    public static class AuthLife {
        public static final String SERVER_NAME = "auth-life";
        public static final String SERVER_PORT = "9030";

        // 在 server_port 基础上 增加 3 为 当前服务 dubbo应用 qos 的 port
        public static final String DUBBO_QOS_PORT = "9033";
        // 在 server_port 基础上 增加 5 为 当前服务dubbo协议的port
        public static final String DUBBO_PROTOCOL_PORT = "9035";
    }

    public static class SystemLife {
        public static final String SERVER_NAME = "system-life";
        public static final String SERVER_PORT = "9050";

        // 在 server_port 基础上 增加 3 为 当前服务 dubbo应用 qos 的 port
        public static final String DUBBO_QOS_PORT = "9053";
        // 在 server_port 基础上 增加 5 为 当前服务dubbo协议的port
        public static final String DUBBO_PROTOCOL_PORT = "9055";

    }

    public static class ShardingLife {
        public static final String SERVER_NAME = "sharding-life";
        public static final String SERVER_PORT = "9070";

        // 在 server_port 基础上 增加 3 为 当前服务 dubbo应用 qos 的 port
        public static final String DUBBO_QOS_PORT = "9073";
        // 在 server_port 基础上 增加 5 为 当前服务dubbo协议的port
        public static final String DUBBO_PROTOCOL_PORT = "9075";

    }


}
