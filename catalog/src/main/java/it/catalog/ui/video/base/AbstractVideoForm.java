package it.catalog.ui.video.base;


import java.time.LocalDateTime;
import java.time.ZoneId;

import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.shared.Tooltip;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.theme.lumo.LumoUtility.Gap;

import it.catalog.common.enums.CategorieVideo;
import it.catalog.service.dto.VideoDto;
import it.catalog.service.interfaces.IVideoService;
import it.catalog.ui.utility.BooleanImageToggle;
import it.catalog.ui.utility.RatingStarsField;

//T deve essere VideoDto o una sua sottoclasse
public abstract class AbstractVideoForm<T extends VideoDto> extends FormLayout {

	protected final TextField titolo = new TextField("Titolo");
	protected final ComboBox<CategorieVideo> categoria = new ComboBox<>("Categoria");
	protected final TextField percorsoFile = new TextField("Path");
	protected final DateTimePicker dataArchiviazione = new DateTimePicker("Data Archiviazione");
	protected final DateTimePicker lastViewField = new DateTimePicker("Ultima Visualizzazione");
	protected final DateTimePicker lastUpdateField = new DateTimePicker("Aggiornamento");
	protected final IntegerField durataMin = new IntegerField("Durata (minuti)");
	protected final IntegerField visualizzazioni = new IntegerField("Visual");
	protected final Checkbox cancelled = new Checkbox("Cancelled");
	protected final TextArea note = new TextArea("Note");
	    
	    private final BooleanImageToggle backup = new BooleanImageToggle(
	    	    "images/backup-colored.png",
	    	    "images/backup-gray.png"
	    	);

	    private final BooleanImageToggle preferito = new BooleanImageToggle(
	    		"images/like-colored.png",
	    		"images/like-gray.png"
	    		);
	    private final RatingStarsField rating = new RatingStarsField();
	    Anchor imageLink = new Anchor();
//	    private final Button salvaButton = new Button("Salva");
//	    private final Button backButton = new Button("← Indietro");
	    protected final FormLayout formLayout; // Contenitore per i campi allineati
	    protected final VerticalLayout mainLayout;
	    protected final VerticalLayout partialFormLout;
    protected final Binder<T> binder;
    
    // Riferimento al service generico
    protected final IVideoService videoService; 
    
//     private boolean viewMode = true;

    public AbstractVideoForm(Class<T> beanType,IVideoService videoService, String pageTitle) {
    	 this.videoService = videoService;
    	this.binder = new Binder<>(beanType);
    	this.formLayout = new FormLayout();
    	this.partialFormLout=new VerticalLayout();
    	this.mainLayout=new VerticalLayout();
//        add(new H1(pageTitle));
//          add(titlelayout);
    	
     // 2. Inizializziamo i campi comuni
        setComponent(pageTitle);
        
//        binder.bind(nome, VideoDto::getNome, VideoDto::setNome);
//        binder.bind(path, VideoDto::getPath, VideoDto::setPath);
     // 3. Binding dei campi comuni
        configureBindings();
    
    }
    

