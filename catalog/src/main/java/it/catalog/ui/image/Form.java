package it.catalog.ui.image;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteParameters;
import com.vaadin.flow.theme.lumo.LumoUtility.Gap;

import it.catalog.service.dto.ImageDto;
import it.catalog.service.dto.TagDto;
import it.catalog.service.impl.ImageFileServiceImpl;
import it.catalog.ui.common.MainLayout;

@Route(value = "images-form/:id/:view", layout = MainLayout.class)
@PageTitle("Immagini - Form")
public class Form extends Composite<VerticalLayout> implements BeforeEnterObserver {

    private final ImageFileServiceImpl service;
    private final Binder<ImageDto> binder = new Binder<>(ImageDto.class);

    private final TextField title = new TextField();
    private final TextArea description = new TextArea();
    private final TextField filename = new TextField();
    private TextField path = new TextField();
    private final ComboBox<String> mimeType = new ComboBox<>();
    private final NumberField sizeBytes = new NumberField();
    private final ComboBox<String> formato = new ComboBox<>();
    private final ComboBox<String> tipoFile = new ComboBox<>();
    private final Checkbox preferito = new Checkbox("Preferito");
    private Checkbox chkCancellato = new Checkbox("Cancellato");
    private final Checkbox backup = new Checkbox();
    private final NumberField rating = new NumberField();
    private final NumberField visualizzazioni = new NumberField();
    private final DateTimePicker dataArchiviazione = new DateTimePicker();
    private final DateTimePicker lastView = new DateTimePicker();
    private DateTimePicker lastUpdate = new DateTimePicker();
    private final TextArea note = new TextArea();
    private final MultiSelectComboBox<TagDto> tagsSelector = new MultiSelectComboBox<>();

    private final Button save = new Button("Salva");
    private final Anchor cancel = new Anchor("images", "Indietro");
    
//    private Instant toInstant(LocalDateTime ldt) { return ldt == null ? null : ldt.atZone(ZoneId.systemDefault()).toInstant(); }
//    private LocalDateTime fromInstant(Instant i) { return i == null ? null : LocalDateTime.ofInstant(i, ZoneId.systemDefault()); }

	
	 private ImageDto dto=new ImageDto();
	 
	 private boolean viewMode = true;
    
    

