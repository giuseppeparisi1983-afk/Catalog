package it.catalog.ui.audio;

import java.util.Optional;

import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Result;
import com.vaadin.flow.data.binder.ValueContext;
import com.vaadin.flow.data.converter.Converter;
import com.vaadin.flow.data.validator.IntegerRangeValidator;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import it.catalog.common.enums.FormatoAudio;
import it.catalog.service.dto.AudioDto;
import it.catalog.service.dto.search.DtoFilter;
import it.catalog.service.interfaces.SearchService;
import it.catalog.ui.common.AbstractCommonFileForm;
import it.catalog.ui.common.MainLayout;

@Route(value = "audio-form", layout = MainLayout.class)
@PageTitle("Audio - Form")
public class Form extends AbstractCommonFileForm<AudioDto, SearchService<AudioDto, DtoFilter>> {

    private final TextField duration = new TextField("Durata");
    private final ComboBox<FormatoAudio> estensione = new ComboBox<>("Estensione");
    private final TextField coverPath = new TextField("Cover path");
    private final Image coverPreview = new Image();

    private final TextField genere = new TextField("Genere");
    private final TextField autore = new TextField("Autore");
    private final TextField album = new TextField("Album");
    private final IntegerField anno = new IntegerField("Anno");
   
    public Form(SearchService<AudioDto, DtoFilter> service) {
    	 super("Modulo Audio",service, AudioDto.class);

        // Da sistemare con gli enumeration
//        mimeType.setItems("audio/mpeg", "audio/flac", "audio/wav", "audio/ogg");
//        formato.setItems("MP3","FLAC","WAV");
        // DA VEDERE
//        formato.setTooltipGenerator(f -> switch (f) {
//            case "MP3" -> "Lossy, compatibilità ampia";
//            case "FLAC" -> "Lossless, alta qualità";
//            case "WAV" -> "PCM non compresso, editing";
//            default -> "";
//        });

        // Layout per campi specifici
//        FormLayout specificLayout = new FormLayout();
        
        HorizontalLayout row1 = new HorizontalLayout();

	      row1.setAlignItems(FlexComponent.Alignment.BASELINE); // Allinea verticalmente al testo;
	      
	      row1.setSpacing(true);
	      //row1.setWidth("100%");
	
	      
	      nome.setWidth("490px");
	      autore.setWidth("280px");
	      duration.setWidth("120px"); 
//	      duration.setStep(1); duration.setMin(0);
	      
	      row1.add(nome,autore,duration); 
	        
	      anno.setWidth("69px");
	      anno.setWidth("120px"); 
	      // Limiti anche lato componente (UI)
	      anno.setMin(1900);
	      anno.setMax(2080);
	      anno.setStep(1);
	      anno.setI18n(new IntegerField.IntegerFieldI18n()
	    		    .setMinErrorMessage("L'anno deve essere maggiore o uguale a 1900")
	    		    .setMaxErrorMessage("L'anno non può superare il 2080"));

	      HorizontalLayout row2 = new HorizontalLayout();
	      row2.setAlignItems(FlexComponent.Alignment.BASELINE);  
	      row2.setSpacing(true);
	      //row2.setWidth("70%");
	   
	      row2.add(genere,album,anno);
	      
	      HorizontalLayout row3 = new HorizontalLayout();
	        row3.setAlignItems(FlexComponent.Alignment.BASELINE);  
	        row3.setSpacing(true);
	        row3.setWidth("90%");
	        
	        coverPath.setWidth("90%");
//	        mimeType.setWidth("135px");
//	        row3.add(coverPath,coverPreview,album);
//	        row3.add(coverPath,mimeType);
	        row3.add(coverPath);
	        
	        HorizontalLayout row4 = new HorizontalLayout();
	        row4.setAlignItems(FlexComponent.Alignment.BASELINE);  
	        row4.setSpacing(true);
	        row4.setWidth("90%");
	        estensione.setWidth("108px");
	        row4.add(path,estensione,dimensione);

	        
	        
	        VerticalLayout topFormRows=new VerticalLayout();
	        topFormRows.getStyle().set("padding", "0").set("margin", "0");
	        topFormRows.setPadding(false);
	        topFormRows.setSpacing(true);
	        topFormRows.getStyle().set("gap", "8px"); // Riduce lo spazio tra righe
	        
	        
	        topFormRows.add(row2,row3,row4);

	        HorizontalLayout topSection = new HorizontalLayout();
	        topSection.setWidthFull();
	        topSection.setSpacing(true);
	        topSection.setAlignItems(FlexComponent.Alignment.START); 
	    
	        settingCover(); // Imposta le proprietà dell'immagine coverPreview
	        
	        // Aggiungiamo prima le righe del form e poi l'immagine
	        topSection.add(topFormRows,coverPreview);
	        
	     // Diamo tutto lo spazio orizzontale rimanente al form, lasciando l'immagine a dimensione fissa
	        topSection.expand(topFormRows);

	        VerticalLayout formLayout=new VerticalLayout();
	        
	        formLayout.setPadding(false);
	        formLayout.setSpacing(true);
//	        formLayout.getStyle().set("padding", "0").set("margin", "0");
	        formLayout.getStyle().set("gap", "8px"); // Riduce lo spazio tra righe
	        
	        formLayout.add(row1,topSection);
//	        formLayout.add(row1,row2,row3,row4); 
	        
//	        formLayout.add(topSection,row4); 
	        
//	        addInfoFile(formLayout); // Aggiungo il blocco delle info del file (Comuni)
 
	        addDateFields(formLayout); // Aggiungo il blocco delle date (Comuni)
	        
	        addStatusFields(formLayout); // Aggiungo il blocco dello stato (Comuni)
	        
//	        // Poi aggiungo il blocco Descrizione, note e Tag (Comuni)
	        addClassificationFields(formLayout);
	        
	        
//	        specificLayout.add(formLayout);    

	          add(formLayout);

        /** l'istruzione che segue serve a creare un'anteprima dell'immagine in tempo reale (Real-time Preview).
         *  Ogni volta che l'utente modifica il percorso nel campo di testo coverPath, l'immagine coverPreview si aggiorna 
         *  all'istante a schermo, senza dover premere "Salva" o ricaricare la pagina.
         *  Così ad esempio se nel campo coverPath c'è una stringa (es. [https://sito.it/foto.jpg](https://sito.it/foto.jpg) 
         *  o images/cover.jpg), usa quel valore come src dell'imagine.
         * */
	     // L'istruzione coverPreview.setSrc(...) imposta esattamente l'attributo src del tag <img> generato da Vaadin.	  
	     // Optional.ofNullable(e.getValue()).orElse("") è un controllo di sicurezza Null-Safe: Se il campo viene svuotato o diventa null, invece di far piantare l'applicazione con una NullPointerException, restituisce una stringa vuota "".     
        coverPath.addValueChangeListener(e -> coverPreview.setSrc(Optional.ofNullable(e.getValue()).orElse("")));

        // --- Popolamento delle ComboBox PRIMA che il metodo setParameter venga eseguito.---
        estensione.setItems(FormatoAudio.values());
        estensione.setItemLabelGenerator(FormatoAudio::getEstensione);
        
        binderSpecificFiled();
          
    }

