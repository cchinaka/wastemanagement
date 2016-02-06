package com.milcomsolutions.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.milcomsolutions.service.UserService;


@Controller
@RequestMapping
public class PasswordResetController extends BaseController {

    @Autowired
    @Qualifier("userService")
    UserService userService;


    @RequestMapping(value = "/passwordreset", method = RequestMethod.GET)
    public String startResetPassword(Model model, @RequestParam(required = false) String message) {
        model.addAttribute("message", message);
        return "access/passwordreset";
    }


    @RequestMapping(value = "/passwordreset", method = RequestMethod.POST)
    public String processPassword(@ModelAttribute("email") String email, RedirectAttributes redAttr) {
        String nextView = "redirect:/login";
        try {
            boolean passwordReset = userService.resetPassword(email);
            if (passwordReset) {
                addSuccessMessage(redAttr, String.format("A new password as been sent to you mail %s", email));
            } else {
                throw new Exception(String.format("Error reseting password for user %s.contact support", email));
            }
        } catch (Exception e) {
            addDefaultErrorMessage(redAttr);
            nextView = "redirect:/passwordreset";
        }
        return nextView;
    }
}
