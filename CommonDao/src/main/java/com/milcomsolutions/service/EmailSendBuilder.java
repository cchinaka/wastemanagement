package com.milcomsolutions.service;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

import javax.mail.internet.InternetAddress;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.validator.routines.EmailValidator;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.tools.generic.ListTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;


@SuppressWarnings("deprecation")
@Component
public class EmailSendBuilder {

    private static final Log LOG = LogFactory.getLog(EmailSendBuilder.class);

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private VelocityEngine velocityEngine;

    @Value("${spring.application.name}")
    private String applicationName;

    @Value("${application.suppourt.mail}")
    private String supportMailAddress;

    @Value("${application.mail.from}")
    private String defaultFromMailAddress;

    @Value("${mail.enabled:false}")
    private boolean mailEnabled;


    public void sendMail(String message) {
        EmailSendBuilder.LOG.info("attempting Sending email message");
        // Map<String,Object> messageInfo=JMSUtils.convertFromJMSMessageString(message);
        // doSend(messageInfo);
    }


    @SuppressWarnings({ "unchecked" })
    public void doSend(Map<String, Object> messageInfo) {
        String[] mailTo = null;
        boolean error = false;
        try {
            Map<String, Object> model = (Map<String, Object>) messageInfo.get("MAIL_MODEL");
            model.put("productname", applicationName);
            model.put("supportMailAddress", supportMailAddress);
            if (messageInfo.get("MAIL_TO") instanceof String[]) {
                mailTo = (String[]) messageInfo.get("MAIL_TO");
            } else {
                mailTo = String.valueOf(messageInfo.get("MAIL_TO")).split("'");
            }
            mailTo = resolveValidEmailAddresses(mailTo);
            if (ArrayUtils.isEmpty(mailTo)) {
                // LOG.info(String.format(">> Sending mail from %s to %s",supportMailAddress, Arrays.toString(mailTo)));
            } else {
                EmailSendBuilder.LOG.info(String.format(">> Sending mail from %s to %s", supportMailAddress, Arrays.toString(mailTo)));
                String mailSubject = String.valueOf(messageInfo.get("MAIL_SUBJECT"));
                model.put("subject", mailSubject);
                String mailTemplate = String.valueOf(messageInfo.get("MAIL_TEMPLATE"));
                String body = String.valueOf(messageInfo.get("MAIL_BODY"));
                if (!messageInfo.containsKey("MAIL_BODY")) {
                    VelocityContext context = new VelocityContext(model);
                    context.put("LIST_TOOL", new ListTool());
                    Template t = null;
                    try {
                        t = velocityEngine.getTemplate("/pg_mail_header.vm");
                        StringWriter writer = new StringWriter();
                        t.merge(context, writer);
                        body = writer.toString();
                        t = velocityEngine.getTemplate(mailTemplate);
                        StringWriter bodywriter = new StringWriter();
                        t.merge(context, bodywriter);
                        body += bodywriter.toString();
                        t = velocityEngine.getTemplate("/pg_mail_footer.vm");
                        StringWriter footerwriter = new StringWriter();
                        t.merge(context, footerwriter);
                        body += footerwriter.toString();
                    } catch (Exception e) {
                        EmailSendBuilder.LOG.error(e);
                        error = true;
                    }
                }
                if (error) {
                    EmailSendBuilder.LOG.info(String.format("Error sending mail from %s to %s", supportMailAddress, mailTo));
                } else {
                    if (mailEnabled) {
                        MimeMessageHelper helper = new MimeMessageHelper(mailSender.createMimeMessage());
                        helper.setFrom(new InternetAddress(defaultFromMailAddress, applicationName));
                        helper.setTo(resolveUserEmailAddress(mailTo));
                        helper.setSubject(mailSubject);
                        helper.setText(body, true);
                        mailSender.send(helper.getMimeMessage());
                    } else {
                        EmailSendBuilder.LOG.info("Mail sending not enabled.see configuration mail.enabled");
                        EmailSendBuilder.LOG.info(body);
                    }
                }
            }
        } catch (Exception e) {
            EmailSendBuilder.LOG.error(e);
        }
    }


    private String[] resolveValidEmailAddresses(String[] mailTo) {
        ArrayList<String> validEmail = new ArrayList<String>();
        for (String email : mailTo) {
            if (EmailValidator.getInstance().isValid(email)) {
                validEmail.add(email);
            }
        }
        return validEmail.toArray(new String[] {});
    }


    private String[] resolveUserEmailAddress(String... userNames) {
        return userNames;
    }
}
