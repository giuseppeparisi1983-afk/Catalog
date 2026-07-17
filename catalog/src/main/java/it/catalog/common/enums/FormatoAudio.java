package it.catalog.common.enums;

public enum FormatoAudio {
    MP3("mp3", "audio/mpeg"),
    WAV("wav", "audio/wav"),
    AAC("aac", "audio/aac"),
    FLAC("flac", "audio/flac"),
    OGG("ogg", "audio/ogg");

    private final String estensione;
    private final String mimeType;

    FormatoAudio(String estensione, String mimeType) {
        this.estensione = estensione;
        this.mimeType = mimeType;
    }

    public String getEstensione() { return estensione; }
    public String getMimeType() { return mimeType; }
}