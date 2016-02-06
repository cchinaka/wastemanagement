package com.milcomsolutions.wastemanagement.vo.api;

public enum ProcessStatus {
	AKNOWLEDGED("Request Acknowledged"), PENDING("Pending Processing"), SERVER_BUSY("Server Busy"), PROCESSED_OK(
			"Processed Successfully"), PROCESSED_ERROR("Processed with Errors");

	private String description;

	ProcessStatus(String description) {
		this.setDescription(description);
	}

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
