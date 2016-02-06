package com.milcomsolutions.entity.core;

public enum VerificationType {

    USERREG("User-Registration");

    VerificationType(String description) {
        setDescription(description);
    }


    private String description;


    public String getDescription() {
        return description;
    }


    public void setDescription(String description) {
        this.description = description;
    }

}
