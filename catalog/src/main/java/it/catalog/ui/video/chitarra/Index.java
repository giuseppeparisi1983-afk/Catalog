package it.catalog.ui.video.chitarra;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.SortDirection;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.QueryParameters;
import com.vaadin.flow.router.Route;

import it.catalog.service.dto.GuitarDto;
import it.catalog.service.dto.TagDto;
import it.catalog.service.dto.search.DtoFilter_;
import it.catalog.service.interfaces.IGuitarService;
import it.catalog.ui.common.MainLayout;
import it.catalog.ui.video.base.AbstractVideoIndex;

@Route(value="chitarra", layout = MainLayout.class)
@PageTitle("Video Chitarra")
public class Index extends AbstractVideoIndex<GuitarDto> {

	private final TextField searchField = new TextField();
	private final Button addButton = new Button("Nuovo");
//	private final Select<String> searchFieldSelector = new Select<>();
	private final ComboBox<String> searchFieldSelector = new ComboBox<>();
	private final MultiSelectComboBox<TagDto> tagFilter = new MultiSelectComboBox<>(); // selezioni più valori e vengono mostrati come “badge”
    private final Button previousButton = new Button("←");
    private final Button nextButton = new Button("→");
    private final Span pageLabel = new Span();
    private final Anchor videoLink= new Anchor("video", "📹 torna a video");
    
	  // private final Grid<GuitarDto> grid = new Grid<>(GuitarDto.class, false);
	    private final IGuitarService chitarraService;
//	    private final IVideoService videoService;
	    
	    private final int pageSize = 30;
	    private int currentPage = 0;
	    private int totalPages = 1;
	    private Sort currentSort = Sort.unsorted();

	    
	    public Index(IGuitarService chitarraService) {
	        
	    	super(GuitarDto.class);
	    	this.chitarraService = chitarraService;
//	        this.videoService = videoService;
	        setSizeFull();
	        
	        add(new H2("Archivio Video Chitarra"),new VerticalLayout(createToolbar(), grid));
	        configureGrid();
	        configurePagination();
	        updateGrid();
	    }

	    private void configureGrid() {
//	        grid.setSizeFull();
	        
	     // Stile compatto (riduce il padding delle celle)
	        grid.addThemeVariants(GridVariant.LUMO_COMPACT, GridVariant.LUMO_ROW_STRIPES);
	        
	        // --- 1. COLONNA FATTO (Icona) ---
	        // Larghezza fissa e piccola (es. 80px)
	        grid.addColumn(new ComponentRenderer<>(guitar -> {
//	            Icon icon = video.getTodo() ? VaadinIcon.CHECK_CIRCLE.create() : VaadinIcon.CIRCLE.create();
//	            icon.setColor(video.getTodo() ? "var(--lumo-success-color)" : "var(--lumo-tertiary-color)");
//	            return icon;
	        	 Checkbox checkbox = new Checkbox(guitar.getTodo());
	        	 checkbox.setReadOnly(true); // evita modifiche da parte dell'utente
		            if (guitar.isCancelled()) {
		            	checkbox.addClassName("riga-cancellata");
		    		}
		            return checkbox;
	        }))
//	        .setWidth("70px")      // Fissa
//	        .setFlexGrow(0)       // Non si allarga mai
	        .setHeader("Fatto").setSortable(true).setKey("todo");

	        grid.addColumn(new ComponentRenderer<>(guitar -> {
	    	    Span span = new Span(guitar.getDifficolta().getLabel());
	    	    if (guitar.isCancelled()) {
	    	        span.addClassName("riga-cancellata");
	    	    }
	    	    return span;
	    	})).setHeader("Difficolta").setSortable(true).setKey("difficolta");
	        
	        grid.addColumn(new ComponentRenderer<>(guitar -> {
	    	    Span span = new Span(guitar.getAutore());
	    	    if (guitar.isCancelled()) {
	    	        span.addClassName("riga-cancellata");
	    	    }
	    	    return span;
	    	})).setHeader("Autore").setSortable(true).setKey("autore");
	       
	        grid.addColumn(new ComponentRenderer<>(guitar -> {
	            Checkbox checkbox = new Checkbox(guitar.getVisto());
	            checkbox.setReadOnly(true); // evita modifiche da parte dell'utente
	            if (guitar.isCancelled()) {
	            	checkbox.addClassName("riga-cancellata");
	    		}
	            return checkbox;
	        })).setHeader("Visto").setSortable(true).setKey("visto");

	        grid.addComponentColumn(this::createActionLinks)
	        .setResizable(true) // L'utente può allargarla
	        .setFlexGrow(0)     // evita che venga ridimensionata automaticamente
	        .setAutoWidth(true)   // la colonna si adatti automaticamente al contenuto
	        .setHeader("Azioni");
	        
	        grid.addSortListener(e -> {
	            currentSort = Sort.by(e.getSortOrder().stream()
	                .map(order -> {
	                    String property = order.getSorted().getKey();
	                    Sort.Direction direction = order.getDirection() == SortDirection.ASCENDING
	                            ? Sort.Direction.ASC
	                            : Sort.Direction.DESC;
	                    return new Sort.Order(direction, property);
	                })
	                .toList());
	            currentPage = 0;
	            updateGrid();
	        });
   
	     // rendi la tabella interattiva
	        grid.addItemClickListener(event -> {
	        	GuitarDto selected = event.getItem();
	    	    if (selected != null) {
	    	    	QueryParameters queryParams = QueryParameters.simple(Map.of("view", "true"));

	    	    	// Navighiamo passando:
	                // 1. La classe target
	                // 2. Il parametro URL (ID del video)
	                // 3. I parametri Query (?view=true)
	        		getUI().ifPresent(ui -> ui.navigate(Form.class, selected.getIdGuitar(), queryParams));
	    	    	
	    	    }
	    	});
	    }

