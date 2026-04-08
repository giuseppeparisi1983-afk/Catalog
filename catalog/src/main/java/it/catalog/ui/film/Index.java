package it.catalog.ui.film;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.renderer.LitRenderer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility.Gap;
import com.vaadin.flow.theme.lumo.LumoUtility.Padding;

import it.catalog.persistence.repository.TagRepository;
import it.catalog.service.dto.FilmDto;
import it.catalog.service.dto.TagDto;
import it.catalog.service.dto.search.DtoFilter;
import it.catalog.service.impl.FilmServiceImpl;
import it.catalog.ui.common.MainLayout;
import it.catalog.ui.utility.AbstractSearchView;

@Route(value="film", layout = MainLayout.class)
@PageTitle("Film")
//@Uses(Icon.class)
public class Index  extends AbstractSearchView<FilmDto, DtoFilter> {

	DateTimeFormatter FORMAT_DATETIME = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
	DateTimeFormatter FORMAT_DATE = DateTimeFormatter.ofPattern("dd/MM/yyyy").withZone(ZoneId.systemDefault());

	
	
    public Index(FilmServiceImpl service, TagRepository tagRepo) {
        
		super(service, FilmDto.class, "Archivio Film", DtoFilter::new);
		
		VerticalLayout layoutColumn2 = new VerticalLayout();
        H1 h1 = new H1();
        HorizontalLayout layoutRow = new HorizontalLayout();
        ComboBox comboBox = new ComboBox();
        TextField textField = new TextField();
        ComboBox comboBox2 = new ComboBox();
        Button buttonPrimary = new Button();
//        Grid multiSelectGrid = new Grid(SamplePerson.class);
//        getContent().setWidth("100%");
//        getContent().getStyle().set("flex-grow", "1");
        layoutColumn2.setWidthFull();
//        getContent().setFlexGrow(1.0, layoutColumn2);
        layoutColumn2.setWidth("100%");
        layoutColumn2.getStyle().set("flex-grow", "1");
        h1.setText("Archivio Film");
        h1.setWidth("max-content");
        layoutRow.setWidthFull();
        layoutColumn2.setFlexGrow(1.0, layoutRow);
        layoutRow.addClassName(Gap.MEDIUM);
        layoutRow.addClassName(Padding.SMALL);
        layoutRow.setWidth("100%");
        layoutRow.getStyle().set("flex-grow", "1");
        layoutRow.setAlignItems(Alignment.CENTER);
        layoutRow.setJustifyContentMode(JustifyContentMode.START);
        comboBox.setLabel("Criterio");
        comboBox.setWidth("min-content");
        setComboBoxSampleData(comboBox);
        textField.setLabel("Text field");
        textField.setWidth("min-content");
        comboBox2.setLabel("Tags");
        comboBox2.setWidth("min-content");
        setComboBoxSampleData(comboBox2);
        buttonPrimary.setText("Nuovo");
        buttonPrimary.setWidth("min-content");
        buttonPrimary.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
//        multiSelectGrid.setSelectionMode(Grid.SelectionMode.MULTI);
//        multiSelectGrid.setWidth("100%");
//        multiSelectGrid.getStyle().set("flex-grow", "0");
//        setGridSampleData(multiSelectGrid);
//        getContent().add(layoutColumn2);
        layoutColumn2.add(h1);
        layoutColumn2.add(layoutRow);
        layoutRow.add(comboBox);
        layoutRow.add(textField);
        layoutRow.add(comboBox2);
        layoutRow.add(buttonPrimary);
//        layoutColumn2.add(multiSelectGrid);
		
		// Inizializza i criteri di ricerca basandosi sulle chiavi delle colonne
		initSearchOptionsByGrid();
    }

