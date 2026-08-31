package it.catalog.common.enums;

import java.util.Arrays;

/**
 * Tipologia e classificazione dei documenti archiviati.
 */
public enum TipoDocumento implements FileExtension {

	FATTURA("fattura", "Documento fiscale ed economico relativo a transazioni, vendite o acquisti."),
	REPORT("report", "Rendiconto di analisi, prospetto informativo o resoconto periodico di attività."),
	CONTRATTO("contratto", "Accordo legale, scrittura privata o termini di servizio formalizzati."),
	CV("Curriculum", "Curriculum Vitae con esperienze lavorative, competenze e percorso formativo."),
	CERTIFICATI("certificati", "Attestati di partecipazione, certificazioni professionali o documenti ufficiali."),
	GUIDA("guida", "Manuale d'uso, documentazione tecnica o istruzioni operative per procedure."),
	ARTICOLO("articolo", "Testo informativo, saggio, pubblicazione o bozza per blog/rivista.");

	private final String label;
	private final String description;

	TipoDocumento(String code, String description) {
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
	 * TipoDocumento fromLabel(String text) { return Arrays.stream(values())
	 * .filter(bl -> bl.label.equalsIgnoreCase(text)) .findFirst() .orElse(null); //
	 * O gestire un valore di default/eccezione }
	 */
}
