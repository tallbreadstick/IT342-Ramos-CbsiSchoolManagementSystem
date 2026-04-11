package edu.cit.ramos.entity;

public enum AccountStatusType {
    PENDING("PENDING"),
    ACTIVE("ACTIVE"),
    SUSPENDED("SUSPENDED"),
    ARCHIVED("ARCHIVED");

    private final String dbValue;

    AccountStatusType(String dbValue) {
        this.dbValue = dbValue;
    }

    public String getDbValue() {
        return dbValue;
    }

    public static AccountStatusType fromDbValue(String value) {
        if (value == null) return null;
        for (AccountStatusType s : values()) {
            if (s.dbValue.equals(value)) return s;
        }
        throw new IllegalArgumentException("Unknown AccountStatusType value: " + value);
    }
}