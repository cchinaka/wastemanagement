package com.milcomsolutions.service;

import java.util.Map;

import com.milcomsolutions.entity.core.User;
import com.milcomsolutions.entity.core.VerificationCode;
import com.milcomsolutions.vo.api.RegistrationVO;



public interface NotificationService {

    void sendApplicationstartupNotification(String serverInfo);


    void sendMailNotification(String subject, String messageStr, String[] emailAddress);


    void sendMail(Map<String, Object> messageInfo);


    void sendPasswordResetMail(User user, String tempPass);


	void sendRegisterationNotification(RegistrationVO userVo, VerificationCode vcode);



}
