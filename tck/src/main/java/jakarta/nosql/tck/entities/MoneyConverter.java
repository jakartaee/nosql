package jakarta.nosql.tck.entities;

import jakarta.nosql.AttributeConverter;

public class MoneyConverter implements AttributeConverter<Money, String> {

    @Override
    public String convertToDatabaseColumn(Money attribute) {
        return "";
    }

    @Override
    public Money convertToEntityAttribute(String dbData) {
        return null;
    }
}
