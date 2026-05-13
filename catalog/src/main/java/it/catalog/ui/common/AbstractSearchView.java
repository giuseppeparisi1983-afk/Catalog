package it.catalog.ui.common;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;

import it.catalog.service.dto.TagDto;
import it.catalog.service.dto.search.DateRangeCriterion;
import it.catalog.service.dto.search.StringCriterion;
import it.catalog.service.interfaces.SearchService;
import it.catalog.ui.utility.BaseFilter;
import it.catalog.ui.utility.Debouncer;
import it.catalog.ui.utility.SearchEventBus;
import it.catalog.ui.utility.SearchFieldOption;

/*Logica universale di ricerca, paginazione e UI*/
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
    
    protected final int pageSize = 25;
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
        
   	 	// Se vuoi che la Grid non superi mai una certa altezza // o mostri
   	 	// esattamente 25 righe visibili (opzionale) grid.setAllRowsVisible(true); 
        // Niente barre di scorrimento interne, niente lazy loading "fastidioso".
        //this.grid.setAllRowsVisible(true);
        
        setSizeFull();
        add(new H2(title));

        configureCommonComponents();
        configureGrid(grid); // Astratto: implementato dai figli
        
        // Abilita il drag & drop delle colonne
        grid.setColumnReorderingAllowed(true);

        
        initLayout();
