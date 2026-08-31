package it.catalog.service.dto.search;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;


/**Questa classe viene utilizzata per la definizione dei campi di ricerca quando 
 * il criterio di ricerca è di tipo stringa (es. ricerca per nome o autore.)*/

@AllArgsConstructor
@NoArgsConstructor
public class StringCriterion implements SearchCriterion {

    private String field; // es. "nome", "cognome"
    private String value; // es. "Mario"

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
