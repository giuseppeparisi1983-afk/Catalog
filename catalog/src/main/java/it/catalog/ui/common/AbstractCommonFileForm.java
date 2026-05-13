package it.catalog.ui.common;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;

import it.catalog.service.dto.TagDto;
import it.catalog.service.interfaces.SearchService;
import it.catalog.ui.utility.AppConverters.DoubleToLong;
import it.catalog.ui.utility.AppConverters.InstantToLocalDate;
import it.catalog.ui.utility.AppConverters.InstantToLocalDateTime;
import it.catalog.ui.utility.BooleanImageToggle;

/** Classe dove dichiariamo tutti i campi comuni. 
 * Questa classe estende AbstractBaseForm<T,S> e "disegna" la parte di form che vedrai in ogni file (Documento, Video, Chitarra).
 * 
 * Nota: il tipo generico S è un sottotipo di SearchService<T, ?>. 
 * Questo ci permette di accedere a tutti i metodi del SearchService, incluso getAllTags() per popolare la combo dei TAGS.
 * */

public abstract class AbstractCommonFileForm<T, S extends SearchService<T, ?>> extends AbstractBaseForm<T, S> {

    // Campi comuni definiti da te
    protected TextField nome = new TextField("Nome");
    protected TextField path = new TextField("Percorso File");
    protected TextArea descrizione = new TextArea("Descrizione");
    protected NumberField dimensione = new NumberField("Dimensione (KB)");
//    protected Checkbox preferito = new Checkbox("Preferito");
//    protected Checkbox backup = new Checkbox("Backup effettuato");
//    protected Checkbox cancelled = new Checkbox("Cancellato");
    protected IntegerField rating = new IntegerField("Rating (1-5)");
    
    protected BooleanImageToggle backup = new BooleanImageToggle(
    	    "images/backup-colored.png",
    	    "images/backup-gray.png"
    	);

    protected BooleanImageToggle preferito = new BooleanImageToggle(
    		"images/like-colored.png",
    		"images/like-gray.png"
    		);
//    protected RatingStarsField rating = new RatingStarsField();

    protected NumberField visualizzazioni = new NumberField("Visualizzazioni");
    protected TextArea note = new TextArea("Note");
    
    
 // Aggiungiamo i TAGS
    protected MultiSelectComboBox<TagDto> tags = new MultiSelectComboBox<>("Tags");
    
    /**
     * Nota: Affinché la MultiSelectComboBox funzioni correttamente (ovvero sappia riconoscere che il Tag "Lavoro" 
     * nel database è lo stesso Tag "Lavoro" già presente nel DTO), è fondamentale che la tua classe TagDto abbia i metodi equals() e hashCode() 
     * implementati correttamente, preferibilmente basandosi sull'ID del tag.
     * */
    
    
    // Date (usiamo DatePicker per semplicità, ma lastUpdate spesso è sola lettura)
    protected DateTimePicker lastView = new DateTimePicker("Ultima visualizzazione");
    protected DatePicker dataArchiviazione = new DatePicker("Data Archiviazione");
    protected DatePicker lastUpdate = new DatePicker("Ultimo aggiornamento");

