package com.milcomsolutions.web.handler;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;
import org.springframework.stereotype.Component;

import com.milcomsolutions.commons.annotations.LogActivity;
import com.milcomsolutions.entity.core.ActivityLog.ActivityResult;


@Component
public class LoginSuccessHandler extends SimpleUrlLogoutSuccessHandler implements AuthenticationSuccessHandler, AuthenticationFailureHandler {

    private static final String HOME = "/dash";

    private final RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    static final Log LOG = LogFactory.getLog(LoginSuccessHandler.class);


    private void storeUserInfoInSession(HttpServletRequest request, Authentication authentication) {
        LOG.info("shit to go down here afte a successful login...");
    }


    @Override
    @LogActivity(value = "User Logon")
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {
        LOG.info("on auth success before commitment check...");
        if (response.isCommitted()) {
            return;
        }
        LOG.info("on auth success after commitment check...");
        storeUserInfoInSession(request, authentication);
        redirectStrategy.sendRedirect(request, response, LoginSuccessHandler.HOME);
    }


    @Override
    @LogActivity(value = "User Logon", result = ActivityResult.FAILED)
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exc)
            throws IOException, ServletException {
        redirectStrategy.sendRedirect(request, response, "/login/failure");
    }


    @Override
    @LogActivity(value = "User LogOut")
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        super.onLogoutSuccess(request, response, authentication);
    }
}
