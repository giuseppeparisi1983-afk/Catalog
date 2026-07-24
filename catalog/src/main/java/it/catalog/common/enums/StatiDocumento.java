package it.catalog.common.enums;

import java.util.Arrays;

public enum StatiDocumento implements FileExtension{

	ATTIVO ("attivo"), ARCHIVIATO ("archiviato"), ELIMINATO ("eliminato");
	
	
private final String label;
	
StatiDocumento(String descrizione) {

		this.label=descrizione;
	}

	@Override
	public String getLabel() {
		return label;
	}

	
	 // Metodo essenziale per riconvertire da DB a Java
    public static StatiDocumento fromLabel(String text) {
        return Arrays.stream(values())
            .filter(bl -> bl.label.equalsIgnoreCase(text))
            .findFirst()
            .orElse(null); // O gestire un valore di default/eccezione
    }
}