	    private Component createActionLinks(GuitarDto guitar) {

	    	String href = "chitarra-form/"+guitar.getIdGuitar()+"?view=false";
	    	Anchor edit = new Anchor(href, "modifica");
	    	//edit.setTarget("_blank"); // il link si apre in un atra scheda
	    	edit.getElement().getThemeList().add("primary");
	    	edit.getStyle().set("text-decoration", "none");
	    	edit.getStyle().set("padding-top", "7.5px");
	    	
	    	Button delete = new Button("cancella", e -> conferma(guitar.getId(),"Sei sicuro di voler cancellare questo video?"));
	    	delete.addClassName("link-button");
	    	delete.setVisible(!guitar.isCancelled());
	    	delete.setWidth("min-content");
	    	delete.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
	    	
	    	Button recovery = new Button("ripristina", e -> conferma(guitar.getId(),"Stai ripristinando questo elemento. Sei sicuro di volerlo fare?"));
	    	recovery.addClassName("link-button");
	    	recovery.setVisible(guitar.isCancelled());
	    	recovery.setWidth("min-content");
	    	recovery.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
	    	
	    	 HorizontalLayout actions = new HorizontalLayout(edit, delete,recovery);
	    	return actions;
	    }
	    
	    private void conferma(Integer id,String msg) {
	    	Dialog conferma = new Dialog();
			conferma.add(new Text(msg));
			
			Button confermaBtn = new Button("Sì", evn -> {
				if (msg.contains("cancellare"))
					chitarraService.delete(id);
				else
					chitarraService.recovery(id);
				updateGrid();
				conferma.close();
				Notification.show(msg.contains("cancellare") ? "Video cancellato" : "Video ripristinato");
			});
			
			Button annullaBtn = new Button("Annulla", evnt -> conferma.close());
			
			conferma.add(new HorizontalLayout(confermaBtn, annullaBtn));
			conferma.open();
	    	
	    }
	   
	    private void configurePagination() {
	        previousButton.addClickListener(e -> {
	            if (currentPage > 0) {
	                currentPage--;
	                updateGrid();
	            }
	        });

	        nextButton.addClickListener(e -> {
	            if (currentPage < totalPages - 1) {
	                currentPage++;
	                updateGrid();
	            }
	        });
	    }
	   
		

