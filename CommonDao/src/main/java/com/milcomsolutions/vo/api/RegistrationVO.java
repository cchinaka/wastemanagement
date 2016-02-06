package com.milcomsolutions.vo.api;

import com.milcomsolutions.entity.core.User;


public class RegistrationVO {

    private User user = new User();

    private boolean sendVerificationEmail;


    public User getUser() {
        return user;
    }


    public void setUser(User user) {
        this.user = user;
    }


    public boolean isSendVerificationEmail() {
        return sendVerificationEmail;
    }


    public void setSendVerificationEmail(boolean sendVerificationEmail) {
        this.sendVerificationEmail = sendVerificationEmail;
    }
}
