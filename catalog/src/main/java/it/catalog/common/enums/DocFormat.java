package it.catalog.common.enums;

/**
 * Enumerazione per i principali formati di documenti, fogli di calcolo e presentazioni.
 */
public enum DocFormat implements FileExtension {

	
	 // Documenti di testo
    TXT("txt", "File di testo semplice privo di formattazione avanzata, leggero e compatibile con qualsiasi sistema."),
    DOC("doc", "Formato legacy Microsoft Word (versioni fino a 2003) per documenti con testo formattato e immagini."),
    DOCX("docx", "Formato standard Microsoft Word basato su XML, altamente efficiente e ampiamente diffuso."),
    ODT("odt", "Formato open source OpenDocument Text, standard aperto per la videoscrittura usato da LibreOffice e OpenOffice."),
    RTF("rtf", "Rich Text Format, formato di testo formattato universale leggibile da quasi tutti i word processor."),

    // Fogli di calcolo
    XLS("xls", "Formato legacy Microsoft Excel per fogli di calcolo ed elaborazione dati numerici."),
    XLSX("xlsx", "Formato moderno Microsoft Excel basato su XML, supporta formule complesse, grafici e grandi moli di dati."),
    ODS("ods", "Formato open source OpenDocument Spreadsheet per la gestione di fogli elettronici calcolati."),
    CSV("csv", "Valori separati da virgola, formato di testo puro universale per lo scambio e l'importazione/esportazione dati."),

    // Presentazioni
    PPT("ppt", "Formato legacy Microsoft PowerPoint per diapositive e presentazioni multimediali."),
    PPTX("pptx", "Formato standard Microsoft PowerPoint basato su XML per presentazioni dinamiche ad alta risoluzione."),
    ODP("odp", "Formato open source OpenDocument Presentation per diapositive e proiezioni."),

    // Documenti portabili
    PDF("pdf", "Portable Document Format di Adobe, garantisce che il layout visivo rimanga identico su qualsiasi dispositivo e stampante.");

    // Immagini
//    JPG("jpg"),
//    JPEG("jpeg"),
//    PNG("png"),
//    GIF("gif"),
//    BMP("bmp"),
//    SVG("svg"),

    // File compressi
//    ZIP("zip"),
//    RAR("rar"),
//    TAR("tar"),
//    GZ("gz"),

    // File audio
//    MP3("mp3"),
//    WAV("wav"),
//    OGG("ogg"),

    // File video
//    MP4("mp4"),
//    MKV("mkv"),
//    AVI("avi"),
//    MOV("mov");

	private final String label;
	 private final String description;

    DocFormat(String extension, String descrizione) {
        this.label = extension;
        this.description = descrizione;
    }

    @Override public String getLabel() { return label; }
    
    @Override
    public String getDescription() {
        return description;
    }
}
