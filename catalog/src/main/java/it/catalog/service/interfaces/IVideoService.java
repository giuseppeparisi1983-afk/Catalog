package it.catalog.service.interfaces;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import it.catalog.persistence.entity.Video;
import it.catalog.service.dto.VideoDto;
import it.catalog.service.dto.search.DtoFilter_;

public interface IVideoService {

	public List<VideoDto> findAll();
	public List<String> getCategorie();
	public Optional<VideoDto> findById(Integer id);
//	public VideoDto findByVideoId(Integer id);
	public VideoDto save(VideoDto video);
	public void delete(Integer id);
	public void recovery(Integer id);
	public List<Video> searchByTitolo(String query);
	public Page<VideoDto> getVideoByTitolo(String titolo, Pageable pageable);
	public long getNumVideoByTitolo(String titolo);
	
	public Page<VideoDto> getVideos(String search, int page, int size, Sort sort);
	public Page<VideoDto> searchByField(DtoFilter_ filtro, int page, int size, Sort sort);
}
