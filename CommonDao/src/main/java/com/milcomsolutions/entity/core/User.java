package com.milcomsolutions.entity.core;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.PostLoad;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.apache.commons.lang.StringUtils;

import com.milcomsolutions.commons.security.AppPasswordEncoder;


@Entity
@Table(name = "AppUser")
public class User extends ProcessActor {

    private static final long serialVersionUID = 1L;

    private String username;

    private String password;

    private Set<UserRole> roles = new HashSet<UserRole>();

    private boolean accountNonExpired = true;

    private boolean credentialsNonExpired = true;

    private boolean accountNonLocked = true;

    private boolean verified;

    private UserDetails userDetail = new UserDetails();

    private String tempPass;

    private Long companyId;

    private Date lastLoggedIn;

    private List<String> roleCodes = new ArrayList<>();

    private String currentPassword;

    public static final String INIT_QL = "u.id,u.username,u.userDetail.firstName, u.userDetail.lastName";

    private Company company;


    public User() {
        super();
    }


    public User(Long userId) {
        super(userId);
    }


    public User(Long id, String username, String firstName, String lastName) {
        this(id);
        setUsername(username);
        setUserDetail(new UserDetails());
        getUserDetail().setFirstName(firstName);
        getUserDetail().setLastName(lastName);
        getUserDetail().setEmail(username);
    }


    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }


    @Temporal(TemporalType.TIMESTAMP)
    public Date getLastLoggedIn() {
        return lastLoggedIn;
    }


    public void setLastLoggedIn(Date lastLoggedIn) {
        this.lastLoggedIn = lastLoggedIn;
    }


    @Override
    public String toString() {
        return String.format("%s: %s; id: %s; last login date: ", this.getClass().getSimpleName(), getName(), getId(), getLastLoggedIn());
    }


    @Column(unique = true)
    public String getUsername() {
        return username;
    }


    public void setUsername(String username) {
        this.username = username;
    }


    public String getPassword() {
        return password;
    }


    public void setPassword(String password) {
        this.password = password;
    }


    public boolean isAccountNonExpired() {
        return accountNonExpired;
    }


    public void setAccountNonExpired(boolean accountNonExpired) {
        this.accountNonExpired = accountNonExpired;
    }


    public boolean isCredentialsNonExpired() {
        return credentialsNonExpired;
    }


    public void setCredentialsNonExpired(boolean credentialsNonExpired) {
        this.credentialsNonExpired = credentialsNonExpired;
    }


    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }


    public void setAccountNonLocked(boolean accountNonLocked) {
        this.accountNonLocked = accountNonLocked;
    }


    @Embedded
    public UserDetails getUserDetail() {
        return userDetail;
    }


    public void setUserDetail(UserDetails userDetail) {
        this.userDetail = userDetail;
    }


    @Override
    public String getName() {
        if (userDetail != null) {
            name = userDetail.getFirstName() + " " + userDetail.getLastName();
        }
        return name;
    }


    @ManyToMany
    public Set<UserRole> getRoles() {
        return roles;
    }


    public void setRoles(Set<UserRole> roles) {
        this.roles = roles;
    }


    @PreUpdate
    @PrePersist
    public void resolveUserDetails() {
        setName(String.format("%s %s", userDetail.getFirstName(), userDetail.getLastName()));
    }


    @PostLoad
    public void setUpUser() {
        setCurrentPassword(getPassword());
    }


    @Override
    public User clone() throws CloneNotSupportedException {
        return (User) super.clone();
    }


    public boolean hasRole(String roleCode) {
        for (UserRole role : roles) {
            if (role.getCode().equals(roleCode)) {
                return true;
            }
        }
        return false;
    }


    public void encodePassword() {
        if (StringUtils.isNotBlank(password)) {
            password = AppPasswordEncoder.getInstance().encodePassword(password, username);
        }
    }


    @Transient
    public String getTempPass() {
        return tempPass;
    }


    public void setTempPass(String tempPass) {
        this.tempPass = tempPass;
    }


    @Transient
    public Long getCompanyId() {
        return companyId;
    }


    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }


    public boolean isVerified() {
        return verified;
    }


    public void setVerified(boolean verified) {
        this.verified = verified;
    }


    @Transient
    public void prepareRoleIds(List<UserRole> findUserRoles) {
        getRoleCodes().clear();
        for (UserRole role : findUserRoles) {
            getRoleCodes().add(role.getCode());
        }
    }


    @Transient
    public String getCurrentPassword() {
        return currentPassword;
    }


    public void setCurrentPassword(String currentPassword) {
        this.currentPassword = currentPassword;
    }


    public void setRoleCodes(List<String> roleCodes) {
        this.roleCodes = roleCodes;
    }


    @Transient
    public List<String> getRoleCodes() {
        return roleCodes;
    }


    @ManyToOne(fetch = FetchType.LAZY)
    public Company getCompany() {
        return company;
    }


    public void setCompany(Company company) {
        this.company = company;
    }
}
