package it.catalog.ui.persone;

import java.lang.reflect.Field;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.QueryParameters;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteParameters;
import com.vaadin.flow.spring.data.VaadinSpringDataHelpers;

import it.catalog.service.dto.PersonaDto;
import it.catalog.service.dto.TagDto;
import it.catalog.service.dto.search.DtoFilter_;
import it.catalog.service.interfaces.PersonaService;

@PageTitle("PersoneView")
@Route("lista-persone")
@Uses(Icon.class)
public class Index extends Composite<VerticalLayout> {

	// aggiunta dei componenti globali
	private final TextField searchField = new TextField();
	private final Grid<PersonaDto> stripedGrid = new Grid(PersonaDto.class,false); // il II° parametro del costruttore messo a false permette di non generare automaticamente le colonne. Per default è true e in questo caso crea automaticamente tutte le colonne dai getter.
	
	private final ComboBox<String> searchFieldSelector = new ComboBox<>();
	
	private final Button addButton = new Button("Aggiungi");

	DateTimeFormatter FORMAT_DATE = DateTimeFormatter.ofPattern("dd/MM/yyyy");
	
    private final PersonaService service;
//	 private final int pageSize = 30;
//	    private int currentPage = 0;
//	    private int totalPages = 1;
	    
    public Index(PersonaService service) {
        
    	this.service=service;
    	
    	VerticalLayout layoutColumn2 = new VerticalLayout();
        // private final Grid<VideoDto> grid = new Grid<>(VideoDto.class, false);
        getContent().setWidth("100%");
        getContent().getStyle().set("flex-grow", "1");
        layoutColumn2.setWidthFull();
        getContent().setFlexGrow(1.0, layoutColumn2);
        layoutColumn2.setWidth("100%");
        layoutColumn2.getStyle().set("flex-grow", "1");
        
        List<String> nomiCampi = Arrays.stream(PersonaDto.class.getDeclaredFields())
        	    .map(Field::getName)
        	    .filter(nome -> !nome.equalsIgnoreCase("id")) // esclude il campo "id"
        	    .collect(Collectors.toList());
        
//        searchFieldSelector.setItems("titolo", "categoria", "note"); // dinamicità dei valori ??
        searchFieldSelector.setItems(nomiCampi); // dinamicità dei valori del criterio di ricerca
//        searchFieldSelector.setValue("titolo");
        searchFieldSelector.setPlaceholder("Seleziona un campo.");
        /**
         * searchFieldSelector.setAllowCustomValue(false); 
         * Nota: La ComboBox è sempre editabile nel senso che l’utente può digitare nel campo.
         * Tuttavia, solo se il suo metodo setAllowCustomValue(true) è attivo, il valore digitato può essere accettato anche se non è tra quelli proposti.
         * Se non lo attivi, il valore digitato non viene accettato come selezione valida, ma può comunque essere visualizzato temporaneamente nel campo.
         * Di default setAllowCustomValue(false) per cui l’utente può solo selezionare tra i valori predefiniti.
         * **/
        
        searchField.setWidth("min-content");
        searchField.setPlaceholder("Cerca...");
        searchField.setClearButtonVisible(true);
        searchField.setValueChangeMode(ValueChangeMode.EAGER); // in questo modo la ricerca parte subito
        searchField.setWidth("300px");
        
        MultiSelectComboBox<TagDto> tagFilter = new MultiSelectComboBox<>(); // selezioni più valori e vengono mostrati come “badge”
        tagFilter.setPlaceholder("Tags");
        tagFilter.setWidth("300px");   
        tagFilter.setItemLabelGenerator(TagDto::getNomeTag);// Mostra solo il nome del tag
        tagFilter.setItems(service.getAllTagsForPersona()); // Popola con i tag disponibili
        
     // campo di ricerca testuale
        searchField.addValueChangeListener(e -> {
            // ogni volta che cambia il testo → refresh
        	stripedGrid.getLazyDataView().refreshAll();
        });

        // criterio di ricerca (ComboBox)
        searchFieldSelector.addValueChangeListener(e -> {
            // ogni volta che cambia il criterio → refresh
        	stripedGrid.getLazyDataView().refreshAll();
        });

        // MultiSelectComboBox dei tag
        tagFilter.addValueChangeListener(e -> {
            // ogni volta che aggiungi o togli un tag → refresh
        	stripedGrid.getLazyDataView().refreshAll();
        });

        
        searchField.addValueChangeListener(e -> {
//        	currentPage = 0;
        	//updateGrid(); // il cerca non può richiamare una findAll
        	
        	 if (e.getValue() == null || e.getValue().trim().isEmpty()) {
        		
                 searchFieldSelector.clear();// Azzera la selezione
                 tagFilter.clear();  // Azzera immediatamente tutte le selezioni
        	    }
        	
        	DtoFilter_ filtroCorrente = new DtoFilter_(searchFieldSelector.getValue(),searchField.getValue(),new ArrayList<>(tagFilter.getSelectedItems())); // inizialmente vuoto
        	
        	stripedGrid.setItems(query -> {
        	    Pageable pageable = VaadinSpringDataHelpers.toSpringPageRequest(query);
        	    
        	    List<String> tagsName=(tagFilter.getValue() != null && !tagFilter.getValue().isEmpty()) ? 
        	    		tagFilter.getValue().stream().map(TagDto::getNomeTag).collect(Collectors.toList()): 
        	    			Collections.EMPTY_LIST;
        	    
        	    Page<PersonaDto> page = service.trovaConFiltro(filtroCorrente, pageable,tagsName);
        	    query.getPageSize(); // utile per ottimizzazioni
        	    return page.stream();
        	});
        	// Costruizione del DataProvider
            stripedGrid.getDataProvider().refreshAll(); // forza il refresh con nuovo filtr
            
        });

        HorizontalLayout search = new HorizontalLayout(searchFieldSelector, searchField,tagFilter);
        search.setWidthFull();
        search.setAlignItems(FlexComponent.Alignment.BASELINE); // In questo modo tutti i componenti vengono allineati sulla stessa linea di base del testo, evitando che uno sembri più basso.
        search.setJustifyContentMode(JustifyContentMode.START);
HorizontalLayout horizontallayout2 = new HorizontalLayout();
search.add(horizontallayout2);
		/*
		 * MenuBar menubar = new MenuBar(); MenuItem shareMenuItem =
		 * menubar.addItem("Share"); SubMenu shareSubMenu = shareMenuItem.getSubMenu();
		 * MenuItem onSocialMeMenuItem = shareSubMenu.addItem("On social media");
		 * SubMenu onSocialMeSubMenu = onSocialMeMenuItem.getSubMenu(); MenuItem
		 * facebookMenuItem = onSocialMeSubMenu.addItem("Facebook"); MenuItem
		 * twitterMenuItem = onSocialMeSubMenu.addItem("Twitter"); MenuItem
		 * instagramMenuItem = onSocialMeSubMenu.addItem("Instagram"); MenuItem
		 * byEmailMenuItem = shareSubMenu.addItem("By email"); MenuItem getLinkMenuItem
		 * = shareSubMenu.addItem("Get link"); search.add(menubar);
		 */

		/* Definizione del listening del MultiSelectComboBox
		 * tagFilter.addValueChangeListener(event -> { Set<String> selectedTags =
		 * event.getValue(); List<Article> results =
		 * articleService.searchByTags(selectedTags); grid.setItems(results); });
		 */
      addButton.addClickListener(e -> addNew());
      
      HorizontalLayout searchBar = new HorizontalLayout(search,addButton);
    searchBar.setWidthFull();
    searchBar.setJustifyContentMode(JustifyContentMode.END);
      
       // stripedGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        layoutColumn2.setAlignSelf(FlexComponent.Alignment.CENTER, stripedGrid);
        stripedGrid.setWidth("100%");
        stripedGrid.getStyle().set("flex-grow", "0");
        setGridSampleData(stripedGrid);
        getContent().add(new H2("Archivio Persone"));
        getContent().add(layoutColumn2);
HorizontalLayout horizontallayout = new HorizontalLayout();
getContent().add(horizontallayout);
        layoutColumn2.add(searchBar);
        layoutColumn2.add(stripedGrid);
    }
    
    
    private void setGridSampleData( Grid<PersonaDto> grid) {
 
    	grid.addColumn(PersonaDto::getId).setHeader("Id").setSortable(true);
    	grid.addColumn(PersonaDto::getNome).setHeader("Nome").setSortable(true);
    	grid.addColumn(PersonaDto::getCognome).setHeader("Cognome").setSortable(true);

    	grid.addColumn(new ComponentRenderer<>(persona -> {
     		Span span = new Span(persona.getDataNascita() != null ? 
     				FORMAT_DATE.format(persona.getDataNascita()) : "");
     		if (!persona.isAttivo()) {
     			span.addClassName("riga-cancellata");
     		}
     		return span;
     	})).setHeader("Data Nascita").setSortable(true);
    	
    	// colonna custom per i tag
    	grid.addColumn(p -> {
    	    if (p.getTags() == null) return "";
    	    return p.getTags().stream()
    	             .map(TagDto::getNomeTag)   // prendi solo il nome
    	             .collect(Collectors.joining(", "));
    	}).setHeader("Tags")
    	/** per il ritorno a capo del valore*/
    	.setRenderer(new ComponentRenderer<>(item -> {
    	    Div div = new Div();
    	    div.setText(item.getTags().stream()
   	             .map(TagDto::getNomeTag)   // prendi solo il nome
   	             .collect(Collectors.joining(", ")));
    	    // Applica uno stile CSS per forzare il 'word-wrap'
    	    div.getElement().getStyle().set("white-space", "normal"); 
    	    return div;
    	})).setSortable(true);

    	
    	grid.setItems(query -> service.trovaTutti(VaadinSpringDataHelpers.toSpringPageRequest(query)).stream());
    	
    	/* Alternativa

    	DtoFilter filtroCorrente = new DtoFilter(); // Nota se voglio fare la findAll il filtro deve essere vuoto
    	
    	stripedGrid.setItems(query -> {
    	    Pageable pageable = VaadinSpringDataHelpers.toSpringPageRequest(query);
    	    Page<PersonaDto> page = samplePersonService.trovaConFiltro(filtroCorrente, pageable); // questo tipo di ricerca funziona solo per i campi di tipo Stringa ??
    	    query.getPageSize(); // utile per ottimizzazioni
    	    return page.stream();
    	}); */
        
        // generazione delle azioni
     		grid.addComponentColumn(persona -> {
         HorizontalLayout actions = new HorizontalLayout();

         Anchor edit = new Anchor("persona-form/" + persona.getId(), "Modifica");
         Anchor delete = new Anchor();
         delete.setText("Cancella");

         delete.getStyle().set("color", "hsl(214, 100%, 43%)");
         
         delete.getElement().addEventListener("click", e -> {
        	 Dialog conferma = new Dialog();
             conferma.add(new Text("Confermi la cancellazione?"));
             Button confermaBtn = new Button("Sì", ev -> {
                 service.cancella(persona.getId());
                // grid.setItems(query -> samplePersonService.trovaTutti(VaadinSpringDataHelpers.toSpringPageRequest(query)).stream()); // aggiornamento dei dati
                 conferma.close();
                 grid.getDataProvider().refreshAll(); // forza il refresh sui dati
                 grid.getElement().callJsFunction("focus"); // sposta il focus sulla tabella
                 Notification.show("Elemento cancellato");
                 conferma.addDialogCloseActionListener(eccv -> {
                	 grid.getElement().callJsFunction("focus");
                 });
        
             });
             Button annullaBtn = new Button("Annulla", evnt -> conferma.close());
             conferma.add(new HorizontalLayout(confermaBtn, annullaBtn));
             getUI().ifPresent(ui -> ui.navigate(Index.class));
             conferma.open();
         
         });

         actions.add(edit, delete);
         return actions;
     }).setHeader("Azioni");
    }

    private void addNew() {
   	 RouteParameters params = new RouteParameters("id", String.valueOf("0"));
	    	QueryParameters queryParams = QueryParameters.simple(Map.of("view", "false"));

   	getUI().ifPresent(ui -> ui.navigate(Form.class, params, queryParams));
   }

}
