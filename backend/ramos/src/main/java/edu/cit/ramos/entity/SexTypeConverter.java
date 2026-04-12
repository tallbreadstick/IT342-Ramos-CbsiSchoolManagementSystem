package edu.cit.ramos.entity;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class SexTypeConverter implements AttributeConverter<SexType, String> {

    @Override
    public String convertToDatabaseColumn(SexType attribute) {
        // Persist the exact DB literal (e.g., 'MALE'/'FEMALE') to satisfy DB check constraints.
        return attribute == null ? null : attribute.getDbValue();
    }

    @Override
    public SexType convertToEntityAttribute(String dbData) {
        return dbData == null ? null : SexType.fromDbValue(dbData);
    }
}
