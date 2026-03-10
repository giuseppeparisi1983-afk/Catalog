package it.catalog.ui.utility;

import java.util.ArrayList;
import java.util.List;

import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.AbstractField.ComponentValueChangeEvent;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.shared.Registration;

public class RatingStarsField extends Composite<HorizontalLayout> implements HasValue<AbstractField.ComponentValueChangeEvent<RatingStarsField, Double>, Double> {

    private Double value = 0.0;
    private final List<Icon> stars = new ArrayList<>();
    private final List<ValueChangeListener<? super ComponentValueChangeEvent<RatingStarsField, Double>>> listeners = new ArrayList<>();
    private boolean readOnly = false;
    private boolean requiredIndicatorVisible = false;

    public RatingStarsField() {
        HorizontalLayout layout = getContent();
        layout.setSpacing(false);
        layout.setPadding(false);
        updateStars();
    }

    private void updateStars() {
        getContent().removeAll();
        stars.clear();

        for (int i = 1; i <= 5; i++) {
            Icon star;

            if (value >= i) {
                star = VaadinIcon.STAR.create(); // piena
            } else if (value >= i - 0.5) {
                star = VaadinIcon.STAR_HALF_LEFT.create(); // mezza
            } else {
                star = VaadinIcon.STAR_O.create(); // vuota
            }

            star.getElement().getStyle().set("color", "gold");
            star.getElement().getStyle().set("cursor", "pointer");

            // Clic frazionario: metà sinistra = +0.5, metà destra = +1.0
            final int starIndex = i;
            star.getElement().addEventListener("click", e -> {
                if (!readOnly) {
                    double offsetX = e.getEventData().getNumber("event.offsetX");
                    double rating = starIndex - 1 + (offsetX < 12 ? 0.5 : 1.0); // soglia 12px
                    setValue(rating);
                }
            }).addEventData("event.offsetX");

            stars.add(star);
            getContent().add(star);
        }
    }


    @Override
    public void setValue(Double value) {
        Double oldValue = this.value;
        this.value = value == null ? 0.0 : value;
        updateStars();
        ComponentValueChangeEvent<RatingStarsField, Double> event =
            new ComponentValueChangeEvent<>(this, this, oldValue, false);
        listeners.forEach(listener -> listener.valueChanged(event));
    }

    @Override
    public Double getValue() {
        return value;
    }

    @Override
    public Registration addValueChangeListener(ValueChangeListener<? super ComponentValueChangeEvent<RatingStarsField, Double>> listener) {
        listeners.add(listener);
        return () -> listeners.remove(listener);
    }

    @Override
    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
    }

    @Override
    public boolean isReadOnly() {
        return readOnly;
    }

    @Override
    public boolean isEmpty() {
        return value == null || value == 0.0;
    }

    @Override
    public void clear() {
        setValue(0.0);
    }

    @Override
    public void setRequiredIndicatorVisible(boolean requiredIndicatorVisible) {
        this.requiredIndicatorVisible = requiredIndicatorVisible;
    }

    @Override
    public boolean isRequiredIndicatorVisible() {
        return requiredIndicatorVisible;
    }
}
