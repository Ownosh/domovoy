package ru.domovoy.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import ru.domovoy.model.Request;

@Converter
public class RequestStatusConverter implements AttributeConverter<Request.RequestStatus, String> {
    
    @Override
    public String convertToDatabaseColumn(Request.RequestStatus status) {
        if (status == null) {
            return null;
        }
        return status.getValue();
    }
    
    @Override
    public Request.RequestStatus convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }
        return Request.RequestStatus.fromValue(dbData);
    }
}








