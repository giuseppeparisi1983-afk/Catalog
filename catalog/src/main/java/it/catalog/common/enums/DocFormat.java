package it.catalog.common.enums;

public enum DocFormat implements FileExtension {

	
	 // Documenti di testo
    TXT("txt"),
    DOC("doc"),
    DOCX("docx"),
    ODT("odt"),
    RTF("rtf"),

    // Fogli di calcolo
    XLS("xls"),
    XLSX("xlsx"),
    ODS("ods"),
    CSV("csv"),

    // Presentazioni
    PPT("ppt"),
    PPTX("pptx"),
    ODP("odp"),

    // Documenti portabili
    PDF("pdf");

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

	private String label;

    DocFormat(String extension) {
        this.label = extension;
    }

    @Override public String getLabel() { return label; }
}
