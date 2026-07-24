package it.catalog.common.enums;

import java.util.Arrays;

public enum TipoDocumento implements FileExtension{

	FATTURA ("fattura"),REPORT("report"),CONTRATTO("contratto"),
	CV("Curriculum"),CERTIFICATI("certificati"),
	GUIDA ("guida"),ARTICOLO("articolo");

	
private final String label;
	
	TipoDocumento(String descrizione) {

		this.label=descrizione;
	}

	@Override
	public String getLabel() {
		return label;
	}

	
	 // Metodo essenziale per riconvertire da DB a Java
    public static TipoDocumento fromLabel(String text) {
        return Arrays.stream(values())
            .filter(bl -> bl.label.equalsIgnoreCase(text))
            .findFirst()
            .orElse(null); // O gestire un valore di default/eccezione
    }
}
