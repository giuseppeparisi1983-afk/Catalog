package it.catalog.service.interfaces;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import it.catalog.service.dto.AudioDto;
import it.catalog.service.dto.TagDto;
import it.catalog.service.dto.search.DtoFilter;

public interface AudioFileService {

//	 public List<AudioDto> findAll();
	 public List<TagDto> getAllTagsForAudio();
	 public Page<AudioDto> findPage(DtoFilter filtro, Pageable pageable);
	 public long count(DtoFilter filter);
	 public AudioDto findById(Long id);
	 public AudioDto save(AudioDto dto);
	 public void delete(Long id);
	 public void recovery(Long id);
}
