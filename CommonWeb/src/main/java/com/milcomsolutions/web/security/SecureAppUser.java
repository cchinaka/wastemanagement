package com.milcomsolutions.web.security;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;

import com.milcomsolutions.entity.core.Company;
import com.milcomsolutions.entity.core.User;
import com.milcomsolutions.entity.core.UserRole;


public class SecureAppUser extends org.springframework.security.core.userdetails.User {

    private static final long serialVersionUID = 1L;

    private User user;

    private List<UserRole> roles;

    private String companyName;

    private Long companyId;


    public SecureAppUser(User user, Company company, List<UserRole> roles, Collection<GrantedAuthority> grantedAuthorities) {
        super(user.getUsername(), user.getPassword(), user.isActive(), user.isAccountNonExpired(), user.isCredentialsNonExpired(), user.isAccountNonLocked(),
                grantedAuthorities);
        this.user = user;
        this.user.setCompanyId(company.getId());
        this.roles = roles;
        this.companyName = company.getName();
        this.companyId = company.getId();
    }


    public SecureAppUser(String username, String password, boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked,
            Collection<GrantedAuthority> authorities) {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
    }


    public User getUser() {
        return user;
    }


    public void setUser(User appUser) {
        this.user = appUser;
    }


    public String getUserName() {
        return user.getUsername();
    }


    public void setUserName(String username) {
        user.setUsername(username);
    }


    public List<UserRole> getRoles() {
        return roles;
    }


    public void setRoles(List<UserRole> roles) {
        this.roles = roles;
    }


    public String getCompanyName() {
        return companyName;
    }


    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }


    public Long getCompanyId() {
        return companyId;
    }


    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }
}
