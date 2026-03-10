package it.catalog.service.interfaces;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import it.catalog.service.dto.ImageDto;
import it.catalog.service.dto.TagDto;
import it.catalog.service.dto.search.DtoFilter_;

public interface ImageFileService {

	public List<TagDto> getAllTagsForImage();
	 public Page<ImageDto> findPage(DtoFilter_ filtro, Pageable pageable, List<String> requiredTags);
//	public Page<ImageDto> findPage(String filterText, Pageable pageable);
	public ImageDto findById(Long id);
	public ImageDto save(ImageDto dto);
	public void delete(Long id);
	 public void recovery(Long id);
	
}
