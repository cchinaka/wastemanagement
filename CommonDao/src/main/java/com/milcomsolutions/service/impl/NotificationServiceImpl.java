package com.milcomsolutions.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.milcomsolutions.commons.AppConstants;
import com.milcomsolutions.entity.core.User;
import com.milcomsolutions.entity.core.VerificationCode;
import com.milcomsolutions.service.EmailSendBuilder;
import com.milcomsolutions.service.NotificationService;
import com.milcomsolutions.service.UserService;
import com.milcomsolutions.vo.api.RegistrationVO;


@Service("notificationService")
public class NotificationServiceImpl implements NotificationService {

    static final String LIST_TOOL = "listTool";

    @Autowired
    UserService userService;

    @Autowired
    JavaMailSender mailSender;

    @Autowired
    VelocityEngine velocityEngine;

    @Value("${spring.application.name}")
    private String applicationName;

    @Value("${application.suppourt.mail}")
    private String supportMailAddress;

    @Value("${application.mail.from}")
    private String defaultFromMailAddress;

    @Value("${mail.enabled:false}")
    private boolean mailEnabled;

    @Value("${spring.product.web.url}")
    private String siteUrl;

    @Autowired
    private EmailSendBuilder emailSendBuilder;

    private static final Log LOG = LogFactory.getLog(NotificationServiceImpl.class);

    private final SimpleDateFormat dateFormat = new SimpleDateFormat(AppConstants.DATE_FORMAT_JAVA);


    @Override
    public void sendApplicationstartupNotification(String serverInfo) {
        Map<String, Object> message = new HashMap<String, Object>();
        Map<String, Object> model = new HashMap<String, Object>();
        model.put("serverInfo", serverInfo);
        model.put("serverEnv", System.getenv());
        model.put("starttime", new Date());
        model.put("serverIp", "");
        model.put("applicationname", applicationName);
        message.put("MAIL_MODEL", model);
        message.put("MAIL_TO", "isholaolalekan@hotmail.com");
        message.put("MAIL_SUBJECT", String.format("%s Application Startup", applicationName));
        message.put("MAIL_TEMPLATE", "/pg_application_startup.vm");
        sendMail(message);
        message.put("SMS_MODEL", model);
        message.put("SMS_TO", new String[] { "08141376868" });
        message.put("SMS_SUBJECT", String.format("%s Application Startup", applicationName));
        message.put("SMS_BODY", "APPlication restart");
        // sendSMS(message);
        NotificationServiceImpl.LOG.info("**Application Startup");
    }


    @Override
    public void sendMailNotification(String subject, String messageStr, String[] emailAddress) {
        Map<String, Object> message = new HashMap<String, Object>();
        message.put("MAIL_TO", emailAddress);
        message.put("MAIL_SUBJECT", subject);
        message.put("MAIL_BODY", message);
        sendMail(message);
    }


    @Override
    public void sendMail(final Map<String, Object> messageInfo) {
        emailSendBuilder.doSend(messageInfo);
    }


    @Override
    public void sendPasswordResetMail(User user, String tempPass) {
        String email = user.getUsername();
        Map<String, Object> message = new HashMap<String, Object>();
        Map<String, Object> model = new HashMap<String, Object>();
        model.put("name", user.getUserDetail().getFirstName());
        model.put("email", user.getUsername());
        model.put("newPassword", tempPass);
        message.put("MAIL_MODEL", model);
        message.put("MAIL_TO", email);
        message.put("MAIL_SUBJECT", String.format("%s Password Reset", applicationName));
        message.put("MAIL_TEMPLATE", "/pg_password_reset.vm");
        NotificationServiceImpl.LOG.info(String.format("new Password for %s %s sent by mail", email, tempPass));
        sendMail(message);
    }


    @Override
    public void sendRegisterationNotification(RegistrationVO userVo, VerificationCode vcode) {
        NotificationServiceImpl.LOG.info("Verification Code :" + vcode.getVerificationCode());
        String email = userVo.getUser().getUserDetail().getEmail();
        String verificationUrl = siteUrl;
        Map<String, Object> message = new HashMap<String, Object>();
        Map<String, Object> model = new HashMap<String, Object>();
        model.put("name", String.format("%s %s", userVo.getUser().getUserDetail().getFirstName(), userVo.getUser().getUserDetail().getLastName()));
        model.put("email", email);
        model.put("date", dateFormat.format(new Date()));
        model.put("verificationUrl", String.format("%s/register/%s/verify", verificationUrl, vcode.getVerificationCode()));
        model.put("verificationCancelUrl", String.format("%s/register/%s/verify/cancel", verificationUrl, vcode.getVerificationCode()));
        message.put("MAIL_MODEL", model);
        message.put("MAIL_TO", email);
        message.put("MAIL_SUBJECT", String.format("%s Registration", applicationName));
        message.put("MAIL_TEMPLATE", "/pg_user_reg.vm");
        sendMail(message);
    }
}
