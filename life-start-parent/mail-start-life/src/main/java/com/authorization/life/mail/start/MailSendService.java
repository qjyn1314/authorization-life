package com.authorization.life.mail.start;

import java.util.Map;

/**
 * <p>
 *
 * </p>
 *
 * @author wangjunming
 * @since 2024-10-01 21:23
 */
public interface MailSendService {

    void sendEmail(String templateCode, String toEmail, Map<String, Object> argsMap);

}