    public AbstractCommonFileForm(S service, Class<T> beanType) {
        super(service, beanType);
        
        
     // 1. Configurazione specifica per i Tags
    	settingTags(service);
        
        // 2. Creiamo un layout per i campi comuni
//        FormLayout commonLayout = new FormLayout();
//        commonLayout.add(nome, path, dimensione, rating, visualizzazioni, 
//                          dataArchiviazione,lastUpdate, lastView, preferito, backup, 
//                          descrizione, note);
        
        // Impostiamo alcune proprietà
//        commonLayout.setColspan(descrizione, 2);
//        commonLayout.setColspan(note, 2);
//        commonLayout.setColspan(path, 2);
//        commonLayout.add(tags, 2); // Occupa due colonne
//        
//        add(commonLayout);
        
        // 3. BINDER MANUALE GENERICO (Valido per tutti i DTO)
        // Usiamo le stringhe per i nomi dei campi così non dipendiamo dal tipo specifico di DTO
        
		/**
		 * i campi NumberField lavorano con i tipi Double, mentre su DocumentoDto
		 * visualizzazioni è di tipo long. Per questo serve una conversione manuale tramite
		 * Converter
		 */
//        binder.forField(visualizzazioni)
//		.withConverter(doubleVal -> doubleVal == null ? null : doubleVal.longValue(),
//				longVal -> longVal == null ? null : longVal.doubleValue(), "Valore non valido")
//		.bind("visualizzazioni");  

    	
    	binder.forField(visualizzazioni)
        .withConverter(new DoubleToLong())
        .bind("visualizzazioni");
        
    	
	       // alternative with image
			
			  binder.forField(preferito) .withConverter( checked -> checked != null &&
			  checked, value -> value != null && value ) .bind("preferito");
			  
//			  binder.forField(rating) //.withNullRepresentation(0.0) // Se il valore è
//			  null, usa 0.0 .withConverter( value -> value, // da NumberField → DTO value
//			  -> value == null ? null : value // da DTO → NumberField ) .bind("rating");
			  
			  binder.forField(backup) .withConverter( checked -> checked != null &&
			  checked, value -> value != null && value ) .bind("backup");
			 
		        
		        
        // Gestione DateTimePicker con conversione tra LocalDateTime e Instant, accettando null
    	binder.forField(lastUpdate).withConverter(new InstantToLocalDate())
    	.bind("lastUpdate");
    	
	
    	binder.forField(dataArchiviazione).withConverter(new InstantToLocalDate()).bind("dataArchiviazione");
    	
    	binder.forField(lastView).withConverter(new InstantToLocalDateTime())
    	.bind("lastView");
    	
    	   // 2. BINDING MANUALE FORZATO (nella classe Padre)
        // Usiamo il nome della proprietà come stringa "tags".
        // Questo "prenota" il campo: bindInstanceFields() lo ignorerà perché già associato.
        binder.forField(tags).bind("tags"); 
        
        // Effettuiamo il binding automatico per i nomi che coincidono
        // Binder assocerà automaticamente 'nome' UI al campo 'nome' del DTO
        //binder.bindInstanceFields(this);
    }
    
    
    private void settingTags(S service) {
        // 1. Recuperiamo i tag esistenti
        List<TagDto> allTags = new ArrayList<>(service.getAllTags());
        
        // 2. Configurazione semplice
        tags.setItems(allTags);
        tags.setItemLabelGenerator(TagDto::getNomeTag);
        tags.setPlaceholder("Seleziona o scrivi nuovi tag...");
        tags.setWidth("360px");

        // 3. Abilitiamo l'inserimento manuale
        tags.setAllowCustomValue(true);
        
        tags.addCustomValueSetListener(e -> {
            String nomeTag = e.getDetail().trim();
            if (nomeTag.isEmpty()) return;

            // Controlliamo se esiste già un tag con questo nome (case-insensitive) nella lista attuale
            Optional<TagDto> esistente = allTags.stream()
                    .filter(t -> t.getNomeTag().equalsIgnoreCase(nomeTag))
                    .findFirst();

            TagDto tagDaSelezionare;

            if (esistente.isPresent()) {
                tagDaSelezionare = esistente.get();
            } else {
                // Creiamo un nuovo DTO. L'ID sarà null, verrà generato dal DB al salvataggio.
                // Uso "Generico" invece di "Audio" per renderlo davvero Abstract
                tagDaSelezionare = new TagDto();
                tagDaSelezionare.setNomeTag(nomeTag);
                
                allTags.add(tagDaSelezionare);
                // 3. RECUPERO DINAMICO DEL DATAVIEW
                // Invece di dichiarare il tipo fuori, lo chiamiamo direttamente qui.
                // getListDataView() è il metodo standard di Vaadin 24 per i componenti in memoria.
                tags.getListDataView().refreshAll();
            }

            // Aggiorniamo la selezione della UI
            Set<TagDto> currentSelection = new HashSet<>(tags.getValue());
            currentSelection.add(tagDaSelezionare);
            tags.setValue(currentSelection);
        });
    	
    }
    
    
    
    // --- METODI HELPER PER IL MIXING ---

    /**
     * Aggiunge i campi di identità (Nome e Path)
     */
    protected void addIdentityFields(HasComponents container) {
        container.add(nome, path,dimensione);
    }

    /**
     * Aggiunge i campi di classificazione (Tags e Descrizione)
     */
    protected void addClassificationFields(HasComponents container) {
        container.add(tags, descrizione, note);
        // Resizing: diciamo al layout che questi devono occupare 2 colonne
        if (container instanceof FormLayout) {
            ((FormLayout) container).setColspan(tags, 2);
            ((FormLayout) container).setColspan(descrizione, 2);
            ((FormLayout) container).setColspan(note, 2);
        }
    }

    /**
     * Aggiunge i campi di stato (visualizzazioni,Rating, Preferito, Backup)
     */
    protected void addStatusFields(HasComponents container) {
        container.add(visualizzazioni,rating, preferito,backup);
    }
    
    /**
     * Aggiunge i campi di tipo date (dataArchiviazione,lastUpdate, lastView)
     */
    protected void addDateFields(HasComponents container) {
        container.add(dataArchiviazione,lastUpdate,lastView);
    }
    
}