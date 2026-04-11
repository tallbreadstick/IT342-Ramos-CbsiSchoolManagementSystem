package edu.cit.ramos.entity;

public enum SexType {
    MALE("Male"),
    FEMALE("Female");

    private final String dbValue;

    SexType(String dbValue) {
        this.dbValue = dbValue;
    }

    public String getDbValue() {
        return dbValue;
    }

    public static SexType fromDbValue(String value) {
        if (value == null) return null;
        for (SexType s : values()) {
            if (s.dbValue.equals(value)) return s;
        }
        throw new IllegalArgumentException("Unknown SexType value: " + value);
    }
}