package com.milcomsolutions.vo.api;

import java.io.File;

import org.springframework.web.multipart.MultipartFile;

import com.milcomsolutions.entity.core.License;
import com.milcomsolutions.entity.core.User;


public class LicenseVo {

    private License license = new License();

    private MultipartFile mpFile;
    
    private File file;

    private User user;

    private String email;


    public LicenseVo() {
    }


    public LicenseVo(License license, MultipartFile mpFile) {
        super();
        setLicense(license);
        setMpFile(mpFile);
    }


    public LicenseVo(License license, MultipartFile mpFile, User user) {
        this(license, mpFile);
        setUser(user);
    }


    public License getLicense() {
        return license;
    }


    public void setLicense(License license) {
        this.license = license;
    }


    public MultipartFile getMpFile() {
        return mpFile;
    }


    public void setMpFile(MultipartFile mpFile) {
        this.mpFile = mpFile;
    }


    public User getUser() {
        return user;
    }


    public void setUser(User user) {
        this.user = user;
    }


    public String getEmail() {
        return email;
    }


    public void setEmail(String email) {
        this.email = email;
    }


	public File getFile() {
		return file;
	}


	public void setFile(File file) {
		this.file = file;
	}
}
