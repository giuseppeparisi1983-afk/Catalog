package it.catalog.ui.utility;

import java.lang.reflect.Field;
import java.time.Instant;
import java.time.LocalDate;

/**
 * classe di supporto che permette di mostrare i criteri di ricerca come le intestazioni della Grid<Dto>
 * */
public class SearchFieldOption {
    private final String label;
    private final String fieldName;
    private final Class<?> fieldType;

    
    public static SearchFieldOption of(Class<?> dtoClass, String label, String fieldName) {
        try {
            Field field = dtoClass.getDeclaredField(fieldName);
            return new SearchFieldOption(label, fieldName, field.getType());
        } catch (NoSuchFieldException | SecurityException e) {
            throw new IllegalArgumentException(
                "Campo '" + fieldName + "' non trovato nella classe " + dtoClass.getSimpleName(), e
            );
        }
    }

	public SearchFieldOption(String label, String fieldName, Class<?> fieldType) {
        this.label = label;
        this.fieldName = fieldName;
        this.fieldType = fieldType;
    }

    public String getLabel() { return label; }
    public String getFieldName() { return fieldName; }
    public Class<?> getFieldType() { return fieldType; }

    public boolean isDateField() {
        return fieldType.equals(LocalDate.class) || fieldType.equals(Instant.class);
    }

    @Override
    public String toString() {
        return label;
    }
}