package it.catalog.common.enums;

/**
 * Enumerazione per i principali formati di immagine con etichetta e descrizione
 * per i tooltip di dettaglio nelle interfacce utente.
 */
public enum ImageFormat implements FileExtension {

	JPG("JPG", "Formato compresso standard, ottimale per fotografie e immagini prive di trasparenza."),
	JPEG("JPEG", "Variante dell'estensione JPG, ampiamente utilizzata su web e fotografia digitale."),
	PNG("PNG", "Formato lossless a compressione senza perdita, supporta il canale alfa per sfondi trasparenti."),
	GIF("GIF", "Formato leggero ideale per brevi animazioni e grafiche semplici con tavolozza a 256 colori."),
	TIFF("TIFF",
			"Formato ad alta fedeltà con supporto per livelli, primario nel settore stampa e grafica professionale."),
	WEBP("WEBP",
			"Formato web moderno di Google con elevata compressione sia lossy che lossless e supporto trasparenza."),
	BMP("BMP", "Formato raster nativo Windows non compresso, ad alta fedeltà ma con file di grandi dimensioni."),
	SVG("SVG", "Grafica vettoriale scalabile all'infinito senza perdita di qualità, ideale per loghi e icone UI.");

	private String label;
	private final String description;

	ImageFormat(String label, String description) {
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
}
