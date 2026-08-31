package it.catalog.common.enums;


public enum ImageType implements FileExtension{

	
    FOTOGRAFIA("Fotografia", "Immagini scattate da fotocamere o smartphone"),
    SFONDO("Sfondo", "Wallpaper ad alta risoluzione per desktop o dispositivi"),
    ILLUSTRAZIONE("Illustrazione", "Disegni digitali, concept art e vettori"),
    CLIP_ART("Clip Art", "Elementi grafici trasparenti e icone per presentazioni"),
    ALTRO("Altro", "Documenti, scansioni e screenshot vari");
	
	
    private final String label;
    private final String description;

	ImageType(String label, String descrizione) {
        this.label = label;
        this.description = descrizione;
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
