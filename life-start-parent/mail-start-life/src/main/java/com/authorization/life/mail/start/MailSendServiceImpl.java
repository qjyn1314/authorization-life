package com.authorization.life.mail.start;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.authorization.remote.system.SystemRemoteRes;
import com.authorization.remote.system.service.SystemRemoteService;
import com.authorization.remote.system.vo.TempVO;
import jakarta.annotation.Resource;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import java.util.Map;

/**
 * <p>
 * 发送邮件参考: https://www.cnblogs.com/ooo0/p/16446829.html
 * </p>
 *
 * @author wangjunming
 * @since 2024-10-01 21:23
 */
@Slf4j
public class MailSendServiceImpl implements MailSendService {

    @Resource
    private SystemRemoteService systemRemoteService;
    @Resource
    private JavaMailSender mailSender;

    @Override
    public void sendEmail(String templateCode, String toEmail, Map<String, Object> argsMap) {
        SystemRemoteRes<TempVO> tempVORes = systemRemoteService.getTempByCode(templateCode);
        TempVO temp = tempVORes.getData();
        Assert.notNull(temp, "未找到相应模板.");
        sendMailContent(toEmail, temp.getTempDesc(), temp.getContent(), argsMap);
    }

    /**
     * 仅发送简单文本的
     *
     * @param toEmail     接收人
     * @param tempDesc    邮件主题
     * @param tempContent 邮件内容
     * @param argsMap     邮件内容的参数
     */
    private void sendMailContent(String toEmail, String tempDesc, String tempContent, Map<String, Object> argsMap) {
        MimeMessage senderMimeMessage = mailSender.createMimeMessage();
        MimeMessage mimeMessage = null;
        try {
            senderMimeMessage.setFrom(new InternetAddress(MailConstant.EMAIL_FROM, MailConstant.EMAIL_FROM_NAME));
            MimeMessageHelper helper = new MimeMessageHelper(senderMimeMessage, true);
            helper.setTo(toEmail);
            helper.setSubject(tempDesc);
            helper.setText(StrUtil.format(tempContent, argsMap), true);
            mimeMessage = helper.getMimeMessage();
        } catch (Exception e) {
            log.error("发送邮件失败,", e);
        }
        Assert.notNull(mimeMessage, "发送邮件失败.");
        mailSender.send(mimeMessage);
    }

}
