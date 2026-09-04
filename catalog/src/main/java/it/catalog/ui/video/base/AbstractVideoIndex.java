package it.catalog.ui.video.base;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.renderer.LitRenderer;
import com.vaadin.flow.router.QueryParameters;

import it.catalog.service.dto.GuitarDto;
import it.catalog.service.dto.TagDto;
import it.catalog.service.dto.VideoRecord;
import it.catalog.service.dto.search.DtoFilter;
import it.catalog.service.interfaces.SearchService;
import it.catalog.ui.common.AbstractBaseForm;
import it.catalog.ui.common.AbstractSearchView;

/*Qui definisci le colonne della Grid comuni a tutti i video.*/
//T è il tipo del DTO (VideoDto o GuitarDto)
public abstract class AbstractVideoIndex<R extends VideoRecord> extends AbstractSearchView<R, DtoFilter> {

	protected DateTimeFormatter FORMAT_DATETIME = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
	protected DateTimeFormatter FORMAT_DATE = DateTimeFormatter.ofPattern("dd/MM/yyyy").withZone(ZoneId.systemDefault());

	public AbstractVideoIndex(SearchService<R, DtoFilter> service, Class<R> beanType, String title) {
		// Passiamo DtoFilter::new alla classe base
		super(service, beanType, title, DtoFilter::new);
	}

	 // Helper per gestire il prefisso delle chiavi
    protected String p(String fieldName) {
        return dtoClass.equals(GuitarDto.class) ? "video." + fieldName : fieldName;
    }
    
