package com.milcomsolutions.listener;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import com.milcomsolutions.commons.AppConstants;
import com.milcomsolutions.entity.core.User;
import com.milcomsolutions.entity.core.UserRole;
import com.milcomsolutions.service.UserService;
import com.milcomsolutions.vo.api.LicenseVo;


@Component
public class ContextRefreshedListener implements ApplicationListener<ContextRefreshedEvent> {

    private final AtomicBoolean contextProcessed = new AtomicBoolean(false);

    private static final Log LOG = LogFactory.getLog(ContextRefreshedListener.class);

    @Value("${PROJECT_HOME}/licesnse.lic")
    String licPath;

    @Autowired
    private UserService userService;

    @Value("${app.version:0.1}")
    private int appVersion;


    public void initializedDefaultRoles() {
        try {
            boolean init = true;
            try {
                ContextRefreshedListener.LOG.info("initializing application default Roles");
                init = (userService.findRoleByCode(AppConstants.ROLE_ADMIN) == null);
            } catch (Exception ex) {
                LOG.error("problem getting roles by code. What's the Matter? " + ex.getMessage());
            }
            if (init) {
                createDefaultSystemRoles();
                File licFile = new File(licPath);
                if (licFile.exists()) {
                    LicenseVo licenseVo = new LicenseVo();
                    licenseVo.setEmail("System@admin.com");
                    licenseVo.setFile(licFile);
                    User user = userService.install(licenseVo);
                    if (user != null) {
                        licFile.renameTo(new File(licPath + ".done"));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private List<UserRole> createDefaultSystemRoles() throws Exception {
        List<UserRole> roles = new ArrayList<>();
        roles.add(new UserRole(AppConstants.ROLE_ADMIN, "System Administrator role"));
        roles.add(new UserRole(AppConstants.ROLE_LICENSE, "Lincese User Role"));
        roles.add(new UserRole(AppConstants.ROLE_USER, "Normal User Role"));
        return userService.saveEntities(roles);
    }


    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        ContextRefreshedListener.LOG.info("Context Event Received");
        if (!contextProcessed.get()) {
            initializedDefaultRoles();
        }
        contextProcessed.set(true);
    }
}
