package it.catalog.common.enums;

import java.util.Arrays;

/**
 * Stato del ciclo di vita operativo di un documento nell'archivio.
 */
public enum StatiDocumento implements FileExtension {

	ATTIVO("attivo", "Documento corrente, visibile nell'archivio principale e pienamente operativo."),
	ARCHIVIATO("archiviato", "Documento storico o completato, conservato per consultazione ma non più in uso attivo."),
	ELIMINATO("eliminato", "Documento contrassegnato come rimosso (soft delete), in attesa di epurazione definitiva.");

	private final String label;
	private final String description;

	StatiDocumento(String code, String description) {
		this.label = code;
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
	 * // Metodo essenziale per riconvertire da DB a Java public static
	 * StatiDocumento fromLabel(String text) { return Arrays.stream(values())
	 * .filter(bl -> bl.label.equalsIgnoreCase(text)) .findFirst() .orElse(null); //
	 * O gestire un valore di default/eccezione }
	 */
}
