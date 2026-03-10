package it.catalog.utility;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import it.catalog.persistence.entity.OggettoTag;
import it.catalog.persistence.entity.Tag;
import it.catalog.service.dto.TagDto;
import it.catalog.service.dto.search.DateRangeCriterion;
import it.catalog.service.dto.search.DtoFilter;
import it.catalog.service.dto.search.SearchCriterion;
import it.catalog.service.dto.search.StringCriterion;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;


/** Gestione dei tipi per la query trovaConFiltro dentro PersonaSerice*/
@Component
public class SpecificationFactory<T> {


    public Specification<T> build(String tipoOggetto,DtoFilter filter) {
        return (root, query, cb) -> {
        	 
//        	query.distinct(true);
        	
        	List<Predicate> predicates = new ArrayList<>();
//        	Join<T, OggettoTag> join = root.join("tags", JoinType.LEFT); 
//        	
//        	predicates.add(cb.equal(join.get("tag").get("tipoOggetto"), tipoOggetto));
        	
            // JOIN 1: oggetto_tag
//            Join<T, OggettoTag> joinOggettoTag = (Join<T, OggettoTag>)
//                    root.getJoins().stream()
//                        .filter(j -> j.getAttribute().getName().equals("tags"))
//                        .findFirst()
//                        .orElseGet(() -> root.join("tags", JoinType.LEFT));
        	Join<T, OggettoTag> joinOggettoTag = root.join("tags", JoinType.LEFT);
        			
            // JOIN 2: tag
//            Join<OggettoTag, Tag> joinTag = (Join<OggettoTag, Tag>)
//                    joinOggettoTag.getJoins().stream()
//                        .filter(j -> j.getAttribute().getName().equals("tag"))
//                        .findFirst()
//                        .orElseGet(() -> joinOggettoTag.join("tag", JoinType.LEFT));

        	Join<OggettoTag, Tag> joinTag = joinOggettoTag.join("tag", JoinType.LEFT);
        	
            // Condizione nella ON-clause
            joinTag.on(cb.equal(joinTag.get("tipoOggetto"), tipoOggetto));

        	
            // --- criteri dinamici ---
        	SearchCriterion c = filter.getCriterion();
        	
        	if (c instanceof StringCriterion sc) {
                Path<String> path = root.get(sc.getField());
                predicates.add(cb.like(cb.lower(path), "%" + sc.getValue().toLowerCase() + "%"));
            }
        	
        	 if (c instanceof DateRangeCriterion dc) {
                 Path<LocalDate> path = root.get(dc.getField());

                 if (dc.getFrom() != null && dc.getTo() != null) {
                     predicates.add(cb.between(path, dc.getFrom(), dc.getTo()));
                 } else if (dc.getFrom() != null) {
                     predicates.add(cb.greaterThanOrEqualTo(path, dc.getFrom()));
                 } else if (dc.getTo() != null) {
                     predicates.add(cb.lessThanOrEqualTo(path, dc.getTo()));
                 }
             }
        	 
        	 // filtro tag aggiuntivi
        	 if (filter.getTags() != null && !filter.getTags().isEmpty()) {
//                 predicates.add(root.join("tags").in(filter.getTags()));
        		 
//        	predicates.add(withTag_(filter.getTags()).toPredicate(root, query, cb));
        		 
        		 List<String> tagNames = filter.getTags().stream()
                         .map(TagDto::getNomeTag)
                         .toList();

                 joinTag.on(joinTag.get("nomeTag").in(tagNames));
        		 
             }

        	 // Ritorna un predicato vuoto (WHERE 1=1) per mantenere tutti i file
             // ma con i tag filtrati nella JOIN
        	 return cb.conjunction();
        	 
//             return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
    
    public Specification<T> buildOld(String fieldName, String rawValue, Class<?> type) {
        return (root, query, cb) -> {
            Path<?> path = root.get(fieldName);

            if (type == String.class) {
                return cb.like(cb.lower(path.as(String.class)), "%" + rawValue.toLowerCase() + "%");

            } else if (type == Boolean.class || type == boolean.class) {
                Boolean value = Boolean.parseBoolean(rawValue);
                return cb.equal(path, value);

            } else if (type == LocalDate.class) {
                // supponiamo che il rawValue sia nel formato "2023-01-01,2023-12-31"
                String[] range = rawValue.split(",");
                if (range.length == 2) {
                    LocalDate start = LocalDate.parse(range[0]);
                    LocalDate end = LocalDate.parse(range[1]);
                    return cb.between(path.as(LocalDate.class), start, end);
                } else {
                    LocalDate date = LocalDate.parse(rawValue);
                    return cb.equal(path.as(LocalDate.class), date);
                }

            } else if (Number.class.isAssignableFrom(type) || type.isPrimitive()) {
                Object value = convertToNumber(rawValue, type);
                return cb.equal(path, value);
            }

            return cb.conjunction(); // fallback - nessun filtro
        };
    }
    
    // da rimuovere
    public Specification<T> withTag(String tipoOggetto, List<String> tagNames) {
        return (root, query, cb) -> {
            Join<T, OggettoTag> join = root.join("tags", JoinType.INNER); 
            /**
            l'istanza join serve per costruire una join tra entità all'interno di una query dinamica 
            T è il tipo dell'entità di partenza (quella da cui stai facendo la join)
            OggettoTag è il tipo dell'entità di destinazione (quella che stai collegando)
            
            join è l'oggetto che ti permette di accedere ai campi di OggettoTag 
            per costruire condizioni (Predicate) nella query
            */
            
            Predicate tipoMatch = cb.equal(join.get("tag").get("tipoOggetto"), tipoOggetto);
            Predicate tagMatch = join.get("tag").get("nomeTag").in(tagNames);
            
            /**
             * Si sta eseguendo unaINNER JOIN, che includere solo i record che hanno corrispondenze nella tabella collegata
             * E' possibile fare una query simile anche sul repository
             * SELECT t FROM Tag t JOIN OggettoTag ot ON ot.idTag = t.idTag WHERE ot.tipoOggetto = 'documento' AND ot.idOggetto = :idDocumento
             * come si intuisce la query seleziona tutti i documenti che hanno almeno un OggettoTag associato Il tipoOggetto è "documento" Il nomeTag è uno tra quelli della lista tagNames
             * */
            return cb.and(tipoMatch, tagMatch);
        };
    }

    public Specification<T> withTag_(List<TagDto> tags) {
        return (root, query, cb) -> {
//            Join<T, OggettoTag> join = root.join("tags", JoinType.INNER); 
        	
        	 // LEFT JOIN oggetto_tag
            Join<T, OggettoTag> joinOggettoTag = root.join("tags", JoinType.LEFT);

            // LEFT JOIN tag
            Join<OggettoTag, Tag> joinTag = joinOggettoTag.join("tag", JoinType.LEFT);
            /**
            l'istanza join serve per costruire una join tra entità all'interno di una query dinamica 
            T è il tipo dell'entità di partenza (quella da cui stai facendo la join)
            OggettoTag è il tipo dell'entità di destinazione (quella che stai collegando)
            
            join è l'oggetto che ti permette di accedere ai campi di OggettoTag 
            per costruire condizioni (Predicate) nella query
            */
            
         //  converto la lista di TagDto in una lista di stringhe
	        List<String> tagNames = (tags != null && !tags.isEmpty()) ? 
	        		tags.stream().map(TagDto::getNomeTag).collect(Collectors.toList()): 
		    			Collections.EMPTY_LIST;
            
//            Predicate tagMatch = join.get("tag").get("nomeTag").in(tagNames);
            
            /**
             * Si sta eseguendo unaINNER JOIN, che includere solo i record che hanno corrispondenze nella tabella collegata
             * E' possibile fare una query simile anche sul repository
             * SELECT t FROM Tag t JOIN OggettoTag ot ON ot.idTag = t.idTag WHERE ot.tipoOggetto = 'documento' AND ot.idOggetto = :idDocumento
             * come si intuisce la query seleziona tutti i documenti che hanno almeno un OggettoTag associato Il tipoOggetto è "documento" Il nomeTag è uno tra quelli della lista tagNames
             * */
//            return cb.and(tipoMatch, tagMatch);
//            return cb.and(tagMatch);
	        
	        // Condizione nella ON-clause
	        joinTag.on(joinTag.get("nomeTag").in(tagNames));

	        // NON aggiungere nulla al WHERE
	        return cb.conjunction();
        };
    }
    
    
    
    private Object convertToNumber(String value, Class<?> type) {
        if (type == Integer.class || type == int.class) return Integer.parseInt(value);
        if (type == Long.class || type == long.class) return Long.parseLong(value);
        if (type == Double.class || type == double.class) return Double.parseDouble(value);
        return null;
    }
}

