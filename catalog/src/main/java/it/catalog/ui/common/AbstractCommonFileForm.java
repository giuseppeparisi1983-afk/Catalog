package it.catalog.ui.common;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;

import it.catalog.common.enums.FileExtension;
import it.catalog.service.dto.FilmDto;
import it.catalog.service.dto.TagDto;
import it.catalog.service.interfaces.SearchService;
import it.catalog.ui.utility.AppConverters;
import it.catalog.ui.utility.BooleanImageToggle;
import it.catalog.ui.utility.RatingStarsField;
import it.catalog.utility.DynamicI18nProvider;

/** Classe dove dichiariamo tutti i campi comuni. 
 * Questa classe estende AbstractBaseForm<T,S> e "disegna" la parte di form che vedrai in ogni file (Documento, Video, Chitarra).
 * 
 * Nota: il tipo generico S è un sottotipo di SearchService<T, ?>. 
 * Questo ci permette di accedere a tutti i metodi del SearchService, incluso getAllTags() per popolare la combo dei TAGS.
 * */

public abstract class AbstractCommonFileForm<T, S extends SearchService<T, ?>> extends AbstractBaseForm<T, S> {

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

    public AbstractCommonFileForm(String title, S service, Class<T> beanType) {
        super(title, service, beanType);
        
        setupCommonComponents(service);
        setupCommonBindings(beanType);

    }

 // Questo metodo verrà chiamato dai figli quando i loro campi sono pronti
    protected void buildLayout() {
        mainFormLayout.setSpacing(true);
        mainFormLayout.setPadding(false);
        mainFormLayout.getStyle().set("gap", "8px");

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
    
    
    private void setupCommonComponents(S service) {
        tags.setItems(new ArrayList<>(service.getAllTags()));
        tags.setItemLabelGenerator(TagDto::getNomeTag);
        tags.setAllowCustomValue(true);
        tags.addCustomValueSetListener(e -> {
            String nomeTag = e.getDetail().trim();
            if (nomeTag.isEmpty()) return;
            TagDto newTag = new TagDto();
            newTag.setNomeTag(nomeTag);
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

    private void setupCommonBindings(Class<T> beanType) {
        
    	if (!FilmDto.class.isAssignableFrom(beanType))
    	binder.forField(nome).asRequired("Campo obbligatorio").bind("nome");
        
    	binder.forField(path).withValidator(v -> v != null && (v.contains("\\") || v.contains("/")), "Percorso non valido").asRequired().bind("path");
        
        // Se non è Audio, l'estensione è un TextField comune
//        if (!AudioDto.class.isAssignableFrom(beanType)) {
//            binder.forField(estensione).asRequired().bind("estensione");
//        }

        binder.forField(dimensione).withConverter(new AppConverters.StringToDoubleConverter()).asRequired().bind("dimensione");
        binder.forField(visualizzazioni).withConverter(new AppConverters.DoubleToLong()).bind("visualizzazioni");
        binder.forField(tags).bind("tags");
        binder.forField(preferito).bind("preferito");
        binder.forField(backup).bind("backup");
        binder.forField(rating).bind("rating");

        // Date Converters
        binder.forField(dataArchiviazione).withConverter(new AppConverters.InstantToLocalDate()).asRequired().bind("dataArchiviazione");
        binder.forField(lastView).withConverter(new AppConverters.InstantToLocalDateTime()).bind("lastView");
        binder.forField(lastUpdate).withConverter(new AppConverters.InstantToLocalDateTime()).bind("lastUpdate");
    }

    /**
     * IL METODO CENTRALIZZATO:
     * Configura qualsiasi ComboBox che usi un Enum di tipo FileExtension
     */
    protected <E extends Enum<E> & FileExtension> void setupExtensionCombo(ComboBox<E> combo, E[] values) {
        combo.setItems(values);
        combo.setItemLabelGenerator(FileExtension::getLabel);
        combo.setPlaceholder("Seleziona formato...");
        combo.setClearButtonVisible(true);
        combo.setWidth("120px"); // Misura standard per tutti
    }
    
    // Gestisce la riga "Path | Estensione | Dimensione"
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
}