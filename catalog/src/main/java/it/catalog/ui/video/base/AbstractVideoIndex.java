package it.catalog.ui.video.base;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
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

import it.catalog.service.dto.GuitarDto;
import it.catalog.service.dto.TagDto;
import it.catalog.service.dto.VideoRecord;
import it.catalog.service.dto.search.DtoFilter;
import it.catalog.service.interfaces.SearchService;
import it.catalog.ui.common.AbstractSearchView;

/*Qui definisci le colonne della Grid comuni a tutti i video.*/
//T è il tipo del DTO (VideoDto o GuitarDto)
public abstract class AbstractVideoIndex<T extends VideoRecord> extends AbstractSearchView<T, DtoFilter> {

	protected DateTimeFormatter FORMAT_DATETIME = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
	protected DateTimeFormatter FORMAT_DATE = DateTimeFormatter.ofPattern("dd/MM/yyyy");

	public AbstractVideoIndex(SearchService<T, DtoFilter> service, Class<T> beanType, String title) {
		// Passiamo DtoFilter::new alla classe base
		super(service, beanType, title, DtoFilter::new);
	}

	@Override
	protected void configureGrid(Grid<T> grid) {
		// 1. Colonna Numero riga (Calcolata lato client)
		grid.addColumn(LitRenderer.<T>of("<span>${index + 1 + " + (pageNumber * pageSize) + "}</span>")).setHeader("#")
				.setFlexGrow(0).setAutoWidth(true).setKey("rowNumber");

		// 2. Colonne Video comuni
		grid.addColumn(new ComponentRenderer<>(video -> {
			Span span = new Span(video.getTitolo());
			if (video.isCancelled())
				span.addClassName("riga-cancellata");
			return span;
		})).setHeader("Titolo").setSortable(true).setKey((dtoClass.equals(GuitarDto.class)) ? "video.titolo" : "titolo");

		grid.addColumn(new ComponentRenderer<>(video -> {
			Span span = new Span(video.getCategoria() != null ? video.getCategoria().getLabel() : "");
			if (video.isCancelled())
				span.addClassName("riga-cancellata");
			return span;
		})).setHeader("Categoria").setSortable(true).setKey((dtoClass.equals(GuitarDto.class)) ? "video.categoria" : "categoria");

		grid.addColumn(new ComponentRenderer<>(video -> {
			Span span = new Span(video.getRating() != null ? video.getRating().toString() : "0");
			if (video.isCancelled())
				span.addClassName("riga-cancellata");
			return span;
		})).setHeader("Rating").setSortable(true).setKey((dtoClass.equals(GuitarDto.class)) ? "video.rating" : "rating");

		 grid.addColumn(new ComponentRenderer<>(video -> {
	      		Span span = new Span(String.valueOf(video.getVisualizzazioni()));
	      		if (video.isCancelled()) {
	      			span.addClassName("riga-cancellata");
	      		}
	      		return span;
	      	}))
	         .setResizable(true) // L'utente può allargarla
	         .setFlexGrow(0)     // evita che venga ridimensionata automaticamente
	         .setAutoWidth(true)   // la colonna si adatti automaticamente al contenuto
	         .setHeader("Visual").setSortable(true).setKey((dtoClass.equals(GuitarDto.class)) ? "video.visualizzazioni" : "visualizzazioni");
		
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
	         .setHeader("Ultima Visual").setSortable(true).setKey((dtoClass.equals(GuitarDto.class)) ? "video.lastView" : "lastView");
		  
		grid.addColumn(new ComponentRenderer<>(video -> {
			Checkbox cb = new Checkbox(video.getPreferito() != null && video.getPreferito());
			cb.setReadOnly(true);
			if (video.isCancelled())
				cb.addClassName("riga-cancellata");
			return cb;
		})).setHeader("Preferito").setSortable(true).setKey((dtoClass.equals(GuitarDto.class)) ? "video.preferito" : "preferito");

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
        .setHeader("Aggiornamento").setSortable(true).setKey((dtoClass.equals(GuitarDto.class)) ? "video.lastUpdate" : "lastUpdate");
        
		grid.addColumn(new ComponentRenderer<>(video -> {
			Span span = new Span(
					video.getDataArchiviazione() != null ? FORMAT_DATE.format(video.getDataArchiviazione()) : "");
			if (video.isCancelled())
				span.addClassName("riga-cancellata");
			return span;
		})).setHeader("Archiviazione").setSortable(true).setKey((dtoClass.equals(GuitarDto.class)) ? "video.dataArchiviazione" : "dataArchiviazione");

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
         .setHeader("Durata (min)").setSortable(true).setKey((dtoClass.equals(GuitarDto.class)) ? "video.durataMin" : "durataMin");
		
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
     	})).setSortable(true).setKey((dtoClass.equals(GuitarDto.class)) ? "video.tags" : "tags");
		
		
		   grid.addColumn(new ComponentRenderer<>(video -> {
	        	 Span span = new Span(video.getPercorsoFile());
	        	 if (video.isCancelled()) {
	        		 span.addClassName("riga-cancellata");
	        	 }
	        	 return span;
	         }))
	         .setResizable(true) // L'utente può allargarla
	         .setFlexGrow(0)     // evita che venga ridimensionata automaticamente
	         .setAutoWidth(true)   // la colonna si adatti automaticamente al contenuto
	         .setHeader("Path").setSortable(true).setKey((dtoClass.equals(GuitarDto.class)) ? "video.percorsoFile" : "percorsoFile");
		   
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
         .setHeader("Backup").setSortable(true).setKey((dtoClass.equals(GuitarDto.class)) ? "video.backup" : "backup");
		 
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
	         .setHeader("Note").setSortable(true).setKey((dtoClass.equals(GuitarDto.class)) ? "video.note" : "note");
		  
		// Gancio per colonne extra dei figli
		addExtraColumns(grid);

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
         .setHeader("Cancelled").setSortable(true).setKey((dtoClass.equals(GuitarDto.class)) ? "video.cancelled" :"cancelled");
		// Colonna Azioni
		grid.addComponentColumn(this::createActionButtons).setHeader("Azioni").setFlexGrow(0).setAutoWidth(true)
				.setKey("Azioni");

		grid.setColumnReorderingAllowed(true);
		grid.getColumns().forEach(col -> {
			col.setAutoWidth(true);
			col.setResizable(true);
		});

		// Navigazione al click sulla riga (sola lettura)
		grid.addItemClickListener(event -> navigateToForm(event.getItem().getId(), true));

		// Carica i criteri di ricerca dalle chiavi della grid
		initSearchOptionsByGrid();
	}

	protected abstract void addExtraColumns(Grid<T> grid);

	protected abstract void navigateToForm(Integer id, boolean viewMode);

	private Component createActionButtons(T video) {
		Anchor edit = new Anchor("#", "modifica");
		edit.getElement().addEventListener("click", e -> navigateToForm(video.getId(), false));

		Button delete = new Button("Cancella", e -> conferma(video.getId().longValue(), "Eliminare questo video?"));
		delete.setVisible(!video.isCancelled());
		delete.addThemeVariants(ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_ERROR);

		Button recovery = new Button("Ripristina",
				e -> conferma(video.getId().longValue(), "Ripristinare questo video?"));
		recovery.setVisible(video.isCancelled());
		recovery.addThemeVariants(ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_SUCCESS);

		return new HorizontalLayout(edit, delete, recovery);
	}
}