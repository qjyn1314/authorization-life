package com.authorization.utils.listener;

import com.alibaba.fastjson2.JSON;
import com.authorization.utils.json.JsonDateUtil;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.annotation.Order;

/** 监听服务是否启动成功 */
@Slf4j
@Order
public class ApplicationEvenListener
    implements CommandLineRunner, ApplicationRunner, ApplicationListener<ApplicationEvent> {

  @Override
  public void run(String... args) {
    log.info("CommandLineRunner args:{}", JSON.toJSONString(args));
    RuntimeMXBean runtimeJvmBean = ManagementFactory.getRuntimeMXBean();
    long uptime = runtimeJvmBean.getUptime();
    String jvmStartTime = JsonDateUtil.formatMillis(uptime);
    log.info("ApplicationEvenListener CommandLineRunner jvmStartTime: {}: ", jvmStartTime);
  }

  @Override
  public void run(ApplicationArguments args) throws Exception {
    log.info("ApplicationArguments args: {}", JSON.toJSONString(args));
  }

  /**
   * 监听到的SpringBoot事件
   *
   * @param event 事件信息
   */
  @Override
  public void onApplicationEvent(ApplicationEvent event) {
    log.info("Application event received: {}, eventData:{}", event, event.getSource());
    if (event instanceof ApplicationStartedEvent startedEvent) {
      log.info("应用启动耗时:{}", JsonDateUtil.formatMillis(startedEvent.getTimeTaken().toMillis()));
    }
    if (event instanceof ApplicationReadyEvent readyEvent) {
      log.info(
          "应用已完全启动，可以开始处理请求, 完全就绪耗时: {}",
          JsonDateUtil.formatMillis(readyEvent.getTimeTaken().toMillis()));
    }
  }
}
