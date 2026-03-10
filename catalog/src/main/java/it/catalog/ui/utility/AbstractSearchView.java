package it.catalog.ui.utility;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.CallbackDataProvider;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.spring.data.VaadinSpringDataHelpers;

import it.catalog.service.dto.TagDto;
import it.catalog.service.dto.search.DateRangeCriterion;
import it.catalog.service.dto.search.StringCriterion;
import it.catalog.service.interfaces.SearchService;

public abstract class AbstractSearchView<T, F extends BaseFilter> extends VerticalLayout {

    protected final SearchService<T, F> service;
    protected final Class<T> dtoClass;
    protected final Supplier<F> filterSupplier;
    
    protected final SearchEventBus eventBus = new SearchEventBus();
    protected final Debouncer debouncer = new Debouncer();
    protected final Grid<T> grid;

    // Componenti UI
    protected final ComboBox<SearchFieldOption> searchFieldSelector = new ComboBox<>();
    protected final TextField searchField = new TextField();
    protected final DatePicker dateFrom = new DatePicker();
    protected final DatePicker dateTo = new DatePicker();
    protected final MultiSelectComboBox<TagDto> tagFilter = new MultiSelectComboBox<>();
    protected final Span pageInfo = new Span();
    
    protected final Button prev = new Button("«");
    protected final Button next = new Button("»");
    
    protected final int pageSize = 30;
    protected int pageNumber = 0;
    protected int totalPages = 1;
    protected Sort currentSort = Sort.unsorted();
    private boolean initialLoadDone = false;

    public AbstractSearchView(SearchService<T, F> service, Class<T> dtoClass, String title, Supplier<F> filterSupplier) {
        this.service = service;
        this.dtoClass = dtoClass;
        this.filterSupplier = filterSupplier;
        this.grid = new Grid<>(dtoClass, false);
        //grid.setPageSize(30); 
        
        setSizeFull();
        add(new H2(title));

        setupEventBus();
        configureCommonComponents();
        configureGrid(grid); // Astratto: implementato dai figli
        
        initLayout();
        setupDataProvider();
        
        initialLoadDone = true;
    }

    private void setupEventBus() {
        eventBus.subscribe(() -> {
            if (!initialLoadDone) return;
            debouncer.debounce(() -> {
                getUI().ifPresent(ui -> ui.access(this::refresh));
            }, 400);
        });
    }

    private void configureCommonComponents() {
        // --- SEARCH FIELD (TEXT) ---
    	searchField.setPlaceholder("Cerca...");
    	searchField.setClearButtonVisible(true);
        searchField.setValueChangeMode(ValueChangeMode.EAGER);
        searchField.addValueChangeListener(e -> {
            // Se l'utente preme la "X" (valore null/empty), resettiamo il selettore
            if (e.getValue() == null || e.getValue().isEmpty()) {
                if (e.isFromClient()) searchFieldSelector.clear();
            }
            validateAndFire();
        });

        // --- DATE PICKERS ---
        dateFrom.setPlaceholder("Da");
        dateFrom.addValueChangeListener(e -> {
            if (e.getValue() == null && e.isFromClient()) {
                dateTo.clear();
                searchFieldSelector.clear();
            }
            validateAndFire();
        });
        dateTo.setPlaceholder("A");
        dateTo.addValueChangeListener(e -> validateAndFire());

        // --- TAGS ---
        tagFilter.setPlaceholder("Tags");
        tagFilter.setItemLabelGenerator(TagDto::getNomeTag);
        tagFilter.setItems(service.getAllTags());
        tagFilter.addValueChangeListener(e -> eventBus.fire());

        // --- SELECTOR (CRITERI) ---
        searchFieldSelector.setPlaceholder("Cerca per...");
        searchFieldSelector.setItemLabelGenerator(SearchFieldOption::getLabel);
        searchFieldSelector.setClearButtonVisible(true);
        searchFieldSelector.addValueChangeListener(e -> {
            SearchFieldOption opt = e.getValue();
            if (opt == null) {
                searchField.clear();
                dateFrom.clear();
                dateTo.clear();
                searchField.setVisible(true);
                dateFrom.setVisible(false);
                dateTo.setVisible(false);
            } else {
                boolean isDate = opt.isDateField();
                searchField.setVisible(!isDate);
                dateFrom.setVisible(isDate);
                dateTo.setVisible(isDate);
                // Puliamo i valori quando si cambia criterio per evitare mix incoerenti
                searchField.clear();
                dateFrom.clear();
                dateTo.clear();
            }
            validateAndFire();
        });
    }

