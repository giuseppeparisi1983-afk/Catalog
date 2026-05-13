package it.catalog.ui.common;

import java.util.List;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.OptionalParameter;

import it.catalog.service.interfaces.SearchService;

/*Classe che gestisce solo il ciclo di vita: Prendi l'ID dall'URL -> Carica i dati -> Mettili nel Binder -> Salva quando clicco*/

public abstract class AbstractBaseForm<T, S extends SearchService<T, ?>> extends VerticalLayout implements HasUrlParameter<Long> {

    protected final S service;
    protected final Class<T> beanType;
    protected final Binder<T> binder;
    protected T bean;

    protected Button save = new Button("Salva");
    protected Button cancel = new Button("Annulla");
    protected boolean isViewMode = false;

    public AbstractBaseForm(S service, Class<T> beanType) {
        this.service = service;
        this.beanType = beanType;
        // BeanValidationBinder attiva automaticamente le annotazioni @NotNull, @Size, ecc. del DTO
        this.binder = new BeanValidationBinder<>(beanType);

        setSizeFull();
        configureToolbar();
    }

    private void configureToolbar() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        save.addClickListener(e -> validateAndSave());
        cancel.addClickListener(e -> navigateBack());

        HorizontalLayout toolbar = new HorizontalLayout(save, cancel);
        add(toolbar);
    }

    @Override
    public void setParameter(BeforeEvent event, @OptionalParameter Long id) {
       
        // 1. L'ID viene catturato automaticamente da HasUrlParameter<Long>
        // se l'URL è documents-form/123, id sarà 123. Se è documents-form senza ID, id sarà null.
    	// Controllo se nell'URL c'è ?view=true
        this.isViewMode = event.getLocation().getQueryParameters()
                .getParameters().getOrDefault("view", List.of("false")).contains("true");

        if (id != null) {
            this.bean = loadBean(id);
        } else {
            this.bean = createNewBean();
        }
        
        binder.readBean(bean);
        updateUIState();
    }

    protected void updateUIState() {
        binder.setReadOnly(isViewMode);
        save.setVisible(!isViewMode);
    }

    private void validateAndSave() {
        try {
            binder.writeBean(bean);
            saveBean(bean);
            Notification.show("Dati salvati con successo");
            navigateBack();
        } catch (ValidationException e) {
            Notification.show("Controlla i campi evidenziati in rosso", 3000, Notification.Position.MIDDLE);
        }
    }

    // Metodi "Gancio" (Hooks)
    protected abstract T loadBean(Long id);
    protected abstract void saveBean(T bean);
    protected abstract T createNewBean();
    protected abstract void navigateBack();
}
