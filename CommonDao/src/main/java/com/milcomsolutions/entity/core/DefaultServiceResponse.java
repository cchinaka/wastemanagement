package com.milcomsolutions.entity.core;

import javax.persistence.MappedSuperclass;


@MappedSuperclass
public class DefaultServiceResponse extends BaseModel {

    private static final long serialVersionUID = -3316838074528036341L;

    private String responseCode;

    private String responseMessage;

    private String responseId;


    public String getResponseId() {
        return responseId;
    }


    public void setResponseId(String responseId) {
        this.responseId = responseId;
    }


    public String getResponseCode() {
        return responseCode;
    }


    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }


    public String getResponseMessage() {
        return responseMessage;
    }


    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }
}
