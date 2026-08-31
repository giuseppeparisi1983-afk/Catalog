package it.catalog.common.enums;


/**
 * Enumerazione per i principali formati e contenitori video.
 */
public enum VideoFormat implements FileExtension{

	MP4(".mp4", "Formato video contenitore più diffuso e compatibile al mondo, ideale per streaming web e dispositivi mobili."),
    MKV(".mkv", "Contenitore Matroska flessibile ad altissima qualità, supporta tracce audio multiple, capitoli e sottotitoli integrati."),
    AVI(".avi", "Formato storico di Microsoft, ampiamente compatibile con lettori multimediali datati e TV con ingresso USB."),
    MOV(".mov", "Formato video contenitore nativo Apple QuickTime, ampiamente utilizzato nel montaggio video professionale."),
    FLV(".flv", "Formato legacy Flash Video, storicamente impiegato per la riproduzione video in streaming su browser web."),
    WEBM(".webm", "Formato video royaty-free sviluppato da Google e ottimizzato per l'HTML5 e la riproduzione web nativa."),
    MPG(".mpg", "Formato con compressione MPEG-1/MPEG-2, standard storico utilizzato per Video CD, DVD e trasmissione televisiva."),
    WMV(".wmv", "Windows Media Video, formato di compressione video proprietario sviluppato da Microsoft."),
    MPEG(".mpeg", "Variante dello standard di codifica MPEG per streaming e archiviazione di contenuti audio/video.");
	
	
	private final String label;
	private final String description;

	VideoFormat(String extension, String descrizione) {
		this.label = extension;
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
