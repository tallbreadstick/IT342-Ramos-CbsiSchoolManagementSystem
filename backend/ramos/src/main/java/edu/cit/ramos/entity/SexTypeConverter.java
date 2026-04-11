package edu.cit.ramos.entity;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = false)
public class SexTypeConverter implements AttributeConverter<SexType, String> {

    @Override
    public String convertToDatabaseColumn(SexType attribute) {
        return attribute == null ? null : attribute.getDbValue();
    }

    @Override
    public SexType convertToEntityAttribute(String dbData) {
        return dbData == null ? null : SexType.fromDbValue(dbData);
    }
}
