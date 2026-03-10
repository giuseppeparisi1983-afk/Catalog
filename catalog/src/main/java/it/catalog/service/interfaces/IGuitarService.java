package it.catalog.service.interfaces;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import it.catalog.persistence.entity.Chitarra;
import it.catalog.service.dto.GuitarDto;
import it.catalog.service.dto.search.DtoFilter_;

public interface IGuitarService {

	public List<GuitarDto> findAll();
	public GuitarDto getGuitarById(Integer id);
	public Optional<GuitarDto> findByVideoId(Integer id);
	public Chitarra save(GuitarDto chitarra);
	public void delete(Integer id);
	public void recovery(Integer id);
	public List<Chitarra> searchByAutore(String autore);
	public Page<GuitarDto> searchByField(DtoFilter_ filtroCorrente, int page, int size, Sort sort);

}
