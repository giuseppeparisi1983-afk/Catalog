package it.catalog.ui.common;

import java.util.List;
import java.util.Map;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasLabel;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.OptionalParameter;
import com.vaadin.flow.router.QueryParameters;

import it.catalog.service.interfaces.SearchService;
import lombok.extern.slf4j.Slf4j;

/*Classe che gestisce solo il ciclo di vita: Prendi l'ID dall'URL -> Carica i dati -> Mettili nel Binder -> Salva quando clicco*/
@Slf4j
public abstract class AbstractBaseForm<T, S extends SearchService<T, ?>> extends VerticalLayout implements HasUrlParameter<Long> {

    protected final S service;
    protected final Class<T> beanType;
    protected final Binder<T> binder;
    protected T bean;

    protected Button save = new Button("Salva");
    protected Button cancel = new Button();
    protected boolean isViewMode = false;
    
    protected int returnPage = 0; // Memorizziamo la pagina di ritorno

    public AbstractBaseForm(String title,S service, Class<T> beanType) {
        this.service = service;
        this.beanType = beanType;
        // BeanValidationBinder attiva automaticamente le annotazioni @NotNull, @Size, ecc. del DTO
        this.binder = new BeanValidationBinder<>(beanType);

        setSizeFull();
        
        H1 h1 = new H1();
        h1.setText(title);
        h1.setWidth("max-content");
        setAlignSelf(FlexComponent.Alignment.CENTER, h1);

        add(h1);
  
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
       
    	Map<String, List<String>> queryParams = event.getLocation().getQueryParameters().getParameters();
    	
    	// 1. L'ID viene catturato automaticamente da HasUrlParameter<Long>
        // se l'URL è documents-form/123, id sarà 123. Se è documents-form senza ID, id sarà null.
    	// Controllo se nell'URL c'è ?view=true
        this.isViewMode = queryParams.getOrDefault("view", List.of("false")).contains("true");

        // Leggiamo la pagina se presente
        if (queryParams.containsKey("page")) {
            this.returnPage = Integer.parseInt(queryParams.get("page").get(0));
        }
        
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
        cancel.setText(isViewMode ? "Indietro":"Annulla");
    }

    private void validateAndSave() {
        try {
            binder.writeBean(bean);
            saveBean(bean);
            Notification.show("Dati salvati con successo");
            navigateBack();
        } catch (ValidationException e) {
        	 // Usiamo un StringBuilder per creare un unico blocco di testo pulito
            StringBuilder sb = new StringBuilder();
            
            sb.append("\n=== ERRORE DI VALIDAZIONE RILEVATO ===");
            
            e.getFieldValidationErrors().forEach(status -> {
                String nomeCampo = "Sconosciuto";
                
                if (status.getField() instanceof HasLabel hasLabelComponent) {
                    String label = hasLabelComponent.getLabel();
                    if (label != null && !label.trim().isEmpty()) {
                        nomeCampo = label;
                    }
                }
                
                if ("Sconosciuto".equals(nomeCampo) && status.getField() instanceof Component component) {
                    nomeCampo = component.getId().orElse(component.getClass().getSimpleName());
                }

                String messaggio = status.getMessage().orElse("Errore di validazione o conversione generico");
                
                // Aggiungiamo la riga del campo con un leggero rientro
                sb.append("\n  ❌ Campo: [").append(nomeCampo).append("] | Causa: ").append(messaggio);
            });
            
            sb.append("\n=======================================");
            
            // Unica chiamata al log: i metadati del logger appariranno solo sulla primissima riga
            log.error(sb.toString());
        	Notification.show("Controlla i campi evidenziati in rosso", 3000, Notification.Position.MIDDLE);
        }
    }

    // Metodi "Gancio" (Hooks)
    protected abstract T loadBean(Long id);
    protected abstract void saveBean(T bean);
    protected abstract T createNewBean();
//    protected abstract void navigateBack();
    
    
    protected void navigateBack() {
        // Costruiamo i parametri di ritorno includendo la pagina
        QueryParameters qp = QueryParameters.simple(Map.of("page", String.valueOf(returnPage)));
        getUI().ifPresent(ui -> ui.navigate(getReturnRoute(), qp));
    }
    
    protected abstract String getReturnRoute(); // Es: return "documents";
    
    
    
}
