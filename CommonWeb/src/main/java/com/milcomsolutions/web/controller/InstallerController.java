package com.milcomsolutions.web.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.milcomsolutions.commons.AppConstants;
import com.milcomsolutions.entity.core.User;
import com.milcomsolutions.service.UserService;
import com.milcomsolutions.vo.api.LicenseVo;


@Controller
@RequestMapping("/install")
public class InstallerController extends BaseController {

    private static final Log LOG = LogFactory.getLog(InstallerController.class);

    @Autowired
    UserService service;


    @ModelAttribute("licenseVo")
    public LicenseVo getLicenseVo() {
        return new LicenseVo();
    }


    @RequestMapping
    public String start(RedirectAttributes redAttr) {
        String view = "install";
        if (service.doRecordsExist(User.class, "id")) {
            InstallerController.LOG.info("installation complete...procceding to home page");
            String message = "Installation already done!";
            redAttr.addFlashAttribute(AppConstants.SUCCESS_MSG, message);
            view = "redirect:/login";
        }
        return view;
    }


    @RequestMapping(method = RequestMethod.POST)
    public String install(LicenseVo licenseVo, RedirectAttributes redAttr) throws Exception {
        String view = "redirect:/login";
        try {
            User user = service.install(licenseVo);
            String message = String.format("Installation Successful");
            if (user != null) {
                // FIXME OK so liceses has been done what next
            }
            redAttr.addFlashAttribute(AppConstants.SUCCESS_MSG, message);
        } catch (Exception e) {
            view = "redirect:/install";
            redAttr.addFlashAttribute(AppConstants.ERROR_MSG, AppConstants.DEFAULT_ERROR_MESSAGE);
            InstallerController.LOG.error(e);
        }
        InstallerController.LOG.info("installing...");
        return view;
    }
}
