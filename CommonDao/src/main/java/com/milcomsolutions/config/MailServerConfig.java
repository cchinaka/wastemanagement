package com.milcomsolutions.config;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;


@Configuration
public class MailServerConfig {

    @Value("${mail.server.protocol:'smtp'}")
    private String protocol;

    @Value("${mail.server.host}")
    private String host;

    @Value("${mail.server.port:25}")
    private int port;

    @Value("${mail.smtp.auth}")
    private boolean auth;

    @Value("${mail.smtp.starttls.enable}")
    private boolean starttls;

    @Value("${mail.server.username}")
    private String username;

    @Value("${mail.server.password}")
    private String password;


    @Bean
    public JavaMailSender mailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        Properties mailProperties = new Properties();
        mailProperties.put("mail.smtp.auth", auth);
        mailProperties.put("mail.smtp.starttls.enable", starttls);
        mailProperties.setProperty("mail.debug", "false");
        // props.setProperty("mail.transport.protocol", "smtp");
        mailProperties.put("mail.smtp.auth", "false");
        mailProperties.put("mail.smtp.socketFactory.port", port);
        mailProperties.put("mail.smtp.socketFactory.fallback", "false");
        if (starttls) {
            mailProperties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        }
        mailSender.setJavaMailProperties(mailProperties);
        mailSender.setHost(host);
        mailSender.setPort(port);
        if (auth) {
            mailSender.setUsername(username);
            mailSender.setPassword(password);
        }
        return mailSender;
    }
}
