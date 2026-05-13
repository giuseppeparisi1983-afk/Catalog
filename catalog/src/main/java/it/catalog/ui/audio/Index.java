package it.catalog.ui.audio;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.renderer.LitRenderer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import it.catalog.service.dto.AudioDto;
import it.catalog.service.dto.TagDto;
import it.catalog.service.dto.search.DtoFilter;
import it.catalog.service.impl.AudioFileServiceImpl;
import it.catalog.ui.common.AbstractSearchView;
import it.catalog.ui.common.MainLayout;

@Route(value = "audio", layout = MainLayout.class)
@PageTitle("Audio")
public class Index extends AbstractSearchView<AudioDto, DtoFilter> {

    //private final AudioFileService service;
    
    DateTimeFormatter FORMAT_DATETIME = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
	DateTimeFormatter FORMAT_DATE = DateTimeFormatter.ofPattern("dd/MM/yyyy").withZone(ZoneId.systemDefault()); 

    public Index(AudioFileServiceImpl service) {
 
     // Passiamo: service, classe DTO, Titolo, e costruttore del filtro
        super(service, AudioDto.class, "Archivio Audio", DtoFilter::new);
        
        // Inizializza i criteri di ricerca basandosi sulle chiavi delle colonne
        initSearchOptionsByGrid();

    }

