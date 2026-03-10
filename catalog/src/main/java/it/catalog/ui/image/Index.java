package it.catalog.ui.image;



import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import it.catalog.persistence.repository.TagRepository;
import it.catalog.service.dto.ImageDto;
import it.catalog.service.dto.TagDto;
import it.catalog.service.dto.search.DtoFilter_;
import it.catalog.service.interfaces.ImageFileService;
import it.catalog.ui.common.MainLayout;

@Route(value = "images", layout = MainLayout.class)
@PageTitle("Immagini")
public class Index extends VerticalLayout {

    private final ImageFileService service;
    private final Grid<ImageDto> grid = new Grid<>(ImageDto.class, false);

    private final ComboBox<String> searchFieldSelector = new ComboBox<>();
    private final TextField searchField = new TextField();
    private final MultiSelectComboBox<TagDto> tagFilter = new MultiSelectComboBox<>();
    private final Button applyFilter = new Button("Applica");

    private final ComboBox<Integer> pageSizeSelect = new ComboBox<>("Per pagina");
    private final Button prev = new Button("«");
    private final Button next = new Button("»");
    private final Span pageInfo = new Span();

    private int pageNumber = 0;
    private Sort sort = Sort.by(Sort.Direction.ASC, "id");

    public Index(ImageFileService service, TagRepository tagRepo) {
        this.service = service;
        setSizeFull();
        add(new H2("Archivio Immagini"));

//        searchFieldSelector.setItems("Titolo", "Filename", "MimeType", "Tutti");
//        searchFieldSelector.setValue("Tutti");
        setComboBoxSampleData(searchFieldSelector);
        searchField.setPlaceholder("Cerca...");
        searchField.setClearButtonVisible(true);
        searchField.setValueChangeMode(ValueChangeMode.EAGER); // in questo modo la ricerca parte subito
        searchField.setWidth("280px");
        searchField.addValueChangeListener(e -> {
          	 if (e.getValue() == null || e.getValue().trim().isEmpty()) {
        		
                searchFieldSelector.clear();// Azzera la selezione
       	    }
//      	 [DA TESTARE]
      	 if ((searchFieldSelector.getValue() == null || searchFieldSelector.getValue().trim().isEmpty()) && !e.getValue().trim().isEmpty()) {
      		 
      		Notification.show("Impossibile proseguire. Selezionare il criterio di ricerca", 3000, Notification.Position.MIDDLE);
      	 
      	 }
      	 else {
       	//currentPage = 0;
           refresh();
      	 }
           });
        
        tagFilter.setPlaceholder("Tags");
        tagFilter.setWidth("300px");   
        tagFilter.setItemLabelGenerator(TagDto::getNomeTag);// Mostra solo il nome del tag
        tagFilter.setItems(service.getAllTagsForImage()); // Popola con i tag disponibili
        applyFilter.addClickListener(e -> { pageNumber = 0; refresh(); });

        HorizontalLayout top = new HorizontalLayout(
                new Anchor("form", "Nuovo"),
                searchFieldSelector, searchField, tagFilter, applyFilter
        );
        add(top);

        grid.addColumn(ImageDto::getId).setHeader("ID").setSortable(true).setAutoWidth(true);
        grid.addColumn(ImageDto::getTitle).setHeader("Titolo").setSortable(true).setAutoWidth(true);
        grid.addColumn(ImageDto::getFilename).setHeader("Filename").setSortable(true).setAutoWidth(true);
        grid.addColumn(ImageDto::getMimeType).setHeader("MIME").setSortable(true).setAutoWidth(true);
        grid.addColumn(ImageDto::getFormato).setHeader("Formato").setSortable(true).setAutoWidth(true);
        grid.addColumn(ImageDto::getTipoFile).setHeader("Tipo").setSortable(true).setAutoWidth(true);
        grid.addColumn(ImageDto::getSizeBytes).setHeader("Dim.").setSortable(true).setAutoWidth(true);

        grid.addComponentColumn(item -> {
            Anchor edit = new Anchor("form?id=" + item.getId(), "Modifica");
            Anchor del = new Anchor("#", "Cancella");
            del.getElement().addEventListener("click", ev -> {
                service.delete(item.getId());
                getUI().ifPresent(ui -> ui.getPage().executeJs("alert('Elemento cancellato');"));
                refresh();
            });
            return new HorizontalLayout(edit, del);
        }).setHeader("Azioni");

        grid.addItemClickListener(e -> getUI().ifPresent(ui -> ui.navigate("form?id=" + e.getItem().getId())));
        grid.setHeight("60vh");
        add(grid);

        pageSizeSelect.setItems(10, 20, 50);
        pageSizeSelect.setValue(10);
        pageSizeSelect.addValueChangeListener(e -> { pageNumber = 0; refresh(); });

        prev.addClickListener(e -> { if (pageNumber > 0) { pageNumber--; refresh(); }});
        next.addClickListener(e -> { pageNumber++; refresh(); });

        grid.addSortListener(e -> {
            var orders = e.getSortOrder();
            if (!orders.isEmpty()) {
                var o = orders.get(0);
             // DA VEDERE
//                String prop = switch (o.getSorted().getHeader().toString()) {
//                    case "ID" -> "id"; case "Titolo" -> "title"; case "Filename" -> "filename";
//                    case "MIME" -> "mimeType"; case "Formato" -> "formato"; case "Tipo" -> "tipoFile";
//                    case "Dim." -> "sizeBytes"; default -> "id";
//                };
//                sort = Sort.by(o.getDirection() == GridSortOrder.Direction.ASCENDING ? Sort.Direction.ASC : Sort.Direction.DESC, prop);
                pageNumber = 0;
                refresh();
            }
        });

        HorizontalLayout pager = new HorizontalLayout(pageSizeSelect, prev, next, pageInfo);
        add(pager);

        refresh();
    }

