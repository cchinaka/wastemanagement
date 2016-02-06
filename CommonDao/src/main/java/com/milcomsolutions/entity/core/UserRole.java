package com.milcomsolutions.entity.core;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "AppUserRole")
public class UserRole implements Serializable {

    private static final long serialVersionUID = 1L;

    private String name;

    private String code;

    private String description;


    public UserRole() {
    }


    public UserRole(String code, String description) {
        setCode(code);
        setDescription(description);
    }


    public UserRole(String code) {
        this(code, null);
    }


    @Id
    public String getCode() {
        return this.code;
    }


    public void setCode(String code) {
        this.code = code;
    }


    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }


    public boolean equals(Object object) {
        return object instanceof UserRole && ((UserRole) object).getCode().equals(getCode());
    }


    public String getDescription() {
        return description;
    }


    public void setDescription(String description) {
        this.description = description;
    }


    @Override
    public int hashCode() {
        if (code == null) {
            return 0;
        }
        return code.hashCode();
    }


    public String toString() {
        return String.format("UserRole[%s]", code);
    }
}
