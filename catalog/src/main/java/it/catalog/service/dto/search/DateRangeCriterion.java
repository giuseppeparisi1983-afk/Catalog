package it.catalog.service.dto.search;

import java.time.LocalDate;

public class DateRangeCriterion implements SearchCriterion {

    private String field;
    private LocalDate from;
    private LocalDate to;

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public LocalDate getFrom() {
        return from;
    }

    public void setFrom(LocalDate from) {
        this.from = from;
    }

    public LocalDate getTo() {
        return to;
    }

    public void setTo(LocalDate to) {
        this.to = to;
    }
}