    public Form(ImageFileServiceImpl service) {
        this.service = service; 

        settingForm();
        // DA SISTEMARE
        mimeType.setItems("image/jpeg", "image/png", "image/gif", "image/webp", "image/tiff");
        mimeType.setWidth("220px");
        sizeBytes.setWidth("150px"); sizeBytes.setStep(1); sizeBytes.setMin(0);

        formato.setItems("JPEG","RAW","TIFF","PNG");
     // DA VEDERE
//        formato.setTooltipGenerator(f -> switch (f) {
//            case "JPEG" -> "Fotografia digitale, formato compresso";
//            case "RAW" -> "Fotografia professionale, editing avanzato";
//            case "TIFF" -> "Archiviazione immagini alta qualità";
//            case "PNG" -> "Grafica web e trasparenze";
//            default -> "";
//        });
        tipoFile.setItems("Fotografia","Sfondo","Illustrazione");

        title.setWidth("340px"); filename.setWidth("320px");
        description.setWidth("560px"); description.setMaxLength(1000);
        rating.setWidth("100px"); visualizzazioni.setWidth("120px");
        note.setWidth("400px");

        save.setText("Salva");
        save.setWidth("min-content");
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        save.addClickListener(e -> save());

        //setResponsiveSteps(new ResponsiveStep("0", 1), new ResponsiveStep("700px", 2));
//        add(new HorizontalLayout(save, cancel),title, filename, mimeType, formato, tipoFile, sizeBytes,
//            rating, visualizzazioni, preferito, backup, tagsSelector, description,
//            dataArchiviazione, lastView, note
//            );
        
        configTags();
        configureBindings();
        
    }

    
    private void settingForm() {
    	
    	VerticalLayout main = new VerticalLayout();
    	main.setWidthFull();
    	main.setWidth("100%");
    	main.getStyle().set("flex-grow", "1");
        getContent().setFlexGrow(1.0, main);
        getContent().add(main);
    	
        H1 h1 = new H1();
        h1.setText("Modulo Imagine");
        h1.setWidth("max-content");
        main.setAlignSelf(FlexComponent.Alignment.CENTER, h1);
        main.add(h1);
        
        HorizontalLayout layoutRow = new HorizontalLayout(); 
    	layoutRow.setWidth("100%");
    	layoutRow.getStyle().set("flex-grow", "1");
    	layoutRow.setAlignItems(Alignment.CENTER);
    	layoutRow.setJustifyContentMode(JustifyContentMode.START);
        layoutRow.add(cancel);
        layoutRow.add(save);

         main.add(layoutRow);
         
         HorizontalLayout layoutRow2 = new HorizontalLayout();
         layoutRow2.addClassName(Gap.SMALL);
         layoutRow2.setWidth("100%");
         layoutRow2.getStyle().set("flex-grow", "1");
         layoutRow2.setAlignItems(Alignment.CENTER);
         layoutRow2.setJustifyContentMode(JustifyContentMode.START);
         //layoutRow2.setAlignSelf(FlexComponent.Alignment.START, title);
         title.setLabel("Nome immagine");
         title.setWidth("250px");
         formato.setLabel("Formato file");
         formato.setWidth("250px");
         tipoFile.setLabel("Tipo file");
         tipoFile.setWidth("250px");
         
         layoutRow2.add(title,formato,tipoFile);
         
         main.add(layoutRow2);
         
         HorizontalLayout layoutRow3 = new HorizontalLayout();
         
         layoutRow3.addClassName(Gap.SMALL);
         layoutRow3.setWidth("100%");
         layoutRow3.getStyle().set("flex-grow", "1");
         
         layoutRow3.setAlignItems(Alignment.CENTER);
         layoutRow3.setJustifyContentMode(JustifyContentMode.START);
         //layoutRow3.setAlignSelf(FlexComponent.Alignment.START, filename);
         
         filename.setLabel("Nome file");
         filename.setWidth("250px");
         mimeType.setLabel("MIME type");
         mimeType.setWidth("250px");
         sizeBytes.setLabel("Dimensione (bytes)");
         sizeBytes.setWidth("250px");
         
         layoutRow3.add(filename,sizeBytes,mimeType);
         
         main.add(layoutRow3);
         path.setLabel("Percorso file");
         path.setWidth("250px");
         main.add(path);
         
         HorizontalLayout layoutRow4 = new HorizontalLayout();
         layoutRow4.addClassName(Gap.SMALL);
         layoutRow4.setWidth("100%");
         layoutRow4.getStyle().set("flex-grow", "1");
         layoutRow4.setAlignItems(Alignment.CENTER);
         layoutRow4.setJustifyContentMode(JustifyContentMode.START);
         
	         rating.setLabel("Rating");
	         rating.setWidth("250px");
	         visualizzazioni.setLabel("Visualizzazioni");
         	visualizzazioni.setWidth("250px");
         	preferito.setLabel("Preferito");
         	description.setLabel("Descrizione");
         	description.setWidth("400px");
         	layoutRow4.add(rating, visualizzazioni, preferito,description);
         	
         	main.add(layoutRow4);
         	
         	HorizontalLayout layoutRow5 = new HorizontalLayout();
         		
         	layoutRow5.addClassName(Gap.SMALL);
         	layoutRow5.setWidth("100%");
         	layoutRow5.getStyle().set("flex-grow", "1");
         	layoutRow5.setAlignItems(Alignment.CENTER);
         	layoutRow5.setJustifyContentMode(JustifyContentMode.START);
         	
         	tagsSelector.setLabel("Tags");
         	tagsSelector.setWidth("250px");
         	backup.setLabel("Backup");
         	chkCancellato.setLabel("Cancellato");
         		
         	layoutRow5.add(tagsSelector, backup, chkCancellato);
         	
         	main.add(layoutRow5);
         	
         	HorizontalLayout layoutRow6 = new HorizontalLayout();	
         	layoutRow6.addClassName(Gap.SMALL);
         	layoutRow6.setWidth("100%");
         	layoutRow6.getStyle().set("flex-grow", "1");
         	layoutRow6.setAlignItems(Alignment.CENTER);
         	layoutRow6.setJustifyContentMode(JustifyContentMode.START);
         	
         	dataArchiviazione.setLabel("Data archiviazione");
         	dataArchiviazione.setWidth("250px");
         	lastView.setLabel("Data ultima visualizzazione");
         	lastView.setWidth("250px");
         	lastUpdate.setLabel("Data ultimo aggiornamento");
         	lastUpdate.setWidth("250px");
         	
         	layoutRow6.add(dataArchiviazione, lastView, lastUpdate);
         	
         	main.add(layoutRow6);
         	
         	note.setLabel("Note");
         	note.setWidth("400px");
         	main.add(note);
         	
         	
         	
         	
         	
         	
         	
         	
         	
    }
    
    
    
