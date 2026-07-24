package it.catalog.ui.audio;

import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import it.catalog.common.enums.AudioFormat;
import it.catalog.service.dto.AudioDto;
import it.catalog.service.dto.search.DtoFilter;
import it.catalog.service.interfaces.SearchService;
import it.catalog.ui.common.AbstractCommonFileForm;
import it.catalog.ui.common.MainLayout;
import it.catalog.ui.utility.AppConverters;

@Route(value = "audio-form", layout = MainLayout.class)
@PageTitle("Audio - Form")
public class Form extends AbstractCommonFileForm<AudioDto, SearchService<AudioDto, DtoFilter>> {

    private TextField duration = new TextField("Durata");
    private ComboBox<AudioFormat> estensione = new ComboBox<>("Formato");
    private TextField coverPath = new TextField("Cover URL");
    private Image coverPreview = new Image();
    private TextField genere = new TextField("Genere");
    private TextField autore = new TextField("Autore");
    private TextField album = new TextField("Album");
    private IntegerField anno = new IntegerField("Anno");

    public Form(SearchService<AudioDto, DtoFilter> service) {
        super("Modulo Audio", service, AudioDto.class);
        
        // 1. CONFIGURAZIONE ComboBox (Chiamiamo il padre)
        setupExtensionCombo(estensione, AudioFormat.values());
        
        // 2. COSTRUIAMO IL LAYOUT
        buildLayout();
        
        // 3. CONFIGURIAMO LA LOGICA SPECIFICA
        setupAudioLogic();

        // 4. BINDING
        binder.forField(anno).asRequired("Campo obbligatorio").bind("anno");
        binder.forField(autore).asRequired("Campo obbligatorio").bind("autore");
        binder.forField(duration)
	    .asRequired("Campo obbligatorio") 
        .withConverter(new AppConverters.StringToDoubleConverter()).bind("duration");
        binder.forField(estensione)
        .asRequired("Campo obbligatorio").bind("estensione"); // Mappa su 'estensione' del DTO
        
        binder.bindInstanceFields(this);
    }

    @Override
    protected void addSpecificTopLayout(VerticalLayout mainLayout) {
        autore.setWidth("280px");
        duration.setWidth("120px");
        HorizontalLayout row = new HorizontalLayout(nome, autore, duration);
        row.setAlignItems(Alignment.BASELINE);
        mainLayout.add(row);
    }

    @Override
    protected void addSpecificMiddleLayout(VerticalLayout mainLayout) {
        VerticalLayout rows = new VerticalLayout();
        rows.setPadding(false);
        
        HorizontalLayout r2 = new HorizontalLayout(genere, album, anno);
//        coverPath.setWidthFull();
        coverPath.setWidth("1060px");
        
        // Qui usiamo il path comune + estensione specifica (combo) + dimensione comune
//        HorizontalLayout r4 = new HorizontalLayout(path, estensione, dimensione);
//        r4.setAlignItems(Alignment.BASELINE);
//        rows.add(r2, coverPath, r4);

        rows.add(r2, coverPath);
        
        addInfoFileLayout(rows, estensione); // Aggiunge la riga con path, estensione e dimensione
        
        HorizontalLayout section = new HorizontalLayout(rows, coverPreview);
        section.expand(rows);
        mainLayout.add(section);
    }

    private void setupAudioLogic() {
        
    	anno.setWidth("69px");
		anno.setWidth("120px");
		// Limiti anche lato componente (UI)
		anno.setMin(1940);
		anno.setMax(2080);
		anno.setStep(1);
		anno.setI18n(
				new IntegerField.IntegerFieldI18n().setMinErrorMessage("L'anno deve essere maggiore o uguale a 1940")
						.setMaxErrorMessage("L'anno non può superare il 2080"));
    	
//    	estensione.setItems(AudioFormat.values());
//        estensione.setItemLabelGenerator(AudioFormat::getLabel);
        coverPreview.setWidth("160px"); coverPreview.setHeight("200px");
        coverPath.setValueChangeMode(ValueChangeMode.EAGER);
        coverPath.addValueChangeListener(e -> {
            coverPreview.setVisible(!e.getValue().isBlank());
            coverPreview.setSrc(e.getValue());
        });
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
    @Override protected String getReturnRoute() { return"audio";}
}