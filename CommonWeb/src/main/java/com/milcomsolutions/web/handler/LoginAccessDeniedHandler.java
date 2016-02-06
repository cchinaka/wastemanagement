package com.milcomsolutions.web.handler;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;


@Component("loginAccessDeniedHandler")
public class LoginAccessDeniedHandler implements AccessDeniedHandler {

    private static final Log LOG = LogFactory.getLog(LoginAccessDeniedHandler.class);


    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException exception) throws IOException, ServletException {
        LOG.info("request: " + request);
        LOG.error("exception in access denied.", exception);
    }
}
