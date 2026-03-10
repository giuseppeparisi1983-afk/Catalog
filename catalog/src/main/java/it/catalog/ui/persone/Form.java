package it.catalog.ui.persone;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteParameters;
import com.vaadin.flow.theme.lumo.LumoUtility.Gap;
import com.vaadin.flow.theme.lumo.LumoUtility.Padding;

import it.catalog.service.dto.PersonaDto;
import it.catalog.service.dto.TagDto;
import it.catalog.service.interfaces.PersonaService;

@PageTitle("Modulo Persona")
@Route("persona-form/:id?")
public class Form extends FormLayout implements BeforeEnterObserver
//,HasUrlParameter<Long> 

{

    private PersonaService samplePersonService;
    private PersonaDto persona = new PersonaDto();
    private TextField nome = new TextField();
    private TextField cognome = new TextField();
    private DatePicker dataNascita = new DatePicker();
    private Checkbox attivo = new Checkbox();
    private MultiSelectComboBox<TagDto> tagsSelector = new MultiSelectComboBox<>();
    private final Binder<PersonaDto> binder = new Binder<>(PersonaDto.class);
	
    public Form(PersonaService service) {
        this.samplePersonService=service;
        
        // Carica tutti i tag disponibili
        List<TagDto> allTags = new ArrayList<>(samplePersonService.getAllTagsForPersona()); // ottieni tutti i tag per il tipo oggetto Persona
        tagsSelector.setItems(allTags);
        tagsSelector.setWidth("300px");
        tagsSelector.setPlaceholder("Tags");
        tagsSelector.setItemLabelGenerator(TagDto::getNomeTag);// Mostra solo il nome del tag
        ListDataProvider<TagDto> dataProvider = new ListDataProvider<>(allTags);
        // Se vuoi permettere all’utente di aggiungere tags non presenti, puoi usare:
        tagsSelector.setAllowCustomValue(true);
        tagsSelector.addCustomValueSetListener(e -> {
            String nomeTag = e.getDetail();
            TagDto nuovoTag=new TagDto(nomeTag,"Persona");
            // Aggiungi il nuovo tag alla lista disponibile
//            List<String> currentItems = new ArrayList<>(tagsSelector.getListDataView().getItems().toList());
//            if (!currentItems.contains(nuovoTag)) {
//                currentItems.add(nuovoTag);
//                tagsSelector.setItems(currentItems);
//            }
            
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

    	
    	VerticalLayout layoutColumn2 = new VerticalLayout();
        H2 h2 = new H2();
        VerticalLayout layoutColumn3 = new VerticalLayout();
        HorizontalLayout layoutRow = new HorizontalLayout();
        Button buttonPrimary = new Button();
        Button buttonPrimary2 = new Button();
        setWidth("100%");
//        getContent().getStyle().set("flex-grow", "1");
        layoutColumn2.setWidthFull();
//        getContent().setFlexGrow(1.0, layoutColumn2);
        layoutColumn2.setSpacing(false);
        layoutColumn2.addClassName(Padding.SMALL);
        layoutColumn2.setWidth("1000px");
        layoutColumn2.getStyle().set("flex-grow", "1");
        h2.setText("Modulo Persona");
        layoutColumn2.setAlignSelf(FlexComponent.Alignment.CENTER, h2);
        h2.setWidth("max-content");
        layoutColumn3.setWidthFull();
        layoutColumn2.setFlexGrow(1.0, layoutColumn3);
        layoutColumn3.setWidth("100%");
        layoutColumn3.setHeight("80px");
        layoutRow.setWidthFull();
        layoutColumn2.setFlexGrow(1.0, layoutRow);
        layoutRow.addClassName(Gap.SMALL);
        layoutRow.setWidth("200px");
        layoutRow.setHeight("80px");
        buttonPrimary.setText("Salva");
        buttonPrimary.setWidth("min-content");
        buttonPrimary.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        buttonPrimary2.setText("Indietro");
        buttonPrimary2.setWidth("min-content");
        buttonPrimary2.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        nome.setLabel("First Name");
        nome.setWidth("min-content");
        cognome.setLabel("Last Name");
        cognome.setWidth("min-content");
        dataNascita.setLabel("Data Nascita");
        dataNascita.setWidth("min-content");
        attivo.setLabel("Attivo");
        attivo.setWidth("min-content");
//        getContent().add(layoutColumn2);
        add(layoutColumn2);
        layoutColumn2.add(h2);
        layoutColumn2.add(layoutColumn3);
        layoutColumn2.add(layoutRow);
        layoutRow.add(buttonPrimary);
        layoutRow.add(buttonPrimary2);
        layoutColumn2.add(nome);
        layoutColumn2.add(cognome);
        layoutColumn2.add(dataNascita);
        layoutColumn2.add(attivo);
        layoutColumn2.add(tagsSelector);
        
        
        buttonPrimary.addClickListener(e -> save());
        
        // listener per il torna indietro
        buttonPrimary2.addClickListener(e -> getUI().ifPresent(ui -> ui.navigate(Index.class)));
        // Alternativo
        //buttonPrimary2.addClickListener(e -> UI.getCurrent().getPage().getHistory().back());
        
        
    }
    
    // listener per il pulsante Salva
    private void save() {
    	if (binder.validate().isOk()) {
    	persona.setTags(new ArrayList<>(tagsSelector.getValue()));
    	binder.writeBeanIfValid(persona);
    	PersonaDto saved=samplePersonService.crea(persona);
        Notification.show("Elemento salvato!", 3000, Notification.Position.TOP_CENTER);
        
        List<TagDto> allTags =samplePersonService.getAllTagsForPersona();
        // 🔄 aggiorna la lista dei tag disponibili
        tagsSelector.setItems(allTags);

     // 🔄 aggiorna la selezione corrente con i tag appena salvati
//        Set<TagDto> selected = allTags.stream()
//            .filter(t -> saved.getTags().contains(t.getIdTag()))
//            .collect(Collectors.toSet());
        tagsSelector.setValue(samplePersonService.getAllTagsById(saved.getId()));
    }
    }
    

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
    	RouteParameters routeParams = event.getRouteParameters();
        Optional<String> idParam = routeParams.get("id");

        if (idParam.isPresent()) {
            try {
            	binder.bindInstanceFields(this); // associa automaticamente i campi del form alle proprietà del DTO basandosi sul nome.
            	this.persona =samplePersonService.trovaPerId(Long.parseLong(idParam.get()));
            	
            	tagsSelector.setValue(new HashSet<>(Optional.ofNullable(this.persona.getTags()).orElse(List.of())));
            	binder.readBean(persona); // Popola automaticamente i campi. IMPORTANTE: prima si definiscono i binding, poi si chiama readBean().               
            } catch (NumberFormatException ex) {
            	event.forwardTo("not-found");
            }
        } else {
            event.forwardTo("not-found");
        }
    }
    
	/*
	 * @Override public void setParameter(BeforeEvent event, @OptionalParameter Long
	 * id) { if (id != null) { this.persona = samplePersonService.trovaPerId(id);
	 * tagSelector.setValue(new HashSet<>(this.persona.getTags())); } else {
	 * this.persona = new PersonaDto(); // nuovo inserimento }
	 * 
	 * binder.readBean(persona); }
	 */
}