    private void save() {
    	
    	/**if (binder.writeBeanIfValid(dto)) {
    	
    	 * bean.setTags(new ArrayList<>(tags.getSelectedItems()));
           DateTimePicker conversione
           bean.setDataArchiviazione(toInstant(dataArchiviazione.getValue()));
           bean.setLastView(toInstant(dataUltimaVisualizzazione.getValue()));
    	 * 
    	 * */
    	binder.writeBeanIfValid(dto);
    	dto.setTags(new ArrayList<>(tagsSelector.getSelectedItems()));
//    	dto.setDataArchiviazione(toInstant(dataArchiviazione.getValue()));
//    	dto.setDataUltimaVisualizzazione(toInstant(dataUltimaVisualizzazione.getValue()));

    	service.save(dto);
        Notification.show("Elemento salvato!", 3000, Notification.Position.TOP_CENTER);
        getUI().ifPresent(ui -> ui.navigate(Index.class)); // Torna alla lista eventualmente
//    }
//    	else 
//            Notification.show("Correggi i campi obbligatori");
    
    }
    
    
     
    @Override
    public void beforeEnter(BeforeEnterEvent event) {
  
        RouteParameters routeParams = event.getRouteParameters();
        Optional<String> idParam = routeParams.get("id");
        viewMode = routeParams.get("view").map(v -> v.equalsIgnoreCase("true")).orElse(true);

        if (idParam.isPresent()) {
            try {
//            	binder.bindInstanceFields(this); // associa automaticamente i campi del form alle proprietà del DTO basandosi sul nome.
            	this.dto =service.findById(Long.parseLong(idParam.get()));
            	bindFieldsFromObject(this.dto);
                updateFieldState(); // aggiorna i campi e visibilità del pulsante
            	binder.readBean(dto); // Popola automaticamente i campi. IMPORTANTE: prima si definiscono i binding, poi si chiama readBean().               
            } catch (NumberFormatException ex) {
            	event.forwardTo("not-found");
            }
        } else
            event.forwardTo("not-found");        
        
    }
    
