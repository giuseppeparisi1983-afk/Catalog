package it.catalog.common.enums;

import java.util.Arrays;

import it.catalog.common.interfaces.HasLabel;

public enum CategorieVideo implements HasLabel {

	spezzoni ("Spezzoni Film"),	guitar ("Chitarra"),documentario ("Documentario"),	musica ("Musica"),sport ("Sport"),guida ("Tutorial");

	private final String label;
	
	CategorieVideo(String descrizione) {

		this.label=descrizione;
	}

	@Override
	public String getLabel() {
		return label;
	}

	
	 // Metodo essenziale per riconvertire da DB a Java
    public static CategorieVideo fromLabel(String text) {
        return Arrays.stream(values())
            .filter(bl -> bl.label.equalsIgnoreCase(text))
            .findFirst()
            .orElse(null); // O gestire un valore di default/eccezione
    }

}
