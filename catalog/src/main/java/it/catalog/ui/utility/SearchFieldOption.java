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
			Class<?> type = resolveType(dtoClass, fieldName);
			return new SearchFieldOption(label, fieldName, type);
		} catch (Exception e) {
			// Se non troviamo il campo, lanciamo un'eccezione più descrittiva
			throw new IllegalArgumentException("Impossibile risolvere il campo '" + fieldName + "' in " + dtoClass.getSimpleName(), e);
		}
	}

	// Naviga ricorsivamente tra i campi (gestisce "video.titolo")
	private static Class<?> resolveType(Class<?> clazz, String fieldName) throws NoSuchFieldException {
		if (fieldName.contains(".")) {
			String left = fieldName.substring(0, fieldName.indexOf("."));
			String right = fieldName.substring(fieldName.indexOf(".") + 1);
			Field field = getAnyField(clazz, left);
			return resolveType(field.getType(), right);
		} else {
			return getAnyField(clazz, fieldName).getType();
		}
	}

	// Cerca il campo nella classe e in tutte le superclassi
	private static Field getAnyField(Class<?> clazz, String fieldName) throws NoSuchFieldException {
		Class<?> current = clazz;
		while (current != null) {
			try {
				return current.getDeclaredField(fieldName);
			} catch (NoSuchFieldException e) {
				current = current.getSuperclass();
			}
		}
		throw new NoSuchFieldException(fieldName);
	}

	public SearchFieldOption(String label, String fieldName, Class<?> fieldType) {
		this.label = label;
		this.fieldName = fieldName;
		this.fieldType = fieldType;
	}

	// Getter, isDateField, toString...
	public String getLabel() { return label; }
	public String getFieldName() { return fieldName; }
	public boolean isDateField() { return fieldType.equals(LocalDate.class) || fieldType.equals(Instant.class); }
}