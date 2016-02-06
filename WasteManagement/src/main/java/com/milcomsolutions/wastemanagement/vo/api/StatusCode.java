package com.milcomsolutions.wastemanagement.vo.api;

public enum StatusCode {
    SUCCESS("00", "Transaction Completed Succesfully"), APPROVED("01", "Approved"), ABORTED("012", "User Aborted Transaction"), UNKNOWN("999", "Unknown Error"), AUTHENTICATION_ERROR(
            "020", "Invalid User Authentication"), NO_FUND("030", "Insufficent Balance"), REQUEST_OK("040", "Initail Request OK"), NO_ACCOUNT("031",
            "No Funding Account"), PENDING("021", "Transaction Pending"), INVALID_REQUEST("022", "Invalid Request"), INVALID_SERVICE_MERCHANT("023",
            "Service type or Merchant Does not Exist"), MERCHANT_STATUS_ERROR("024", "Inactive Merchant"), TRANSACTION_FAILED("02", "Transaction Failed"), CHANNEL_RESPONSE_UNMAPPED(
            "997", "Payment Gateway Unsucessful"), INTERNAL_ERROR("998", "An internal Error Occured"), REFRENCE_GENERATED("025", "Payment Reference generated"), UNKNOWN_ORDER(
            "026", "No such Order Request"), ALREADY_PROCESSED("027", "Transaction Already Processed"), DUPLICATE_REQUEST("028", "Duplicate Order Ref"), INVALID_BANKCODE(
            "029", "Invalid Bank Code"), INVALID_DATEFORMAT("032", "Invalid Date Format"), INVALID_AUTHTOKEN("033", "Invalid Authentication Token"), INVALID_FUNDINGSOURCE(
            "034", "Invalid Funding Source"), LIMIT_EXCEEDED("035", "Payment Limit Exceeded"), DUPLICATE_UNIQUEREF("036", "Duplicate Unique Reference"), DUPLICATE_lINEITEM(
            "037", "Duplicate Line Item ID"), LOW_AMOUNT("038", "Processing Fees exceeds Amount Specified"), INVALID_PAYMENT_MODE("039",
            "Invalid Payment Mode Type Supplied"), TRANSACTION_FORWARDED("041", "Transaction Forwarded for Processing"), COMPANY_NOTONTOPUP("042",
            "You are not enabled for Account Transfer"), INACTIVE_SERVICETYPE("043", "Service Type is not Active"), INVALID_INTEGRATION_SERVICETYPE("044",
            "Service Type is not Enabled for Integration"), REFERENCE_WITH_RRR("055", "RRR Already Exist for the orderId");
    ;;

    private String code;

    private String description;


    StatusCode(String code, String description) {
        this.code = code;
        this.description = description;
    }


    public String getCode() {
        return code;
    }


    public void setCode(String code) {
        this.code = code;
    }


    public String getDescription() {
        return description;
    }


    public void setDescription(String description) {
        this.description = description;
    }


    // @Override
    // public String toString() {
    // // TODO Auto-generated method stub
    // return getCode();
    // }
    public static StatusCode getStatusByCode(String code) {
        StatusCode retrunStatusCode = null;
        for (StatusCode statusCode : StatusCode.values()) {
            if (statusCode.getCode().equalsIgnoreCase(code)) {
                retrunStatusCode = statusCode;
                break;
            }
        }
        return retrunStatusCode;
    }
}
