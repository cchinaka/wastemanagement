package com.milcomsolutions.entity.core;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class VerificationCode extends BaseModel {

	private static final long serialVersionUID = 1L;

	private Date expirationDate;
	private VerificationType verifivationType;
	private String verificationCode;
	private long verificationItemId;
	private String owner;

	@Temporal(TemporalType.TIMESTAMP)
	public Date getExpirationDate() {
		return expirationDate;
	}

	public void setExpirationDate(Date expirationDate) {
		this.expirationDate = expirationDate;
	}

	@Column(nullable = false, unique = true)
	public String getVerificationCode() {
		return verificationCode;
	}

	public void setVerificationCode(String verificationCode) {
		this.verificationCode = verificationCode;
	}

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	public VerificationType getVerifivationType() {
		return verifivationType;
	}

	public void setVerifivationType(VerificationType verifivationType) {
		this.verifivationType = verifivationType;
	}

	@Column(nullable = false)
	public long getVerificationItemId() {
		return verificationItemId;
	}

	public void setVerificationItemId(long verificationItemId) {
		this.verificationItemId = verificationItemId;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}
}
