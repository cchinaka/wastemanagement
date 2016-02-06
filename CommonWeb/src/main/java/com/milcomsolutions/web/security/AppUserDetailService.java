package com.milcomsolutions.web.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.milcomsolutions.entity.core.Company;
import com.milcomsolutions.entity.core.User;
import com.milcomsolutions.entity.core.UserRole;
import com.milcomsolutions.service.UserService;


@Service
public class AppUserDetailService implements UserDetailsService {

    private static final Log LOG = LogFactory.getLog(AppUserDetailService.class);

    @Autowired
    @Qualifier("userService")
    private UserService userService;


    private Collection<GrantedAuthority> getGrantedAuthorities(Collection<UserRole> roles) {
        List<GrantedAuthority> listOfAuthorities = new ArrayList<GrantedAuthority>();
        for (UserRole role : roles) {
            listOfAuthorities.add(new SimpleGrantedAuthority(role.getCode()));
        }
        return listOfAuthorities;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException, DataAccessException {
        AppUserDetailService.LOG.info("will load user service detail in here. pair: " + username);
        SecureAppUser secUser = null;
        User user = null;
        try {
            user = userService.findUserByUsername(username);
            LOG.info("user: " + user);
        } catch (Exception e) {
            e.printStackTrace();
            throw new UsernameNotFoundException("Invalid username or password");
        }
        try {
            List<UserRole> roles = userService.getUserRoles(user);
            Company userCompany = userService.getUserCompanyLight(user);
            secUser = new SecureAppUser(user, userCompany, roles, getGrantedAuthorities(roles));
            AppUserDetailService.LOG.info("secUser: " + secUser);
        } catch (Exception e) {
            LOG.error("problem getting roles and initing a secure user", e);
        }
        return secUser;
    }
}
