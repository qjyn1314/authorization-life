package com.authorization.life.datasource.start.datasource.ps6y;

import com.p6spy.engine.logging.Category;
import com.p6spy.engine.spy.appender.FormattedLogger;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

@Slf4j
public class P6SpyLogger extends FormattedLogger {
    @Override
    public void logException(Exception e) {
        log.error("sql-logException", e);
    }

    @Override
    public void logText(String sql) {
        log.info("sql-logText-{}", sql);
    }

    /**
     * 根据类别打印不同等级的日志
     *
     * @param connectionId: 连接ID
     * @param now:          当前时间
     * @param elapsed:      花费时间
     * @param category:     类别
     * @param prepared:     预编译SQL
     * @param sql:          最终执行的SQL
     * @param url:          数据库连接地址
     * @date 2020/1/16 9:52
     * @author lixiangx@leimingtech.com
     **/
    @Override
    public void logSQL(int connectionId, String now, long elapsed, Category category, String prepared, String sql, String url) {
        final String msg = strategy.formatMessage(connectionId, now, elapsed,
                category.toString(), prepared, sql, url);
        if (!StringUtils.hasText(msg)) {
            return;
        }
        if (Category.ERROR.equals(category)) {
            log.error(msg);
        } else if (Category.WARN.equals(category)) {
            log.warn(msg);
        } else if (Category.DEBUG.equals(category)) {
            log.debug(msg);
        } else {
            log.info(msg);
        }
    }

    @Override
    public boolean isCategoryEnabled(Category category) {
        return true;
    }
}