    @Override
    protected void configureGrid(Grid<AudioDto> grid) {
    	
    	   // 1. Aggiungi la colonna del numero di riga come PRIMA colonna
        grid.addColumn(LitRenderer.<AudioDto>of("<span>${index + 1}</span>"))
            .setHeader("#")
            .setFlexGrow(0)
            .setAutoWidth(true)
            .setResizable(true)
            .setKey("rowNumber"); // Chiave opzionale
    	
         grid.addColumn(new ComponentRenderer<>(audio -> {
      	    Span span = new Span(audio.getNome());
      	    if (audio.isCancelled()) {
      	        span.addClassName("riga-cancellata");
      	    }
      	    return span;
      	}))
         .setResizable(true) // L'utente può allargarla
         .setFlexGrow(0)     // evita che venga ridimensionata automaticamente
         .setAutoWidth(true)   // la colonna si adatti automaticamente al contenuto 
         .setHeader("Titolo").setSortable(true).setKey("nome");
         
         grid.addColumn(new ComponentRenderer<>(audio -> {
        	 Span span = new Span(audio.getGenere());
        	 if (audio.isCancelled()) {
        		 span.addClassName("riga-cancellata");
        	 }
        	 return span;
         }))
         .setResizable(true) // L'utente può allargarla
         .setFlexGrow(0)     // evita che venga ridimensionata automaticamente
         .setAutoWidth(true)   // la colonna si adatti automaticamente al contenuto
         .setHeader("Genere").setSortable(true).setKey("genere");

         grid.addColumn(new ComponentRenderer<>(audio -> {
        	 Span span = new Span(audio.getFilename());
        	 if (audio.isCancelled()) {
        		 span.addClassName("riga-cancellata");
        	 }
        	 return span;
         }))
         .setResizable(true) // L'utente può allargarla
         .setFlexGrow(0)     // evita che venga ridimensionata automaticamente
         .setAutoWidth(true)   // la colonna si adatti automaticamente al contenuto
         .setHeader("Nome File").setSortable(true).setKey("filename");

         grid.addColumn(new ComponentRenderer<>(audio -> {
        	 Span span = new Span(audio.getMimeType());
        	 if (audio.isCancelled()) {
        		 span.addClassName("riga-cancellata");
        	 }
        	 return span;
         }))
         .setResizable(true) // L'utente può allargarla
         .setFlexGrow(0)     // evita che venga ridimensionata automaticamente
         .setAutoWidth(true)   // la colonna si adatti automaticamente al contenuto
         .setHeader("MIME").setSortable(true).setKey("mimeType");

         grid.addColumn(new ComponentRenderer<>(audio -> {
        	 Span span = new Span(audio.getFormato());
        	 if (audio.isCancelled()) {
        		 span.addClassName("riga-cancellata");
        	 }
        	 return span;
         }))
         .setResizable(true) // L'utente può allargarla
         .setFlexGrow(0)     // evita che venga ridimensionata automaticamente
         .setAutoWidth(true)   // la colonna si adatti automaticamente al contenuto
         .setHeader("Formato").setSortable(true).setKey("formato");

         grid.addColumn(new ComponentRenderer<>(audio -> {
        	 Span span = new Span(audio.getAutore());
        	 if (audio.isCancelled()) {
        		 span.addClassName("riga-cancellata");
        	 }
        	 return span;
         }))
         .setResizable(true) // L'utente può allargarla
         .setFlexGrow(0)     // evita che venga ridimensionata automaticamente
         .setAutoWidth(true)   // la colonna si adatti automaticamente al contenuto
         .setHeader("Autore").setSortable(true).setKey("autore");

         grid.addColumn(new ComponentRenderer<>(audio -> {
        	 Span span = new Span(audio.getAlbum());
        	 if (audio.isCancelled()) {
        		 span.addClassName("riga-cancellata");
        	 }
        	 return span;
         }))
         .setResizable(true) // L'utente può allargarla
         .setFlexGrow(0)     // evita che venga ridimensionata automaticamente
         .setAutoWidth(true)   // la colonna si adatti automaticamente al contenuto
         .setHeader("Album").setSortable(true).setKey("album");
         
         grid.addColumn(new ComponentRenderer<>(audio -> {
       		Span span = new Span(audio.getAnnoPubblicazione() != null ? audio.getAnnoPubblicazione().toString() : "0");
       		if (audio.isCancelled())
       			span.addClassName("riga-cancellata");
       		return span;
       	}))
         .setResizable(true) // L'utente può allargarla
         .setFlexGrow(0)     // evita che venga ridimensionata automaticamente
         .setAutoWidth(true)   // la colonna si adatti automaticamente al contenuto
         .setHeader("Anno").setSortable(true).setKey("annoPubblicazione");
         
         
         grid.addColumn(new ComponentRenderer<>(audio -> {
             Checkbox checkbox = new Checkbox(audio.isCancelled());
             checkbox.setReadOnly(true); // evita modifiche da parte dell'utente
             if (audio.isCancelled()) {
             	checkbox.addClassName("riga-cancellata");
     		}
             return checkbox;
         }))
         .setResizable(true) // L'utente può allargarla
         .setFlexGrow(0)     // evita che venga ridimensionata automaticamente
         .setAutoWidth(true)   // la colonna si adatti automaticamente al contenuto
         .setHeader("Cancelled").setSortable(true).setKey("cancelled");
         
         
         // caselle checkbox
         grid.addColumn(new ComponentRenderer<>(audio -> {
             Checkbox checkbox = new Checkbox(audio.isPreferito());
             checkbox.setReadOnly(true); // evita modifiche da parte dell'utente
             if (audio.isCancelled()) 
             	checkbox.addClassName("riga-cancellata");
             return checkbox;
         }))
         .setResizable(true) // L'utente può allargarla
         .setFlexGrow(0)     // evita che venga ridimensionata automaticamente
         .setAutoWidth(true)   // la colonna si adatti automaticamente al contenuto
         .setHeader("Preferito").setSortable(true).setKey("preferito");
         
         
         grid.addColumn(new ComponentRenderer<>(audio -> {
      		Span span = new Span(audio.getRating() != null ? audio.getRating().toString() :"0");
      		if (audio.isCancelled())
      			span.addClassName("riga-cancellata");
      		return span;
      	}))
         .setResizable(true) // L'utente può allargarla
         .setFlexGrow(0)     // evita che venga ridimensionata automaticamente
         .setAutoWidth(true)   // la colonna si adatti automaticamente al contenuto
         .setHeader("Valutazione").setSortable(true).setKey("rating");
         
         grid.addColumn(new ComponentRenderer<>(audio -> {
      		Span span = new Span(audio.getDataArchiviazione() != null ? FORMAT_DATE.format(audio.getDataArchiviazione()) : "");
      		if (audio.isCancelled()) {
      			span.addClassName("riga-cancellata");
      		}
      		return span;
      	}))
         .setResizable(true) // L'utente può allargarla
         .setFlexGrow(0)     // evita che venga ridimensionata automaticamente
         .setAutoWidth(true)   // la colonna si adatti automaticamente al contenuto
         .setHeader("Archiviazione").setSortable(true).setKey("dataArchiviazione");
         
         grid.addColumn(new ComponentRenderer<>(audio -> {
        	 Span span = new Span(audio.getDescription());
        	 if (audio.isCancelled()) {
        		 span.addClassName("riga-cancellata");
        	 }
        	 return span;
         }))
         .setResizable(true) // L'utente può allargarla
         .setFlexGrow(0)     // evita che venga ridimensionata automaticamente
         .setAutoWidth(true)   // la colonna si adatti automaticamente al contenuto
         .setHeader("Descrizione").setSortable(true).setKey("description");
         
         grid.addColumn(new ComponentRenderer<>(audio -> {
             Checkbox checkbox = new Checkbox(audio.isBackup());
             checkbox.setReadOnly(true); // evita modifiche da parte dell'utente
             if (audio.isCancelled()) {
             	checkbox.addClassName("riga-cancellata");
     		}
             return checkbox;
         }))
         .setResizable(true) // L'utente può allargarla
         .setFlexGrow(0)     // evita che venga ridimensionata automaticamente
         .setAutoWidth(true)   // la colonna si adatti automaticamente al contenuto
         .setHeader("Backup").setSortable(true).setKey("backup");
         
         
         grid.addColumn(new ComponentRenderer<>(audio -> {
      		Span span = new Span(audio.getDurationSeconds().toString());
      		if (audio.isCancelled()) {
      			span.addClassName("riga-cancellata");
      		}
      		return span;
      	}))
         .setResizable(true) // L'utente può allargarla
         .setFlexGrow(0)     // evita che venga ridimensionata automaticamente
         .setAutoWidth(true)   // la colonna si adatti automaticamente al contenuto
         .setHeader("Durata (min)").setSortable(true).setKey("durationSeconds");
         
         grid.addColumn(new ComponentRenderer<>(audio -> {
      		Span span = new Span(audio.getLastView() != null ? FORMAT_DATETIME.format(audio.getLastView().atZone(ZoneId.systemDefault())) : "");
      		if (audio.isCancelled()) {
      			span.addClassName("riga-cancellata");
      		}
      		return span;
      	}))
         .setResizable(true) // L'utente può allargarla
         .setFlexGrow(0)     // evita che venga ridimensionata automaticamente
         .setAutoWidth(true)   // la colonna si adatti automaticamente al contenuto
         .setHeader("Ultima Visual").setSortable(true).setKey("lastView");

         grid.addColumn(new ComponentRenderer<>(audio -> {
        	 Span span = new Span(audio.getLastUpdate() != null ? FORMAT_DATETIME.format(audio.getLastUpdate().atZone(ZoneId.systemDefault())) : "");
        	 if (audio.isCancelled()) {
        		 span.addClassName("riga-cancellata");
        	 }
        	 return span;
         }))
         .setResizable(true) // L'utente può allargarla
         .setFlexGrow(0)     // evita che venga ridimensionata automaticamente
         .setAutoWidth(true)   // la colonna si adatti automaticamente al contenuto
         .setHeader("Aggiornamento").setSortable(true).setKey("lastUpdate");
         
//         grid.addColumn(AudioDto::getSizeBytes).setHeader("Dimensione (byte)").setSortable(true).setAutoWidth(true).setKey("sizeBytes");
         
         grid.addColumn(new ComponentRenderer<>(audio -> {
       		Span span = new Span(Long.toString(audio.getSizeBytes()));
       		if (audio.isCancelled()) {
       			span.addClassName("riga-cancellata");
       		}
       		return span;
       	}))
         .setResizable(true) // L'utente può allargarla
         .setFlexGrow(0)     // evita che venga ridimensionata automaticamente
         .setAutoWidth(true)   // la colonna si adatti automaticamente al contenuto
         .setHeader("Dimensione (byte)").setSortable(true).setKey("sizeBytes");
         
         
         grid.addColumn(new ComponentRenderer<>(audio -> {
        	 Span span = new Span(audio.getNote());
        	 if (audio.isCancelled()) {
        		 span.addClassName("riga-cancellata");
        	 }
        	 return span;
         }))
         .setResizable(true) // L'utente può allargarla
         .setFlexGrow(0)     // evita che venga ridimensionata automaticamente
         .setAutoWidth(true)   // la colonna si adatti automaticamente al contenuto
         .setHeader("Note").setSortable(true).setKey("note");
         
         
         grid.addColumn(new ComponentRenderer<>(audio -> {
        	 Span span = new Span(audio.getCoverPath());
        	 if (audio.isCancelled()) {
        		 span.addClassName("riga-cancellata");
        	 }
        	 return span;
         }))
         .setResizable(true) // L'utente può allargarla
         .setFlexGrow(0)     // evita che venga ridimensionata automaticamente
         .setAutoWidth(true)   // la colonna si adatti automaticamente al contenuto
         .setHeader("Path").setSortable(true).setKey("coverPath");
         
         grid.addColumn(new ComponentRenderer<>(audio -> {
      		Span span = new Span(String.valueOf(audio.getVisualizzazioni()));
      		if (audio.isCancelled()) {
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
         
         
         grid.addComponentColumn(item -> {
        	 Anchor edit = new Anchor("audio-form?id=" + item.getId()+"&view=false", "modifica");
             Anchor del = new Anchor("audio", "cancella");
             del.getElement().addEventListener("click", ev -> {
            	 conferma(item.getId(),"Sei sicuro di voler cancellare questo elemento ?");
             });
             Anchor rec = new Anchor("audio", "ripristino");
             rec.getElement().addEventListener("click", ev -> {
            	 conferma(item.getId(),"Stai ripristinando questo elemento. Sei sicuro di volerlo fare ?");
             });
             
				if (item.isCancelled()) {
					rec.setVisible(true);
					del.setVisible(false);
				} else {
					rec.setVisible(false);
					del.setVisible(true);
				}

             return new HorizontalLayout(edit, del,rec);
         })
         .setResizable(true) // L'utente può allargarla
         .setFlexGrow(0)     // evita che venga ridimensionata automaticamente
         .setAutoWidth(true)   // la colonna si adatti automaticamente al contenuto
         .setHeader("Azioni");

         
         grid.addItemClickListener(e -> getUI().ifPresent(ui -> ui.navigate("audio-form?id=" + e.getItem().getId()+"&view=true")));
//         grid.setHeight("60vh");

             
//             grid.addSortListener(e -> {
//                 currentSort = Sort.by(e.getSortOrder().stream()
//                     .map(order -> {
//                         String property = order.getSorted().getKey();
//                         Sort.Direction direction = order.getDirection() == SortDirection.ASCENDING
//                                 ? Sort.Direction.ASC
//                                 : Sort.Direction.DESC;
//                         return new Sort.Order(direction, property);
//                     })
//                     .toList());
//                 pageNumber = 0;
//                 refresh();
//             });

    }

//		private void newAudio() {
////   	 RouteParameters params = new RouteParameters("id", String.valueOf("0"));
//			QueryParameters queryParams = QueryParameters.simple(Map.of("view", "false"));
//			getUI().ifPresent(ui -> ui.navigate(Form.class, queryParams));
//		}
//    
    
    @Override
    protected void navigateToForm(Long id) {
        // Gestisci la navigazione al form (nuovo o modifica)
        String route = "audio-form" + (id != null ? "?id=" + id : "");
        getUI().ifPresent(ui -> ui.navigate(route));
    }
    
}

