package it.catalog.ui.video.base;

import java.time.format.DateTimeFormatter;

import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.renderer.LitRenderer;

import it.catalog.service.dto.AudioDto;
import it.catalog.service.dto.VideoDto;

//T è il tipo del DTO (VideoDto o GuitarDto)
public abstract class AbstractVideoIndex<T extends VideoDto> extends VerticalLayout {

 protected Grid<T> grid;
 
	DateTimeFormatter FORMAT_DATETIME = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
	DateTimeFormatter FORMAT_DATE = DateTimeFormatter.ofPattern("dd/MM/yyyy");
	
 // Variabili per le colonne comuni (utili per il riordinamento)
// protected Grid.Column<T> colTitolo;
// protected Grid.Column<T> colCategoria;
// protected Grid.Column<T> colRating;
// protected Grid.Column<T> colPreferito;
// protected Grid.Column<T> colPath;

 public AbstractVideoIndex(Class<T> beanType) {
     setSizeFull();

     // 1. Creiamo la Grid vuota
     grid = new Grid<>(beanType, false);
     //this.grid.setSizeFull();

     // 2. Chiamiamo il metodo che configura le colonne comuni
     configureCommonColumns();

     // 3. Aggiungiamo la grid al layout
     //add(grid);
 }

 // Metodo che definisce le colonne base
 protected void configureCommonColumns() {
	
	   // 1. Aggiungi la colonna del numero di riga come PRIMA colonna
     grid.addColumn(LitRenderer.<T>of("<span>${index + 1}</span>"))
         .setHeader("#")
         .setFlexGrow(0)
         .setAutoWidth(true)
         .setResizable(true)
         .setKey("rowNumber"); // Chiave opzionale
	 
	 
	 // aggiunta delle colonne manualmente
     grid.addColumn(new ComponentRenderer<>(video -> {
 	    Span span = new Span(video.getTitolo());
 	    if (video.isCancelled()) {
 	        span.addClassName("riga-cancellata");
 	    }
 	    return span;
 	}))
     .setHeader("Titolo").setSortable(true).setKey("titolo");
     
     grid.addColumn(new ComponentRenderer<>(video -> {
 		Span span = new Span(video.getCategoria().getLabel());
 		if (video.isCancelled()) 
 			span.addClassName("riga-cancellata");
 		return span;
 	})).setHeader("Categoria").setSortable(true).setKey("categoria");
     
     grid.addColumn(new ComponentRenderer<>(video -> {
 		Span span = new Span(video.getRating().toString());
 		if (video.isCancelled())
 			span.addClassName("riga-cancellata");
 		return span;
 	})).setHeader("Rating").setSortable(true).setKey("rating");
//     grid.addColumn(Video::getPreferito).setHeader("Preferito");
     
     // caselle checkbox
     grid.addColumn(new ComponentRenderer<>(video -> {
         Checkbox checkbox = new Checkbox(video.getPreferito());
         checkbox.setReadOnly(true); // evita modifiche da parte dell'utente
         if (video.isCancelled()) 
         	checkbox.addClassName("riga-cancellata");
         return checkbox;
     })).setHeader("Preferito").setSortable(true).setKey("preferito");
     
     grid.addColumn(new ComponentRenderer<>(video -> {
 		Span span = new Span(video.getDurataMin().toString());
 		if (video.isCancelled()) {
 			span.addClassName("riga-cancellata");
 		}
 		return span;
 	})).setHeader("Durata (min)").setSortable(true).setKey("durata_min");
     
     grid.addColumn(new ComponentRenderer<>(video -> {
 		Span span = new Span(String.valueOf(video.getVisualizzazioni()));
 		if (video.isCancelled()) {
 			span.addClassName("riga-cancellata");
 		}
 		return span;
 	})).setHeader("Views").setSortable(true).setKey("visualizzazioni");
     
     grid.addColumn(new ComponentRenderer<>(video -> {
 		Span span = new Span(video.getUltimaVisualizzazione() != null ? FORMAT_DATETIME.format(video.getUltimaVisualizzazione()) : "");
 		if (video.isCancelled()) {
 			span.addClassName("riga-cancellata");
 		}
 		return span;
 	})).setHeader("Ultima Visual.").setSortable(true).setKey("ultima_visualizzazione");
     
     grid.addColumn(new ComponentRenderer<>(video -> {
 		Span span = new Span(video.getPercorsoFile());
 		if (video.isCancelled())
 			span.addClassName("riga-cancellata");
 		return span;
 	}))
     .setHeader("Path")
//     .setTooltipGenerator(T::getPath) // Tooltip automatico
     .setSortable(true).setKey("percorsoFile");
     
     grid.addColumn(new ComponentRenderer<>(video -> {
 		Span span = new Span(video.getDataArchiviazione() != null ? FORMAT_DATE.format(video.getDataArchiviazione()) : "");
 		if (video.isCancelled()) {
 			span.addClassName("riga-cancellata");
 		}
 		return span;
 	})).setHeader("Archiviazione").setSortable(true).setKey("data_archiviazione");
     
     grid.addColumn(new ComponentRenderer<>(video -> {
 		Span span = new Span(video.getNote());
 		if (video.isCancelled()) {
 			span.addClassName("riga-cancellata");
 		}
 		return span;
 	})).setHeader("Note").setSortable(true).setKey("note");
     
     
     grid.addColumn(new ComponentRenderer<>(video -> {
         Checkbox checkbox = new Checkbox(video.getBackup());
         checkbox.setReadOnly(true); // evita modifiche da parte dell'utente
         if (video.isCancelled()) {
         	checkbox.addClassName("riga-cancellata");
 		}
         return checkbox;
     })).setHeader("Backup").setSortable(true).setKey("backup");
     
     grid.addColumn(new ComponentRenderer<>(video -> {
         Checkbox checkbox = new Checkbox(video.isCancelled());
         checkbox.setReadOnly(true); // evita modifiche da parte dell'utente
         if (video.isCancelled()) {
         	checkbox.addClassName("riga-cancellata");
 		}
         return checkbox;
     })).setHeader("Cancelled").setSortable(true).setKey("cancelled");

//     grid.setSizeFull();
     
     // riordina le colonne manualmente
     grid.setColumnReorderingAllowed(true);
     grid.getColumns().forEach(col -> col.setAutoWidth(true)); // adatta alla pagina
  // Abilita il ridimensionamento per tutte le colonne
     grid.getColumns().forEach(column -> column.setResizable(true));

 }

 public Grid<T> getGrid() {
     return grid;
 }
}