	private void binderSpecificFiled() {

		binder.forField(anno)
        .asRequired("Campo obbligatorio")
        .bind("anno");
		 
		binder.forField(autore).asRequired("Campo obbligatorio").bind("autore");
		binder.forField(duration)
		// 1. Controlla prima la Stringa del TextField (se è vuota si ferma qui)
	    .asRequired("Campo obbligatorio") 
	    // 2. Se c'è del testo, lo passa al convertitore
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
	    }).bind("duration");
		
		binder.forField(estensione).asRequired("Campo obbligatorio").bind("estensione");
		
		binder.forField(coverPath).withValidator(
				value -> value == null || value.isBlank() || value.contains("\\") || value.contains("//"), // questo non è un campo obbligatorio, ma se l'utente scrive qualcosa, deve contenere almeno un carattere '\' o '/'
    	        "Il percorso del file deve contenere almeno un carattere '\\'" // Questo messaggio viene mostrato nel caso la validazione fallisce ovvero se tutte le condizioni della lambda restituiscono false
    	    ).bind("coverPath");
        
		/**Questo binda AUTOMATICAMENTE solo i campi non ancora bindati 
        I campi comuni (nome, data, tags, ecc.) sono già stati "occupati" dalla classe base, quindi non vengono sovrascritti*/
		binder.bindInstanceFields(this);
	}
	

	// setting the image coverPreview properties
	private void settingCover() {
        coverPreview.setWidth("160px");   // Dimensione fissa per evitare il crollo se vuota
        coverPreview.setHeight("200px");
        coverPreview.getStyle()
            .set("object-fit", "cover")                           // Mantiene le proporzioni
            .set("border-radius", "var(--lumo-border-radius-m)")   // Stile pulito Vaadin
            .set("background-color", "var(--lumo-contrast-5pct)"); // Sfondo grigio placeholder se vuota

        // Se l'immagine è vuota e NON vuoi mostrare neanche il rettangolo grigio:
        // 1. Fondamentale: dice a Vaadin di aggiornare il valore a OGNI singolo carattere digitato
        coverPath.setValueChangeMode(ValueChangeMode.EAGER);

        // 2. Gestiamo il cambio di valore nell'evento (scatta ogni volta che il testo cambia)
        coverPath.addValueChangeListener(e -> {
            String valore = e.getValue(); // Usiamo la variabile dell'evento!
            boolean haValore = valore != null && !valore.isBlank();
            
            coverPreview.setVisible(haValore);
            if (haValore) {
                coverPreview.setSrc(valore);
            }
        });
        coverPreview.setAlt("Cover preview");      
//        coverPreview.getStyle().set("margin-left", "auto"); 
	
	}
	
	
    // Implementazione dei metodi della logica
    @Override protected AudioDto loadBean(Long id) { 
    	
    	AudioDto dto = null;
    	
    	  try {
//          	binder.bindInstanceFields(this); // associa automaticamente i campi del form alle proprietà del DTO basandosi sul nome.
    		  dto =service.findById(id);
    		  if (dto != null) {
    			  binder.readBean(dto); // Popola automaticamente i campi. IMPORTANTE: prima si definiscono i binding, poi si chiama readBean().               
    	  }
    	  } catch (NumberFormatException ex) {

          }
    	
    	  return dto; 
    
    }
    
    @Override protected void saveBean(AudioDto bean) {service.save(bean); }
    @Override protected AudioDto createNewBean() { return new AudioDto(); }
    @Override protected void navigateBack() { getUI().ifPresent(ui -> ui.navigate("audio")); }
    

}
