package it.catalog.common.enums;

import java.util.Arrays;


/**
 * Livello di difficoltà tecnica per le lezioni e i brani di chitarra.
 */
public enum Livello implements FileExtension {

	 BASE("base", "Livello principiante: accordi aperti, ritmiche semplici, primi arpeggi e basi della tecnica."),
	    INTERMEDIO("intermedio", "Livello intermedio: accordi col barré, scale pentatoniche, tecniche di bending e legato."),
	    AVANZATO("avanzato", "Livello esperto: tecniche complesse (sweep picking, tapping, assoli veloci) e teoria musicale avanzata.");

	private final String label;
	private final String description;

	private Livello(String label, String description) {
		this.label = label;
		this.description = description;
	}

	@Override
	public String getLabel() {
		return label;
	}
	
	   @Override
	    public String getDescription() {
	        return description;
	    }
	
		/*
		 * // Metodo essenziale per riconvertire da DB a Java public static Livello
		 * fromLabel(String text) { return Arrays.stream(values()) .filter(bl ->
		 * bl.label.equalsIgnoreCase(text)) .findFirst() .orElse(null); // O gestire un
		 * valore di default/eccezione }
		 */
}
