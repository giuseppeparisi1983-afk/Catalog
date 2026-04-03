package it.catalog.ui.documenti;

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
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.IntegerField;
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

import it.catalog.common.enums.StatiDocumento;
import it.catalog.common.enums.TipoDocumento;
import it.catalog.service.dto.DocumentoDto;
import it.catalog.service.dto.TagDto;
import it.catalog.service.impl.DocumentoServiceImpl;
import it.catalog.ui.common.MainLayout;

@Route(value="documents-form/:id/:view", layout = MainLayout.class)
@PageTitle("Documento - Form")
/**
 * Visualizzazione e Salvataggio parte tags KO
 * */
//@Menu(order = 1, icon = LineAwesomeIconUrl.PENCIL_RULER_SOLID)
public class Form extends Composite<VerticalLayout> implements BeforeEnterObserver{
//public class Form extends Composite<VerticalLayout> implements BeforeEnterObserver,HasUrlParameter<Long> {

	private TextField nome = new TextField();
	private TextField estensione = new TextField();
	private NumberField dimensione = new NumberField();
	private NumberField visualizzazioni = new NumberField();
	private TextField lingua = new TextField();
	private IntegerField versione = new IntegerField();
	private IntegerField rating = new IntegerField();
	//@PropertyId("categoria")
	private ComboBox<TipoDocumento> categoria = new ComboBox<>("Categoria");
	private Checkbox chkPreferito = new Checkbox("Preferito");
	private Checkbox chkCancellato = new Checkbox("Cancellato");
	private TextField autore = new TextField();
	private DateTimePicker lastView = new DateTimePicker();
	private TextArea descrizione = new TextArea();
	private TextField path = new TextField();
	private TextField origine = new TextField();
	private DateTimePicker dataCreazione = new DateTimePicker();
	private DateTimePicker lastUpdate = new DateTimePicker();
	private TextArea note = new TextArea();
	private MultiSelectComboBox<TagDto> tagsSelector = new MultiSelectComboBox<>();
	
//	@PropertyId("stato")
	private  Select<StatiDocumento> stato = new Select<>(); // da eliminare lo stato deve essere gestito
	
    private Button indietro = new Button();
    private Button salva = new Button();
    
	private DocumentoServiceImpl service;
	 private final Binder<DocumentoDto> binder = new Binder<>(DocumentoDto.class);
	
	 private DocumentoDto dto=new DocumentoDto();
	 
	 private boolean viewMode = true;

