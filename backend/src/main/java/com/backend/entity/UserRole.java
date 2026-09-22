package com.backend.entity;

import java.util.Locale;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum UserRole {
    ADMIN, MANAGER, OPERATOR, PRODUCTION;

    @JsonValue
    public String value() {
        return name().toLowerCase(Locale.ROOT);
    }

    @JsonCreator
    public static UserRole fromValue(String value) {
        return valueOf(value.toUpperCase(Locale.ROOT));
    }
}
