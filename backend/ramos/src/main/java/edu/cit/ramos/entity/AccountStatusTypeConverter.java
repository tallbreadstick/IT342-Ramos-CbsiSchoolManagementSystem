package edu.cit.ramos.entity;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = false)
public class AccountStatusTypeConverter implements AttributeConverter<AccountStatusType, String> {

    @Override
    public String convertToDatabaseColumn(AccountStatusType attribute) {
        return attribute == null ? null : attribute.getDbValue();
    }

    @Override
    public AccountStatusType convertToEntityAttribute(String dbData) {
        return dbData == null ? null : AccountStatusType.fromDbValue(dbData);
    }
}
