package it.catalog.ui.video;

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
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.SortDirection;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.QueryParameters;
import com.vaadin.flow.router.Route;

import it.catalog.service.dto.TagDto;
import it.catalog.service.dto.VideoDto;
import it.catalog.service.dto.search.DtoFilter_;
import it.catalog.service.interfaces.IVideoService;
import it.catalog.ui.common.MainLayout;
import it.catalog.ui.video.base.AbstractVideoIndex;

@Route(value="video", layout = MainLayout.class)
@PageTitle("Video")
//@CssImport("./styles/styles.css")
public class Index extends AbstractVideoIndex<VideoDto> {
	
    private final TextField searchField = new TextField();
    private final Button addButton = new Button("Nuovo");
    private final Button previousButton = new Button("←");
    private final Button nextButton = new Button("→");
    private final Span pageLabel = new Span();
    
//    private final Select<String> searchFieldSelector = new Select<>();
    private final ComboBox<String> searchFieldSelector = new ComboBox<>();
    private final MultiSelectComboBox<TagDto> tagFilter = new MultiSelectComboBox<>(); // selezioni più valori e vengono mostrati come “badge”
    private final Anchor guitarLink= new Anchor("chitarra", "🎸 Chitarra");

    private final IVideoService videoService;

    private final int pageSize = 30;
    private int currentPage = 0;
    private int totalPages = 1;
    private Sort currentSort = Sort.unsorted();

    public Index(IVideoService videoService) {
    	super(VideoDto.class);
    	this.videoService = videoService;
    	
    	
    	setSizeFull();
        //configureHeader();
        configureGrid();
        configurePagination();
        add(new H2("Archivio Video"),configureHeader(), grid, createPaginationControls());
        updateGrid();
    }