	@Override
	protected void configureGrid(Grid<R> grid) {
		// 1. Colonna Numero riga (Calcolata lato client)
		grid.addColumn(LitRenderer.<R>of("<span>${index + 1 + " + (pageNumber * pageSize) + "}</span>")).setHeader("#")
				.setFlexGrow(0).setAutoWidth(true).setKey("rowNumber");

		// 2. Colonne Video comuni
		grid.addColumn(new ComponentRenderer<>(video -> {
			Span span = new Span(video.getNome());
			if (video.isCancelled())
				span.addClassName("riga-cancellata");
			return span;
		}))
		.setResizable(true) // L'utente può allargarla
		.setFlexGrow(0)     // evita che venga ridimensionata automaticamente
		.setAutoWidth(true)   // la colonna si adatti automaticamente al contenuto
		.setHeader("Titolo").setSortable(true)
		.setKey(p("nome"));

		grid.addColumn(new ComponentRenderer<>(video -> {
			Span span = new Span(video.getDurataMin().toString());
			if (video.isCancelled()) {
				span.addClassName("riga-cancellata");
			}
			return span;
		}))
		.setResizable(true) // L'utente può allargarla
		.setFlexGrow(0)     // evita che venga ridimensionata automaticamente
		.setAutoWidth(true)   // la colonna si adatti automaticamente al contenuto
		.setHeader("Durata (min)").setSortable(true)
		.setKey(p("durataMin"));

		
		grid.addColumn(new ComponentRenderer<>(video -> {
			Span span = new Span(video.getCategoria() != null ? video.getCategoria().getLabel() : "");
			if (video.isCancelled())
				span.addClassName("riga-cancellata");
			return span;
		}))
		.setResizable(true) // L'utente può allargarla
		.setFlexGrow(0)     // evita che venga ridimensionata automaticamente
		.setAutoWidth(true)   // la colonna si adatti automaticamente al contenuto
		.setHeader("Categoria").setSortable(true).
		setKey(p("categoria"));		
		
		   grid.addColumn(new ComponentRenderer<>(video -> {
	        	 Span span = new Span(video.getPath());
	        	 if (video.isCancelled()) {
	        		 span.addClassName("riga-cancellata");
	        	 }
	        	 return span;
	         }))
	         .setResizable(true) // L'utente può allargarla
	         .setFlexGrow(0)     // evita che venga ridimensionata automaticamente
	         .setAutoWidth(true)   // la colonna si adatti automaticamente al contenuto
	         .setHeader("Path File").setSortable(true)
	         .setKey(p("path"));
		   
		   grid.addColumn(new ComponentRenderer<>(video -> {
		       	 Span span = new Span(video.getEstensione().getLabel());
		       	 if (video.isCancelled())
		       		 span.addClassName("riga-cancellata");
		       	 return span;
		        }))
		        .setResizable(true) // L'utente può allargarla
		        .setFlexGrow(0)     // evita che venga ridimensionata automaticamente
		        .setAutoWidth(true)   // la colonna si adatti automaticamente al contenuto
		        .setHeader("Estensione").setSortable(true).setKey(p("estensione"));
				
				grid.addColumn(new ComponentRenderer<>(video -> {
		        	Span span = new Span(String.valueOf(video.getDimensione()));   	
		        	if (video.isCancelled())
		        		span.addClassName("riga-cancellata");
		        	return span;
		        }))
		        .setResizable(true) // L'utente può allargarla
		        .setFlexGrow(0)     // evita che venga ridimensionata automaticamente
		        .setAutoWidth(true)   // la colonna si adatti automaticamente al contenuto
		        .setHeader("Dimensione (byte)").setSortable(true).setKey(p("dimensione"));
		   
			grid.addColumn(new ComponentRenderer<>(video -> {
				Span span = new Span(
						video.getDataArchiviazione() != null ? FORMAT_DATE.format(video.getDataArchiviazione()) : "");
				if (video.isCancelled())
					span.addClassName("riga-cancellata");
				return span;
			}))
			.setResizable(true) // L'utente può allargarla
			.setFlexGrow(0)     // evita che venga ridimensionata automaticamente
			.setAutoWidth(true)   // la colonna si adatti automaticamente al contenuto
			.setHeader("Data Archiviazione").setSortable(true)
			.setKey(p("dataArchiviazione"));
			
	        grid.addColumn(new ComponentRenderer<>(video -> {
	          	 Span span = new Span(video.getLastUpdate() != null ? FORMAT_DATETIME.format(video.getLastUpdate().atZone(ZoneId.systemDefault())) : "");
	          	 if (video.isCancelled()) {
	          		 span.addClassName("riga-cancellata");
	          	 }
	          	 return span;
	           }))
	           .setResizable(true) // L'utente può allargarla
	           .setFlexGrow(0)     // evita che venga ridimensionata automaticamente
	           .setAutoWidth(true)   // la colonna si adatti automaticamente al contenuto
	           .setHeader("Data Aggiornamento").setSortable(true)
	           .setKey(p("lastUpdate"));
		   
		 grid.addColumn(new ComponentRenderer<>(video -> {
             Checkbox checkbox = new Checkbox(video.getBackup());
             checkbox.setReadOnly(true); // evita modifiche da parte dell'utente
             if (video.isCancelled()) {
             	checkbox.addClassName("riga-cancellata");
     		}
             return checkbox;
         }))
         .setResizable(true) // L'utente può allargarla
         .setFlexGrow(0)     // evita che venga ridimensionata automaticamente
         .setAutoWidth(true)   // la colonna si adatti automaticamente al contenuto
         .setHeader("Backup").setSortable(true)
         .setKey(p("backup"));
		 
		  grid.addColumn(new ComponentRenderer<>(video -> {
	      		Span span = new Span(video.getLastView() != null ? FORMAT_DATETIME.format(video.getLastView().atZone(ZoneId.systemDefault())) : "");
	      		if (video.isCancelled()) {
	      			span.addClassName("riga-cancellata");
	      		}
	      		return span;
	      	}))
	         .setResizable(true) // L'utente può allargarla
	         .setFlexGrow(0)     // evita che venga ridimensionata automaticamente
	         .setAutoWidth(true)   // la colonna si adatti automaticamente al contenuto
	         .setHeader("Ultima Visualizzazione").setSortable(true)
	         .setKey(p("lastView"));
			 
		  grid.addColumn(new ComponentRenderer<>(video -> {
		      		Span span = new Span(video.getVisualizzazioni() != null ? String.valueOf(video.getVisualizzazioni()): "0");
		      		if (video.isCancelled()) {
		      			span.addClassName("riga-cancellata");
		      		}
		      		return span;
		      	}))
		         .setResizable(true) // L'utente può allargarla
		         .setFlexGrow(0)     // evita che venga ridimensionata automaticamente
		         .setAutoWidth(true)   // la colonna si adatti automaticamente al contenuto
		         .setHeader("Visual").setSortable(true)
		         .setKey(p("visualizzazioni"));
			 
			grid.addColumn(new ComponentRenderer<>(video -> {
				Span span = new Span(video.getRating() != null ? video.getRating().toString() : "0");
				if (video.isCancelled())
					span.addClassName("riga-cancellata");
				return span;
			}))
			.setResizable(true) // L'utente può allargarla
			.setFlexGrow(0)     // evita che venga ridimensionata automaticamente
			.setAutoWidth(true)   // la colonna si adatti automaticamente al contenuto
			.setHeader("Valutazione").setSortable(true)
			.setKey(p("rating"));
		 
			grid.addColumn(new ComponentRenderer<>(video -> {
				Checkbox cb = new Checkbox(video.getPreferito() != null && video.getPreferito());
				cb.setReadOnly(true);
				if (video.isCancelled())
					cb.addClassName("riga-cancellata");
				return cb;
			}))
			.setResizable(true) // L'utente può allargarla
			.setFlexGrow(0) // evita che venga ridimensionata automaticamente
			.setAutoWidth(true) // la colonna si adatti automaticamente al contenuto
			.setHeader("Preferito").setSortable(true).setKey(p("preferito"));
		 
		 grid.addColumn(new ComponentRenderer<>(video -> {
         	 Span span = new Span(video.getDescrizione());
         	 if (video.isCancelled()) {
         		 span.addClassName("riga-cancellata");
         	 }
         	 return span;
          }))
          .setResizable(true) // L'utente può allargarla
          .setFlexGrow(0)     // evita che venga ridimensionata automaticamente
          .setAutoWidth(true)   // la colonna si adatti automaticamente al contenuto
          .setHeader("Descrizione").setSortable(true).setKey(p("descrizione"));
		 
		  grid.addColumn(new ComponentRenderer<>(video -> {
	        	 Span span = new Span(video.getNote());
	        	 if (video.isCancelled()) {
	        		 span.addClassName("riga-cancellata");
	        	 }
	        	 return span;
	         }))
	         .setResizable(true) // L'utente può allargarla
	         .setFlexGrow(0)     // evita che venga ridimensionata automaticamente
	         .setAutoWidth(true)   // la colonna si adatti automaticamente al contenuto
	         .setHeader("Note").setSortable(true)
	         .setKey(p("note"));
		  
		// Gancio per colonne extra dei figli
		addExtraColumns(grid);

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
     	})).setSortable(true)
     	.setKey(p("tags"));
		
		 grid.addColumn(new ComponentRenderer<>(video -> {
             Checkbox checkbox = new Checkbox(video.isCancelled());
             checkbox.setReadOnly(true); // evita modifiche da parte dell'utente
             if (video.isCancelled()) {
             	checkbox.addClassName("riga-cancellata");
     		}
             return checkbox;
         }))
         .setResizable(true) // L'utente può allargarla
         .setFlexGrow(0)     // evita che venga ridimensionata automaticamente
         .setAutoWidth(true)   // la colonna si adatti automaticamente al contenuto
         .setHeader("Cancelled").setSortable(true)
         .setKey(p("cancelled"));
		
		 // Colonna Azioni
		grid.addComponentColumn(this::createActionButtons) 
		.setResizable(true) // L'utente può allargarla
 		.setFlexGrow(0) // evita che venga ridimensionata automaticamente
 		.setAutoWidth(true) // la colonna si adatti automaticamente al contenuto
         .setHeader("Azioni");