	 public Form(DocumentoServiceImpl service) {
        
    	this.service=service;
  
    	VerticalLayout layoutColumn2 = new VerticalLayout();
        H1 h1 = new H1();
        HorizontalLayout layoutRow = new HorizontalLayout();

        HorizontalLayout layoutRow2 = new HorizontalLayout();
        HorizontalLayout layoutRow3 = new HorizontalLayout();
        HorizontalLayout layoutRow4 = new HorizontalLayout();
        HorizontalLayout layoutRow5 = new HorizontalLayout();
        HorizontalLayout layoutRow6 = new HorizontalLayout();
        getContent().setWidth("100%");
        getContent().getStyle().set("flex-grow", "1");
        layoutColumn2.setWidthFull();
        getContent().setFlexGrow(1.0, layoutColumn2);
        layoutColumn2.setWidth("100%");
        layoutColumn2.getStyle().set("flex-grow", "1");
        h1.setText("Modulo documenti");
        layoutColumn2.setAlignSelf(FlexComponent.Alignment.CENTER, h1);
        h1.setWidth("max-content");
        layoutRow.setWidthFull();
        layoutColumn2.setFlexGrow(1.0, layoutRow);
        layoutRow.addClassName(Gap.MEDIUM);
        layoutRow.setWidth("100%");
        layoutRow.getStyle().set("flex-grow", "1");
        indietro.setText("Indietro");
        indietro.setWidth("min-content");
        indietro.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        indietro.addClickListener(e -> getUI().ifPresent(ui -> ui.navigate(Index.class)));
        salva.setText("Salva");
        salva.setWidth("min-content");
        salva.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        // listener per il torna indietro
        salva.addClickListener(e -> save());
        layoutRow2.setWidthFull();
        layoutColumn2.setFlexGrow(1.0, layoutRow2);
        layoutRow2.addClassName(Gap.SMALL);
        layoutRow2.setWidth("100%");
        layoutRow2.getStyle().set("flex-grow", "1");
        layoutRow2.setAlignItems(Alignment.CENTER);
        layoutRow2.setJustifyContentMode(JustifyContentMode.START);
        nome.setLabel("Nome documento");
        layoutRow2.setAlignSelf(FlexComponent.Alignment.START, nome);
//        nome.setWidth("min-content");
        nome.setWidth("250px");
        layoutRow2.setAlignSelf(FlexComponent.Alignment.START, estensione);
        estensione.setLabel("Estensione");
        estensione.setWidth("90px");
        estensione.setHeight("70px");
        layoutRow2.setAlignSelf(FlexComponent.Alignment.START, dimensione);
        dimensione.setLabel("Dimensione");
        dimensione.setWidth("90px");
        lingua.setLabel("Lingua");
        layoutRow2.setAlignSelf(FlexComponent.Alignment.END, lingua);
        lingua.setWidth("70px");
        lingua.setHeight("70px");
        versione.setLabel("Versione");
        layoutRow2.setAlignSelf(FlexComponent.Alignment.START, versione);
        versione.setWidth("70px");
        layoutRow3.setWidthFull();
        layoutColumn2.setFlexGrow(1.0, layoutRow3);
        layoutRow3.addClassName(Gap.SMALL);
        layoutRow3.setWidth("100%");
        layoutRow3.getStyle().set("flex-grow", "1");
        layoutRow3.setAlignItems(Alignment.END);
        layoutRow3.setJustifyContentMode(JustifyContentMode.START);
        categoria.setLabel("Categoria");
        categoria.setWidth("min-content");
//        categoria.setItems(Categorie.values());
        categoria.setPlaceholder("Seleziona categoria");
        categoria.setClearButtonVisible(true);
        autore.setLabel("Autore");
        autore.setWidth("min-content");
        lastView.setLabel("Ultima Visualizzazione");
        lastView.setWidth("min-content");
        lastUpdate.setLabel("Ultimo Aggirnamento");
        lastUpdate.setWidth("min-content");
        descrizione.setLabel("Descrizione");
        descrizione.setWidth("100%");
        path.setLabel("Path");
        path.setWidth("650px");
        layoutRow4.setWidthFull();
        layoutColumn2.setFlexGrow(1.0, layoutRow4);
        layoutRow4.addClassName(Gap.MEDIUM);
        layoutRow4.setWidth("100%");
        layoutRow4.getStyle().set("flex-grow", "1");
        dataCreazione.setLabel("Data creazione");
        dataCreazione.setWidth("min-content");
        origine.setLabel("Origine");
        origine.setWidth("min-content");
        layoutRow5.setWidthFull();
        layoutColumn2.setFlexGrow(1.0, layoutRow5);
        layoutRow5.addClassName(Gap.MEDIUM);
        layoutRow5.setWidth("100%");
        layoutRow5.getStyle().set("flex-grow", "1");
        layoutRow6.setWidthFull();
        layoutColumn2.setFlexGrow(1.0, layoutRow6);
        layoutRow6.addClassName(Gap.MEDIUM);
        layoutRow6.setWidth("100%");
        layoutRow6.getStyle().set("flex-grow", "1");
        note.setLabel("Note");
        note.setWidth("100%");
        stato.setLabel("Stato");
        stato.setWidth("min-content");
//        stato.setItems(Stati.values());
        stato.setPlaceholder("Seleziona stato");
//        stato.setClearButtonVisible(true);
        getContent().add(layoutColumn2);
        layoutColumn2.add(h1);
        layoutColumn2.add(layoutRow);
        layoutRow.add(indietro);
        layoutRow.add(salva);
        layoutColumn2.add(layoutRow2);
        layoutRow2.add(nome);
        layoutRow2.add(estensione);
        layoutRow2.add(dimensione);
        layoutRow2.add(lingua);
        layoutRow2.add(versione);
        configTags();
        layoutRow2.add(tagsSelector);
        layoutColumn2.add(layoutRow3);
        layoutRow3.add(categoria,stato);
        layoutColumn2.add(autore,lastView);
        layoutRow4.add(path,origine);
        layoutColumn2.add(layoutRow4);
        layoutColumn2.add(descrizione);
        layoutColumn2.add(layoutRow5);
        layoutRow5.add(dataCreazione,lastUpdate);
        layoutColumn2.add(layoutRow5);
        layoutRow6.add(note);
        layoutColumn2.add(layoutRow6);
        configureBindings();
    }
    
