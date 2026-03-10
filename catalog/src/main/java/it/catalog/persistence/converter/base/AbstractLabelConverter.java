package it.catalog.persistence.converter.base;

import java.util.stream.Stream;

import it.catalog.common.interfaces.HasLabel;
import jakarta.persistence.AttributeConverter;

// 2. Classe base generica (nessuna logica ripetuta!)
public abstract class AbstractLabelConverter<E extends Enum<E> & HasLabel> 
        implements AttributeConverter<E, String> {

    private final Class<E> enumClass;

    public AbstractLabelConverter(Class<E> enumClass) {
        this.enumClass = enumClass;
    }

    @Override
    public String convertToDatabaseColumn(E attribute) {
        return (attribute == null) ? null : attribute.getLabel();
    }

    @Override
    public E convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isEmpty()) {
            return null;
        }
        // Cerca l'enum che ha quella label
        return Stream.of(enumClass.getEnumConstants())
                .filter(e -> e.getLabel().equalsIgnoreCase(dbData))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Label sconosciuta: " + dbData));
    }
}
