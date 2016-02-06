package com.milcomsolutions.web.handler;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;
import org.springframework.stereotype.Component;

import com.milcomsolutions.commons.annotations.LogActivity;


@Component
public class LogoutSuccessHandler extends SimpleUrlLogoutSuccessHandler {

    static final Log LOG = LogFactory.getLog(LogoutSuccessHandler.class);


    @Override
    @LogActivity(value = "User LogOut")
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        super.onLogoutSuccess(request, response, authentication);
    }
}