    private void refresh() {
    	// DA VEDERE
    	   String text = Optional.ofNullable(searchField.getValue()).orElse("").trim();
        String criterion = Optional.ofNullable(searchFieldSelector.getValue()).orElse("Tutti");
        
        DtoFilter_ filtro = new DtoFilter_(criterion, text,new ArrayList<>(tagFilter.getSelectedItems())); // inizialmente
        
        
        List<String> requiredTags = tagFilter.getSelectedItems().stream() .map(TagDto::getNomeTag) .toList();
        Pageable pageable = PageRequest.of(pageNumber, pageSizeSelect.getValue(), sort);
//
        Page<ImageDto> page = service.findPage(filtro, pageable, requiredTags);
        
        if (page.isEmpty()) {
			grid.setVisible(false);
			pageInfo.setVisible(false);
			Notification.show("Nessun dato trovato", 3000, Notification.Position.MIDDLE);
		} else {
			grid.setVisible(true);
			pageInfo.setVisible(true);
			grid.setItems(page.getContent());
			pageNumber = page.getTotalPages() - 1;
			pageInfo.setText("Pagina " + (pageNumber + 1) + " di " + Math.max(page.getTotalPages(), 1) + " — Totale: " + page.getTotalElements());
		}
        
    }
    
    private void setComboBoxSampleData(ComboBox<String> comboBox) {
   	 List<String> sampleItems = Arrays.stream(ImageDto.class.getDeclaredFields())
        	    .map(Field::getName)
        	    .filter(nome -> !nome.equalsIgnoreCase("id")) // esclude il campo "id"
        	    .collect(Collectors.toList());
       comboBox.setItems(sampleItems);
       comboBox.setPlaceholder("Cerca per...");
       comboBox.setWidth("min-content");
       comboBox.setClearButtonVisible(true);
       
       comboBox.addValueChangeListener(e -> {
   		// ogni volta che cambia il criterio → refresh
   		grid.getLazyDataView().refreshAll();
   	});
   }
}
