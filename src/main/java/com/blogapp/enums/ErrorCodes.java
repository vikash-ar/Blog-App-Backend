package com.blogapp.enums;

public enum ErrorCodes {

    USER_NOT_FOUND("CODE_001"),
    CATEGORY_NOT_FOUND("CODE_002"),
    POST_NOT_FOUND("CODE_003"),
    COMMENT_NOT_FOUND("CODE_004"),
    INVALID_USERNAME_PASSWORD("CODE_005"),
    JWT_SIGNATURE_EXCEPTION("CODE_006"),
    USER_ALREADY_PRESENT("CODE_007"),
    INVALID_ACCESS_TOKEN("CODE_008"),
    TOKEN_EXPIRED("CODE_009"),
    CONNECTION_FAILED("CODE_003"),
    EMAIL_CANNOT_BE_CHANGED("CODE_004"),
    EMAIL_ALREADY_EXISTS("CODE_006"),
    USER_NOT_PRESENT("CODE_007");
    private String value;
    public String getValue() {
        return value;
    }
    public void setValue(String value) {
        this.value = value;
    }
    private ErrorCodes(String value) {
        this.value = value;
    }
}