//		grid.setColumnReorderingAllowed(true);
//		grid.getColumns().forEach(col -> {
//			col.setAutoWidth(true);
//			col.setResizable(true);
//		});

		// Navigazione al click sulla riga (sola lettura)
//		grid.addItemClickListener(event -> navigateToForm(event.getItem().getId(), true));

		// Carica i criteri di ricerca dalle chiavi della grid
		initSearchOptionsByGrid();
	}
	
	// Gestione dell'estensione del file (colonna aggiunta dinamicamente dai figli se necessario)
//	protected void addExtensionColumn(Grid<T> grid) {
//        grid.addColumn(new ComponentRenderer<>(item -> {
//            FileExtension ext = item.getEstensione();
//            String label = (ext != null) ? ext.getLabel() : "-";
//            
//            Span span = new Span(label);
//            
//            // Centralizziamo anche lo stile "cancellato"!
//            if (item.isCancelled()) {
//                span.addClassName("riga-cancellata");
//            }
//            return span;
//        }))
//        .setHeader("Formato")
//        .setSortable(true)
//        // Gestione dinamica della chiave per il sorting sul DB
//        .setKey(dtoClass.equals(GuitarDto.class) ? "video.estensione" : "estensione");
//    }

	protected abstract void addExtraColumns(Grid<R> grid);

