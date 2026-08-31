package it.catalog.common.enums;

/**
 * Categorizzazione per i contenuti video dell'applicazione.
 */
public enum CategorieVideo implements FileExtension {

	SPEZZONI("Spezzoni Film", "Estratti, scene iconiche o frammenti tratti da film e serie TV."),
	GUITAR("Chitarra", "Videolezioni, esecuzioni, esercitazioni ed analisi di brani per chitarra."),
	DOCUMENTARIO("Documentario", "Contenuti approfonditi a carattere divulgativo, storico, scientifico o culturale."),
	MUSICA("Musica", "Videoclip musicali, concerti dal vivo e brani d'ascolto."),
	SPORT("Sport", "Highlights di eventi sportivi, allenamenti e sintesi di gare."),
	GUIDA("Tutorial", "Guide pratiche passo-passo, spiegazioni tecniche e dimostrazioni d'uso.");

	private final String label;
	private final String description;

	CategorieVideo(String label, String description) {
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

//	 // Metodo essenziale per riconvertire da DB a Java
//    public static CategorieVideo fromLabel(String text) {
//        return Arrays.stream(values())
//            .filter(bl -> bl.label.equalsIgnoreCase(text))
//            .findFirst()
//            .orElse(null); // O gestire un valore di default/eccezione
//    }

}