    private void setComponent(String pageTitle) {
    
    	HorizontalLayout level1 = new HorizontalLayout(titolo, rating);
        level1.setAlignItems(FlexComponent.Alignment.BASELINE); // Allinea verticalmente al testo
        level1.setSpacing(true);
    	
        titolo.getStyle().set("margin", "0");
        durataMin.getStyle().set("margin", "0");
        durataMin.setWidth("120px");
        
        visualizzazioni.setWidth("80px");
        
        HorizontalLayout level2 = new HorizontalLayout();
        level2.setAlignItems(FlexComponent.Alignment.BASELINE); // Allinea verticalmente al testo
        level2.setSpacing(true);
        categoria.setWidth("150px");
        categoria.setItems(CategorieVideo.values());
    	categoria.setItemLabelGenerator(CategorieVideo::getLabel);
        preferito.setWidth("40px");
        visualizzazioni.setWidth("60px");
        
        level2.add(categoria,durataMin,visualizzazioni,preferito);
        
        HorizontalLayout row1 = new HorizontalLayout();
        row1.setAlignItems(FlexComponent.Alignment.CENTER); // Allinea verticalmente al testo
        row1.setSpacing(true);
        
        partialFormLout.setAlignItems(FlexComponent.Alignment.BASELINE); // Allinea verticalmente al testo
        partialFormLout.setSpacing(true);
        partialFormLout.getStyle().set("padding", "0").set("margin", "0");
        partialFormLout.setPadding(false);
        partialFormLout.getStyle().set("gap", "8px"); // Riduce lo spazio tra righe
        partialFormLout.add(level1,level2,lastViewField);
        
        row1.add(partialFormLout,imageLink);
        
        percorsoFile.setWidth("70%");
        
        dataArchiviazione.setWidth("150px");

        HorizontalLayout level4 = new HorizontalLayout( backup,dataArchiviazione);
        level4.setAlignItems(FlexComponent.Alignment.BASELINE); // Allinea verticalmente al testo
        level4.setSpacing(true);
        
        
        HorizontalLayout level5 = new HorizontalLayout(note, cancelled);
        level5.setAlignItems(FlexComponent.Alignment.BASELINE); // Allinea verticalmente al testo
        level5.setSpacing(true);

        VerticalLayout formLayout=new VerticalLayout();

        formLayout.setSpacing(true);
        formLayout.getStyle().set("padding", "0").set("margin", "0");
        formLayout.setPadding(false);
        formLayout.getStyle().set("gap", "8px"); // Riduce lo spazio tra righe
        
        formLayout.add(row1,percorsoFile, level4,level5);
        
        // imagine del play
        Image image = new Image("images/video-player.jpg", "Play Video");
        image.setWidth("200px");
        image.getStyle().set("cursor", "pointer");
        
     // --- LOGICA DEL CLICK ---
        image.addClickListener(event -> gestisciClickImmagine());       
        
     // Wrappo l'immagine in un Anchor
//      imageLink.setHref(binder.getBean().getPercorsoFile() !=null ? binder.getBean().getPercorsoFile() : ""); // DA AGGIUNGERE
        // da rivedere perchè binder.getBean() arriva con null
        imageLink.add(image);
        
        // Aggiunta ToolTip
        Tooltip tooltip = Tooltip.forComponent(imageLink);
        tooltip.setText("Play video");
        
        Tooltip tooltipBackup = Tooltip.forComponent(backup);
        tooltipBackup.setText("Backup");
		
		   Tooltip tooltipLike = Tooltip.forComponent(preferito);
        tooltipLike.setText("Mi Piace");
        
        HorizontalLayout layoutRow = new HorizontalLayout(); // layout tra form e immagine
        layoutRow.setWidthFull();
        layoutRow.addClassName(Gap.MEDIUM);
        layoutRow.setWidth("100%");
        layoutRow.getStyle().set("flex-grow", "1");
        layoutRow.setAlignItems(Alignment.START);
        layoutRow.setJustifyContentMode(JustifyContentMode.CENTER);
        
        VerticalLayout layoutColumn1 = new VerticalLayout(); // layout di padding per spostare l'immagine
        layoutColumn1.setWidthFull();
        layoutColumn1.setFlexGrow(1.0, layoutColumn1);
        layoutColumn1.setWidth("100%");
        layoutColumn1.setHeight("120px");
        
        layoutRow.add(formLayout,layoutColumn1);
     // 1. Aggiungiamo il TITOLO dinamico in cima
  	  H1 titolo = new H1(pageTitle);

        VerticalLayout titlelayout = new VerticalLayout();
        titlelayout.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);
        titlelayout.add(titolo);
//        add(new VerticalLayout(titlelayout,layoutRow));
        mainLayout.add(titlelayout,layoutRow);
        add(mainLayout);
//        add(nome, path); // Aggiungi altri campi comuni qui

    	
    }
    
    private void configureBindings() {
        // TextField: accetta stringa vuota
        binder.forField(titolo)
            .withNullRepresentation("")
            .asRequired("Il titolo è obbligatorio")
            .withValidator(name -> name != null && name.length() >= 3, "Minimo 3 caratteri")
            .bind(VideoDto::getTitolo,VideoDto::setTitolo);
        
        binder.forField(percorsoFile)
        .withNullRepresentation("")
        .asRequired("Il path è obbligatorio")
        .bind(VideoDto::getPercorsoFile,VideoDto::setPercorsoFile);
        
        binder.forField(durataMin)
        .withConverter(
        		value -> value, // da IntegerField → DTO
        		value -> value == null ? null : value // da DTO → IntegerField
        		)
        .asRequired("Durata video è obbligatorio")
        .withValidator(n -> n != null && n > 0, "Deve essere maggiore di 0")
        .bind(VideoDto::getDurataMin, VideoDto::setDurataMin);
        
        binder.forField(visualizzazioni)
        .withConverter(
        		value -> value, // da IntegerField → DTO
        		value -> value == null ? null : value // da DTO → IntegerField
        		)
        .bind(VideoDto::getVisualizzazioni, VideoDto::setVisualizzazioni);

        // ComboBox: accetta null
        binder.forField(categoria)
        .asRequired("Categoria obbligatoria")
        .bind(VideoDto::getCategoria, VideoDto::setCategoria);

        // alternative with image
        binder.forField(preferito)
        .withConverter(
            checked -> checked != null && checked,
            value -> value != null && value
        )
        .bind(VideoDto::getPreferito, VideoDto::setPreferito);
        
        
        binder.forField(backup)
        .bind(VideoDto::getBackup, VideoDto::setBackup);
        
   
        binder.forField(cancelled)
        .withConverter(
        		checked -> checked != null && checked,
        		value -> value != null && value
        		)
        .bind(VideoDto::isCancelled, VideoDto::setCancelled);
        
        // DatePicker: accetta null
        binder.forField(dataArchiviazione).withConverter(
				// UI → DTO
				localDateTime -> localDateTime == null ? null
						: localDateTime.atZone(ZoneId.systemDefault()).toInstant(),

				// DTO → UI
				instant -> instant == null ? null : LocalDateTime.ofInstant(instant, ZoneId.systemDefault()),

				"Data non valida").bind(VideoDto::getDataArchiviazione, VideoDto::setDataArchiviazione);
        
        binder.forField(lastViewField).withConverter(
				// UI → DTO
				localDateTime -> localDateTime == null ? null
						: localDateTime.atZone(ZoneId.systemDefault()).toInstant(),

				// DTO → UI
				instant -> instant == null ? null : LocalDateTime.ofInstant(instant, ZoneId.systemDefault()),

				"Data non valida").bind(VideoDto::getLastView, VideoDto::setLastView);
        
        
        binder.forField(lastUpdateField).withConverter(
				// UI → DTO
				localDateTime -> localDateTime == null ? null
						: localDateTime.atZone(ZoneId.systemDefault()).toInstant(),

				// DTO → UI
				instant -> instant == null ? null : LocalDateTime.ofInstant(instant, ZoneId.systemDefault()),

				"Data non valida").bind(VideoDto::getLastUpdate, VideoDto::setLastUpdate);


        // NumberField: non accetta null → serve converter
        binder.forField(rating) 
        //.withNullRepresentation(0.0) // Se il valore è null, usa 0.0
            .withConverter(
                value -> value, // da NumberField → DTO
                value -> value == null ? null : value // da DTO → NumberField
            )
            .bind(VideoDto::getRating, VideoDto::setRating);

        // TextArea: accetta stringa vuota
        binder.forField(note)
            .withNullRepresentation("")
            .bind(VideoDto::getNote, VideoDto::setNote);
        
    }

       	
    private void gestisciClickImmagine() {
        T bean = binder.getBean();
        // Controllo se il bean esiste e se ha un ID (è salvato sul db)
        if (bean != null && bean.getId() != null) {
            
        	bean.setVisualizzazioni(bean.getVisualizzazioni() + 1);
        	//bean.setLastView(null); DA VERIFICARE
            // 1. Chiamata al Service Generico
            videoService.save(bean);
            
            // Aggiorna il form
            visualizzazioni.setValue(bean.getVisualizzazioni());
            //lastViewField.setValue(bean.getLastView());  DA VERIFICARE
            
        } else {
            Notification.show("Salva il video prima di poter cliccare l'immagine!");
        }
    }
    
    public Binder<T> getBinder() { return binder; }
    public void setBean(T bean) { binder.setBean(bean); 
	 // Wrappo l'immagine in un Anchor
    // Questo è stato aggiunto qui poichè l’istanza del bean deve essere PRIMA assegnata esplicitamente con binder.setBean(bean); 
    // Senza questa assegnazione, il binder non ha un bean da restituire e quindi getBean() ritorna null.
    imageLink.setHref(binder.getBean().getPercorsoFile() !=null ? binder.getBean().getPercorsoFile() : "");

    }

    // Metodo centrale per gestire la visualizzazione
    public void setReadOnlyMode(boolean readOnly) {
        // Il binder rende read-only tutti i campi collegati
        binder.setReadOnly(readOnly);
        
        // Se hai campi non collegati al binder, gestiscili qui
        // es. un pulsante custom o un campo calcolato
    }
}