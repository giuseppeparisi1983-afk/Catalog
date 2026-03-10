package it.catalog.service.dto.search;

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
