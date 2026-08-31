package it.catalog.common.enums;

/**
 * Enumerazione per i principali formati audio.
 */
public enum AudioFormat implements FileExtension {
    
	MP3("mp3", "Formato audio compresso con perdita (lossy) universale, bilancia un'ottima qualità con dimensioni ridotte."),
    WAV("wav", "Formato audio non compresso ad alta fedeltà (PCM), standard per la registrazione e produzione musicale professionale."),
    AAC("aac", "Formato lossy ad alta efficienza e qualità superiore all'MP3 a parità di bitrate, standard per dispositivi Apple e streaming."),
    FLAC("flac", "Formato compresso senza perdita di qualità (lossless), ideale per l'archiviazione di musica in qualità CD/studio."),
    OGG("ogg", "Formato contenitore open source e privo di royalty, ottimizzato per lo streaming web e i videogiochi.");
    
	private final String label;
	private final String description;

    AudioFormat(String estensione, String descrizione) {
        this.label = estensione;
        this.description = descrizione;
    }

    @Override public String getLabel() { return label; }

    @Override
    public String getDescription() {
        return description;
    }

}