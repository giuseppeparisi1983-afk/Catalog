package it.catalog.ui.documenti;

import java.lang.reflect.Field;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.SortDirection;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.QueryParameters;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteParameters;
import com.vaadin.flow.theme.lumo.LumoUtility.Gap;

import it.catalog.common.enums.StatiDocumento;
import it.catalog.service.dto.DocumentoDto;
import it.catalog.service.dto.TagDto;
import it.catalog.service.dto.search.DtoFilter_;
import it.catalog.service.interfaces.DocumentoService;
import it.catalog.ui.common.MainLayout;

//@Menu(order = 0, icon = LineAwesomeIconUrl.PENCIL_RULER_SOLID)
@Uses(Icon.class)
@Route(value="documents", layout = MainLayout.class)
@PageTitle("Documenti")
@CssImport("./styles/styles.css")
public class Index extends Composite<VerticalLayout> {

	private final TextField searchField = new TextField();
	private final ComboBox<String> searchFieldSelector = new ComboBox<>();
	 MultiSelectComboBox<TagDto> tagFilter = new MultiSelectComboBox<>(); // selezioni più valori e vengono mostrati come “badge”
	private final Grid<DocumentoDto> grid = new Grid(DocumentoDto.class, false);
	
	DateTimeFormatter FORMAT_DATETIME = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
	DateTimeFormatter FORMAT_DATE = DateTimeFormatter.ofPattern("dd/MM/yyyy");
	private final Span pageLabel = new Span();
	private final Button previousButton = new Button("←");
    private final Button nextButton = new Button("→");
    private final Anchor homeLink = new Anchor("/", "🏠 Torna alla Home");
    private final int pageSize = 10;
    private int currentPage = 0;
    private int totalPages = 1;
    private Sort currentSort = Sort.unsorted();

    private final DocumentoService service;
	
    public Index(DocumentoService docService) {
        
    	this.service=docService;
    	
    	VerticalLayout layoutColumn2 = new VerticalLayout();
        HorizontalLayout layoutRow = new HorizontalLayout();
        
        Button addButton = new Button();
        getContent().setWidth("100%");
        getContent().getStyle().set("flex-grow", "1");
        layoutColumn2.setWidthFull();
        getContent().setFlexGrow(1.0, layoutColumn2);
        layoutColumn2.setWidth("100%");
        layoutColumn2.getStyle().set("flex-grow", "1");
//        layoutRow.setWidthFull();
        layoutColumn2.setFlexGrow(1.0, layoutRow);
        layoutRow.addClassName(Gap.MEDIUM);
        layoutRow.setWidth("100%");
        layoutRow.setHeight("min-content");
        setComboBoxSampleData(searchFieldSelector);
        searchField.setWidth("min-content");
        searchField.setPlaceholder("Cerca...");
        searchField.setClearButtonVisible(true);
        searchField.setValueChangeMode(ValueChangeMode.EAGER); // in questo modo la ricerca parte subito
        searchField.setWidth("300px");
        
       
        tagFilter.setPlaceholder("Tags");
        tagFilter.setWidth("300px");   
        tagFilter.setItemLabelGenerator(TagDto::getNomeTag);// Mostra solo il nome del tag
        tagFilter.setItems(service.getAllTagsForDoc()); // Popola con i tag disponibili

        // campo di ricerca testuale
        searchField.addValueChangeListener(e -> {
            // ogni volta che cambia il testo → refresh
        	grid.getLazyDataView().refreshAll();
        });

        // criterio di ricerca (ComboBox)
        searchFieldSelector.addValueChangeListener(e -> {
            // ogni volta che cambia il criterio → refresh
        	grid.getLazyDataView().refreshAll();
        });

        searchField.addValueChangeListener(e -> {
        	if (e.getValue() == null || e.getValue().trim().isEmpty()) {
        		
        		searchFieldSelector.clear();// Azzera la selezione
        		tagFilter.clear();  // Azzera immediatamente tutte le selezioni
        	}
//       	 [DA TESTARE]
        	if ((searchFieldSelector.getValue() == null || searchFieldSelector.getValue().trim().isEmpty()) && !e.getValue().trim().isEmpty()) {
        		
        		tagFilter.clear();  // Azzera immediatamente tutte le selezioni
        		Notification.show("Impossibile proseguire. Selezionare il criterio di ricerca", 3000, Notification.Position.MIDDLE);
        		
        	}
        	else {
        		currentPage = 0;
        		updateGrid();
        	}
        });

        // MultiSelectComboBox dei tag
        tagFilter.addValueChangeListener(e -> {
            // ogni volta che aggiungi o togli un tag → refresh
        	grid.getLazyDataView().refreshAll();
        });
        
        addButton.setText("Nuovo");
        layoutRow.setAlignSelf(FlexComponent.Alignment.END, addButton);
        addButton.setWidth("95px");
        addButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        addButton.addClickListener(e -> addNew());
        grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        grid.setWidth("100%");
        grid.getStyle().set("flex-grow", "0");
        setGridSampleData(grid);
        getContent().add(new H2("Archivio Documenti"));
        getContent().add(layoutColumn2);
//        layoutColumn2.add(homeLink);
     // Allinea il singolo componente a destra
        layoutColumn2.setHorizontalComponentAlignment(FlexComponent.Alignment.END, homeLink);
        layoutColumn2.add(layoutRow);
        layoutRow.add(searchFieldSelector);
        layoutRow.add(searchField);
        layoutRow.add(tagFilter);
        
        VerticalLayout layoutColumn3 = new VerticalLayout();
        
        
        layoutColumn3.add(homeLink);
        layoutColumn3.add(addButton);
        
        layoutRow.add(layoutColumn3);
        configurePagination();
        layoutColumn2.add(grid,createPaginationControls());
        updateGrid();
    }

//    record SampleItem(String value, String label, Boolean disabled) {
//    }

