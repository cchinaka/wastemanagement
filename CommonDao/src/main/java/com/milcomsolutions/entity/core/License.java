package com.milcomsolutions.entity.core;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


@Entity
public class License extends BaseModel {

    private static final long serialVersionUID = 3721238737000361963L;

    private Date expiryDate;

    private String licenseKey;

    private byte[] licenseFile;

    private String companyId;

    private String companyName;

    private Long fileSize;

    private String fileName;


    @Temporal(TemporalType.TIMESTAMP)
    public Date getExpiryDate() {
        return expiryDate;
    }


    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }


    public String getLicenseKey() {
        return licenseKey;
    }


    public void setLicenseKey(String licenseKey) {
        this.licenseKey = licenseKey;
    }


    @Lob
    public byte[] getLicenseFile() {
        return licenseFile;
    }


    public void setLicenseFile(byte[] licenseFile) {
        this.licenseFile = licenseFile;
    }


    public String getCompanyId() {
        return companyId;
    }


    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }


    public String getFileName() {
        return fileName;
    }


    public void setFileName(String fileName) {
        this.fileName = fileName;
    }


    public Long getFileSize() {
        return fileSize;
    }


    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }


    public String getCompanyName() {
        return companyName;
    }


    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
}
