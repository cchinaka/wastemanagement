package com.milcomsolutions.web.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.milcomsolutions.commons.AppConstants;
import com.milcomsolutions.commons.security.AppPasswordEncoder;
import com.milcomsolutions.entity.core.User;
import com.milcomsolutions.service.UserService;
import com.milcomsolutions.web.security.UserUtil;


@Controller
@RequestMapping({ "/admin/profile", "/user/profile" })
public class UserController extends BaseController {

    private static final Log LOG = LogFactory.getLog(UserController.class);

    @Autowired
    @Qualifier("userService")
    UserService userService;

  

    @ModelAttribute("user")
    public User getUser() {
        return UserUtil.getAuthenticatedUser();
    }


    @RequestMapping("/password")
    public String startPasswordRacketeering() {
        return "profile/password";
    }


    @RequestMapping(method = RequestMethod.GET)
    public String getProfile(@ModelAttribute("user") User user, Model model) {
        model.addAttribute("user", user);
        return "profile/user";
    }


    @RequestMapping(method = RequestMethod.POST)
    public String saveProfile(@ModelAttribute("user") User user, RedirectAttributes redAttribs) {
        try {
            userService.saveUser(user, true);
            addSuccessMessage(redAttribs, "User Profile Updated");
        } catch (Exception e) {
            addDefaultErrorMessage(redAttribs);
            LOG.error(e);
        }
        return "redirect:/user/profile";
    }


    @RequestMapping(value = "/validatepassword", method = RequestMethod.POST)
    public @ResponseBody
    boolean validatePassword(@RequestParam String currentPassword) {
        String oldPassword = UserUtil.getAuthenticatedUser().getPassword();
        return oldPassword.equals(AppPasswordEncoder.getInstance().encodePassword(currentPassword, getUser().getUsername()));
    }


    @RequestMapping(value = "/password", method = RequestMethod.POST)
    public String processPassword(@ModelAttribute("user") User user, RedirectAttributes redAttr) {
        try {
            userService.updateUserPassword(user);
            redAttr.addFlashAttribute(AppConstants.SUCCESS_MSG, AppConstants.DEFAULT_SUCCESS_MESSAGE);
        } catch (Exception e) {
            redAttr.addFlashAttribute(AppConstants.ERROR_MSG, AppConstants.DEFAULT_ERROR_MESSAGE);
            UserController.LOG.error(e);
        }
        return "redirect:/admin/profile/password";
    }
}