 // listener per il pulsante Salva
    private void save() {
    	binder.writeBeanIfValid(dto);
    	service.save(dto);
        Notification.show("Elemento salvato!", 3000, Notification.Position.TOP_CENTER);
        getUI().ifPresent(ui -> ui.navigate(Index.class)); // Torna alla lista eventualmente
    }

    
	@Override
    public void beforeEnter(BeforeEnterEvent event) {
    	RouteParameters routeParams = event.getRouteParameters();
        Optional<String> idParam = routeParams.get("id");

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
        } else {
            event.forwardTo("not-found");
        }
		 
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
	            TagDto nuovoTag=new TagDto(nomeTag,"Documento");
	          
	            
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

	    	 nome.setReadOnly(viewMode);
	    	 estensione.setReadOnly(viewMode);
	    	 dimensione.setReadOnly(viewMode);
	    	 lingua.setReadOnly(viewMode);
	    	 versione.setReadOnly(viewMode);
	    	 categoria.setReadOnly(viewMode);
	    	 autore.setReadOnly(viewMode);
	    	 lastView.setReadOnly(viewMode);
	    	 descrizione.setReadOnly(viewMode);
	    	 path.setReadOnly(viewMode);
	    	 origine.setReadOnly(viewMode);
	    	 dataCreazione.setReadOnly(viewMode);
	    	 lastUpdate.setReadOnly(viewMode);
	    	 note.setReadOnly(viewMode);
	    	 stato.setReadOnly(viewMode);
	        
