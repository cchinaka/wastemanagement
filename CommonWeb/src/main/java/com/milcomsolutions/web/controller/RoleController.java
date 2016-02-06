package com.milcomsolutions.web.controller;

import java.util.List;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.milcomsolutions.entity.core.UserRole;


@RestController
@RequestMapping("/guest/roles")
public class RoleController extends BaseController {

    @ModelAttribute("role")
    public UserRole getUserRole() {
        return new UserRole();
    }


    @RequestMapping(method = RequestMethod.GET)
    public List<UserRole> loadRoles(Model model) {
        return genericService.findAllEntities(UserRole.class);
    }


    @RequestMapping(value = "{roleId}", method = RequestMethod.GET)
    public UserRole editRole(@PathVariable Long roleId, Model model) {
        return genericService.findEntityById(UserRole.class, roleId);
    }


    @RequestMapping(method = RequestMethod.POST)
    public UserRole saveRole(@ModelAttribute("role") UserRole role, RedirectAttributes redAttribs) throws Exception {
        role = genericService.saveEntity(role);
        return role;
    }
}
