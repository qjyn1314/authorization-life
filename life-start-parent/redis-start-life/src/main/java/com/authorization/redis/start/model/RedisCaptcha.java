package com.authorization.redis.start.model;

import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

/**
 * 生成的key示例格式: sso-oauth-server:auth:captcha-code:{bizType}:{bizId}:{uuid}
 *
 * @author wangjunming
 * @since 2025-07-09 15:34
 */
@Setter
@Getter
public class RedisCaptcha implements Serializable {
  /** 业务类型 */
  private String bizType;

  /** 业务ID */
  private String bizId;

  /** uuid */
  private String uuid;

  /** 验证码 */
  private String code;

  /** 过期时间(单位:分钟) */
  private Integer expiredTime;

  /** 格式化后的key */
  private String key;

  /** 验证码图片信息 */
  private Captcha captcha;
}
