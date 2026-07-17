package it.catalog.ui.common;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.shared.Tooltip;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;

import it.catalog.service.dto.AudioDto;
import it.catalog.service.dto.TagDto;
import it.catalog.service.interfaces.SearchService;
import it.catalog.ui.utility.AppConverters.DoubleToLong;
import it.catalog.ui.utility.BooleanImageToggle;
import it.catalog.ui.utility.RatingStarsField;
import it.catalog.utility.DynamicI18nProvider;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.Result;
import com.vaadin.flow.data.binder.ValueContext;
import com.vaadin.flow.data.converter.Converter;

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
    private TextField estensione = new TextField("Estensione");
    protected TextArea descrizione = new TextArea("Descrizione");
    protected TextField dimensione = new TextField("Dimensione (KB)");
//    protected Checkbox preferito = new Checkbox("Preferito");
//    protected Checkbox backup = new Checkbox("Backup effettuato");
//    protected Checkbox cancelled = new Checkbox("Cancellato");
//    protected IntegerField rating = new IntegerField("Rating (1-5)");
    
    protected BooleanImageToggle backup = new BooleanImageToggle(
    	    "images/backup-colored.png",
    	    "images/backup-gray.png"
    	);

    protected BooleanImageToggle preferito = new BooleanImageToggle(
    		"images/like-colored.png",
    		"images/like-gray.png"
    		);
    protected RatingStarsField rating = new RatingStarsField();

    protected NumberField visualizzazioni = new NumberField("Visual");
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
    
    
 // 2. Definisci la zona di riferimento (es. fuso orario del server o dell'utente)
    ZoneId fusoOrario = ZoneId.systemDefault(); // o ZoneId.of("Europe/Rome")

    public AbstractCommonFileForm(String title,S service, Class<T> beanType) {
        super(title,service, beanType);
        
        // Setting Title
        
        
     // 1. Configurazione specifica per i Tags
    	settingTags(service);
    	
    	Tooltip.forComponent(preferito).setText("Like");
    	Tooltip.forComponent(backup).setText("Backup");
    	Tooltip.forComponent(rating).setText("Valuta");
    	
    	dataArchiviazione.setI18n(DynamicI18nProvider.getI18nForCurrentUser());
    	lastUpdate.setI18n(DynamicI18nProvider.getI18nForCurrentUser());
    	lastView.setDatePickerI18n(DynamicI18nProvider.getI18nForCurrentUser());
    	
        path.setWidth("90%");
        estensione.setWidth("85px");
        dimensione.setWidth("120px");
        
    	// Imposta l'attributo HTML 'title' sul tag <div> generato
//    	backup.getElement().setAttribute("title", "BackUp");       
        
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

    	binder.forField(nome).asRequired("Campo obbligatorio").bind("nome");
//    	binder.forField(path).asRequired("Campo obbligatorio").bind("path");
    	binder.forField(path).withValidator(
    	        value -> value != null && value.matches(".*\\\\.*"),
    	        "Il percorso del file deve contenere almeno un carattere '\\'"
    	    ).asRequired("Campo obbligatorio").bind("path");
//    	binder.forField(dimensione).asRequired("Campo obbligatorio").bind("dimensione");
    	
    	if (!AudioDto.class.isAssignableFrom(beanType)) 
    	/** Se il tipo del bean NON è un tipo compatibile con AudioDto, allora il campo estensione 
    	 * viene gestito in modo centralizzato su questa classe, viceversa se ho a che fare con un AudioDto
    	 * estensione viene gestito direttamente sul suo form perchè non è un TextField ma una ComboBox*/
    	    binder.forField(estensione).asRequired("Campo obbligatorio").bind("estensione");
    	
    	
    	binder.forField(dimensione)
        .withConverter(new Converter<String, Double>() {
            @Override
            public Result<Double> convertToModel(String value, ValueContext context) {
                // Se il campo è vuoto, restituiamo null (o 0.0 a seconda delle tue esigenze)
                if (value == null || value.trim().isEmpty()) {
                    return Result.ok(null); 
                }
                
                // Sostituiamo la virgola con il punto per rendere il parsing universale
                String normalizedValue = value.replace(",", ".").trim();
                
                try {
                    double parsed = Double.parseDouble(normalizedValue);
                    return Result.ok(parsed);
                } catch (NumberFormatException e) {
                    // Messaggio chiaro se l'utente scrive lettere o simboli strani
                    return Result.error("Inserisci un numero decimale valido (es. 1.5 o 1,5)");
                }
            }

            @Override
            public String convertToPresentation(Double value, ValueContext context) {
                // Come mostrare il Double nel campo di testo quando carichi il DTO
                if (value == null) {
                    return "";
                }
                // Mostriamo il valore formattato con il punto (o puoi usare la virgola se preferisci)
                return String.valueOf(value);
            }
        }).asRequired("Campo obbligatorio").bind("dimensione");
    	
    	
    	
    	
    	binder.forField(visualizzazioni)
        .withConverter(new DoubleToLong())
        .bind("visualizzazioni");
        
    	
	       // alternative with image
			
			  binder.forField(preferito) .withConverter( checked -> checked != null &&
			  checked, value -> value != null && value ) .bind("preferito");
			  
			  binder.forField(rating) //.withNullRepresentation(0.0) // Se il valore è null, usa 0.0 
			  .withConverter( value -> value, // da NumberField → DTO value
					  value -> value == null ? null : value // da DTO → NumberField 
					  ) .bind("rating");
			  
			  binder.forField(backup) .withConverter( checked -> checked != null &&
			  checked, value -> value != null && value ) .bind("backup");

        // Gestione DateTimePicker con conversione tra LocalDateTime e Instant, accettando null
//    	binder.forField(lastUpdate).withConverter(new InstantToLocalDate())
//    	.bind("lastUpdate");
    	
			  binder.forField(lastUpdate)
		      .withConverter(
		          // Da UI (LocalDate) a DTO (Instant) -> fissa l'inizio giornata
		          localDate -> localDate == null ? null : localDate.atStartOfDay(fusoOrario).toInstant(),
		          
		          // Da DTO (Instant) a UI (LocalDate)
		          instant -> instant == null ? null : instant.atZone(fusoOrario).toLocalDate()
		      ).bind("lastUpdate");
			  
	
//    	binder.forField(dataArchiviazione).withConverter(new InstantToLocalDate())
//    	.asRequired("Campo obbligatorio").bind("dataArchiviazione");
    	
    	
  	  binder.forField(dataArchiviazione)
      .withConverter(
          // Da UI (LocalDate) a DTO (Instant) -> fissa l'inizio giornata
          localDate -> localDate == null ? null : localDate.atStartOfDay(fusoOrario).toInstant(),
          
          // Da DTO (Instant) a UI (LocalDate)
          instant -> instant == null ? null : instant.atZone(fusoOrario).toLocalDate()
      ).asRequired("Campo obbligatorio").bind("dataArchiviazione");
    	
//    	binder.forField(lastView).withConverter(new InstantToLocalDateTime())
//    	.bind("lastView");
    	
    	binder.forField(lastView)
    	.withConverter(
    			// Da UI (LocalDateTime) a DTO (Instant)
    			localDateTime -> localDateTime == null ? null : localDateTime.atZone(fusoOrario).toInstant(),
    					
    					// Da DTO (Instant) a UI (LocalDateTime)
    					instant -> instant == null ? null : LocalDateTime.ofInstant(instant, fusoOrario)
    			).bind("lastView");  
    	
    	
    	

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
        tags.setPlaceholder("Seleziona o scrivi nuovi tag...");;
        tags.setWidth("62%");
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
        
        tags.addClassName("auto-width-combo");

    	
    }
    
    
    
    // --- METODI HELPER PER IL MIXING ---

    /**
     * Aggiunge i campi di identità (Nome e Path)
     */
