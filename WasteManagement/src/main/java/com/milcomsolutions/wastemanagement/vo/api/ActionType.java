package com.milcomsolutions.wastemanagement.vo.api;

public enum ActionType {

	DB("DataBase"), HTTP("Http endpoint call"), EMAIL("Email Notifiation");

	private String description;

	ActionType(String description) {
		this.setDescription(description);
	}

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