//        setupDataProvider();
        setupEventBus();
        
        // 1. Dichiariamo che l'inizializzazione è finita
        this.initialLoadDone = true;

        // 2. Eseguiamo il PRIMO caricamento esplicito
        refresh();
    }

    private void setupEventBus() {
        eventBus.subscribe(() -> {
            if (!initialLoadDone) return;
            debouncer.debounce(() -> {
            	getUI().ifPresent(ui -> {
                    // Verifichiamo se la UI è ancora attiva
                    if (ui.isClosing()) return;
                    
                    ui.access(() -> {
                        refresh();
                        // Con @Push abilitato nell'AppShell, questo ora funzionerà
                    });
                });
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
//        tagFilter.addValueChangeListener(e -> eventBus.fire());

        tagFilter.addValueChangeListener(e -> {
            this.pageNumber = 0; // TORNA SEMPRE A PAGINA 1
            eventBus.fire();
        });
        
        // --- SELECTOR (CRITERI) ---
        searchFieldSelector.setPlaceholder("Cerca per...");
        searchFieldSelector.setItemLabelGenerator(SearchFieldOption::getLabel);
        searchFieldSelector.setClearButtonVisible(true);
		searchFieldSelector.addValueChangeListener(e -> {
			if (e.isFromClient()) { // Solo se l'azione arriva dall'utente
				SearchFieldOption opt = e.getValue();
				if (opt == null) {
					searchField.clear();
		            dateFrom.clear();
		            dateTo.clear();
		            tagFilter.clear();
				} 
				else {
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
			}
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

     // RESETTA SEMPRE LA PAGINA quando cambiano i criteri di ricerca
        this.pageNumber = 0; 
        
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

        prev.addClickListener(e -> { if (pageNumber > 0) { pageNumber--;  refresh(); }});
        next.addClickListener(e -> { if (pageNumber < totalPages - 1) { pageNumber++;  refresh(); }});
        HorizontalLayout pager = new HorizontalLayout(prev, pageInfo, next);
        pager.setAlignItems(Alignment.CENTER);

        add(top, grid, pager);
    }

	/*
	 * private void setupDataProvider() {
	 * 
	 * grid.setPageSize(pageSize); // Assicurati che sia PRIMA del setDataProvider
	 * 
	 * // Se vuoi che la Grid non superi mai una certa altezza // o mostri
	 * esattamente 25 righe visibili (opzionale) grid.setAllRowsVisible(true); //
	 * Niente barre di scorrimento interne, niente lazy loading "fastidioso".
	 * 
	 * CallbackDataProvider<T, F> dataProvider =DataProvider.fromFilteringCallbacks(
	 * query -> { // Pageable pageable =
	 * VaadinSpringDataHelpers.toSpringPageRequest(query); //Pageable pageable =
	 * PageRequest.of(pageNumber, pageSize, currentSort);
	 * 
	 * // int offset = query.getOffset(); // int limit = query.getLimit() > 0 ?
	 * query.getLimit() : pageSize; // // int pageSize = offset / limit; //// //
	 * Sort sort = VaadinSpringDataHelpers.toSpringPageRequest(query).getSort();
	 * //// //Sort sort = VaadinSpringDataHelpers.toSpringSort(query); //// //
	 * Pageable pageable = PageRequest.of(pageSize, limit, sort);
	 * 
	 * // Pageable pageable = PageRequest.of( // query.getPage(), // pagina
	 * richiesta // 10, // numero di elementi per pagina // Sort.by("title") //
	 * opzionale // );
	 * 
	 * // 1. Calcola la pagina corretta basandoti sull'offset e il limit di Vaadin
	 * int numpage = query.getOffset() / query.getLimit(); int limit =
	 * query.getLimit();
	 * 
	 * // 2. Crea il PageRequest con la dimensione ESATTA chiesta da Vaadin //
	 * Pageable pageable = PageRequest.of(numpage, limit, //
	 * VaadinSpringDataHelpers.toSpringDataSort(query));
	 * 
	 * // query.getLimit() ora sarà 25 perché l'abbiamo impostato sulla Grid //
	 * Pageable pageable = PageRequest.of(query.getPage(), query.getPageSize(), //
	 * VaadinSpringDataHelpers.toSpringDataSort(query)); // Chiedi al backend
	 * esattamente i 25 record della pagina X PageRequest pageable =
	 * PageRequest.of(pageNumber, pageSize, Sort.by("title"));
	 * 
	 * Page<T> page = service.findPage(pageable); query.getPageSize(); // utile per
	 * ottimizzazioni grid.setVisible(true); pageInfo.setVisible(true);
	 * next.setVisible(true); prev.setVisible(true); totalPages =
	 * page.getTotalPages(); pageNumber = pageSize; return
	 * page.getContent().stream(); }, query -> {
	 * 
	 * long total = service.count();
	 * 
	 * if (total == 0) { grid.setVisible(false); pageInfo.setVisible(false);
	 * next.setVisible(false); prev.setVisible(false);
	 * Notification.show("Nessun dato trovato", 3000, Notification.Position.MIDDLE);
	 * } query.getPageSize(); // utile per ottimizzazioni
	 * 
	 * return (int) total; } );
	 * 
	 * // Forza il refresh per applicare la page size //
	 * grid.getDataProvider().refreshAll(); grid.setDataProvider(dataProvider); }
	 */

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

	public void conferma(Long id, String msg) {
		Dialog conferma = new Dialog();
		conferma.add(new Text(msg));

		Button confermaBtn = new Button("Conferma", evn -> {
			if (msg.contains("cancellare"))
				service.delete(id);
			else
				service.recovery(id);
			refresh();
			conferma.close();
			Notification.show(msg.contains("cancellare") ? "Elemento cancellato" : "Elemento ripristinato");
		});

		Button annullaBtn = new Button("Annulla", evnt -> conferma.close());

		conferma.add(new HorizontalLayout(confermaBtn, annullaBtn));
		conferma.open();

	}
    public void refresh() {
    	 // 1. Costruiamo il filtro aggiornato con quello che c'è scritto nella UI
        F filter = buildFilter();
        
        // 2. Prepariamo la richiesta paginata
//        PageRequest pageable = PageRequest.of(pageNumber, pageSize, Sort.by("nome"));
        
        PageRequest pageable = PageRequest.of(pageNumber, pageSize, 
        	    currentSort.isUnsorted() ? Sort.by(Sort.Direction.DESC, "id") : currentSort);

        // 3. Chiamiamo il service passando SIA la paginazione SIA il filtro
        // NOTA: il tuo service.findPage deve accettare (Pageable, Filter)
        Page<T> page = service.findPage(pageable, filter); 
        
        // 4. Aggiorniamo la Grid (Sostituzione totale dei dati)
        grid.setItems(page.getContent());
        
        // 5. Aggiorniamo i controlli della UI
        this.totalPages = page.getTotalPages();
        boolean hasData = totalPages > 0;
        
        grid.setVisible(hasData);
        pageInfo.setVisible(hasData);
        next.setVisible(hasData);
        prev.setVisible(hasData);
    	
    	  if (!hasData) {
            Notification.show("Nessun dato trovato", 3000, Notification.Position.MIDDLE);
        }
        
        pageInfo.setText("Pagina " + (pageNumber + 1) + " di " + (totalPages == 0 ? 1 : totalPages));
        prev.setEnabled(pageNumber > 0);
        next.setEnabled(pageNumber < totalPages - 1);;
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