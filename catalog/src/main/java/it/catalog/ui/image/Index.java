package it.catalog.ui.image;



import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;

import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.renderer.LitRenderer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import it.catalog.persistence.repository.TagRepository;
import it.catalog.service.dto.ImageDto;
import it.catalog.service.dto.TagDto;
import it.catalog.service.dto.search.DtoFilter;
import it.catalog.service.impl.ImageFileServiceImpl;
import it.catalog.ui.common.MainLayout;
import it.catalog.ui.utility.AbstractSearchView;

@Route(value = "images", layout = MainLayout.class)
@PageTitle("Immagini")
public class Index extends AbstractSearchView<ImageDto, DtoFilter> {

	DateTimeFormatter FORMAT_DATETIME = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
	DateTimeFormatter FORMAT_DATE = DateTimeFormatter.ofPattern("dd/MM/yyyy").withZone(ZoneId.systemDefault());

    public Index(ImageFileServiceImpl service, TagRepository tagRepo) {
       
    	super(service, ImageDto.class, "Archivio Immagini", DtoFilter::new);

		// Inizializza i criteri di ricerca basandosi sulle chiavi delle colonne
		initSearchOptionsByGrid();
 
    }

    
    @Override
	protected void configureGrid(Grid<ImageDto> grid) {
    	
    	 
    	 // 1. Aggiungi la colonna del numero di riga come PRIMA colonna
        grid.addColumn(LitRenderer.<ImageDto>of("<span>${index + 1}</span>"))
            .setHeader("#")
            .setFlexGrow(0)
            .setAutoWidth(true)
            .setResizable(true)
            .setKey("rowNumber"); // Chiave opzionale
        
        grid.addColumn(new ComponentRenderer<>(image -> {
      	    Span span = new Span(image.getNome());
      	    if (image.isCancelled())
      	        span.addClassName("riga-cancellata");

      	    return span;
      	}))
         .setResizable(true) // L'utente può allargarla
         .setFlexGrow(0)     // evita che venga ridimensionata automaticamente
         .setAutoWidth(true)   // la colonna si adatti automaticamente al contenuto 
         .setHeader("Titolo").setSortable(true).setKey("nome");
        
        grid.addColumn(new ComponentRenderer<>(image -> {
       	 Span span = new Span(image.getFilename());
       	 if (image.isCancelled())
       		 span.addClassName("riga-cancellata");
       	 return span;
        }))
        .setResizable(true) // L'utente può allargarla
        .setFlexGrow(0)     // evita che venga ridimensionata automaticamente
        .setAutoWidth(true)   // la colonna si adatti automaticamente al contenuto
        .setHeader("Nome File").setSortable(true).setKey("filename");
        
        
        grid.addColumn(new ComponentRenderer<>(image -> {
       	 Span span = new Span(image.getMimeType());
       	 if (image.isCancelled())
       		 span.addClassName("riga-cancellata");

       	 return span;
        }))
        .setResizable(true) // L'utente può allargarla
        .setFlexGrow(0)     // evita che venga ridimensionata automaticamente
        .setAutoWidth(true)   // la colonna si adatti automaticamente al contenuto
        .setHeader("MIME").setSortable(true).setKey("mimeType");
       
        grid.addColumn(new ComponentRenderer<>(image -> {
       	 Span span = new Span(image.getFormato());
       	 if (image.isCancelled())
       		 span.addClassName("riga-cancellata");
       	 return span;
        }))
        .setResizable(true) // L'utente può allargarla
        .setFlexGrow(0)     // evita che venga ridimensionata automaticamente
        .setAutoWidth(true)   // la colonna si adatti automaticamente al contenuto
        .setHeader("Formato").setSortable(true).setKey("formato");
        
        grid.addColumn(new ComponentRenderer<>(image -> {
          	 Span span = new Span(image.getTipoFile());
          	 if (image.isCancelled())
          		 span.addClassName("riga-cancellata");
          	 return span;
           }))
           .setResizable(true) // L'utente può allargarla
           .setFlexGrow(0)     // evita che venga ridimensionata automaticamente
           .setAutoWidth(true)   // la colonna si adatti automaticamente al contenuto
           .setHeader("Tipo").setSortable(true).setKey("tipoFile");

        grid.addColumn(new ComponentRenderer<>(image -> {
        	Span span = new Span(Long.toString(image.getSizeBytes()));   	
        	if (image.isCancelled())
        		span.addClassName("riga-cancellata");
        	return span;
        }))
        .setResizable(true) // L'utente può allargarla
        .setFlexGrow(0)     // evita che venga ridimensionata automaticamente
        .setAutoWidth(true)   // la colonna si adatti automaticamente al contenuto
        .setHeader("Dimensione (byte)").setSortable(true).setKey("sizeBytes");
 
        // caselle checkbox
        grid.addColumn(new ComponentRenderer<>(image -> {
            Checkbox checkbox = new Checkbox(image.isPreferito());
            checkbox.setReadOnly(true); // evita modifiche da parte dell'utente
            if (image.isCancelled()) 
            	checkbox.addClassName("riga-cancellata");
            return checkbox;
        }))
        .setResizable(true) // L'utente può allargarla
        .setFlexGrow(0)     // evita che venga ridimensionata automaticamente
        .setAutoWidth(true)   // la colonna si adatti automaticamente al contenuto
        .setHeader("Preferito").setSortable(true).setKey("preferito");
        
        
        grid.addColumn(new ComponentRenderer<>(image -> {
     		Span span = new Span(image.getRating() != null ? image.getRating().toString() :"0");
     		if (image.isCancelled())
     			span.addClassName("riga-cancellata");
     		return span;
     	}))
        .setResizable(true) // L'utente può allargarla
        .setFlexGrow(0)     // evita che venga ridimensionata automaticamente
        .setAutoWidth(true)   // la colonna si adatti automaticamente al contenuto
        .setHeader("Valutazione").setSortable(true).setKey("rating");
        
        
        
        grid.addColumn(new ComponentRenderer<>(image -> {
      		Span span = new Span(image.getDataArchiviazione() != null ? FORMAT_DATE.format(image.getDataArchiviazione()) : "");
      		if (image.isCancelled()) {
      			span.addClassName("riga-cancellata");
      		}
      		return span;
      	}))
         .setResizable(true) // L'utente può allargarla
         .setFlexGrow(0)     // evita che venga ridimensionata automaticamente
         .setAutoWidth(true)   // la colonna si adatti automaticamente al contenuto
         .setHeader("Archiviazione").setSortable(true).setKey("dataArchiviazione");
       
        grid.addColumn(new ComponentRenderer<>(image -> {
       	 Span span = new Span(image.getDescription());
       	 if (image.isCancelled())
       		 span.addClassName("riga-cancellata");
       	 return span;
        }))
        .setResizable(true) // L'utente può allargarla
        .setFlexGrow(0)     // evita che venga ridimensionata automaticamente
        .setAutoWidth(true)   // la colonna si adatti automaticamente al contenuto
        .setHeader("Descrizione").setSortable(true).setKey("description");
        
        grid.addColumn(new ComponentRenderer<>(image -> {
            Checkbox checkbox = new Checkbox(image.isBackup());
            checkbox.setReadOnly(true); // evita modifiche da parte dell'utente
            if (image.isCancelled()) {
            	checkbox.addClassName("riga-cancellata");
    		}
            return checkbox;
        }))
        .setResizable(true) // L'utente può allargarla
        .setFlexGrow(0)     // evita che venga ridimensionata automaticamente
        .setAutoWidth(true)   // la colonna si adatti automaticamente al contenuto
        .setHeader("Backup").setSortable(true).setKey("backup");
        
        
        grid.addColumn(new ComponentRenderer<>(image -> {
      		Span span = new Span(image.getLastView() != null ? FORMAT_DATETIME.format(image.getLastView().atZone(ZoneId.systemDefault())) : "");
      		if (image.isCancelled()) {
      			span.addClassName("riga-cancellata");
      		}
      		return span;
      	}))
         .setResizable(true) // L'utente può allargarla
         .setFlexGrow(0)     // evita che venga ridimensionata automaticamente
         .setAutoWidth(true)   // la colonna si adatti automaticamente al contenuto
         .setHeader("Ultima Visual").setSortable(true).setKey("lastView");
        
        
        grid.addColumn(new ComponentRenderer<>(image -> {
      		Span span = new Span(String.valueOf(image.getVisualizzazioni()));
      		if (image.isCancelled()) {
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

        grid.addColumn(new ComponentRenderer<>(image -> {
       	 Span span = new Span(image.getNote());
       	 if (image.isCancelled()) {
       		 span.addClassName("riga-cancellata");
       	 }
       	 return span;
        }))
        .setResizable(true) // L'utente può allargarla
        .setFlexGrow(0)     // evita che venga ridimensionata automaticamente
        .setAutoWidth(true)   // la colonna si adatti automaticamente al contenuto
        .setHeader("Note").setSortable(true).setKey("note");
        
        grid.addColumn(new ComponentRenderer<>(image -> {
            Checkbox checkbox = new Checkbox(image.isCancelled());
            checkbox.setReadOnly(true); // evita modifiche da parte dell'utente
            if (image.isCancelled()) {
            	checkbox.addClassName("riga-cancellata");
    		}
            return checkbox;
        }))
        .setResizable(true) // L'utente può allargarla
        .setFlexGrow(0)     // evita che venga ridimensionata automaticamente
        .setAutoWidth(true)   // la colonna si adatti automaticamente al contenuto
        .setHeader("Cancelled").setSortable(true).setKey("cancelled");
        
         grid.addComponentColumn(item -> {
             Anchor edit = new Anchor("images-form/" + item.getId() + "/false", "modifica");
             
             Anchor del = new Anchor("images", "cancella");
             del.getElement().addEventListener("click", ev -> {
            	 conferma(item.getId(), "Sei sicuro di voler cancellare questo elemento ?");
             });
             
             Anchor recovery = new Anchor("images", "ripristino");
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
 				.ifPresent(ui -> ui.navigate("images-form/" + e.getItem().getId() + "/true")));
    	
    }
 
    
    @Override
	protected void navigateToForm(Long id) {
		// Gestisci la navigazione al form (nuovo o modifica)
		String route = "images-form/" + (id != null ? id : "0") + "/false";
		getUI().ifPresent(ui -> ui.navigate(route));
	}
}
