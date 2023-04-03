package com.authorization.life.datasource.start.ps6y;

import cn.hutool.core.map.MapUtil;
import com.authorization.utils.json.JsonHelper;
import com.p6spy.engine.spy.appender.MessageFormattingStrategy;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Slf4j
public class P6spyLogFormatStrategy implements MessageFormattingStrategy {
    /**
     * 日志格式化方式（打印SQL日志会进入此方法，耗时操作，生产环境不建议使用）
     *
     * @param connectionId: 连接ID
     * @param now:          当前时间
     * @param elapsed:      花费时间
     * @param category:     类别
     * @param preparedSql:  预编译SQL
     * @param sql:          最终执行的SQL
     * @param url:          数据库连接地址
     * @return 格式化日志结果
     */
    @Override
    public String formatMessage(int connectionId, String now, long elapsed,
                                String category, String preparedSql, String sql,
                                String url) {
        //取出所有換行 和回车
        Map<Object, Object> sqlMsgMap = MapUtil.builder()
                .put("linkInfo", url)
                .put("elapsed", elapsed + "毫秒")
                .put("finalExecSql", sql.replaceAll("\r\n|\r|\n", ""))
                .build();
        return JsonHelper.toJson(sqlMsgMap);
    }
}
