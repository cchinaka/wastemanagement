package com.milcomsolutions.config;

import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.ui.velocity.VelocityEngineFactoryBean;

@Configuration
public class NotificationConfig {


	@Value("file:${REMITA_HOME}/templates/mail")
	private String templateDir;
	
	
	@Value("${mail.server.host}")
	private String mailServerHost;
	@Value("${mail.server.port}")
	private int mailServerPort;
	@Value("${mail.server.username}")
	private String mailServerUserName;
	@Value("${mail.server.password}")
	private String mailServerPassword;
	
	@Value("${mail.server.protocol:'smtp'}")
    private String protocol;

    @Value("${mail.server.host}")
    private String host;

    @Value("${mail.server.port:25}")
    private int port;

    @Value("${mail.smtp.auth:true}")
    private boolean auth;

    @Value("${mail.smtp.starttls.enable:false}")
    private boolean starttls;

    @Value("${mail.debug:false}")
    private boolean debugMail;

	
	@Bean
	public  JavaMailSender javaMailSender(){
		JavaMailSenderImpl javaMailSender=new JavaMailSenderImpl();
		javaMailSender.setHost(mailServerHost);
		javaMailSender.setPort(mailServerPort);
		javaMailSender.setUsername(mailServerUserName);
		javaMailSender.setPassword(mailServerPassword);
		javaMailSender.setJavaMailProperties(getMailProperties());
		
		return javaMailSender;
	}
	
	private Properties getMailProperties() {
        Properties properties = new Properties();
        properties.setProperty("mail.transport.protocol", protocol);
        properties.setProperty("mail.smtp.auth", auth+StringUtils.EMPTY);
        properties.setProperty("mail.smtp.starttls.enable", starttls+StringUtils.EMPTY);
        properties.setProperty("mail.debug", debugMail+StringUtils.EMPTY);
        return properties;
    }
	
	@Bean
	public VelocityEngineFactoryBean velocityEngineconfig(){
		VelocityEngineFactoryBean velocityBean=new VelocityEngineFactoryBean();
		velocityBean.setResourceLoaderPath(templateDir);
		return velocityBean;
	}
}
