package it.catalog.ui.audio;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import it.catalog.service.dto.AudioDto;
import it.catalog.service.dto.TagDto;
import it.catalog.service.impl.AudioFileServiceImpl;
import it.catalog.ui.common.MainLayout;

@Route(value = "audio-form", layout = MainLayout.class)
@PageTitle("Audio - Form")
public class Form extends FormLayout implements BeforeEnterObserver {

    private final AudioFileServiceImpl service;

    private Long idParam;
    private final Binder<AudioDto> binder = new Binder<>(AudioDto.class);

    private final TextField title = new TextField("Titolo");
    private final TextArea description = new TextArea("Descrizione");
    private final TextField filename = new TextField("Filename");
    private final ComboBox<String> mimeType = new ComboBox<>("MIME type");
    private final NumberField durationSeconds = new NumberField("Durata (s)");
    private final NumberField sizeBytes = new NumberField("Dimensione (bytes)");
    private final ComboBox<String> formato = new ComboBox<>("Formato audio");
    private final TextField coverPath = new TextField("Cover path");
    private final Image coverPreview = new Image();

    private final TextField genere = new TextField("Genere");
    private final TextField autore = new TextField("Autore");
    private final TextField album = new TextField("Album");
    private final NumberField annoPubblicazione = new NumberField("Anno");

    private final Checkbox preferito = new Checkbox("Preferito");
    private final Checkbox backup = new Checkbox("Backup");
    private final NumberField rating = new NumberField("Rating");
    private final NumberField visualizzazioni = new NumberField("Visualizzazioni");
    private final DateTimePicker dataArchiviazione = new DateTimePicker("Data archiviazione");
    private final DateTimePicker dataUltimaVisualizzazione = new DateTimePicker("Ultima visualizzazione");
    private final TextArea note = new TextArea("Note");
    private final MultiSelectComboBox<TagDto> tags = new MultiSelectComboBox<>();

    private final Button save = new Button("Salva");
    private final Anchor cancel = new Anchor("audio", "Annulla");