    private Component configureHeader() {
//        searchFieldSelector.setItems("titolo", "categoria", "note");
////        searchFieldSelector.setValue("titolo");
//        searchFieldSelector.setPlaceholder("Criterio di ricerca...");
       
//        searchFieldSelector.setLabel("Campo di ricerca");
//        searchFieldSelector.setReadOnly(true);
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
//   	 [DA TESTARE]
   	 if ((searchFieldSelector.getValue() == null || searchFieldSelector.getValue().trim().isEmpty()) && !e.getValue().trim().isEmpty()) {
   		 
   		Notification.show("Impossibile proseguire. Selezionare il criterio di ricerca", 3000, Notification.Position.MIDDLE);
   	 
   	 }
   	 else {
    	currentPage = 0;
        updateGrid();
   	 }
        });

        addButton.addClickListener(e -> addVideo());
        
    	guitarLink.getElement().getThemeList().add("primary");
    	guitarLink.getStyle().set("text-decoration", "none");
    	guitarLink.getStyle().set("padding-top", "7.5px");
        
        HorizontalLayout search = new HorizontalLayout(searchFieldSelector, searchField);
        search.setAlignItems(Alignment.BASELINE);
        search.setWidthFull();
        search.setJustifyContentMode(JustifyContentMode.START);
        
        
        HorizontalLayout searchBar = new HorizontalLayout(search,addButton);
        searchBar.setAlignItems(Alignment.BASELINE);
        searchBar.setWidthFull();
        searchBar.setJustifyContentMode(JustifyContentMode.END);
        
        return new VerticalLayout(guitarLink,searchBar);
    }

    private void configureGrid() {

        grid.addComponentColumn(this::createActionLinks)
        .setResizable(true) // L'utente può allargarla
        .setFlexGrow(0)     // evita che venga ridimensionata automaticamente
        .setAutoWidth(true)   // la colonna si adatti automaticamente al contenuto
        .setHeader("Azioni");
//        grid.setSizeFull();

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
        	VideoDto selected = event.getItem();
        	if (selected != null) {
//        		RouteParameters params = new RouteParameters("id", String.valueOf(selected.getId()));
        		QueryParameters queryParams = QueryParameters.simple(Map.of("view", "true"));
        		 // Navighiamo passando:
                // 1. La classe target
                // 2. Il parametro URL (ID del video)
                // 3. I parametri Query (?view=true)
        		getUI().ifPresent(ui -> ui.navigate(Form.class, selected.getId(), queryParams));
        		
        	}
        });
        
    }
    
    private Component createActionLinks(VideoDto video) {
//    	Button edit = new Button("Modifica", e -> editVideo(video));
    	
    	String href = "video-form/"+video.getId()+"?view=false";
    	Anchor edit = new Anchor(href, "modifica");
    	//edit.setTarget("_blank"); // il link si apre in un atra scheda
    	edit.getElement().getThemeList().add("primary");
    	edit.getStyle().set("text-decoration", "none");
    	edit.getStyle().set("padding-top", "7.5px");


//    	Anchor edit = new Anchor(RouteConfiguration.forSessionScope().getUrl(VideoFormView.class), "modifica");
    	
//    	edit.addClassName("link-evidenziato");
//    	edit.getStyle().set("color", "blue").set("text-decoration", "underline");

    	
    	Button delete = new Button("Cancella", e -> conferma(video.getId(),"Sei sicuro di voler cancellare questo video?"));
    	delete.addClassName("link-button");
    	delete.setVisible(!video.isCancelled());
    	delete.setWidth("min-content");
    	delete.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
    	Button recovery = new Button("Ripristino", e -> conferma(video.getId(),"Stai ripristinando questo elemento. Sei sicuro di volerlo fare?"));
    	recovery.addClassName("link-button");
    	recovery.setVisible(video.isCancelled());
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
				videoService.delete(id);
			else
				videoService.recovery(id);
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

    private Component createPaginationControls() {
        HorizontalLayout pagination = new HorizontalLayout(previousButton, pageLabel, nextButton);
        pagination.setAlignItems(Alignment.CENTER);
        pagination.setJustifyContentMode(JustifyContentMode.CENTER);
        return pagination;
    }

    private void updateGrid() {

    	DtoFilter_ filtroCorrente = new DtoFilter_(searchFieldSelector.getValue(), searchField.getValue(),new ArrayList<>(tagFilter.getSelectedItems())); // inizialmente vuoto
    	
    	    Page<VideoDto> page = videoService.searchByField(filtroCorrente,currentPage, pageSize, currentSort);
//    	Page<VideoDto> page = videoService.getVideos(searchField.getValue(), currentPage, pageSize, currentSort);
        if (page.isEmpty()) {
			grid.setVisible(false);
			pageLabel.setVisible(false);
			Notification.show("Nessun dato trovato", 3000, Notification.Position.MIDDLE);
		} else {
			grid.setVisible(true);
			pageLabel.setVisible(true);
			grid.setItems(page.getContent());
			totalPages = page.getTotalPages();
			pageLabel.setText("Pagina " + (currentPage + 1) + " di " + (totalPages == 0 ? 1 : totalPages));			
		}
        
    }
    
    private void addVideo() {
//    	 RouteParameters params = new RouteParameters("id", String.valueOf("0"));
	    	QueryParameters queryParams = QueryParameters.simple(Map.of("view", "false"));
//	    	getUI().ifPresent(ui -> ui.navigate(Form.class));
    	getUI().ifPresent(ui -> ui.navigate(Form.class, queryParams));
    }
    
    private void setComboBoxSampleData(ComboBox<String> comboBox) {
    	 List<String> sampleItems = Arrays.stream(VideoDto.class.getDeclaredFields())
         	    .map(Field::getName)
         	    .filter(nome -> !nome.equalsIgnoreCase("id")) // esclude il campo "id"
         	    .collect(Collectors.toList());
        comboBox.setItems(sampleItems);
        comboBox.setPlaceholder("Cerca per...");
        comboBox.setWidth("min-content");
        comboBox.setClearButtonVisible(true);
        

    }
}