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
        FormLayout specificLayout = new FormLayout();
        
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
	      anno.setWidth("120px"); anno.setStep(1);

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
	        
//	        VerticalLayout firstRow=new VerticalLayout();
//	        firstRow.getStyle().set("padding", "0").set("margin", "0");
//	        firstRow.setPadding(false);
//	        firstRow.getStyle().set("gap", "8px"); // Riduce lo spazio tra righe
//	        
//	        
//	        firstRow.add(row1,row2,row3);
//	        
//	        HorizontalLayout firstPart = new HorizontalLayout();
//	        firstPart.setAlignItems(FlexComponent.Alignment.STRETCH);  
//	        coverPreview.getStyle().set("margin-left", "auto"); 
//	        firstPart.add(firstRow,coverPreview);
	        
	        HorizontalLayout row4 = new HorizontalLayout();
	        row4.setAlignItems(FlexComponent.Alignment.BASELINE);  
	        row4.setSpacing(true);
	        row4.setWidth("90%");
	        estensione.setWidth("108px");
	        row4.add(path,estensione,dimensione);
	        

	        VerticalLayout formLayout=new VerticalLayout();
	        
	        formLayout.setSpacing(true);
	        formLayout.getStyle().set("padding", "0").set("margin", "0");
	        formLayout.setPadding(false);
	        formLayout.getStyle().set("gap", "8px"); // Riduce lo spazio tra righe
	        
	        
	        formLayout.add(row1,row2,row3,row4); 
	        
//	        formLayout.add(firstPart,row4); 
	        
//	        addInfoFile(formLayout); // Aggiungo il blocco delle info del file (Comuni)
 
	        addDateFields(formLayout); // Aggiungo il blocco delle date (Comuni)
	        
	        addStatusFields(formLayout); // Aggiungo il blocco dello stato (Comuni)
	        
//	        // Poi aggiungo il blocco dei Tag e Descrizione (Comuni)
	        addClassificationFields(formLayout);
	          
	          
	        HorizontalLayout content = new HorizontalLayout();
	        content.setAlignItems(FlexComponent.Alignment.START);  
	        coverPreview.getStyle().set("margin-left", "auto"); 
	        content.add(formLayout,coverPreview);
	        
	        
	        // 4. Aggiungiamo il layout finito alla View
//	        specificLayout.add(formLayout);    
	        specificLayout.add(content);    
	          
//	           // 3. RICHIAMIAMO I METODI HELPER NELL'ORDINE VOLUTO

	          add(specificLayout);

        coverPreview.setAlt("Cover preview"); coverPreview.setWidth("220px");
        coverPath.addValueChangeListener(e -> coverPreview.setSrc(Optional.ofNullable(e.getValue()).orElse("")));

        // --- Popolamento delle ComboBox PRIMA che il metodo setParameter venga eseguito.---
        estensione.setItems(FormatoAudio.values());
        estensione.setItemLabelGenerator(FormatoAudio::getEstensione);
        
        binderFiled();
          
    }

	private void binderFiled() {
			
		 // Binder DA VEDERE per l'obbligatorietà dei campi
//        binder.forField(duration).withConverter(
//                v -> v == null ? null : v.intValue(), v -> v == null ? null : v.doubleValue(), "Numero non valido")
//            .bind(AudioDto::getDurationSeconds, AudioDto::setDurationSeconds);
//        binder.forField(sizeBytes).withConverter(
//                v -> v == null ? 0L : v.longValue(), v -> v == null ? null : v.doubleValue(), "Numero non valido")
//            .bind(AudioDto::getSizeBytes, AudioDto::setSizeBytes);
//        binder.forField(formato).asRequired("Formato obbligatorio").bind(AudioDto::getFormato, AudioDto::setFormato);
//        binder.forField(genere).bind(AudioDto::getGenere, AudioDto::setGenere);
//        binder.forField(anno).withConverter(
//                v -> v == null ? null : v.intValue(), v -> v == null ? null : v.doubleValue(), "Anno non valido")
//            .bind(AudioDto::getAnnoPubblicazione, AudioDto::setAnnoPubblicazione);
        
        // obbligatorietà dei campi
		 binder.forField(anno).asRequired("Campo obbligatorio").bind("anno");
		binder.forField(autore).asRequired("Campo obbligatorio").bind("autore");

//		binder.forField(duration).asRequired("Campo obbligatorio").bind("duration");
		
		binder.forField(duration)
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
	    }).asRequired("Campo obbligatorio").bind("duration");
		
		binder.forField(estensione).asRequired("Campo obbligatorio").bind("estensione");
		
		binder.forField(coverPath).withValidator(
    	        value -> value != null && value.matches(".*\\\\.*"),
    	        "Il percorso del file deve contenere almeno un carattere '\\'"
    	    ).asRequired("Campo obbligatorio").bind("coverPath");
        
		/**Questo binda AUTOMATICAMENTE solo i campi non ancora bindati 
        I campi comuni (nome, data, tags, ecc.) sono già stati "occupati" dalla classe base, quindi non vengono sovrascritti*/
		binder.bindInstanceFields(this);
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