	    private Component createToolbar() {
//	        searchFieldSelector.setItems("titolo", "categoria", "note");
	        //searchFieldSelector.setValue("titolo");
//	        searchFieldSelector.setPlaceholder("Criterio di ricerca...");
//	        searchFieldSelector.setAllowCustomValue(false); // per non renderlo editabile non funziona
	        
	        setComboBoxSampleData(searchFieldSelector);
	     
	        // criterio di ricerca (ComboBox)
	    	searchFieldSelector.addValueChangeListener(e -> {
	    		// ogni volta che cambia il criterio → refresh
	    		grid.getLazyDataView().refreshAll();
	    	});
	    	
	        searchField.setPlaceholder("Cerca...");
	        searchField.setClearButtonVisible(true);
	        searchField.setValueChangeMode(ValueChangeMode.EAGER); // in questo modo la ricerca parte subito
	        searchField.setWidth("300px");
	        searchField.addValueChangeListener(e -> {
	       	 if (e.getValue() == null || e.getValue().trim().isEmpty()) {
	        		
                 searchFieldSelector.clear();// Azzera la selezione
        	    }
	       	 //	   	 [DA TESTARE]
	       	 if ((searchFieldSelector.getValue() == null || searchFieldSelector.getValue().trim().isEmpty()) && !e.getValue().trim().isEmpty()) {
	       		 
	       		Notification.show("Impossibile proseguire. Selezionare il criterio di ricerca", 3000, Notification.Position.MIDDLE);
	       	 
	       	 }
	       	 else {
	        	currentPage = 0;
	            updateGrid();
	       	 }
	        });
	        
	        addButton.addClickListener(e -> addVideo());
	        
	        videoLink.getElement().getThemeList().add("primary");
	    	videoLink.getStyle().set("text-decoration", "none");
	    	videoLink.getStyle().set("padding-top", "7.5px");

	        HorizontalLayout search = new HorizontalLayout(searchFieldSelector, searchField);
	        search.setWidthFull();
	        search.setJustifyContentMode(JustifyContentMode.START);
	        
	        
	        HorizontalLayout searchBar = new HorizontalLayout(search,addButton);
	        searchBar.setWidthFull();
	        searchBar.setAlignItems(Alignment.BASELINE);
	        searchBar.setJustifyContentMode(JustifyContentMode.END);
	        
	        return new VerticalLayout(videoLink,searchBar);
	    }

	    private void updateGrid() {

	    	DtoFilter_ filtroCorrente = new DtoFilter_(searchFieldSelector.getValue(), searchField.getValue()
	    	    		,new ArrayList<>(tagFilter.getSelectedItems())); // inizialmente
	    		// vuoto
	    	    
	    	    Page<GuitarDto> page = chitarraService.searchByField(filtroCorrente, currentPage, pageSize, currentSort);
	    	    if (page.isEmpty()) {
	    			grid.setVisible(false);
	    			pageLabel.setVisible(false);
	    			Notification.show("Nessun dato trovato", 3000, Notification.Position.MIDDLE);
	    		} else {
	    			grid.setVisible(true);
	    			pageLabel.setVisible(true);
	    			grid.setItems(page.getContent());
	    			// Per ordinamento e paginazione nativi del Grid
	    			//  grid.setItems(query -> samplePersonService.list(VaadinSpringDataHelpers.toSpringPageRequest(query)).stream());
	    			// Questo metodo traduce la richiesta di dati del Grid (Vaadin) in un oggetto PageRequest di Spring Data.
	    			// Vaadin Grid usa un approccio chiamato "lazy loading" o "virtual scrolling", che è diverso dalla paginazione tradizionale.
	    			// Il Grid non mostra pulsanti di navigazione. Invece, carica automaticamente i dati man mano che l’utente scorre verso il basso. 
	    			// Vaadin invia una Query con offset e limit ogni volta che servono nuove righe.
	    			totalPages = page.getTotalPages();
	    			pageLabel.setText("Pagina " + (currentPage + 1) + " di " + (totalPages == 0 ? 1 : totalPages));			
	    		}
	    }
	    
	    private void addVideo() {
		    	QueryParameters queryParams = QueryParameters.simple(Map.of("view", "false"));
	    	getUI().ifPresent(ui -> ui.navigate(Form.class, queryParams));
	    }
	    
	    private void setComboBoxSampleData(ComboBox<String> comboBox) {
	    	 List<String> sampleItems = Arrays.stream(GuitarDto.class.getDeclaredFields())
	         	    .map(Field::getName)
	         	    .filter(nome -> !nome.equalsIgnoreCase("idGuitar")) // esclude il campo "idGuitar"
	         	    .collect(Collectors.toList());
	        comboBox.setItems(sampleItems);
	        comboBox.setPlaceholder("Cerca per...");
	        comboBox.setWidth("min-content");
	        comboBox.setClearButtonVisible(true);
	    }
	    
}