//    protected void addIdentityFields(HasComponents container) {
////    	dimensione.getStyle().set("width", "120px");
//    	dimensione.setWidth("120px");
//    	container.add(path,dimensione);
//    }

    /**
     * Aggiunge i campi di classificazione (Tags e Descrizione)
     */
    protected void addClassificationFields(HasComponents container) {
        
//    	VerticalLayout rowLayout=new VerticalLayout();
//		rowLayout.setSpacing(true);
//		rowLayout.getStyle().set("padding", "0").set("margin", "0");
//		rowLayout.setPadding(false);
//		rowLayout.getStyle().set("gap", "8px"); // Riduce lo spazio tra righe
//		    	
//         rowLayout.add(tags, descrizione, note);
//    	container.add(rowLayout);
//    	note.setWidthFull();
//    	descrizione.setWidthFull();

    	descrizione.setWidth("62%");
    	note.setWidth("62%");
    	
    	container.add( descrizione,note,tags);

    	
        // Resizing: diciamo al layout che questi devono occupare 2 colonne
//        if (container instanceof FormLayout) {
//            ((FormLayout) container).setColspan(tags, 2);
//            ((FormLayout) container).setColspan(descrizione, 2);
//            ((FormLayout) container).setColspan(note, 2);
//        }
    }

    /**
     * Aggiunge i campi di stato (visualizzazioni,Rating, Preferito)
     */
    protected void addStatusFields(HasComponents container) {
       
    	 HorizontalLayout rowLayout = new HorizontalLayout();
         rowLayout.setAlignItems(FlexComponent.Alignment.BASELINE);  
         rowLayout.setSpacing(true);
         rowLayout.setWidth("70%");
//         rowLayout.setWidthFull();
         
         visualizzazioni.setWidth("90px");
     	// Spinge se stesso a destra occupando lo spazio vuoto a sinistra
    	 rating.getStyle().set("margin-left", "auto"); 
         rowLayout.add(lastView,visualizzazioni,rating,preferito);
         
         container.add(rowLayout);
    }

    
    /**
     * Aggiunge il blocco dei campi che danno le informazioni sul file di riferimento
     */
    protected void addInfoFile(HasComponents container) {
    	
    	HorizontalLayout rowLayout = new HorizontalLayout();
    	rowLayout.setAlignItems(FlexComponent.Alignment.BASELINE);  
    	rowLayout.setSpacing(true);
    	rowLayout.setWidth("90%");
    	
         path.setWidth("90%");
         estensione.setWidth("85px");
         dimensione.setWidth("120px");         
    	
    	rowLayout.add(path,estensione,dimensione);
    	
    	container.add(rowLayout);
    }

    /**
     * Aggiunge i campi di tipo date (dataArchiviazione,lastUpdate, lastView)
     */
    protected void addDateFields(HasComponents container) {
        
        HorizontalLayout rowLayout = new HorizontalLayout();
        rowLayout.setAlignItems(FlexComponent.Alignment.BASELINE);  
        rowLayout.setSpacing(true);
        rowLayout.setWidth("70%");
        // Spinge se stesso a destra occupando lo spazio vuoto a sinistra
        backup.getStyle().set("margin-left", "auto"); 
        rowLayout.add(dataArchiviazione,lastUpdate,backup);
    	
    	container.add(rowLayout);
    }
    
}