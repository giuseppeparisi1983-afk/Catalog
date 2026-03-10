package it.catalog.service.mapper;

import java.util.List;

import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import it.catalog.persistence.entity.Video;
import it.catalog.service.dto.VideoDto;
import it.catalog.utility.PathPrefixProvider;

@Mapper(componentModel = "spring")
public interface VideoMapper {
    
//	DateTimeFormatter FORMATTER_TIME = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
//	DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
	
	
	
//	@Mapping(target = "dataArchiviazione",source = "dataArchiviazione")
//	@Mapping(target = "ultimaVisualizzazione", source = "ultimaVisualizzazione")
	 @Mapping(target = "percorsoFile", expression = "java(prefixResolver.stripPrefix(dto.getPercorsoFile()))")
//	 @Mapping(target = "categoria", expression = "java(dto.getCategoria().getDescrizione())")
	 Video toEntity(VideoDto dto, @Context PathPrefixProvider prefixResolver);
	
	
//	@Mapping(source = "dataArchiviazione", target = "dataArchiviazione", dateFormat = "dd/MM/yyyy")
//	@Mapping(source = "ultimaVisualizzazione", target = "ultimaVisualizzazione", dateFormat = "dd/MM/yyyy")
//	@Mapping(target = "dataArchiviazione", expression = "java(video.getDataArchiviazione() != null ? video.getDataArchiviazione().format(FORMATTER) : null)")
//	@Mapping(target = "ultimaVisualizzazione", expression = "java(video.getUltimaVisualizzazione() != null ? video.getUltimaVisualizzazione().format(FORMATTER_TIME) : null)")
//	@Mapping(target = "rating", expression = "java(video.getRating().setScale(1, java.math.RoundingMode.HALF_UP))")
	@Mapping(target = "rating",source = "rating",  qualifiedByName = "arrotondaADecimale")
	@Mapping(target = "percorsoFile", expression = "java(video.getPercorsoFile()!=null ? prefixProvider.getPrefix() + video.getPercorsoFile(): \"\")")
	VideoDto toDto(Video video, @Context PathPrefixProvider prefixProvider);
	
	 @Named("arrotondaADecimale")
	    default Double arrotondaADecimale(Double valore) {
	        if (valore == null) return null;
	        return Math.round(valore * 10.0) / 10.0;
	    }
	 
    List<VideoDto> toDtoList(List<Video> videos);
}

