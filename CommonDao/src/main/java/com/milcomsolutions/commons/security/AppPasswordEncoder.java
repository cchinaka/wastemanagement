package com.milcomsolutions.commons.security;


import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class AppPasswordEncoder extends Md5PasswordEncoder {

    private static AppPasswordEncoder encoder;


    @Override
    public String encodePassword(String rawPass, Object salt) {
        return super.encodePassword(rawPass, salt);
    }


    public static AppPasswordEncoder getInstance() {
        if (AppPasswordEncoder.encoder == null) {
            AppPasswordEncoder.encoder = new AppPasswordEncoder();
        }
        return AppPasswordEncoder.encoder;
    }


    public static String generatePassword(String rawPass, String salt) {
        return AppPasswordEncoder.getInstance().encodePassword(rawPass, salt);
    }

}