	        // visibile solo se view=false
	        salva.setVisible(editable); 
	        path.setVisible(editable);
	        //modificaButton.setVisible(viewMode);
	    }
	
	  private void configureBindings() {
		  
			binder.forField(nome).withNullRepresentation("").asRequired("Campo obbligatorio")
					.withValidator(name -> name.length() >= 3, "Minimo 3 caratteri")
					.bind(DocumentoDto::getNome, DocumentoDto::setNome);

			binder.forField(estensione).asRequired("Campo obbligatorio").bind(DocumentoDto::getEstensione,
					DocumentoDto::setEstensione);

			/**
			 * i campi NumberField lavorano con i tipi Double, mentre su DocumentoDto
			 * dimensione è di tipo Long. Per questo serve una conversione manuale tramite
			 * Converter
			 */
			binder.forField(dimensione)
					.withConverter(doubleVal -> doubleVal == null ? null : doubleVal.longValue(),
							longVal -> longVal == null ? null : longVal.doubleValue(), "Valore non valido")
					.withValidator(n -> n != null && n > 0.0, "Deve essere maggiore di 0")
					.bind(DocumentoDto::getDimensione, DocumentoDto::setDimensione);

			binder.forField(visualizzazioni)
			.withConverter(doubleVal -> doubleVal == null ? null : doubleVal.longValue(),
					longVal -> longVal == null ? null : longVal.doubleValue(), "Valore non valido")
			.bind(DocumentoDto::getVisualizzazioni, DocumentoDto::setVisualizzazioni);
			
			
			binder.forField(lingua).withNullRepresentation("").bind(DocumentoDto::getLingua, DocumentoDto::setLingua);

			binder.forField(versione).withConverter(value -> value, // da IntegerField → DTO
					value -> value == null ? null : value // da DTO → IntegerField
			).withValidator(n -> n != null && n > 0, "Il valore deve essere maggiore di 0")
					.bind(DocumentoDto::getVersione, DocumentoDto::setVersione);

			binder.forField(categoria).asRequired("Campo obbligatorio").bind(DocumentoDto::getCategoria,
					DocumentoDto::setCategoria);

			binder.forField(autore).bind(DocumentoDto::getAutore, DocumentoDto::setAutore);

			binder.forField(rating).withConverter(value -> value, // da IntegerField → DTO
					value -> value == null ? null : value // da DTO → IntegerField
			).bind(DocumentoDto::getRating, DocumentoDto::setRating);
			
			binder.forField(chkPreferito)
		    .bind(DocumentoDto::isPreferito, DocumentoDto::setPreferito);
			
			// DateTimePicker: accetta null
			binder.forField(lastUpdate).withConverter(
					// UI → DTO
					localDateTime -> localDateTime == null ? null
							: localDateTime.atZone(ZoneId.systemDefault()).toInstant(),

					// DTO → UI
					instant -> instant == null ? null : LocalDateTime.ofInstant(instant, ZoneId.systemDefault()),

					"Data non valida").bind(DocumentoDto::getLastUpdate, DocumentoDto::setLastUpdate);

			binder.forField(descrizione).bind(DocumentoDto::getDescrizione, DocumentoDto::setDescrizione);

			binder.forField(path).asRequired("Campo obbligatorio")
					.withValidator(name -> name.length() >= 5, "Minimo 5 caratteri")
					.bind(DocumentoDto::getPath, DocumentoDto::setPath);

			binder.forField(origine).bind(DocumentoDto::getOrigine, DocumentoDto::setOrigine);

			// DateTimePicker: accetta null
			binder.forField(dataCreazione).withConverter(
					// UI → DTO
					localDateTime -> localDateTime == null ? null
							: localDateTime.atZone(ZoneId.systemDefault()).toInstant(),

					// DTO → UI
					instant -> instant == null ? null : LocalDateTime.ofInstant(instant, ZoneId.systemDefault()),

					"Data non valida").bind(DocumentoDto::getDataArchiviazione, DocumentoDto::setDataArchiviazione);

			binder.forField(lastView).withConverter(
					// UI → DTO
					localDateTime -> localDateTime == null ? null
							: localDateTime.atZone(ZoneId.systemDefault()).toInstant(),

					// DTO → UI
					instant -> instant == null ? null : LocalDateTime.ofInstant(instant, ZoneId.systemDefault()),

					"Data non valida").bind(DocumentoDto::getLastView, DocumentoDto::setLastView);

			binder.forField(note).bind(DocumentoDto::getNote, DocumentoDto::setNote);

			// ComboBox: accetta null
			binder.forField(stato).asRequired("Campo obbligatorio").bind(DocumentoDto::getStato,
					DocumentoDto::setStato);

	  }
	
	  private void bindFieldsFromObject(DocumentoDto dto) {
		// 1. Impostiamo i valori possibili (tutte le costanti dell'Enum)
		  stato.setItems( StatiDocumento.values());		  
		   // 2. Diciamo a Vaadin quale testo mostrare per ogni opzione.
	        // Così ad esempio invece di mostrare "ARCHIVIATO", userà il metodo getLabel() e si vedrà "archiviato"
	        stato.setItemLabelGenerator(StatiDocumento::getLabel);
//	    	 imageLink.setHref(dto.getPercorsoFile() !=null ? dto.getPercorsoFile() : "");
	        categoria.setItems(TipoDocumento.values());
	        categoria.setItemLabelGenerator(TipoDocumento::getLabel);
	    	
	        binder.readBean(dto); // gestisce tutto in sicurezza
	    }
	  
	  
	  
		/*
		 * @Override public void setParameter(BeforeEvent event, @OptionalParameter Long
		 * id) { Location location = event.getLocation(); QueryParameters queryParams =
		 * location.getQueryParameters(); String viewParam = queryParams.getParameters()
		 * .getOrDefault("view", List.of("true")) .get(0);
		 * 
		 * viewMode = Boolean.parseBoolean(viewParam); }
		 */
	
	
}
