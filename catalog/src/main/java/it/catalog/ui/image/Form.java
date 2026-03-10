package it.catalog.ui.image;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import it.catalog.persistence.repository.TagRepository;
import it.catalog.service.dto.ImageDto;
import it.catalog.service.dto.TagDto;
import it.catalog.service.interfaces.ImageFileService;
import it.catalog.ui.common.MainLayout;

@Route(value = "images-form", layout = MainLayout.class)
@PageTitle("Immagini - Form")
public class Form extends FormLayout implements BeforeEnterObserver {

    private final ImageFileService service;
    private final TagRepository tagRepo;
    private Long idParam;
    private final Binder<ImageDto> binder = new Binder<>(ImageDto.class);

    private final TextField title = new TextField("Titolo");
    private final TextArea description = new TextArea("Descrizione");
    private final TextField filename = new TextField("Filename");
    private final ComboBox<String> mimeType = new ComboBox<>("MIME type");
    private final NumberField sizeBytes = new NumberField("Dimensione (bytes)");
    private final ComboBox<String> formato = new ComboBox<>("Formato");
    private final ComboBox<String> tipoFile = new ComboBox<>("Tipo file");
    private final Checkbox preferito = new Checkbox("Preferito");
    private final Checkbox backup = new Checkbox("Backup");
    private final NumberField rating = new NumberField("Rating");
    private final NumberField visualizzazioni = new NumberField("Visualizzazioni");
    private final DateTimePicker dataArchiviazione = new DateTimePicker("Data archiviazione");
    private final DateTimePicker dataUltimaVisualizzazione = new DateTimePicker("Ultima visualizzazione");
    private final TextArea note = new TextArea("Note");
    private final MultiSelectComboBox<TagDto> tags = new MultiSelectComboBox<>("Tags");

    private final Button save = new Button("Salva");
    private final Anchor cancel = new Anchor("", "Annulla");

    public Form(ImageFileService service, TagRepository tagRepo) {
        this.service = service; this.tagRepo = tagRepo;

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
        tags.setItems(service.getAllTagsForImage());
        tags.setWidth("360px");
        tags.setPlaceholder("Tags");
        tags.setItemLabelGenerator(TagDto::getNomeTag);

        // Binder bindings
        binder.forField(title).asRequired("Titolo obbligatorio").bind(ImageDto::getTitle, ImageDto::setTitle);
        binder.forField(description).bind(ImageDto::getDescription, ImageDto::setDescription);
        binder.forField(filename).asRequired("Filename obbligatorio").bind(ImageDto::getFilename, ImageDto::setFilename);
        binder.forField(mimeType).asRequired("MIME obbligatorio").bind(ImageDto::getMimeType, ImageDto::setMimeType);
        binder.forField(sizeBytes).withConverter(
                v -> v == null ? 0L : v.longValue(),
                v -> v == null ? null : v.doubleValue(),
                "Numero non valido").bind(ImageDto::getSizeBytes, ImageDto::setSizeBytes);
        binder.forField(formato).asRequired("Formato obbligatorio").bind(ImageDto::getFormato, ImageDto::setFormato);
        binder.forField(tipoFile).asRequired("Tipo file obbligatorio").bind(ImageDto::getTipoFile, ImageDto::setTipoFile);
        binder.forField(preferito).bind(ImageDto::isPreferito, ImageDto::setPreferito);
        binder.forField(backup).bind(ImageDto::isBackup, ImageDto::setBackup);
        binder.forField(rating).withConverter(
                v -> v == null ? null : v.intValue(),
                v -> v == null ? null : v.doubleValue(),
                "Valore non valido").bind(ImageDto::getRating, ImageDto::setRating);
        binder.forField(visualizzazioni).withConverter(
                v -> v == null ? 0L : v.longValue(),
                v -> v == null ? null : v.doubleValue(), "Valore non valido")
            .bind(ImageDto::getVisualizzazioni, ImageDto::setVisualizzazioni);
        binder.forField(note).bind(ImageDto::getNote, ImageDto::setNote);

        save.addClickListener(e -> {
            ImageDto bean = Optional.ofNullable(service.findById(idParam)).orElse(new ImageDto());
            if (binder.writeBeanIfValid(bean)) {
                bean.setTags(new ArrayList<>(tags.getSelectedItems()));
                // DateTimePicker conversione
                bean.setDataArchiviazione(toInstant(dataArchiviazione.getValue()));
                bean.setDataUltimaVisualizzazione(toInstant(dataUltimaVisualizzazione.getValue()));
                service.save(bean);
                Notification.show("Salvato");
                getUI().ifPresent(ui -> ui.navigate(""));
            } else {
                Notification.show("Correggi i campi obbligatori");
            }
        });

        setResponsiveSteps(new ResponsiveStep("0", 1), new ResponsiveStep("700px", 2));
        add(title, filename, mimeType, formato, tipoFile, sizeBytes,
            rating, visualizzazioni, preferito, backup, tags, description,
            dataArchiviazione, dataUltimaVisualizzazione, note,
            new HorizontalLayout(save, cancel));
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        var params = event.getLocation().getQueryParameters().getParameters();
        if (params.containsKey("id")) {
            try {
                idParam = Long.parseLong(params.get("id").get(0));
                ImageDto dto = service.findById(idParam);
                if (dto != null) {
                    binder.readBean(dto);
                    tags.setValue(dto.getTags() == null ? Set.of() : new HashSet<>(dto.getTags()));
                    dataArchiviazione.setValue(fromInstant(dto.getDataArchiviazione()));
                    dataUltimaVisualizzazione.setValue(fromInstant(dto.getDataUltimaVisualizzazione()));
                }
            } catch (NumberFormatException ignored) {}
        } else {
            binder.readBean(new ImageDto());
        }
    }

    private Instant toInstant(LocalDateTime ldt) { return ldt == null ? null : ldt.atZone(ZoneId.systemDefault()).toInstant(); }
    private LocalDateTime fromInstant(Instant i) { return i == null ? null : LocalDateTime.ofInstant(i, ZoneId.systemDefault()); }
}