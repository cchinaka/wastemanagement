package com.milcomsolutions.entity.core;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;


@Entity
public class ActivityLog extends BaseModel {

    private static final long serialVersionUID = 1L;

    private String name;
    
    private String companyId;

    private String resourceId;
    
    private ActivityResult activityResult=ActivityResult.FAILED;

    private String clientIp;
   
    private String username;
    
    private Boolean guest=Boolean.FALSE;
    
    private String referrer;
    
    public ActivityLog() {
        super();
    }
    
    public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public String getResourceId() {
		return resourceId;
	}

	public void setResourceId(String resourceId) {
		this.resourceId = resourceId;
	}
	
	@Enumerated(EnumType.STRING)
	public ActivityResult getActivityResult() {
		return activityResult;
	}

	public void setActivityResult(ActivityResult activityResult) {
		this.activityResult = activityResult;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getClientIp() {
		return clientIp;
	}

	public void setClientIp(String clientIp) {
		this.clientIp = clientIp;
	}

	public Boolean getGuest() {
		return guest;
	}

	public void setGuest(Boolean guest) {
		this.guest = guest;
	}

	public String getReferrer() {
		return referrer;
	}

	public void setReferrer(String referrer) {
		this.referrer = referrer;
	}

	public enum ActivityResult{
    	SUCCESSFUL,FAILED, UNKNOWN
    }

}
