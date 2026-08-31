package it.catalog.utility;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import it.catalog.persistence.entity.Chitarra;
import it.catalog.service.dto.TagDto;
import it.catalog.service.dto.search.DateRangeCriterion;
import it.catalog.service.dto.search.DtoFilter;
import it.catalog.service.dto.search.SearchCriterion;
import it.catalog.service.dto.search.StringCriterion;
import jakarta.persistence.criteria.From;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;


/** Gestione della findAll() sui Tipi di file*/
@Component
public class SpecificationFactory<T> {


    public Specification<T> build(DtoFilter filter) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            // 2. Gestione dei criteri di ricerca dinamici
            SearchCriterion criterion = filter.getCriterion();
            if (criterion != null) {
                
                // Caso: Ricerca testuale (StringCriterion)
                if (criterion instanceof StringCriterion sc && sc.getValue() != null && !sc.getValue().isBlank()) {
                    // Accediamo a sc.getField() e sc.getValue() perché il compilatore ora sa che è uno StringCriterion
                    predicates.add(cb.like(
                        cb.lower(root.get(sc.getField())), 
                        "%" + sc.getValue().toLowerCase() + "%"
                    ));
                } 
                
                // Caso: Ricerca per data (DateRangeCriterion)
                else if (criterion instanceof DateRangeCriterion dc) {
                    // Accediamo a dc.getField(), dc.getFrom(), dc.getTo()
                    if (dc.getFrom() != null) {
                        predicates.add(cb.greaterThanOrEqualTo(root.get(dc.getField()), dc.getFrom()));
                    }
                    if (dc.getTo() != null) {
                        predicates.add(cb.lessThanOrEqualTo(root.get(dc.getField()), dc.getTo()));
                    }
                }
            }

            // 3. Filtro per Tags (Relazione Many-to-Many)
            if (filter.getTags() != null && !filter.getTags().isEmpty()) {

                // Estraiamo gli ID dei tag selezionati nel filtro
                List<Long> tagIds = filter.getTags().stream()
                                          .map(TagDto::getIdTag)
                                          .toList();
                
             // Determiniamo il percorso verso i tag in base al tipo di entità (Root)
                From<?, ?> from;
                if (root.getJavaType().equals(Chitarra.class)) {
                    // Se stiamo cercando Chitarre, dobbiamo passare per la join "video"
                    from = root.join("video");
                } else {
                    // Se stiamo cercando Video (o altre entità con i tag diretti), usiamo root
                    from = root;
                }
                
                // Facciamo una join con la tabella dei tag
                Join<?, ?> tagJoin = from.join("tags");

                
                // Aggiungiamo il predicato IN
                predicates.add(tagJoin.get("idTag").in(tagIds));
                
                // Nota: Se l'utente seleziona più tag, questa query restituirà i file che hanno ALMENO UNO dei tag.
                // Se vuoi file che abbiano TUTTI i tag, la logica sarebbe diversa.
                query.distinct(true); // Evita duplicati dovuti alla join
            }

            return cb.and(predicates.toArray(new Predicate[0]));
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
    
    private Object convertToNumber(String value, Class<?> type) {
        if (type == Integer.class || type == int.class) return Integer.parseInt(value);
        if (type == Long.class || type == long.class) return Long.parseLong(value);
        if (type == Double.class || type == double.class) return Double.parseDouble(value);
        return null;
    }
}

