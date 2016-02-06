package com.milcomsolutions.web.security;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.core.context.SecurityContextHolder;

import com.milcomsolutions.entity.core.User;


public class UserUtil {

    private static final Log LOG = LogFactory.getLog(UserUtil.class);


    public static User getAuthenticatedUser() {
        User user = null;
        try {
            if (!(SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof String)) {
                user = ((SecureAppUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();
                UserUtil.LOG.debug("authenticated user name: " + user.getName());
            }
        } catch (Exception e) {
            UserUtil.LOG.error("problem loading secured user. Message: " + e.getMessage());
        }
        return user;
    }


    public static SecureAppUser getSecuredUser() {
        SecureAppUser user = null;
        try {
            if (!(SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof String)) {
                user = ((SecureAppUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
            }
        } catch (Exception e) {
            UserUtil.LOG.error("problem loading secured user. Message: " + e.getMessage());
        }
        return user;
    }
}
