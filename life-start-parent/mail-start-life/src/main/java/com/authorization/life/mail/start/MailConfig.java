package com.authorization.life.mail.start;

import org.jasypt.encryption.StringEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Properties;

@Configuration
public class MailConfig {

    @Bean
    public MailSendService mailSendService() {
        return new MailSendServiceImpl();
    }

    @Autowired
    private StringEncryptor stringEncryptor;

    @Bean
    @Primary
    public MailProperties mailProperties() {
        MailProperties mailProperties = new MailProperties();
        mailProperties.setHost("smtp.163.com");
        mailProperties.setUsername("authorization_life@163.com");
        mailProperties.setPassword(stringEncryptor.decrypt("/gR+LeISNhe0pMRKbL4UduP13MVwNhO+d3Sz3qtGD3jYhAKpdoPhrA=="));
        mailProperties.setPort(465);
        mailProperties.setProtocol("smtp");
        mailProperties.setDefaultEncoding(StandardCharsets.UTF_8);
        mailProperties.getProperties().put("mail.smtp.auth", "true");
        mailProperties.getProperties().put("mail.smtp.starttls.enable", "true");
        mailProperties.getProperties().put("mail.smtp.ssl.enable", "true");
        mailProperties.getProperties().put("mail.smtp.ssl.required.enable", "true");
        return mailProperties;
    }

    @Bean
    public JavaMailSender javaMailSender() {
        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
        javaMailSender.setHost(mailProperties().getHost());
        javaMailSender.setPort(mailProperties().getPort());
        javaMailSender.setUsername(mailProperties().getUsername());
        javaMailSender.setPassword(mailProperties().getPassword());
        javaMailSender.setDefaultEncoding(StandardCharsets.UTF_8.name());
        javaMailSender.setProtocol(mailProperties().getProtocol());
        Map<String, String> propertiesMap = mailProperties().getProperties();
        Properties properties = new Properties();
        propertiesMap.forEach(properties::setProperty);
        javaMailSender.setJavaMailProperties(properties);
        return javaMailSender;
    }

}