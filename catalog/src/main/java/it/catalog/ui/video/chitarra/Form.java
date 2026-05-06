package it.catalog.ui.video.chitarra;


import java.util.List;
import java.util.Map;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.OptionalParameter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import it.catalog.common.enums.CategorieVideo;
import it.catalog.common.enums.Difficolta;
import it.catalog.service.dto.GuitarDto;
import it.catalog.service.dto.VideoDto;
import it.catalog.service.interfaces.IGuitarService;
import it.catalog.service.interfaces.IVideoService;
import it.catalog.ui.common.MainLayout;
import it.catalog.ui.video.base.AbstractVideoForm;
import lombok.extern.slf4j.Slf4j;

@Route(value = "chitarra-form", layout = MainLayout.class)
@PageTitle("Video Chitarra - Form")
@Slf4j
public class Form extends AbstractVideoForm<VideoDto>  implements BeforeEnterObserver,HasUrlParameter<Integer>{

   
	// Variabili sentinella
    private boolean isReadOnly = false;
    private Long currentId = null; // Se null = INSERT, se valorizzato = EDIT/VIEW
//    
//    private final IntegerField idField = new IntegerField();
    private final ComboBox<Difficolta> difficolta = new ComboBox<>("Difficoltà");
    private final TextField autore = new TextField("Autore");
    private final Checkbox visto = new Checkbox("Visto");
    private final Checkbox todo = new Checkbox("Da fare");

    private final Button salva = new Button("Salva");
    private final Button annulla = new Button("Annulla");

    private final IGuitarService service;

    
    public Form(IGuitarService chitarraService, IVideoService videoService) {
   
    	// 1. Passiamo il service e la classe specifica al costruttore del padre (SUPER) per il Binder
        super(VideoDto.class,videoService,"Modulo Video Chitarra");
    	
    	this.service = chitarraService;
      
        // 2. Inizializziamo i campi aggiuntivi
        setComponent();
        
     // 3. Binding dei campi comuni
        configureBindings();
    }
	
    private void  setComponent() {
    	
    	setSizeFull();
    	setHeightFull();
    	
    	// 1. Impostiamo SOLO Chitarra come item possibile (così il binder non si lamenta)
        categoria.setItems(CategorieVideo.guitar);

        // 2. Blocchiamo il campo in sola lettura
        categoria.setReadOnly(true);
    	
    	  HorizontalLayout level2 = new HorizontalLayout();
          level2.setAlignItems(FlexComponent.Alignment.BASELINE);
          level2.setSpacing(true);
          
          level2.add(todo,visto);

          difficolta.setItems(Difficolta.values());
          difficolta.setItemLabelGenerator(Difficolta::getLabel);
          difficolta.setWidth("290px");
//          difficolta.setWidth("min-content");
          //difficolta.setFlexGrow(0);       // Non si allarga mai
          difficolta.setPlaceholder("Seleziona livello di difficoltà...");
          // Definizione dei listening per i button
          salva.addClickListener(e -> saveVideo());      
          annulla.addClickListener(e -> getUI().ifPresent(ui -> ui.navigate(Index.class)));
          partialFormLout.addComponentAtIndex(0, new HorizontalLayout(annulla, salva));

          // Aggiungiamo i nuovi campi al FormLayout che abbiamo ereditato (protected)
          // Così appaiono insieme agli altri campi (Nome, Path) in modo ordinato (vanno sotto quelli comuni)
          HorizontalLayout addFiled = new HorizontalLayout();
          addFiled.setAlignItems(FlexComponent.Alignment.BASELINE); // Allinea verticalmente al testo
          addFiled.setSpacing(true);
          addFiled.add(difficolta,autore,  level2);
          mainLayout.add(addFiled); 
    }
    
    private void configureBindings() {
		
    	// ComboBox: difficolta null
//		binder.forField(difficolta)
//		.asRequired("Imposta il livello di difficolta")
//				.bind(GuitarDto::getDifficolta, GuitarDto::setDifficolta);
//
//		// TextField: Non accetta stringa vuota
//		binder.forField(autore)
//		.withNullRepresentation("")
//		.asRequired("L'autore è obbligatorio")
//				.withValidator(name -> name.length() >= 3, "Minimo 3 caratteri")
//				.bind(GuitarDto::getAutore, GuitarDto::setAutore);
//
//		binder.forField(visto)
//				.withConverter(checked -> checked != null && checked, value -> value != null && value)
//				.bind(GuitarDto::getVisto, GuitarDto::setVisto);
//
//		binder.forField(todo)
//				.withConverter(checked -> checked != null && checked, value -> value != null && value)
//				.bind(GuitarDto::getTodo, GuitarDto::setTodo);
	}
    
   
      
  
    // ============================================================
    // PARTE 1: LETTURA DAL DB (Load)
    // ============================================================
    // Questo metodo viene chiamato AUTOMATICAMENTE da Vaadin quando entri nella pagina.
    // Esempio URL: .../video/form/5 (dove 5 è il parametro)

	@Override
    public void setParameter(BeforeEvent event, @OptionalParameter Integer id) {
     
		GuitarDto guitar=null;	
		if (id != null) {
            // CASO MODIFICA: Cerco nel DB per idVideo
			// getGuitarById(id) mi restituisce un oggetto vuoto nel caso di Not found
			guitar= service.getGuitarById(id);      			
        } else {
            // CASO NUOVO INSERIMENTO: Creo un DTO vuoto
        	 guitar=new GuitarDto();
        }
		
//		if(guitar.getCategoria() == null)
//			guitar.setCategoria(CategorieVideo.guitar);         	 // FORZIAMO LA CATEGORIA NEL DTO
//		
//		setBean(guitar);// Popolo il form (e il binder)

       
    }
	
    // ============================================================
    // PARTE 2: SCRITTURA SU DB (Save)
   // ============================================================
    private void saveVideo() {
        if (binder.validate().isOk()) {
            // 2. Recupero il DTO aggiornato dal Binder
             // (Contiene i dati digitati dall'utente + l'ID se era una modifica)
        	//GuitarDto dtoDaSalvare = binder.getBean();

             try {
                 // 3. Chiamata al Service per scrivere su DB
            	// QUI USIAMO IL SERVICE SPECIFICO
            	 //service.save(dtoDaSalvare);

                 Notification.show("Video salvato correttamente!",3000, Notification.Position.TOP_CENTER);
                 getUI().ifPresent(ui -> ui.navigate(Index.class)); // Torna alla lista eventualmente
             } catch (Exception e) {
                log.error("Errore durante il salvataggio.",e);
            	 Notification.show("Errore durante il salvataggio: " + e.getMessage(),3000, Notification.Position.TOP_CENTER);
             }
         }
        else
            Notification.show("Errore di validazione",3000, Notification.Position.TOP_CENTER);
    }
    
	
	// 2. Gestione Query Parameters (URL: /video-chitarra/chitarra-form/123?view=true)
    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        Map<String, List<String>> params = event.getLocation().getQueryParameters().getParameters();
        
        // Controllo la variabile sentinella "view"
        if (params.containsKey("view") && "true".equals(params.get("view").get(0))) {
            this.isReadOnly = true;
        } else {
            this.isReadOnly = false;
        }

        applyMode();
    }
    
	 private void applyMode() {
	        // Applica il readonly al form padre
	        setReadOnlyMode(isReadOnly);
	        
	        // Nascondi il bottone salva se siamo in visualizzazione
	        salva.setVisible(!isReadOnly);
	        
	        if (isReadOnly) {
	        	annulla.setText("Indietro");
	        }
	    }
    
}
