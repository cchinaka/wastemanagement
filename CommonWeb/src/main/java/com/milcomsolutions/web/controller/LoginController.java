package com.milcomsolutions.web.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.milcomsolutions.commons.AppConstants;
import com.milcomsolutions.commons.annotations.LogActivity;


@Controller
@RequestMapping
public class LoginController extends BaseController {

    private static final Log LOG = LogFactory.getLog(LoginController.class);


    @RequestMapping("/login")
    public String login(Model model, @RequestParam(required = false) String message) {
        model.addAttribute("message", message);
        return "access/login";
    }


    @RequestMapping(value = "/denied")
    public String denied() {
        return "access/denied";
    }


    @RequestMapping(value = "/login/verify/email", method = RequestMethod.GET)
    public String requestEmailVerification() {
        return "access/email-verify";
    }


    @RequestMapping(value = "/login/verify/email", method = RequestMethod.POST)
    public String handleEmailVerification(@RequestParam(required = true) String email, RedirectAttributes redAttribs) throws Exception {
        try {
            userService.sendUserRegistrationEmailVerificationCode(email);
            addSuccessMessage(redAttribs, String.format(AppConstants.EMAIL_VERIFICATION_SENT_MSG, email));
        } catch (Exception e) {
            addErrorMessage(redAttribs, "A problem was encountered while sending the verification message.");
            LOG.error(e);
        }
        return "redirect:/";
    }


    @RequestMapping(value = "/login/failure")
    public String loginFailure(HttpServletRequest req, RedirectAttributes redAttribs) {
        String message = "Login Failed. Please ensure you've entered your Username and Password correctly.";
        addErrorMessage(redAttribs, message);
        return "redirect:/login";
    }


    @RequestMapping(value = "/login/verify")
    public String loginVeriy(HttpServletRequest req, RedirectAttributes redAttribs) {
        String message = String.format(AppConstants.EMAIL_VERIFICATION_REQU_MSG, StringUtils.EMPTY);
        redAttribs.addFlashAttribute(AppConstants.ERROR_MSG, message);
        return "redirect:/login";
    }


    @RequestMapping(value = "/logout/success")
    @LogActivity(value = "User Logout")
    public String logoutSuccess() {
        return "redirect:/";
    }
}
