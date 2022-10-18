package com.qrcodegenerate.common.constant;

public enum Status {
    SUCCESS("SUCCESS"), EXIST("EXIST"), NOT_EXIST("NOT EXIST"), FAILED("FAILED"), ERROR("ERROR");

    private String value;

    private Status(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }
}
