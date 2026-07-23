package it.catalog.common.enums;

public enum FilmFormat implements FileExtension{

	MP4(".mp4"),
	MKV(".mkv"),
	AVI(".avi"),
	MOV(".mov"),
	FLV(".flv"),
	WEBM(".webm"),
	MPG(".mpg"),
	WMV(".wmv"),
	MPEG(".mpeg");
	
	
	private String label;

	FilmFormat(String extension) {
		this.label = extension;
	}

	@Override
	public String getLabel() {
		return label;
	}
}
