package edu.cit.ramos.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum SexType {
    MALE("MALE", "Male"),
    FEMALE("FEMALE", "Female");

    private final String dbValue; // stored in the database (exact literal expected by DB check)
    private final String displayName; // human-friendly name for JSON

    SexType(String dbValue, String displayName) {
        this.dbValue = dbValue;
        this.displayName = displayName;
    }

    @JsonValue
    public String getDisplayName() {
        return displayName;
    }

    public String getDbValue() {
        return dbValue;
    }

    public static SexType fromDbValue(String value) {
        if (value == null) return null;
        String v = value.trim();
        for (SexType s : values()) {
            if (s.dbValue.equalsIgnoreCase(v) || s.displayName.equalsIgnoreCase(v) || s.name().equalsIgnoreCase(v)) return s;
        }
        throw new IllegalArgumentException("Unknown SexType db value: " + value);
    }

    @JsonCreator
    public static SexType fromString(String value) {
        if (value == null) return null;
        String v = value.trim();
        // try matching enum name
        for (SexType s : values()) {
            if (s.name().equalsIgnoreCase(v)) return s;
        }
        // try matching display name
        for (SexType s : values()) {
            if (s.displayName.equalsIgnoreCase(v)) return s;
        }
        // try matching db value
        for (SexType s : values()) {
            if (s.dbValue.equalsIgnoreCase(v)) return s;
        }
        throw new IllegalArgumentException("Unknown SexType value: " + value);
    }
}