    public Form(AudioFileServiceImpl service) {
        this.service = service;

        // Da sistemare con gli enumeration
        mimeType.setItems("audio/mpeg", "audio/flac", "audio/wav", "audio/ogg");
        formato.setItems("MP3","FLAC","WAV");
        // DA VEDERE
//        formato.setTooltipGenerator(f -> switch (f) {
//            case "MP3" -> "Lossy, compatibilità ampia";
//            case "FLAC" -> "Lossless, alta qualità";
//            case "WAV" -> "PCM non compresso, editing";
//            default -> "";
//        });

        sizeBytes.setWidth("150px"); sizeBytes.setStep(1); sizeBytes.setMin(0);
        durationSeconds.setWidth("120px"); durationSeconds.setStep(1); durationSeconds.setMin(0);
        annoPubblicazione.setWidth("120px"); annoPubblicazione.setStep(1);

        coverPreview.setAlt("Cover preview"); coverPreview.setWidth("120px");
        coverPath.addValueChangeListener(e -> coverPreview.setSrc(Optional.ofNullable(e.getValue()).orElse("")));

     // Carica tutti i tag disponibili
        List<TagDto> allTags = new ArrayList<>(service.getAllTags());
        tags.setWidth("360px");
        tags.setItems(allTags);
        tags.setWidth("360px");
        tags.setPlaceholder("Tags");
        tags.setItemLabelGenerator(TagDto::getNomeTag);
        ListDataProvider<TagDto> dataProvider = new ListDataProvider<>(allTags);
        // Se vuoi permettere all’utente di aggiungere tags non presenti, puoi usare:
        tags.setAllowCustomValue(true);
        tags.addCustomValueSetListener(e -> {
            String nomeTag = e.getDetail();
            TagDto nuovoTag=new TagDto(nomeTag,"Audio");
            
            // Aggiungi il nuovo tag agli items senza ricreare il provider
            if (!allTags.contains(nuovoTag)) {
            	allTags.add(nuovoTag);
                dataProvider.refreshAll(); // aggiorna la vista
            }

         // Mantieni la selezione precedente e aggiungi il nuovo tag
            Set<TagDto> currentSelection = new HashSet<>(tags.getValue());
            currentSelection.add(nuovoTag);
            tags.setValue(currentSelection);
        });

        // Binder bindings
        binder.forField(title).asRequired("Titolo obbligatorio").bind(AudioDto::getTitle, AudioDto::setTitle);
        binder.forField(description).bind(AudioDto::getDescription, AudioDto::setDescription);
        binder.forField(filename).asRequired("Filename obbligatorio").bind(AudioDto::getFilename, AudioDto::setFilename);
        binder.forField(mimeType).asRequired("MIME obbligatorio").bind(AudioDto::getMimeType, AudioDto::setMimeType);
        binder.forField(durationSeconds).withConverter(
                v -> v == null ? null : v.intValue(), v -> v == null ? null : v.doubleValue(), "Numero non valido")
            .bind(AudioDto::getDurationSeconds, AudioDto::setDurationSeconds);
        binder.forField(sizeBytes).withConverter(
                v -> v == null ? 0L : v.longValue(), v -> v == null ? null : v.doubleValue(), "Numero non valido")
            .bind(AudioDto::getSizeBytes, AudioDto::setSizeBytes);
        binder.forField(formato).asRequired("Formato obbligatorio").bind(AudioDto::getFormato, AudioDto::setFormato);
        binder.forField(coverPath).bind(AudioDto::getCoverPath, AudioDto::setCoverPath);
        binder.forField(genere).bind(AudioDto::getGenere, AudioDto::setGenere);
        binder.forField(autore).bind(AudioDto::getAutore, AudioDto::setAutore);
        binder.forField(album).bind(AudioDto::getAlbum, AudioDto::setAlbum);
        binder.forField(annoPubblicazione).withConverter(
                v -> v == null ? null : v.intValue(), v -> v == null ? null : v.doubleValue(), "Anno non valido")
            .bind(AudioDto::getAnnoPubblicazione, AudioDto::setAnnoPubblicazione);
        binder.forField(preferito).bind(AudioDto::isPreferito, AudioDto::setPreferito);
        binder.forField(backup).bind(AudioDto::isBackup, AudioDto::setBackup);
        binder.forField(rating).withConverter(v -> v == null ? null : v.intValue(), v -> v == null ? null : v.doubleValue(), "Valore non valido")
              .bind(AudioDto::getRating, AudioDto::setRating);
        binder.forField(visualizzazioni).withConverter(v -> v == null ? 0L : v.longValue(), v -> v == null ? null : v.doubleValue(), "Valore non valido")
              .bind(AudioDto::getVisualizzazioni, AudioDto::setVisualizzazioni);
        binder.forField(note).bind(AudioDto::getNote, AudioDto::setNote);

        save.addClickListener(e -> {
            AudioDto bean = Optional.ofNullable(service.findById(idParam)).orElse(new AudioDto());
            if (binder.writeBeanIfValid(bean)) {
                bean.setTags(new ArrayList<>(tags.getSelectedItems()));
                bean.setDataArchiviazione(toInstant(dataArchiviazione.getValue()));
                bean.setDataUltimaVisualizzazione(toInstant(dataUltimaVisualizzazione.getValue()));
                service.save(bean);
                Notification.show("Salvato");
                // 🔄 aggiorna la lista dei tag disponibili
                
//                List<TagDto> allTags = new ArrayList<>(service.getAllTagsForAudio());
//                tags.setItems(allTags);
//                tags.setValue(samplePersonService.getAllTagsById(saved.getId()));
                getUI().ifPresent(ui -> ui.navigate("audio")); 
                // Nota: se ritorno sulla index non serve aggiornare il MultiSelectComboBox dei tags
            } else {
                Notification.show("Correggi i campi obbligatori");
            }
        });

        setResponsiveSteps(new ResponsiveStep("0", 1), new ResponsiveStep("800px", 2));
		HorizontalLayout horizontallayout = new HorizontalLayout(new Anchor("audio", "Indietro"), new H2("Audio"));
		horizontallayout.add(coverPreview);
		horizontallayout.add(visualizzazioni);
		HorizontalLayout horizontallayout2 = new HorizontalLayout();
		horizontallayout.add(horizontallayout2);
		add(horizontallayout, title, filename, mimeType, formato, durationSeconds, sizeBytes, coverPath, genere, autore, album, annoPubblicazione, rating, visualizzazioni, preferito, backup, tags, description, dataArchiviazione, dataUltimaVisualizzazione, note, new HorizontalLayout(save, new Anchor("audio", "Annulla")));
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        var params = event.getLocation().getQueryParameters().getParameters();
        if (params.containsKey("id")) {
            try {
                idParam = Long.parseLong(params.get("id").get(0));
                AudioDto dto = service.findById(idParam);
                if (dto != null) {
                    binder.readBean(dto);
                    tags.setValue(dto.getTags() == null ? Set.of() : new HashSet<>(dto.getTags()));
                    dataArchiviazione.setValue(fromInstant(dto.getDataArchiviazione()));
                    dataUltimaVisualizzazione.setValue(fromInstant(dto.getDataUltimaVisualizzazione()));
                    coverPreview.setSrc(Optional.ofNullable(dto.getCoverPath()).orElse(""));
                    binder.readBean(dto); // Popola automaticamente i campi. IMPORTANTE: prima si definiscono i binding, poi si chiama readBean().
                }
            } catch (NumberFormatException ignored) {}
        } else {
            binder.readBean(new AudioDto());
        }
    }

    private Instant toInstant(LocalDateTime ldt) { return ldt == null ? null : ldt.atZone(ZoneId.systemDefault()).toInstant(); }
    private LocalDateTime fromInstant(Instant i) { return i == null ? null : LocalDateTime.ofInstant(i, ZoneId.systemDefault()); }
}
