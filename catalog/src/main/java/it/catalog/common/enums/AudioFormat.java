package it.catalog.common.enums;

public enum AudioFormat implements FileExtension {
    MP3("mp3", "audio/mpeg"),
    WAV("wav", "audio/wav"),
    AAC("aac", "audio/aac"),
    FLAC("flac", "audio/flac"),
    OGG("ogg", "audio/ogg");

    private final String label;
    private final String mimeType;

    AudioFormat(String estensione, String mimeType) {
        this.label = estensione;
        this.mimeType = mimeType;
    }

    @Override public String getLabel() { return label; }
    public String getMimeType() { return mimeType; }
}