    private void setComboBoxSampleData(ComboBox<String> comboBox) {
		/*
		 * List<SampleItem> sampleItems = new ArrayList<>(); sampleItems.add(new
		 * SampleItem("first", "First", null)); sampleItems.add(new SampleItem("second",
		 * "Second", null)); sampleItems.add(new SampleItem("third", "Third",
		 * Boolean.TRUE)); sampleItems.add(new SampleItem("fourth", "Fourth", null));
		 */
    	 List<String> sampleItems = Arrays.stream(DocumentoDto.class.getDeclaredFields())
         	    .map(Field::getName)
         	    .filter(nome -> !nome.equalsIgnoreCase("idDocumento")) // esclude il campo "idDocumento"
         	    .collect(Collectors.toList());
        comboBox.setItems(sampleItems);
//        comboBox.setItemLabelGenerator(Item::label);
        comboBox.setPlaceholder("Cerca per...");
        comboBox.setWidth("min-content");
        comboBox.setClearButtonVisible(true);
        
//        comboBox.setRenderer(new ComponentRenderer<>(item -> {
//            ComboBox<Item> inner = new ComboBox<>();
//            inner.setEnabled(!item.disabled());
//            return new Text(item.label());
//        }));
    }

	private void setGridSampleData(Grid<DocumentoDto> grid) {
		
		 grid.addColumn(new ComponentRenderer<>(doc -> {
	    	    Span span = new Span(doc.getNome());
	    	    if (doc.getStato().equals(StatiDocumento.ELIMINATO)) {
	    	        span.addClassName("riga-cancellata");
	    	    }
	    	    return span;
	    	})).setHeader("Nome").setSortable(true).setKey("nome");
		 
		 grid.addColumn(new ComponentRenderer<>(doc -> {
			 Span span = new Span(String.valueOf(doc.getDimensione()));
			 if (doc.getStato().equals(StatiDocumento.ELIMINATO)) {
				 span.addClassName("riga-cancellata");
			 }
			 return span;
		 })).setHeader("Dimensione").setSortable(true).setKey("dimensione");

		 grid.addColumn(new ComponentRenderer<>(doc -> {
			 Span span = new Span(doc.getDescrizione());
			 if (doc.getStato().equals(StatiDocumento.ELIMINATO)) {
				 span.addClassName("riga-cancellata");
			 }
			 return span;
		 })).setHeader("Descrizione").setSortable(true).setKey("descrizione");

		 grid.addColumn(new ComponentRenderer<>(doc -> {
			 Span span = new Span(String.valueOf(doc.getVersione()));
			 if (doc.getStato().equals(StatiDocumento.ELIMINATO)) {
				 span.addClassName("riga-cancellata");
			 }
			 return span;
		 })).setHeader("Versione").setSortable(true).setKey("versione");

		 grid.addColumn(new ComponentRenderer<>(doc -> {
			 Span span = new Span(doc.getStato().getLabel());
			 if (doc.getStato().equals(StatiDocumento.ELIMINATO)) {
				 span.addClassName("riga-cancellata");
			 }
			 return span;
		 })).setHeader("Stato").setSortable(true).setKey("stato");

		 grid.addColumn(new ComponentRenderer<>(doc -> {
			 Span span = new Span(doc.getEstensione());
			 if (doc.getStato().equals(StatiDocumento.ELIMINATO)) {
				 span.addClassName("riga-cancellata");
			 }
			 return span;
		 })).setHeader("Estensione").setSortable(true).setKey("estensione");

		 grid.addColumn(new ComponentRenderer<>(doc -> {
	    	    Span span = new Span(doc.getOrigine());
	    	    if (doc.getStato().equals(StatiDocumento.ELIMINATO)) {
	    	        span.addClassName("riga-cancellata");
	    	    }
	    	    return span;
	    	})).setHeader("Origine").setSortable(true).setKey("origine");

		 grid.addColumn(new ComponentRenderer<>(doc -> {
			 Span span = new Span(doc.getCategoria().getLabel());
			 if (doc.getStato().equals(StatiDocumento.ELIMINATO)) {
				 span.addClassName("riga-cancellata");
			 }
			 return span;
		 })).setHeader("Categorie").setSortable(true).setKey("categoria");

		 grid.addColumn(new ComponentRenderer<>(doc -> {
			 Span span = new Span(doc.getAutore());
			 if (doc.getStato().equals(StatiDocumento.ELIMINATO))
				 span.addClassName("riga-cancellata");
			 return span;
		 })).setHeader("Autore").setSortable(true).setKey("autore");
		
		grid.addColumn(new ComponentRenderer<>(doc -> {
    		Span span = new Span(doc.getDataCreazione() != null ? FORMAT_DATETIME.format(doc.getDataCreazione()) : "");
    		if (doc.getStato().equals(StatiDocumento.ELIMINATO)) {
    			span.addClassName("riga-cancellata");
    		}
    		return span;
    	})).setHeader("Data Archiviazione").setSortable(true).setKey("dataInserimento");

		grid.addColumn(new ComponentRenderer<>(doc -> {
			Span span = new Span(doc.getLastUpdate() != null ? FORMAT_DATETIME.format(doc.getLastUpdate()) : "");
			if (doc.getStato().equals(StatiDocumento.ELIMINATO)) {
				span.addClassName("riga-cancellata");
			}
			return span;
		})).setHeader("Ultimo Aggiornamento").setSortable(true).setKey("lastUpdate");
		
		grid.addColumn(new ComponentRenderer<>(doc -> {
			Span span = new Span(doc.getLastView() != null ? FORMAT_DATETIME.format(doc.getLastView()) : "");
			if (doc.getStato().equals(StatiDocumento.ELIMINATO)) {
				span.addClassName("riga-cancellata");
			}
			return span;
		})).setHeader("Ultima Visualizzazione").setSortable(true).setKey("lastView");
		
		grid.addColumn(new ComponentRenderer<>(doc -> {
			Span span = new Span(doc.getLingua());
			if (doc.getStato().equals(StatiDocumento.ELIMINATO))
				span.addClassName("riga-cancellata");
			return span;
		})).setHeader("Lingua").setSortable(true).setKey("lingua");

		grid.addColumn(new ComponentRenderer<>(doc -> {
	    		Span span = new Span(doc.getNote());
	    		if (doc.getStato().equals(StatiDocumento.ELIMINATO))
	    			span.addClassName("riga-cancellata");
	    		return span;
	    	})).setHeader("Note").setSortable(true).setKey("note");
		
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
    	})).setSortable(true).setKey("tags");

		grid.addColumn(new ComponentRenderer<>(doc -> {
			Span span = new Span(doc.getPath());
			if (doc.getStato().equals(StatiDocumento.ELIMINATO))
				span.addClassName("riga-cancellata");
			return span;
		})).setHeader("Path").setSortable(true).setKey("path");
		
		grid.addComponentColumn(document -> {
	         HorizontalLayout actions = new HorizontalLayout();

	         Anchor edit = new Anchor("documents-form/" + document.getIdDocumento()+"?view=false", "Modifica");
	     	edit.getElement().getThemeList().add("primary");
	    	edit.getStyle().set("text-decoration", "none");
	    	edit.getStyle().set("padding-top", "7.5px");
	    	
	         Button delete = new Button("Cancella", e -> conferma(document.getIdDocumento(),
	        		 "Sei sicuro di voler cancellare questo documento?"));

	         delete.addClassName("link-button");
	     	delete.setVisible(!document.getStato().equals(StatiDocumento.ELIMINATO));
	     	delete.setWidth("min-content");
	     	delete.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
	     	Button recovery = new Button("Ripristino", e -> conferma(document.getIdDocumento(),
	     			"Stai ripristinando questo elemento. Sei sicuro di volerlo fare?"));
	     	recovery.addClassName("link-button");
	     	recovery.setVisible(document.getStato().equals(StatiDocumento.ELIMINATO));
	     	recovery.setWidth("min-content");
	     	recovery.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
	     	
	         actions.add(edit, delete,recovery);
	         return actions;
	     }).setHeader("Azioni");
		
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
		
		
		// riordina le colonne manualmente
        grid.setColumnReorderingAllowed(true);
        grid.getColumns().forEach(col -> col.setAutoWidth(true)); // adatta alla pagina
     // Abilita il ridimensionamento per tutte le colonne
        grid.getColumns().forEach(column -> column.setResizable(true));
        
     // rendi la tabella interattiva
        grid.addItemClickListener(event -> {
        	DocumentoDto selected = event.getItem();
    	    if (selected != null) {
    	    	RouteParameters params = new RouteParameters("id", String.valueOf(selected.getIdDocumento()));
    	    	QueryParameters queryParams = QueryParameters.simple(Map.of("view", "true"));
    	    	getUI().ifPresent(ui -> ui.navigate(Form.class, params, queryParams));
    	    	
    	    }
    	});
	}

	private void updateGrid() {
//       grid.setItems(query -> service.list(VaadinSpringDataHelpers.toSpringPageRequest(query)).stream());

		DtoFilter_ filtroCorrente = new DtoFilter_(searchFieldSelector.getValue(), searchField.getValue(),new ArrayList<>(tagFilter.getSelectedItems())); // inizialmente
		// vuoto
		List<String> tagNames = tagFilter.getSelectedItems().stream().map(TagDto::getNomeTag).toList(); 
		
//		grid.setItems(query -> {
			//Pageable pageable = VaadinSpringDataHelpers.toSpringPageRequest(query);
			Pageable pageable = PageRequest.of(currentPage, pageSize, currentSort);
			Page<DocumentoDto> page = service.search(filtroCorrente, tagNames, pageable);
// gestione del caso Not found
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
//			query.getPageSize(); // utile per ottimizzazioni
//			return page.stream();
//		});
// Costruizione del DataProvider
		//grid.getDataProvider().refreshAll(); // forza il refresh con nuovo filtro
	}
	
	
	 private void conferma(Long id,String msg) {
	    	Dialog conferma = new Dialog();
			conferma.add(new Text(msg));
			
			Button confermaBtn = new Button("Sì", evn -> {
				if (msg.contains("cancellare"))
					 service.cancella(id);
				else
					service.recovery(id);
				updateGrid();
				conferma.close();
				Notification.show(msg.contains("cancellare") ? "Documento cancellato" : "Documento ripristinato");
			});
			
			Button annullaBtn = new Button("Annulla", evnt -> conferma.close());
			
			conferma.add(new HorizontalLayout(confermaBtn, annullaBtn));
			conferma.open();
	    	
	    }
	 
    private void addNew() {
      	 RouteParameters params = new RouteParameters("id", String.valueOf("0"));
   	    	QueryParameters queryParams = QueryParameters.simple(Map.of("view", "false"));

      	getUI().ifPresent(ui -> ui.navigate(Form.class, params, queryParams));
      }
    
    private Component createPaginationControls() {
        HorizontalLayout pagination = new HorizontalLayout(previousButton, pageLabel, nextButton);
        pagination.setAlignItems(Alignment.CENTER);
        pagination.setJustifyContentMode(JustifyContentMode.CENTER);
        return pagination;
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
}
