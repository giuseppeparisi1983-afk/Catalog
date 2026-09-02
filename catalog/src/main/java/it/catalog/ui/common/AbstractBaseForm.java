package it.catalog.ui.common;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Sort;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasLabel;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
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
import java.util.function.Supplier;
import it.catalog.service.dto.HasId;
import it.catalog.service.dto.search.StringCriterion;
import it.catalog.service.interfaces.SearchService;
import it.catalog.ui.utility.BaseFilter;
import lombok.extern.slf4j.Slf4j;

/*Classe che gestisce solo il ciclo di vita: Prendi l'ID dall'URL -> Carica i dati -> Mettili nel Binder -> Salva quando clicco*/
@Slf4j
public abstract class AbstractBaseForm<T, S extends SearchService<T, F>, F extends BaseFilter>  
extends VerticalLayout implements HasUrlParameter<Long> {

    // COSTANTI STANDARD PER LA NAVIGAZIONE UTILIZZATI DA TUTTI I FORM
    public static final String P_VIEW = "view";
    public static final String P_PAGE = "page";
    public static final String P_POS = "pos";
    public static final String P_F_VAL = "f_val";
    public static final String P_F_FIELD = "f_field";
    public static final String P_S_PROP = "s_prop";
    public static final String P_S_DIR = "s_dir";
    
    protected final S service;
    protected final Class<T> beanType;
    protected final Binder<T> binder;
    protected T bean;
    protected final Supplier<F> filterSupplier; 
    
    protected Button save = new Button("Salva");
    protected Button cancel = new Button();
    protected boolean isViewMode = false;
    
    protected int returnPage = 0; // Memorizziamo la pagina di ritorno
    protected int currentPosition = -1; // La posizione assoluta del record nella ricerca
    protected Button btnPrev = new Button("«");
    protected Button btnNext = new Button("»");
    protected Span navInfo = new Span(); // Es: "3 di 150"
    protected long totalElements = 0; // variabile per la gestione del limite massimo
    // Memorizziamo il filtro e il sort correnti (ricevuti dall'URL)
    protected F currentFilter;
    protected Sort currentSort;
    
    public AbstractBaseForm(String title,S service, Class<T> beanType, Supplier<F> filterSupplier) {
        this.service = service;
        this.beanType = beanType;
        this.filterSupplier = filterSupplier;
        // BeanValidationBinder attiva automaticamente le annotazioni @NotNull, @Size, ecc. del DTO
        this.binder = new BeanValidationBinder<>(beanType);

        setSizeFull();
        
        H1 h1 = new H1();
        h1.setText(title);
        h1.setWidth("max-content");
        setAlignSelf(FlexComponent.Alignment.CENTER, h1);

        add(h1);
  
        configureToolbar();
        configureNavigationHandlers(); // Metodo per i click
    }

    private void configureNavigationHandlers() {
        // Configura i listener una sola volta. 
        // Leggeranno il valore aggiornato di 'currentPosition' ogni volta che vengono cliccati.
        btnPrev.addClickListener(e -> navigateToAdjacent(false));
        btnNext.addClickListener(e -> navigateToAdjacent(true));
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
        this.isViewMode = queryParams.getOrDefault(P_VIEW, List.of("false")).contains("true");
        this.returnPage = Integer.parseInt(queryParams.getOrDefault(P_PAGE, List.of("0")).get(0));
        this.currentPosition = Integer.parseInt(queryParams.getOrDefault(P_POS, List.of("-1")).get(0));// RECUPERO POSIZIONE ATTUALE
        
        
        log.debug("--- SET PARAMETER --- ID: {}, POS: {}, PAGE: {}", id, currentPosition, returnPage);
        
        // 1. Leggiamo ID e modalità view (già esistente)
        // 2. Leggiamo i parametri di ricerca dall'URL per ricostruire il filtro
        this.currentFilter = rebuildFilterFromUrl(event.getLocation().getQueryParameters());
        this.currentSort = rebuildSortFromUrl(event.getLocation().getQueryParameters());
        
        // Leggiamo la pagina se presente
//        if (queryParams.containsKey(P_PAGE)) {
//            this.returnPage = Integer.parseInt(queryParams.get(P_PAGE).get(0));
//        }
        
     // CALCOLO TOTALE RECORD (per sapere quando disabilitare btnNext)
        if (currentPosition != -1) {
            this.totalElements = service.count(currentFilter);
        }
        
        if (id != null) {
            this.bean = loadBean(id);
        } else {
            this.bean = createNewBean();
        }
        
        binder.readBean(bean);
        updateUIState();
        updateNavigationButtons(); // Aggiornamento dello stato dei bottoni e della label
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
        catch (RuntimeException  e) {
            // CATTURIAMO L'ERRORE DI BUSINESS
            // Non navighiamo indietro! Restiamo sul form per far correggere l'utente.
            Notification.show(e.getMessage(), 5000, Notification.Position.MIDDLE)
                       .addThemeVariants(NotificationVariant.LUMO_ERROR);
        } catch (Exception e) {
            Notification.show("Errore tecnico durante il salvataggio", 5000, Notification.Position.MIDDLE);
        }
        
        
    }

    protected void updateNavigationButtons() {
    	 // Se non abbiamo una posizione valida o siamo in inserimento (id null), nascondiamo la nav
        boolean navVisible = (currentPosition != -1 && bean != null && ((HasId)bean).getId() != null);
        btnPrev.setVisible(navVisible);
        btnNext.setVisible(navVisible);
        navInfo.setVisible(navVisible);

        if (navVisible) {
            btnPrev.setEnabled(currentPosition > 0); // Abilita "Indietro" solo se non siamo sul primo elemento (pos > 0)
            
            // Abilita "Avanti" solo se non siamo sull'ultimo elemento (pos < totale - 1)
            btnNext.setEnabled(currentPosition < totalElements - 1);
            
            // Aggiorna l'etichetta dinamica
            navInfo.setText("Item " + (currentPosition + 1) + " di " + totalElements);
        }
        
    }
    
    private void navigateToAdjacent(boolean next) {
        int targetPos = next ? this.currentPosition + 1 : this.currentPosition - 1;   // Calcola la posizione target
        
        log.debug("Tentativo di navigazione verso posizione: {}", targetPos);
        
     // Chiedi al service l'ID del record a quella posizione
        service.findIdAtPosition(currentFilter, currentSort, targetPos).ifPresentOrElse(newId -> {
        	log.debug("Trovato nuovo ID: {}. Navigo verso POS: {}", newId, targetPos);
        	// Prepariamo i parametri per mantenere lo stato della ricerca
        	Map<String, String> p = new HashMap<>();
            p.put(P_VIEW, String.valueOf(isViewMode));
            p.put(P_PAGE, String.valueOf(returnPage));
            p.put(P_POS, String.valueOf(targetPos)); // scriviamo la NUOVA posizione nell'URL
            
            // Aggiungiamo i parametri del filtro (questo metodo deve essere implementato dai figli)
            p.putAll(getFilterParamsForUrl());

            getUI().ifPresent(ui -> ui.navigate(this.getClass(), newId, QueryParameters.simple(p)));
        }, () -> {
            log.warn("Nessun ID trovato alla posizione {}", targetPos);
        Notification.show("Nessun altro elemento trovato");
    });
    }
    
    // Helper per i figli: crea la mappa filtri standard
    protected Map<String, String> getStandardFilterMap() {
        Map<String, String> params = new HashMap<>();
        if (currentFilter != null && currentFilter.getCriterion() instanceof StringCriterion sc) {
            params.put(P_F_VAL, sc.getValue());
            params.put(P_F_FIELD, sc.getField());
        }
        return params;
    }

    /**
     * Ricostruisce il filtro leggendo i parametri standard f_val e f_field dall'URL.
     */
    protected F rebuildFilterFromUrl(QueryParameters qp) {
        F filter = filterSupplier.get(); // Crea una nuova istanza (es. new DtoFilter())
        Map<String, List<String>> params = qp.getParameters();

        if (params.containsKey(P_F_VAL) && params.containsKey(P_F_FIELD)) {
            String val = params.get(P_F_VAL).get(0);
            String field = params.get(P_F_FIELD).get(0);
            
            // Creiamo il criterio standard (StringCriterion)
            StringCriterion sc = new StringCriterion();
            sc.setField(field);
            sc.setValue(val);
            filter.setCriterion(sc);
        }
        return filter;
    }

    /**
     * Prepara la mappa dei parametri da aggiungere all'URL durante la navigazione
     */
    protected Map<String, String> getFilterParamsForUrl() {
        Map<String, String> params = new HashMap<>();
        // Parametri di base (pagina e posizione)
//        params.put(P_VIEW, String.valueOf(isViewMode));
//        params.put(P_PAGE, String.valueOf(returnPage));
//        params.put(P_POS, String.valueOf(currentPosition));

        // Parametri del filtro
        if (currentFilter != null && currentFilter.getCriterion() instanceof StringCriterion sc) {
            if (sc.getValue() != null && !sc.getValue().isBlank()) {
                params.put(P_F_VAL, sc.getValue());
                params.put(P_F_FIELD, sc.getField());
            }
        }
        
        // FONDAMENTALE: Manteniamo l'ordinamento
        if (currentSort != null && currentSort.isSorted()) {
            currentSort.forEach(order -> {
                params.put(P_S_PROP, order.getProperty());
                params.put(P_S_DIR, order.getDirection().name());
            });
        }
        
        
        
        return params;
    }

    /**
     * Ricostruisce l'ordinamento (Sort) - Implementazione base
     */
    protected Sort rebuildSortFromUrl(QueryParameters qp) {
    	 Map<String, List<String>> params = qp.getParameters();
    	    if (params.containsKey(P_S_PROP)) {
    	        String prop = params.get(P_S_PROP).get(0);
    	        String dir = params.getOrDefault(P_S_DIR, List.of("ASC")).get(0);
    	        return Sort.by(Sort.Direction.fromString(dir), prop);
    	    }
    	    return Sort.by(Sort.Direction.DESC, "id"); // Default di sicurezza
    }
    
    // Metodi "Gancio" (Hooks)
    protected abstract T loadBean(Long id);
    protected abstract void saveBean(T bean);
    protected abstract T createNewBean();
    protected abstract String getReturnRoute();
 
    protected void navigateBack() {
        // Costruiamo i parametri di ritorno includendo la pagina
        QueryParameters qp = QueryParameters.simple(Map.of(P_PAGE, String.valueOf(returnPage)));
        getUI().ifPresent(ui -> ui.navigate(getReturnRoute(), qp));
    }
    
    
}