    private void configTags() {
		
		 // Carica tutti i tag disponibili
       List<TagDto> allTags = new ArrayList<>(service.getAllTags());
		 	tagsSelector.setItems(allTags);
	        tagsSelector.setWidth("360px");
	        tagsSelector.setPlaceholder("Tags");
	        tagsSelector.setItemLabelGenerator(TagDto::getNomeTag);// Mostra solo il nome del tag
	        ListDataProvider<TagDto> dataProvider = new ListDataProvider<>(allTags);
	        // Se vuoi permettere all’utente di aggiungere tags non presenti, puoi usare:
	        tagsSelector.setAllowCustomValue(true);
	        tagsSelector.addCustomValueSetListener(e -> {
	            String nomeTag = e.getDetail();
	            TagDto nuovoTag=new TagDto(nomeTag,"Image");
	          
	            
	            // Aggiungi il nuovo tag agli items senza ricreare il provider
	            if (!allTags.contains(nuovoTag)) {
	            	allTags.add(nuovoTag);
	                dataProvider.refreshAll(); // aggiorna la vista
	            }

	         // Mantieni la selezione precedente e aggiungi il nuovo tag
	            Set<TagDto> currentSelection = new HashSet<>(tagsSelector.getValue());
	            currentSelection.add(nuovoTag);
	            tagsSelector.setValue(currentSelection);
	        });
		
		
	}
    
    
	  private void updateFieldState() {
	    	 boolean editable = !viewMode;

	    	 title.setReadOnly(viewMode);
	    	 description.setReadOnly(viewMode);
	    	 filename.setReadOnly(viewMode);
	    	 mimeType.setReadOnly(viewMode);
	    	 sizeBytes.setReadOnly(viewMode);
	    	 formato.setReadOnly(viewMode);
	    	 tipoFile.setReadOnly(viewMode);
	    	 chkCancellato.setReadOnly(viewMode);
	    	 
	    	 preferito.setReadOnly(viewMode);
	    	 path.setReadOnly(viewMode);
	    	 rating.setReadOnly(viewMode);
	    	 visualizzazioni.setReadOnly(viewMode);
	    	 
	    	 lastView.setReadOnly(viewMode);
	    	 dataArchiviazione.setReadOnly(viewMode);
	    	 lastUpdate.setReadOnly(viewMode);

	    	 backup.setReadOnly(viewMode);
	    	 note.setReadOnly(viewMode);
	    	 tagsSelector.setReadOnly(viewMode);
	        
	        // visibile solo se view=false
	    	//save.setVisible(editable); 
	        //path.setVisible(editable);
	        //modificaButton.setVisible(viewMode);
	    }
    
    
	  private void configureBindings() {


		// Binder bindings
	        binder.forField(title).withNullRepresentation("").
	        asRequired("Campo obbligatorio").withValidator(name -> name.length() >= 3, "Minimo 3 caratteri")
	        .bind(ImageDto::getNome, ImageDto::setNome);
	        
	        
	        binder.forField(description).bind(ImageDto::getDescription, ImageDto::setDescription);
	        binder.forField(filename).asRequired("Campo obbligatorio")
	        .withValidator(name -> name.length() >= 3, "Minimo 3 caratteri")
	        .bind(ImageDto::getFilename, ImageDto::setFilename);
	        
	        binder.forField(mimeType).asRequired("MIME obbligatorio").bind(ImageDto::getMimeType, ImageDto::setMimeType);
	        binder.forField(sizeBytes).withConverter(
	                v -> v == null ? 0L : v.longValue(),
	                v -> v == null ? null : v.doubleValue(),
	                "Numero non valido").bind(ImageDto::getSizeBytes, ImageDto::setSizeBytes);
	        binder.forField(formato).asRequired("Formato obbligatorio").bind(ImageDto::getFormato, ImageDto::setFormato);
	        binder.forField(tipoFile).asRequired("Tipo file obbligatorio").bind(ImageDto::getTipoFile, ImageDto::setTipoFile);
	        binder.forField(preferito).bind(ImageDto::isPreferito, ImageDto::setPreferito);
	        binder.forField(backup).bind(ImageDto::isBackup, ImageDto::setBackup);
	        binder.forField(chkCancellato).bind(ImageDto::isCancelled, ImageDto::setCancelled);

	        binder.forField(path).asRequired("Campo obbligatorio")
	        .withValidator(name -> name.contains("/"), "Path non valido")
	        .bind(ImageDto::getPath, ImageDto::setPath);
	        
	        binder.forField(rating).withConverter(
	                v -> v == null ? null : v.intValue(),
	                v -> v == null ? null : v.doubleValue(),
	                "Valore non valido").bind(ImageDto::getRating, ImageDto::setRating);
	        binder.forField(visualizzazioni).withConverter(
	                v -> v == null ? 0L : v.longValue(),
	                v -> v == null ? null : v.doubleValue(), "Valore non valido")
	            .bind(ImageDto::getVisualizzazioni, ImageDto::setVisualizzazioni);
	        
	        // Gestione DateTimePicker con conversione tra LocalDateTime e Instant, accettando null
	    	binder.forField(lastUpdate).withConverter(
					// UI → DTO
					localDateTime -> localDateTime == null ? null
							: localDateTime.atZone(ZoneId.systemDefault()).toInstant(),

					// DTO → UI
					instant -> instant == null ? null : LocalDateTime.ofInstant(instant, ZoneId.systemDefault()),

					"Data non valida").bind(ImageDto::getLastUpdate, ImageDto::setLastUpdate);
	    	
    	
	    	binder.forField(dataArchiviazione).withConverter(
	    						// UI → DTO
	    						localDateTime -> localDateTime == null ? null
	    								: localDateTime.atZone(ZoneId.systemDefault()).toInstant(),

	    						// DTO → UI
	    						instant -> instant == null ? null : LocalDateTime.ofInstant(instant, ZoneId.systemDefault()),

	    						"Data non valida").bind(ImageDto::getDataArchiviazione, ImageDto::setDataArchiviazione);

	    	binder.forField(lastView).withConverter(
	    						// UI → DTO
	    						localDateTime -> localDateTime == null ? null
	    								: localDateTime.atZone(ZoneId.systemDefault()).toInstant(),

	    						// DTO → UI
	    						instant -> instant == null ? null : LocalDateTime.ofInstant(instant, ZoneId.systemDefault()),

	    						"Data non valida").bind(ImageDto::getLastView, ImageDto::setLastView);
	    				
	    				
	        binder.forField(note).bind(ImageDto::getNote, ImageDto::setNote);
			
	  }
	  
	  
	  private void bindFieldsFromObject(ImageDto dto) {
			// 1. Impostiamo i valori possibili (tutte le costanti dell'Enum)
//		  		mimeType.setItems(MimeType.values());		  
			   // 2. Diciamo a Vaadin quale testo mostrare per ogni opzione.
		        // Così ad esempio invece di mostrare "ARCHIVIATO", userà il metodo getLabel() e si vedrà "archiviato"
//				mimeType.setItemLabelGenerator(MimeType::getLabel);
		        
//		        formato.setItems(Formato.values());
//		    	formato.setItemLabelGenerator(Formato::getLabel);
//		    	 imageLink.setHref(dto.getPercorsoFile() !=null ? dto.getPercorsoFile() : "");
//		        tipoFile.setItems(TipoFile.values());
//		    	tipoFile.setItemLabelGenerator(TipoFile::getLabel);
		  		tagsSelector.setValue(dto.getTags() == null ? Set.of() : new HashSet<>(dto.getTags()));
		        binder.readBean(dto); // gestisce tutto in sicurezza
		    }
    
    
    
    
    
    
    
    

  }