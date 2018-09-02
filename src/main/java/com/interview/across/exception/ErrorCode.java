package com.interview.across.exception;

/**
 * @ author: Chenglong Li
 * @ date: 01/09/2018
 */
public enum ErrorCode {
  ;

  public enum Internal implements IError {

    OPERATION_FAILED("10500002", "Operation failed. %s"),
    OPERATION_FAILED_DEFAULT("10500001",
        "Operation failed. Please contact the service provider for help");

    private String code;
    private String message;

    Internal(String code, String message) {
      this.code = code;
      this.message = message;
    }

    @Override
    public String getCode() {
      return code;
    }

    @Override
    public String getMessage() {
      return message;
    }
  }

  public enum BadRequest implements IError {
    MISSING_PARAMETER("10400001", "Missing parameters: %s"),
    FAILED_UPLOAD_HEADPORTRAIT("10400002", "Upload head portrait failed"),
    FAILED_BIND_NUMBER("10400003", "Bind number failed"),
    INVALID_PARAMETER("10400004", "Invalid parameters. %s"),
    INVALID_PARAMETER_IN_BODY("10400005", "The message body can not be parsed"),
    MISMATCH_PARAMATER_TYPE("10400007", "The parameter type is mismatched. %s"),
    INVALID_HEADER("10400008", "Invalid header. %s"),
    EXPIRED_OTP("10400009", "The otp is expired"),
    INVALID_OTP("10400010", "The otp is invalid"),
    INVALID_PASSWORD("10400011", "Invalid password"),
    ILLEGAL_ARGUMENT("10400012", "Illegal argument. %s"),
    UNSUPPORTED_SEND_TYPE("10400013", "Unsupported send type"),
    NOT_MULTIPART_REQUEST("10400014", "The current request is not a multipart request"),
    EXCEEDED_SEND_TIME("10400015", "Maximum send number of times %s exceeded"),
    EXCEEDED_SEND_WRONG_CODE_TIME("10400016",
        "Maximum verify code of times %s exceeded(wrong otp)"),
    INVALID_ORDER("10400017", "The order is invalid"),
    INVALID_PRODUCT("10400018", "The product is invalid"),
    INVALID_TRANSACTION("10400019", "The transaction number is invalid"),
    INVALID_BILLING_SESSION_TYPE("10400020", "The request type must be START,RESERVE,CLAIM,END"),
    INVALID_BILLING_SESSION_id("10400021", "The session id is invalid"),
    INSUFFICIENT_BALANCE("10400022", "Insufficient Balance"),
    EXPIRED_URL("10400029", "The url is expired"),
    INVALID_URL("10400030", "The url is invalid"),
    ACTIVATED_EMAIL("10400031", "The email has already been activated");

    private String code;
    private String message;

    BadRequest(String code, String message) {
      this.code = code;
      this.message = message;
    }

    @Override
    public String getCode() {
      return code;
    }

    @Override
    public String getMessage() {
      return message;
    }
  }

}
