package com.milcomsolutions.web.controller;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.milcomsolutions.commons.AppConstants;
import com.milcomsolutions.entity.core.License;
import com.milcomsolutions.service.UserService;
import com.milcomsolutions.vo.api.LicenseVo;
import com.milcomsolutions.web.security.UserUtil;


@Controller
@RequestMapping({ "/licensing" })
public class LicenseController {

    private static final Log LOG = LogFactory.getLog(LicenseController.class);

    @Autowired
    UserService service;


    @ModelAttribute
    public LicenseVo getLicenseVo() {
        LicenseVo licenseVo = new LicenseVo();
        licenseVo.setUser(UserUtil.getAuthenticatedUser());
        try {
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("companyId", UserUtil.getAuthenticatedUser().getCompanyId());
            licenseVo.setLicense(service.findEntityByParams(License.class, params));
        } catch (Exception e) {
            LicenseController.LOG.error("problem loading license...");
        }
        return licenseVo;
    }


    @RequestMapping
    public String start(@ModelAttribute LicenseVo licenseVo) {
        return "licensing";
    }


    @RequestMapping(method = RequestMethod.POST)
    public String saveLicense(@ModelAttribute LicenseVo licenseVo, RedirectAttributes redAttr) throws Exception {
        try {
            service.addLicense(licenseVo);
            redAttr.addFlashAttribute(AppConstants.SUCCESS_MSG,
                    "License  Saved Successfully. <a href='logout' class='alert-link'>Click here to relogin.</a>");
        } catch (Exception e) {
            redAttr.addFlashAttribute(AppConstants.ERROR_MSG, AppConstants.DEFAULT_ERROR_MESSAGE);
            LicenseController.LOG.error(e);
            e.printStackTrace();
        }
        return "redirect:/licensing";
    }
}