    protected void validateAndFire() {
        SearchFieldOption opt = searchFieldSelector.getValue();
        
        // Se c'è un valore (testo o date) ma manca il criterio
        if (opt == null) {
            boolean hasText = !searchField.isEmpty();
            boolean hasDate = dateFrom.getValue() != null || dateTo.getValue() != null;
            if (hasText || hasDate) {
                Notification.show("Selezionare prima il criterio di ricerca", 3000, Notification.Position.MIDDLE);
                return;
            }
        }

        // Controllo coerenza date
        if (opt != null && opt.isDateField()) {
            if (dateFrom.getValue() != null && dateTo.getValue() != null) {
                if (dateFrom.getValue().isAfter(dateTo.getValue())) {
                    Notification.show("Intervallo date non valido", 3000, Notification.Position.MIDDLE);
                    return;
                }
            }
        }

        eventBus.fire();
    }

    private void initLayout() {
        HorizontalLayout filters = new HorizontalLayout(searchFieldSelector, searchField, dateFrom, dateTo, tagFilter);
        filters.setAlignItems(Alignment.BASELINE);
        filters.setWidthFull();

        dateFrom.setVisible(false);
        dateTo.setVisible(false);

        Button addButton = new Button("Nuovo", e -> navigateToForm(null));
        addButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        
        HorizontalLayout top = new HorizontalLayout(filters, addButton);
        top.setWidthFull();
//        top.setExpand(filters, 1);
        top.setFlexGrow(1.0, filters);	// Espandiamo i filtri per spingere il bottone "Nuovo" tutto a destra
        top.setAlignItems(Alignment.BASELINE);

        prev.addClickListener(e -> { if (pageNumber > 0) { pageNumber--; eventBus.fire(); }});
        next.addClickListener(e -> { if (pageNumber < totalPages - 1) { pageNumber++; eventBus.fire(); }});
        HorizontalLayout pager = new HorizontalLayout(prev, pageInfo, next);
        pager.setAlignItems(Alignment.CENTER);

        add(top, grid, pager);
    }

    private void setupDataProvider() {
       
//    	grid.setPageSize(pageSize); // Assicurati che sia PRIMA del setDataProvider
    	
    	CallbackDataProvider<T, F> dataProvider =DataProvider.fromFilteringCallbacks(
            query -> {
                Pageable pageable = VaadinSpringDataHelpers.toSpringPageRequest(query);
                //Pageable pageable = PageRequest.of(pageNumber, pageSize, currentSort);
                
//                int offset = query.getOffset();
//                int limit = query.getLimit() > 0 ? query.getLimit() : pageSize;
//
//                int pageSize = offset / limit;
////
//                Sort sort = VaadinSpringDataHelpers.toSpringPageRequest(query).getSort();
////                //Sort sort = VaadinSpringDataHelpers.toSpringSort(query);
////
//                Pageable pageable = PageRequest.of(pageSize, limit, sort);

                
                Page<T> page = service.findPage(buildFilter(), pageable);
                query.getPageSize(); // utile per ottimizzazioni                
                grid.setVisible(true);
    			pageInfo.setVisible(true);
    			next.setVisible(true);
    			prev.setVisible(true);
                totalPages = page.getTotalPages();
                pageNumber = pageSize;
                return page.getContent().stream();
            },
            query -> {
	        	
	        	long total = service.count(buildFilter());
	        	     	
	        	 if (total == 0) {
	        			grid.setVisible(false);
    	    			pageInfo.setVisible(false);
    	    			next.setVisible(false);
    	    			prev.setVisible(false);
    	    			Notification.show("Nessun dato trovato", 3000, Notification.Position.MIDDLE);
	             }
	        	 query.getPageSize(); // utile per ottimizzazioni
	            
	        	 return (int) total;
	        }
        );
    	
    	// Forza il refresh per applicare la page size
//        grid.getDataProvider().refreshAll();
    	grid.setDataProvider(dataProvider);
    }

    protected F buildFilter() {
        F filter = filterSupplier.get();
        SearchFieldOption opt = searchFieldSelector.getValue();

        if (opt != null) {
            if (opt.isDateField()) {
                DateRangeCriterion c = new DateRangeCriterion();
                c.setField(opt.getFieldName());
                c.setFrom(dateFrom.getValue());
                c.setTo(dateTo.getValue());
                filter.setCriterion(c);
            } else {
                StringCriterion c = new StringCriterion();
                c.setField(opt.getFieldName());
                c.setValue(searchField.getValue());
                filter.setCriterion(c);
            }
        }
        filter.setTags(new ArrayList<>(tagFilter.getSelectedItems()));
        return filter;
    }

    public void refresh() {
        grid.getDataProvider().refreshAll();
        pageInfo.setText("Pagina " + (pageNumber + 1) + " di " + (totalPages == 0 ? 1 : totalPages));
    }

    protected void initSearchOptionsByGrid() {
        List<SearchFieldOption> options = grid.getColumns().stream()
                .filter(col -> col.getKey() != null && !col.getKey().equals("rowNumber") && !col.getKey().equals("Azioni"))
                .map(col -> SearchFieldOption.of(dtoClass, col.getHeaderText(), col.getKey()))
                .toList();
        searchFieldSelector.setItems(options);
    }

    // Metodi da implementare nelle View specifiche
    protected abstract void configureGrid(Grid<T> grid);
    protected abstract void navigateToForm(Long id);
}