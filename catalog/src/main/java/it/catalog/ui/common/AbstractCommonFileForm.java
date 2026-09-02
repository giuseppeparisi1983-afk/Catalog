package it.catalog.ui.common;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.ComponentRenderer;

import it.catalog.common.enums.FileExtension;
import it.catalog.service.dto.FilmDto;
import it.catalog.service.dto.TagDto;
import it.catalog.service.interfaces.SearchService;
import it.catalog.ui.utility.AppConverters;
import it.catalog.ui.utility.BaseFilter;
import it.catalog.ui.utility.BooleanImageToggle;
import it.catalog.ui.utility.RatingStarsField;
import it.catalog.utility.DynamicI18nProvider;

/** Classe dove dichiariamo tutti i campi comuni. 
 * Questa classe estende AbstractBaseForm<T,S> e "disegna" la parte di form che vedrai in ogni file (Documento, Video, Chitarra).
 * 
 * Nota: il tipo generico S è un sottotipo di SearchService<T, ?>. 
 * Questo ci permette di accedere a tutti i metodi del SearchService, incluso getAllTags() per popolare la combo dei TAGS.
 * */

public abstract class AbstractCommonFileForm<T, S extends SearchService<T, F>, F extends BaseFilter> 
extends AbstractBaseForm<T, S, F> {

    // Campi comuni
    protected TextField nome = new TextField("Nome");
    protected TextField path = new TextField("Percorso File");
//    protected TextField estensione = new TextField("Estensione");
    protected TextArea descrizione = new TextArea("Descrizione");
    protected TextField dimensione = new TextField("Dimensione (KB)");
    protected BooleanImageToggle backup = new BooleanImageToggle("images/backup-colored.png", "images/backup-gray.png");
    protected BooleanImageToggle preferito = new BooleanImageToggle("images/like-colored.png", "images/like-gray.png");
    protected RatingStarsField rating = new RatingStarsField();
    protected NumberField visualizzazioni = new NumberField("Visual");
    protected TextArea note = new TextArea("Note");
    protected MultiSelectComboBox<TagDto> tags = new MultiSelectComboBox<>("Tags");
    protected DateTimePicker lastView = new DateTimePicker("Ultima visualizzazione");
    protected DatePicker dataArchiviazione = new DatePicker("Data Archiviazione");
    protected DateTimePicker lastUpdate = new DateTimePicker("Ultimo Aggiornamento");

    protected ZoneId fusoOrario = ZoneId.systemDefault();
    protected VerticalLayout mainFormLayout = new VerticalLayout();

    public AbstractCommonFileForm(String title, S service, Class<T> beanType, Supplier<F> filterSupplier, String prefix) {
        super(title, service, beanType, filterSupplier); 
        
        setupCommonComponents(service);
        setupCommonBindings(prefix);

    }

 // Questo metodo verrà chiamato dai figli quando i loro campi sono pronti
    protected void buildLayout() {
        mainFormLayout.setSpacing(true);
        mainFormLayout.setPadding(false);
        mainFormLayout.getStyle().set("gap", "8px");


        // Definizione della barra di Navigazione in alto
        addNavigationLayout(mainFormLayout);
        
        
        // 1. Gancio per i campi specifici del figlio (che ora non saranno più null!)
        addSpecificTopLayout(mainFormLayout);
        
        // 2. Altro gancio
        addSpecificMiddleLayout(mainFormLayout);

        // 3. Blocchi comuni
        addDateFields(mainFormLayout);
        addStatusFields(mainFormLayout);
        addClassificationFields(mainFormLayout);

        add(mainFormLayout);
    }
    
	/*
	 * NUOVO METODO HELPER: sposto la creazione della barra di navigazione in un
	 * metodo separato nella classe Padre, così ogni figlio nel caso debba fare l'override di buildLayout()
	 * può richiamare questo metodo dove preferisce.
	 */
    protected void addNavigationLayout(VerticalLayout container) {
        HorizontalLayout navBar = new HorizontalLayout(btnPrev, navInfo, btnNext);
        navBar.setAlignItems(FlexComponent.Alignment.CENTER);
        navBar.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        navBar.setWidthFull();
//        navBar.getStyle().set("background", "var(--lumo-contrast-5pct)");
        navBar.getStyle().set("padding", "5px");
        navBar.getStyle().set("border-radius", "var(--lumo-border-radius-m)");
        
        container.add(navBar);
    }
    
    
    
    private void setupCommonComponents(S service) {
        tags.setItems(new ArrayList<>(service.getAllTags()));
        tags.setItemLabelGenerator(TagDto::getNomeTag);
        tags.setAllowCustomValue(true);
        tags.addCustomValueSetListener(e -> {
            String nomeTag = e.getDetail().trim();
            if (nomeTag.isEmpty()) return;
            TagDto newTag = new TagDto();
            newTag.setNomeTag(nomeTag);
            newTag.setTipoOggetto(getTagType());  // assegniamo il tipo di oggetto specifico per il figlio
            Set<TagDto> current = new HashSet<>(tags.getValue());
            current.add(newTag);
            tags.setValue(current);
            tags.getListDataView().refreshAll();
        });

        lastUpdate.setReadOnly(true);
        dataArchiviazione.setI18n(DynamicI18nProvider.getI18nForCurrentUser());
        lastView.setDatePickerI18n(DynamicI18nProvider.getI18nForCurrentUser());
        lastUpdate.setDatePickerI18n(DynamicI18nProvider.getI18nForCurrentUser());
        
        nome.setWidth("490px");
//        path.setWidth("90%");
        path.setWidth("850px");
//        estensione.setWidth("90px");
        dimensione.setWidth("150px");
        tags.setWidth("62%");
        descrizione.setWidth("62%");
        note.setWidth("62%");
    }

    protected void setupCommonBindings(String prefix) {
        
//    	if (!FilmDto.class.isAssignableFrom(beanType))
    	binder.forField(nome).asRequired("Campo obbligatorio").bind(prefix +"nome");
        
    	binder.forField(path).withValidator(v -> v != null && (v.contains("\\") || v.contains("/")), "Percorso non valido").asRequired().bind(prefix +"path");
        
        // Se non è Audio, l'estensione è un TextField comune
//        if (!AudioDto.class.isAssignableFrom(beanType)) {
//            binder.forField(estensione).asRequired().bind("estensione");
//        }

        binder.forField(dimensione).asRequired("Campo obbligatorio").withConverter(new AppConverters.StringToDoubleConverter()).bind(prefix +"dimensione");
        binder.forField(visualizzazioni).withConverter(new AppConverters.DoubleToLong()).bind(prefix +"visualizzazioni");
        binder.forField(tags).bind(prefix +"tags");
        binder.forField(preferito).bind(prefix +"preferito");
        binder.forField(backup).bind(prefix +"backup");
        binder.forField(rating).bind(prefix +"rating");

        // Date Converters
        binder.forField(dataArchiviazione).asRequired("Campo obbligatorio").withConverter(new AppConverters.InstantToLocalDate()).bind(prefix +"dataArchiviazione");
        binder.forField(lastView).withConverter(new AppConverters.InstantToLocalDateTime()).bind(prefix +"lastView");
        binder.forField(lastUpdate).withConverter(new AppConverters.InstantToLocalDateTime()).bind(prefix +"lastUpdate");
    }

    /**
     * IL METODO CENTRALIZZATO:
     * Configura qualsiasi ComboBox che usi un Enum di tipo FileExtension
     */
    protected <E extends Enum<E> & FileExtension> void setupCombo(ComboBox<E> combo, E[] values) {
        combo.setItems(values);
        
        combo.setRenderer(new ComponentRenderer<>(tipo -> {
    	    Span span = new Span(tipo.getLabel());
    	    
    	    // L'attributo HTML "title" attiva il tooltip nativo del browser al passaggio del mouse
    	    span.getElement().setAttribute("title", tipo.getDescription());
    	    
    	    return span;
    	}));
    	
        combo.setItemLabelGenerator(FileExtension::getLabel);
        combo.setPlaceholder("Seleziona formato...");
        combo.setClearButtonVisible(true);
        combo.setWidth("200px"); // Misura standard per tutti
    }
    
    // Gestisce la riga "Path | Estensione | Dimensione"
    // Questo metodo viene chiamato dal metodo addSpecificMiddleLayout(VerticalLayout mainLayout) dei figli (Audio, Video, Documento,....) per aggiungere la riga con il path del file, l'estensione specifica e la dimensione.
    protected void addInfoFileLayout(HasComponents container, Component extension) {
    	HorizontalLayout row = new HorizontalLayout(path, extension, dimensione);
        row.setAlignItems(FlexComponent.Alignment.BASELINE);
        row.setWidth("90%");
        container.add(row);
    }
    
    // Metodi di blocco comuni
    protected void addStatusFields(HasComponents container) {
        HorizontalLayout row = new HorizontalLayout(lastView, visualizzazioni, rating, preferito);
        row.setAlignItems(FlexComponent.Alignment.BASELINE);
        row.setWidth("70%");
        container.add(row);
    }

    protected void addDateFields(HasComponents container) {
        HorizontalLayout row = new HorizontalLayout(dataArchiviazione, lastUpdate, backup);
        row.setAlignItems(FlexComponent.Alignment.BASELINE);
        row.setWidth("70%");
        container.add(row);
    }

    protected void addClassificationFields(HasComponents container) {
        container.add(descrizione, note, tags);
    }

    // GANCI PER I FIGLI
    protected abstract void addSpecificTopLayout(VerticalLayout mainLayout);
    protected abstract void addSpecificMiddleLayout(VerticalLayout mainLayout);
    protected abstract String getTagType(); // Definisce il tipo di oggetto per i nuovi tag
}