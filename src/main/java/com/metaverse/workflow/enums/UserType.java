package com.metaverse.workflow.enums;

public enum UserType {
    AGENCY("AGENCY"),
    CALL_CENTER("CALL-CENTER"),
    ALL("ALL");

    private final String value;

    UserType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
