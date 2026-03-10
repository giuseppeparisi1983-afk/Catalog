package it.catalog.ui.utility;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.vaadin.flow.component.AbstractField.ComponentValueChangeEvent;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.shared.Registration;


public class BooleanImageToggle extends Div implements HasValue<ComponentValueChangeEvent<BooleanImageToggle, Boolean>, Boolean>, Serializable {

    private static final long serialVersionUID = 1L;

    private Boolean value = false;
    private boolean readOnly = false;
    private boolean requiredIndicatorVisible = false;

    private final Image image;
    private final String coloredSrc;
    private final String grayscaleSrc;
    private final List<ValueChangeListener<? super ComponentValueChangeEvent<BooleanImageToggle, Boolean>>> listeners = new ArrayList<>();

    public BooleanImageToggle(String coloredSrc, String grayscaleSrc) {
        this.coloredSrc = coloredSrc;
        this.grayscaleSrc = grayscaleSrc;
        this.image = new Image();
        image.setWidth("35px");
        image.setHeight("35px");
        add(image);

        updateImage();

        addClickListener(e -> {
            if (!readOnly) {
                Boolean oldValue = this.value;
                this.value = !this.value;
                updateImage();
                fireValueChange(oldValue, this.value);
            }
        });
    }

    private void updateImage() {
        image.setSrc(value ? coloredSrc : grayscaleSrc);
    }

    private void fireValueChange(Boolean oldValue, Boolean newValue) {
        ComponentValueChangeEvent<BooleanImageToggle, Boolean> event =
            new ComponentValueChangeEvent<>(this, this, oldValue, false);
        listeners.forEach(listener -> listener.valueChanged(event));
    }

    @Override
    public void setValue(Boolean value) {
        Boolean oldValue = this.value;
        this.value = value;
        updateImage();
        fireValueChange(oldValue, value);
    }

    @Override
    public Boolean getValue() {
        return value;
    }

    @Override
    public Registration addValueChangeListener(ValueChangeListener<? super ComponentValueChangeEvent<BooleanImageToggle, Boolean>> listener) {
        listeners.add(listener);
        return () -> listeners.remove(listener);
    }

    @Override
    public Boolean getEmptyValue() {
        return false;
    }

    @Override
    public boolean isEmpty() {
        return !value;
    }

    @Override
    public void clear() {
        setValue(false);
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
    public void setRequiredIndicatorVisible(boolean requiredIndicatorVisible) {
        this.requiredIndicatorVisible = requiredIndicatorVisible;
    }

    @Override
    public boolean isRequiredIndicatorVisible() {
        return requiredIndicatorVisible;
    }
}
