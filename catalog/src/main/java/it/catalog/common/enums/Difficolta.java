package it.catalog.common.enums;

import java.util.Arrays;

public enum Difficolta implements FileExtension {

	BASE("base"), INTERMEDIO("intermedio"), AVANZATO("avanzato");

	private final String label;

	private Difficolta(String label) {
		this.label = label;
	}

	@Override
	public String getLabel() {
		return label;
	}
	
	 // Metodo essenziale per riconvertire da DB a Java
    public static Difficolta fromLabel(String text) {
        return Arrays.stream(values())
            .filter(bl -> bl.label.equalsIgnoreCase(text))
            .findFirst()
            .orElse(null); // O gestire un valore di default/eccezione
    }
}