 @Override
	protected void configureGrid(Grid<FilmDto> grid) {
    	
    	 
    	 // 1. Aggiungi la colonna del numero di riga come PRIMA colonna
        grid.addColumn(LitRenderer.<FilmDto>of("<span>${index + 1}</span>"))
            .setHeader("#")
            .setFlexGrow(0)
            .setAutoWidth(true)
            .setResizable(true)
            .setKey("rowNumber"); // Chiave opzionale
        
        grid.addColumn(new ComponentRenderer<>(film -> {
      	    Span span = new Span(film.getNome());
      	    if (film.isCancelled())
      	        span.addClassName("riga-cancellata");

      	    return span;
      	}))
         .setResizable(true) // L'utente può allargarla
         .setFlexGrow(0)     // evita che venga ridimensionata automaticamente
         .setAutoWidth(true)   // la colonna si adatti automaticamente al contenuto 
         .setHeader("Titolo").setSortable(true).setKey("nome");

        grid.addColumn(new ComponentRenderer<>(film -> {
        	Span span = new Span(film.getGenere());
        	if (film.isCancelled())
        		span.addClassName("riga-cancellata");
        	
        	return span;
        }))
        .setResizable(true) // L'utente può allargarla
        .setFlexGrow(0)     // evita che venga ridimensionata automaticamente
        .setAutoWidth(true)   // la colonna si adatti automaticamente al contenuto 
        .setHeader("Genere").setSortable(true).setKey("genere");

        grid.addColumn(new ComponentRenderer<>(film -> {
        	Span span = new Span(String.valueOf(film.getAnnoUscita()));
        	if (film.isCancelled())
        		span.addClassName("riga-cancellata");
        	
        	return span;
        }))
        .setResizable(true) // L'utente può allargarla
        .setFlexGrow(0)     // evita che venga ridimensionata automaticamente
        .setAutoWidth(true)   // la colonna si adatti automaticamente al contenuto 
        .setHeader("Anno").setSortable(true).setKey("annoUscita");
		 
		 
		  grid.addColumn(new ComponentRenderer<>(film -> {
       	 Span span = new Span(film.getFilename());
       	 if (film.isCancelled())
       		 span.addClassName("riga-cancellata");
       	 return span;
        }))
        .setResizable(true) // L'utente può allargarla
        .setFlexGrow(0)     // evita che venga ridimensionata automaticamente
        .setAutoWidth(true)   // la colonna si adatti automaticamente al contenuto
        .setHeader("Nome File").setSortable(true).setKey("filename");

		  grid.addColumn(new ComponentRenderer<>(film -> {
			  Span span = new Span(film.getPathFile());
			  if (film.isCancelled())
				  span.addClassName("riga-cancellata");
			  return span;
		  }))
		  .setResizable(true) // L'utente può allargarla
		  .setFlexGrow(0)     // evita che venga ridimensionata automaticamente
		  .setAutoWidth(true)   // la colonna si adatti automaticamente al contenuto
		  .setHeader("Path").setSortable(true).setKey("pathFile");
		
		
		 grid.addColumn(new ComponentRenderer<>(film -> {
       	 Span span = new Span(film.getFormato());
       	 if (film.isCancelled())
       		 span.addClassName("riga-cancellata");
       	 return span;
        }))
        .setResizable(true) // L'utente può allargarla
        .setFlexGrow(0)     // evita che venga ridimensionata automaticamente
        .setAutoWidth(true)   // la colonna si adatti automaticamente al contenuto
        .setHeader("Formato").setSortable(true).setKey("formato");

        grid.addColumn(new ComponentRenderer<>(film -> {
        	Span span = new Span(Long.toString(film.getSizeBytes()));   	
        	if (film.isCancelled())
        		span.addClassName("riga-cancellata");
        	return span;
        }))
        .setResizable(true) // L'utente può allargarla
        .setFlexGrow(0)     // evita che venga ridimensionata automaticamente
        .setAutoWidth(true)   // la colonna si adatti automaticamente al contenuto
        .setHeader("Dimensione (byte)").setSortable(true).setKey("sizeBytes");
 
        // caselle checkbox
        grid.addColumn(new ComponentRenderer<>(film -> {
            Checkbox checkbox = new Checkbox(film.isPreferito());
            checkbox.setReadOnly(true); // evita modifiche da parte dell'utente
            if (film.isCancelled()) 
            	checkbox.addClassName("riga-cancellata");
            return checkbox;
        }))
        .setResizable(true) // L'utente può allargarla
        .setFlexGrow(0)     // evita che venga ridimensionata automaticamente
        .setAutoWidth(true)   // la colonna si adatti automaticamente al contenuto
        .setHeader("Preferito").setSortable(true).setKey("preferito");
        
        
        grid.addColumn(new ComponentRenderer<>(film -> {
     		Span span = new Span(film.getVoto() != null ? film.getVoto().toString() :"0");
     		if (film.isCancelled())
     			span.addClassName("riga-cancellata");
     		return span;
     	}))
        .setResizable(true) // L'utente può allargarla
        .setFlexGrow(0)     // evita che venga ridimensionata automaticamente
        .setAutoWidth(true)   // la colonna si adatti automaticamente al contenuto
        .setHeader("Valutazione").setSortable(true).setKey("voto");
        
        
        grid.addColumn(new ComponentRenderer<>(film -> {
          	 Span span = new Span(film.getRegista());
          	 if (film.isCancelled()) {
          		 span.addClassName("riga-cancellata");
          	 }
          	 return span;
           }))
           .setResizable(true) // L'utente può allargarla
           .setFlexGrow(0)     // evita che venga ridimensionata automaticamente
           .setAutoWidth(true)   // la colonna si adatti automaticamente al contenuto
           .setHeader("Regista").setSortable(true).setKey("regista");
        
        grid.addColumn(new ComponentRenderer<>(film -> {
          	 Span span = new Span(film.getProtagonisti());
          	 if (film.isCancelled()) {
          		 span.addClassName("riga-cancellata");
          	 }
          	 return span;
           }))
           .setResizable(true) // L'utente può allargarla
           .setFlexGrow(0)     // evita che venga ridimensionata automaticamente
           .setAutoWidth(true)   // la colonna si adatti automaticamente al contenuto
           .setHeader("Protagonisti").setSortable(true).setKey("protagonisti");
        
        grid.addColumn(new ComponentRenderer<>(film -> {
         	 Span span = new Span(film.getTrama());
         	 if (film.isCancelled()) {
         		 span.addClassName("riga-cancellata");
         	 }
         	 return span;
          }))
          .setResizable(true) // L'utente può allargarla
          .setFlexGrow(0)     // evita che venga ridimensionata automaticamente
          .setAutoWidth(true)   // la colonna si adatti automaticamente al contenuto
          .setHeader("Trama").setSortable(true).setKey("trama");
        
        
        grid.addColumn(new ComponentRenderer<>(film -> {
          	 Span span = new Span(film.getDurata());
          	 if (film.isCancelled()) {
          		 span.addClassName("riga-cancellata");
          	 }
          	 return span;
           }))
           .setResizable(true) // L'utente può allargarla
           .setFlexGrow(0)     // evita che venga ridimensionata automaticamente
           .setAutoWidth(true)   // la colonna si adatti automaticamente al contenuto
           .setHeader("Durata").setSortable(true).setKey("durata");
        
		  grid.addColumn(new ComponentRenderer<>(film -> {
	          	 Span span = new Span(film.getTrailer());
	          	 if (film.isCancelled())
	          		 span.addClassName("riga-cancellata");
	          	 return span;
	           }))
	           .setResizable(true) // L'utente può allargarla
	           .setFlexGrow(0)     // evita che venga ridimensionata automaticamente
	           .setAutoWidth(true)   // la colonna si adatti automaticamente al contenuto
	           .setHeader("Trailer").setSortable(true).setKey("trailer");
		  
        grid.addColumn(new ComponentRenderer<>(film -> {
      		Span span = new Span(film.getDataArchiviazione() != null ? FORMAT_DATE.format(film.getDataArchiviazione()) : "");
      		if (film.isCancelled()) {
      			span.addClassName("riga-cancellata");
      		}
      		return span;
      	}))
         .setResizable(true) // L'utente può allargarla
         .setFlexGrow(0)     // evita che venga ridimensionata automaticamente
         .setAutoWidth(true)   // la colonna si adatti automaticamente al contenuto
         .setHeader("Archiviazione").setSortable(true).setKey("dataArchiviazione");
		 
		  grid.addColumn(new ComponentRenderer<>(film -> {
            Checkbox checkbox = new Checkbox(film.isBackup());
            checkbox.setReadOnly(true); // evita modifiche da parte dell'utente
            if (film.isCancelled()) {
            	checkbox.addClassName("riga-cancellata");
    		}
            return checkbox;
        }))
        .setResizable(true) // L'utente può allargarla
        .setFlexGrow(0)     // evita che venga ridimensionata automaticamente
        .setAutoWidth(true)   // la colonna si adatti automaticamente al contenuto
        .setHeader("Backup").setSortable(true).setKey("backup");
        
        
        grid.addColumn(new ComponentRenderer<>(film -> {
      		Span span = new Span(film.getLastView() != null ? FORMAT_DATETIME.format(film.getLastView().atZone(ZoneId.systemDefault())) : "");
      		if (film.isCancelled()) {
      			span.addClassName("riga-cancellata");
      		}
      		return span;
      	}))
         .setResizable(true) // L'utente può allargarla
         .setFlexGrow(0)     // evita che venga ridimensionata automaticamente
         .setAutoWidth(true)   // la colonna si adatti automaticamente al contenuto
         .setHeader("Ultima Visual").setSortable(true).setKey("lastView");

        grid.addColumn(new ComponentRenderer<>(film -> {
        	Span span = new Span(film.getLastView() != null ? FORMAT_DATETIME.format(film.getLastUpdate().atZone(ZoneId.systemDefault())) : "");
        	if (film.isCancelled()) {
        		span.addClassName("riga-cancellata");
        	}
        	return span;
        }))
        .setResizable(true) // L'utente può allargarla
        .setFlexGrow(0)     // evita che venga ridimensionata automaticamente
        .setAutoWidth(true)   // la colonna si adatti automaticamente al contenuto
        .setHeader("Aggiornamento").setSortable(true).setKey("lastUpdate");
        
        
        grid.addColumn(new ComponentRenderer<>(film -> {
      		Span span = new Span(String.valueOf(film.getVisualizzazioni()));
      		if (film.isCancelled()) {
      			span.addClassName("riga-cancellata");
      		}
      		return span;
      	}))
         .setResizable(true) // L'utente può allargarla
         .setFlexGrow(0)     // evita che venga ridimensionata automaticamente
         .setAutoWidth(true)   // la colonna si adatti automaticamente al contenuto
         .setHeader("Visual").setSortable(true).setKey("visualizzazioni");
        
        // colonna custom per i tag
     	grid.addColumn(p -> {
     	    if (p.getTags() == null) return "";
     	    return p.getTags().stream()
     	             .map(TagDto::getNomeTag)   // prendi solo il nome
     	             .collect(Collectors.joining(", "));
     	})
     	.setResizable(true) // L'utente può allargarla
        .setFlexGrow(0)     // evita che venga ridimensionata automaticamente
        .setAutoWidth(true)   // la colonna si adatti automaticamente al contenuto
     	.setHeader("Tags")
     	/** per il ritorno a capo del valore*/
     	.setRenderer(new ComponentRenderer<>(item -> {
     	    Div div = new Div();
     	    div.setText(item.getTags().stream()
    	             .map(TagDto::getNomeTag)   // prendi solo il nome
    	             .collect(Collectors.joining(", ")));
     	    // Applica uno stile CSS per forzare il 'word-wrap'
     	    div.getElement().getStyle().set("white-space", "normal"); 
     	   if (item.isCancelled())
     		  div.addClassName("riga-cancellata");
     	    return div;
     	})).setSortable(true).setKey("tags");

        grid.addColumn(new ComponentRenderer<>(film -> {
       	 Span span = new Span(film.getNote());
       	 if (film.isCancelled()) {
       		 span.addClassName("riga-cancellata");
       	 }
       	 return span;
        }))
        .setResizable(true) // L'utente può allargarla
        .setFlexGrow(0)     // evita che venga ridimensionata automaticamente
        .setAutoWidth(true)   // la colonna si adatti automaticamente al contenuto
        .setHeader("Note").setSortable(true).setKey("note");
        
        grid.addColumn(new ComponentRenderer<>(film -> {
            Checkbox checkbox = new Checkbox(film.isCancelled());
            checkbox.setReadOnly(true); // evita modifiche da parte dell'utente
            if (film.isCancelled()) {
            	checkbox.addClassName("riga-cancellata");
    		}
            return checkbox;
        }))
        .setResizable(true) // L'utente può allargarla
        .setFlexGrow(0)     // evita che venga ridimensionata automaticamente
        .setAutoWidth(true)   // la colonna si adatti automaticamente al contenuto
        .setHeader("Cancelled").setSortable(true).setKey("cancelled");
        
         grid.addComponentColumn(item -> {
             Anchor edit = new Anchor("film-form/" + item.getId() + "/false", "modifica");
             
             Anchor del = new Anchor("film", "cancella");
             del.getElement().addEventListener("click", ev -> {
            	 conferma(item.getId(), "Sei sicuro di voler cancellare questo elemento ?");
             });
             
             Anchor recovery = new Anchor("film", "ripristino");
 			recovery.getElement().addEventListener("click", ev -> {
 				conferma(item.getId(), "Stai ripristinando questo elemento. Sei sicuro di volerlo fare ?");
 			});
             
 			if (item.isCancelled()) {
 				recovery.setVisible(true);
				del.setVisible(false);
			} else {
				recovery.setVisible(false);
				del.setVisible(true);
			}
             
             return new HorizontalLayout(edit, del, recovery);
         })
         .setResizable(true) // L'utente può allargarla
 		.setFlexGrow(0) // evita che venga ridimensionata automaticamente
 		.setAutoWidth(true) // la colonna si adatti automaticamente al contenuto
         .setHeader("Azioni");

         
         grid.getColumns().forEach(col -> col.setAutoWidth(true)); // adatta alla pagina

      // rendi la tabella interattiva
 		grid.addItemClickListener(e -> getUI()
 				.ifPresent(ui -> ui.navigate("film-form/" + e.getItem().getId() + "/true")));
		 
	}



    record SampleItem(String value, String label, Boolean disabled) {
    }

    private void setComboBoxSampleData(ComboBox comboBox) {
        List<SampleItem> sampleItems = new ArrayList<>();
        sampleItems.add(new SampleItem("first", "First", null));
        sampleItems.add(new SampleItem("second", "Second", null));
        sampleItems.add(new SampleItem("third", "Third", Boolean.TRUE));
        sampleItems.add(new SampleItem("fourth", "Fourth", null));
        comboBox.setItems(sampleItems);
        comboBox.setItemLabelGenerator(item -> ((SampleItem) item).label());
    }

//    private void setGridSampleData(Grid grid) {
//        grid.setItems(query -> samplePersonService.list(VaadinSpringDataHelpers.toSpringPageRequest(query)).stream());
//    }

    @Override
	protected void navigateToForm(Long id) {
		// Gestisci la navigazione al form (nuovo o modifica)
		String route = "film-form/" + (id != null ? id : "0") + "/false";
		getUI().ifPresent(ui -> ui.navigate(route));
	}
}