//	protected abstract void navigateToForm(Integer id, boolean viewMode);
	
	
	 // Implementiamo il gancio del nonno (AbstractSearchView)
    @Override
    protected void navigateToForm(Long id, Integer position) {
        Map<String, String> params = new HashMap<>();
        params.put(AbstractBaseForm.P_VIEW, id != null ? "true" : "false"); 
        params.put(AbstractBaseForm.P_PAGE, String.valueOf(this.pageNumber));
        // Se abbiamo la posizione (non è un nuovo inserimento), la passiamo
        if (position != null) {
            params.put(AbstractBaseForm.P_POS, String.valueOf(position));
        }
        
        
        // Aggiungiamo i filtri per le freccette (f_val, f_field)
        params.putAll(getFilterMapForUrl()); 

        QueryParameters qp = QueryParameters.simple(params);
        
        // Usiamo il casting alla classe che implementa il parametro
        Class target = getFormClass();
        getUI().ifPresent(ui -> ui.navigate(target, id, qp));
        
    }
	
    private Map<String, String> getFilterMapForUrl() {
        Map<String, String> p = new HashMap<>();
        if (searchField.getValue() != null && !searchField.getValue().isBlank()) {
            p.put(AbstractBaseForm.P_F_VAL, searchField.getValue());
            // Null check per il selettore
            if (searchFieldSelector.getValue() != null) {
                p.put(AbstractBaseForm.P_F_FIELD, searchFieldSelector.getValue().getFieldName());
            }
            
            // Prendiamo il sort dalla Grid o dalla variabile currentSort
    	    if (currentSort != null && currentSort.isSorted()) {
    	        currentSort.forEach(order -> {
    	            p.put(AbstractBaseForm.P_S_PROP, order.getProperty());
    	            p.put(AbstractBaseForm.P_S_DIR, order.getDirection().name());
    	        });
    	    }
            
        }
        return p;
    }
    
    // Metodi che i figli (Video/Chitarra) implementeranno
    protected abstract Class<? extends Component> getFormClass();
    
    protected abstract String getReturnRoute();
    
	private Component createActionButtons(R video) {
		 Anchor edit = new Anchor("video-form/" + video.getId() + "?view=false&page=" +String.valueOf(this.pageNumber), "modifica");
         
         Anchor del = new Anchor("video", "cancella");
         del.getElement().addEventListener("click", ev -> {
        	 conferma(video.getId(), "Sei sicuro di voler cancellare questo elemento ?");
         });
         
         Anchor recovery = new Anchor("video", "ripristino");
			recovery.getElement().addEventListener("click", ev -> {
				conferma(video.getId(), "Stai ripristinando questo elemento. Sei sicuro di volerlo fare ?");
			});
         
			if (video.isCancelled()) {
				recovery.setVisible(true);
			del.setVisible(false);
		} else {
			recovery.setVisible(false);
			del.setVisible(true);
		}
         
         return new HorizontalLayout(edit, del, recovery);
	}
	
}