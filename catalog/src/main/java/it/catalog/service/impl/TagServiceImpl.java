package it.catalog.service.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import it.catalog.persistence.entity.Tag;

import it.catalog.persistence.repository.TagRepository;
import it.catalog.service.dto.TagDto;
import it.catalog.service.interfaces.TagService;
import it.catalog.service.mapper.TagMapper;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepo;
//    private final OggettoTagRepository oggettoTagRepo;
    private final TagMapper tagMapper;

    public TagServiceImpl(TagRepository tagRepository,TagMapper tagMapper) {
        this.tagRepo = tagRepository;
        this.tagMapper = tagMapper;
       
    }

   private Tag createIfNotExists(String nomeTag,String tipoOggetto) {
       log.info("Parametri ingresso {} , {}",nomeTag,tipoOggetto); 
	   return tagRepo.findByNomeTag(nomeTag)
                      .orElseGet(() -> tagRepo.save(new Tag(nomeTag,tipoOggetto)));
    }
    
//   @Override
//   public List<Long> findIdByTypeAndTags(String tipoOggetto, List<String> tagNames) {
//	   
//	   return 
//			   tagMapper.mapToList(tagRepo.findByTipoOggettoAndIdOggetto(tipoOggetto, tagNames));
//	   
////    	return oggettoTagRepo.findByTipoOggettoAndIdOggetto(tipoOggetto, idOggetto)
////                .stream()
////                .map(assoc -> tagRepo.findById(assoc.getTag().getIdTag()).orElse(null))
////                .filter(Objects::nonNull)
////                .toList().stream().map(tagMapper::toDto).toList();
//   }

   @Override
    /**
     * Ricerca tutti i tag associati all'oggetto
     * */
    public List<TagDto> findTagsByObject(String tipoOggetto, Long idOggetto) {

    	return 
    			tagMapper.mapToList(tagRepo.findByTipoOggettoAndIdOggetto(tipoOggetto, idOggetto));
    	
//    	return oggettoTagRepo.findByTipoOggettoAndIdOggetto(tipoOggetto, idOggetto)
//                .stream()
//                .map(assoc -> tagRepo.findById(assoc.getTag().getIdTag()).orElse(null))
//                .filter(Objects::nonNull)
//                .toList().stream().map(tagMapper::toDto).toList();
    }

//    @Override
    /**
     * Ricerca tutti i tag associati all'oggetto
     * */
//    public List<OggettoTag> findObjectTagByObject(String tipoOggetto, Long idOggetto) {
//    	return oggettoTagRepo.findByTipoOggettoAndIdOggetto(tipoOggetto, idOggetto);
//    }
    
//    @Override
//    @Transactional
//    /**
//     * Salva tutti i tag associati all'oggetto
//     * */
//    public void saveTagsForObject(String tipoOggetto, Long idOggetto, List<String> nomiTag) {
//        // cancella associazioni precedenti
//        oggettoTagRepo.deleteByTipoOggettoAndIdOggetto(tipoOggetto, idOggetto);
//
//        // crea o recupera i tag e associa
//        for (String nome : nomiTag) {
//            Tag tag = createIfNotExists(nome);
//            OggettoTag assoc = new OggettoTag(tag, idOggetto);
//            oggettoTagRepo.save(assoc);
//        }
//    }
    
    @Override
    public List<TagDto> findByTipoOggetto(String tipo) {
    	
     	return 
    			tagMapper.mapToList(tagRepo.findByTipoOggetto(tipo));
//    	return oggettoTagRepo.findByTipoOggetto(tipo).stream()
//    			 .map(OggettoTag::getTag)
//    			.map(tagMapper::toDto).toList();
    }
    
    /**
     * Il comportamento corretto è:

			-  Se un tag è nel tagSelector ma non nel DB → inserisci.

			- Se un tag è nel DB ma non nel tagSelector → rimuovi.

			- Se un tag è sia nel DB che nel tagSelector → non fare nulla.
     * 
     * */
//    @Override
//    @Transactional
//    public void upsertTagsForObject(String tipoOggetto, Long idOggetto, List<String> desiredTagNames) {
//        Set<String> desired = new HashSet<>(desiredTagNames);
//        Set<String> current = findTagsByObject(tipoOggetto, idOggetto)
//                              .stream()
//                              .map(TagDto::getNomeTag)
//                              .collect(Collectors.toSet());
//
//        // Tags da rimuovere
//        for (String name : current) {
//            if (!desired.contains(name)) {
//                Tag tag = tagRepo.findByNomeTag(name).orElse(null);
//                if (tag != null) {
//                    oggettoTagRepo.deleteByIdIdOggettoAndTag(idOggetto, tag);
//                    log.info("rimosso il tag {} con id: {}",tag.getNomeTag(),tag.getIdTag());
//                }
//            }
//        }
//    }

    
	/*
	 * @Override public TagDto create(TagDto dto) { Tag tag =
	 * tagMapper.toEntity(dto); return tagMapper.toDto(tagRepo.save(tag)); }
	 * 
	 * @Override public Optional<TagDto> findById(Long id) { return
	 * tagRepo.findById(id).map(tagMapper::toDto); }
	 * 
	 * @Override public List<TagDto> findAll() { return
	 * tagMapper.mapToList(tagRepo.findAll()); }
	 */
}