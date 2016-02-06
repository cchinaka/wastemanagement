package com.milcomsolutions.entity.core;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;


@Entity
public class Company extends BaseModel {

    private static final long serialVersionUID = 6406332916791004395L;

    public static final String INIT_LIGHTEST = "c.id";

    public static final String INIT_LIGHT_WITH_PARENT = "c.id,c.name,c.code,c.parentCompany.id";

    public static final String INIT_LIGHT = "c.id,c.name,c.code";

    private String name;

    private String code;

    private Set<User> users = new HashSet<User>();

    private Set<Company> childCompanies = new HashSet<>();

    private Company parentCompany;

    private Map<String, Object> parentCompanyInfo = new HashMap<>();

    private String ref1;


    public Company() {
        super();
    }


    public Company(Long id) {
        this();
        setId(id);
    }


    public Company(Long id, String name, String code) {
        this(id);
        setName(name);
        setCode(code);
    }


    public Company(Long id, String name, String code, Long parentId) {
        this(id, name, code);
        setParentCompany(new Company(parentId));
    }


    @Column(unique = true)
    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }


    @OneToMany(mappedBy = "company", cascade = CascadeType.PERSIST)
    @JsonIgnore
    public Set<User> getUsers() {
        return users;
    }


    public void setUsers(Set<User> users) {
        this.users = users;
    }


    @Column(unique = true)
    public String getCode() {
        return code;
    }


    public void setCode(String code) {
        this.code = code;
    }


    @ManyToOne
    @JsonIgnore
    public Company getParentCompany() {
        return parentCompany;
    }


    public void setParentCompany(Company parentCompany) {
        this.parentCompany = parentCompany;
    }


    @OneToMany(mappedBy = "parentCompany")
    @JsonIgnore
    public Set<Company> getChildCompanies() {
        return childCompanies;
    }


    public void setChildCompanies(Set<Company> childCompanies) {
        this.childCompanies = childCompanies;
    }


    @Transient
    public Map<String, Object> getParentCompanyInfo() {
        return parentCompanyInfo;
    }


    public void setParentCompanyInfo(Map<String, Object> parentCompanyInfo) {
        this.parentCompanyInfo = parentCompanyInfo;
    }


    public String getRef1() {
        return ref1;
    }


    public void setRef1(String ref1) {
        this.ref1 = ref1;
    }
}
