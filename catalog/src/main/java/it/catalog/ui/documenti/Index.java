package it.catalog.ui.documenti;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.renderer.LitRenderer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import it.catalog.common.enums.StatiDocumento;
import it.catalog.service.dto.DocumentoDto;
import it.catalog.service.dto.TagDto;
import it.catalog.service.dto.search.DtoFilter;
import it.catalog.service.impl.DocumentoServiceImpl;
import it.catalog.ui.common.MainLayout;
import it.catalog.ui.utility.AbstractSearchView;

//@Menu(order = 0, icon = LineAwesomeIconUrl.PENCIL_RULER_SOLID)
@Uses(Icon.class)
@Route(value = "documents", layout = MainLayout.class)
@PageTitle("Documenti")
@CssImport("./styles/styles.css")
public class Index extends AbstractSearchView<DocumentoDto, DtoFilter> {

	DateTimeFormatter FORMAT_DATETIME = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
	DateTimeFormatter FORMAT_DATE = DateTimeFormatter.ofPattern("dd/MM/yyyy").withZone(ZoneId.systemDefault());

	public Index(DocumentoServiceImpl service) {

		super(service, DocumentoDto.class, "Archivio Documenti", DtoFilter::new);

		// Inizializza i criteri di ricerca basandosi sulle chiavi delle colonne
		initSearchOptionsByGrid();

	}

	/*
	 * private void setComboBoxSampleData(ComboBox<String> comboBox) {
	 * 
	 * List<SampleItem> sampleItems = new ArrayList<>(); sampleItems.add(new
	 * SampleItem("first", "First", null)); sampleItems.add(new SampleItem("second",
	 * "Second", null)); sampleItems.add(new SampleItem("third", "Third",
	 * Boolean.TRUE)); sampleItems.add(new SampleItem("fourth", "Fourth", null));
	 * 
	 * List<String> sampleItems =
	 * Arrays.stream(DocumentoDto.class.getDeclaredFields()) .map(Field::getName)
	 * .filter(nome -> !nome.equalsIgnoreCase("idDocumento")) // esclude il campo
	 * "idDocumento" .collect(Collectors.toList()); comboBox.setItems(sampleItems);
	 * // comboBox.setItemLabelGenerator(Item::label);
	 * comboBox.setPlaceholder("Cerca per..."); comboBox.setWidth("min-content");
	 * comboBox.setClearButtonVisible(true);
	 * 
	 * // comboBox.setRenderer(new ComponentRenderer<>(item -> { // ComboBox<Item>
	 * inner = new ComboBox<>(); // inner.setEnabled(!item.disabled()); // return
	 * new Text(item.label()); // })); }
	 */

	@Override
	protected void configureGrid(Grid<DocumentoDto> grid) {

		// 1. Aggiungi la colonna del numero di riga come PRIMA colonna
		grid.addColumn(LitRenderer.<DocumentoDto>of("<span>${index + 1}</span>")).setHeader("#").setFlexGrow(0)
		.setAutoWidth(true).setResizable(true).setKey("rowNumber"); // Chiave opzionale

		grid.addColumn(new ComponentRenderer<>(doc -> {
			Span span = new Span(doc.getNome());
			if (doc.getStato().equals(StatiDocumento.ELIMINATO)) {
				span.addClassName("riga-cancellata");
			}
			return span;
		})).setResizable(true) // L'utente può allargarla
		.setFlexGrow(0) // evita che venga ridimensionata automaticamente
		.setAutoWidth(true) // la colonna si adatti automaticamente al contenuto
		.setHeader("Nome").setSortable(true).setKey("nome");

		grid.addColumn(new ComponentRenderer<>(doc -> {
			Span span = new Span(String.valueOf(doc.getDimensione()));
			if (doc.getStato().equals(StatiDocumento.ELIMINATO)) {
				span.addClassName("riga-cancellata");
			}
			return span;
		})).setResizable(true) // L'utente può allargarla
		.setFlexGrow(0) // evita che venga ridimensionata automaticamente
		.setAutoWidth(true) // la colonna si adatti automaticamente al contenuto
		.setHeader("Dimensione").setSortable(true).setKey("dimensione");

		grid.addColumn(new ComponentRenderer<>(doc -> {
			Span span = new Span(doc.getDescrizione());
			if (doc.getStato().equals(StatiDocumento.ELIMINATO)) {
				span.addClassName("riga-cancellata");
			}
			return span;
		})).setResizable(true) // L'utente può allargarla
		.setFlexGrow(0) // evita che venga ridimensionata automaticamente
		.setAutoWidth(true) // la colonna si adatti automaticamente al contenuto
		.setHeader("Descrizione").setSortable(true).setKey("descrizione");

		grid.addColumn(new ComponentRenderer<>(doc -> {
			Span span = new Span(String.valueOf(doc.getVersione()));
			if (doc.getStato().equals(StatiDocumento.ELIMINATO)) {
				span.addClassName("riga-cancellata");
			}
			return span;
		})).setResizable(true) // L'utente può allargarla
		.setFlexGrow(0) // evita che venga ridimensionata automaticamente
		.setAutoWidth(true) // la colonna si adatti automaticamente al contenuto
		.setHeader("Versione").setSortable(true).setKey("versione");

		grid.addColumn(new ComponentRenderer<>(doc -> {
			Span span = new Span(doc.getStato().getLabel());
			if (doc.getStato().equals(StatiDocumento.ELIMINATO)) {
				span.addClassName("riga-cancellata");
			}
			return span;
		})).setResizable(true) // L'utente può allargarla
		.setFlexGrow(0) // evita che venga ridimensionata automaticamente
		.setAutoWidth(true) // la colonna si adatti automaticamente al contenuto
		.setHeader("Stato").setSortable(true).setKey("stato");

		grid.addColumn(new ComponentRenderer<>(doc -> {
			Span span = new Span(doc.getEstensione());
			if (doc.getStato().equals(StatiDocumento.ELIMINATO)) {
				span.addClassName("riga-cancellata");
			}
			return span;
		})).setResizable(true) // L'utente può allargarla
		.setFlexGrow(0) // evita che venga ridimensionata automaticamente
		.setAutoWidth(true) // la colonna si adatti automaticamente al contenuto
		.setHeader("Estensione").setSortable(true).setKey("estensione");

		grid.addColumn(new ComponentRenderer<>(doc -> {
			Span span = new Span(doc.getOrigine());
			if (doc.getStato().equals(StatiDocumento.ELIMINATO)) {
				span.addClassName("riga-cancellata");
			}
			return span;
		})).setResizable(true) // L'utente può allargarla
		.setFlexGrow(0) // evita che venga ridimensionata automaticamente
		.setAutoWidth(true) // la colonna si adatti automaticamente al contenuto
		.setHeader("Origine").setSortable(true).setKey("origine");

		grid.addColumn(new ComponentRenderer<>(doc -> {
			Span span = new Span(doc.getCategoria().getLabel());
			if (doc.getStato().equals(StatiDocumento.ELIMINATO)) {
				span.addClassName("riga-cancellata");
			}
			return span;
		})).setResizable(true) // L'utente può allargarla
		.setFlexGrow(0) // evita che venga ridimensionata automaticamente
		.setAutoWidth(true) // la colonna si adatti automaticamente al contenuto
		.setHeader("Categorie").setSortable(true).setKey("categoria");

		grid.addColumn(new ComponentRenderer<>(doc -> {
			Span span = new Span(doc.getAutore());
			if (doc.getStato().equals(StatiDocumento.ELIMINATO))
				span.addClassName("riga-cancellata");
			return span;
		})).setResizable(true) // L'utente può allargarla
		.setFlexGrow(0) // evita che venga ridimensionata automaticamente
		.setAutoWidth(true) // la colonna si adatti automaticamente al contenuto
		.setHeader("Autore").setSortable(true).setKey("autore");

		grid.addColumn(new ComponentRenderer<>(doc -> {
			Span span = new Span(
					doc.getDataArchiviazione() != null ? FORMAT_DATE.format(doc.getDataArchiviazione().atZone(ZoneId.systemDefault())) : "");
			if (doc.getStato().equals(StatiDocumento.ELIMINATO)) {
				span.addClassName("riga-cancellata");
			}
			return span;
		})).setResizable(true) // L'utente può allargarla
		.setFlexGrow(0) // evita che venga ridimensionata automaticamente
		.setAutoWidth(true) // la colonna si adatti automaticamente al contenuto
		.setHeader("Data Archiviazione").setSortable(true).setKey("dataArchiviazione");

		grid.addColumn(new ComponentRenderer<>(doc -> {
			Span span = new Span(doc.getLastUpdate() != null ? FORMAT_DATE.format(doc.getLastUpdate().atZone(ZoneId.systemDefault())) : "");
			if (doc.getStato().equals(StatiDocumento.ELIMINATO)) {
				span.addClassName("riga-cancellata");
			}
			return span;
		})).setResizable(true) // L'utente può allargarla
		.setFlexGrow(0) // evita che venga ridimensionata automaticamente
		.setAutoWidth(true) // la colonna si adatti automaticamente al contenuto
		.setHeader("Ultimo Aggiornamento").setSortable(true).setKey("lastUpdate");

		grid.addColumn(new ComponentRenderer<>(doc -> {
			Span span = new Span(doc.getLastView() != null ? FORMAT_DATETIME.format(doc.getLastView().atZone(ZoneId.systemDefault())) : "");
			if (doc.getStato().equals(StatiDocumento.ELIMINATO)) {
				span.addClassName("riga-cancellata");
			}
			return span;
		})).setResizable(true) // L'utente può allargarla
		.setFlexGrow(0) // evita che venga ridimensionata automaticamente
		.setAutoWidth(true) // la colonna si adatti automaticamente al contenuto
		.setHeader("Ultima Visualizzazione").setSortable(true).setKey("lastView");

		grid.addColumn(new ComponentRenderer<>(doc -> {
			Span span = new Span(doc.getLingua());
			if (doc.getStato().equals(StatiDocumento.ELIMINATO))
				span.addClassName("riga-cancellata");
			return span;
		})).setResizable(true) // L'utente può allargarla
		.setFlexGrow(0) // evita che venga ridimensionata automaticamente
		.setAutoWidth(true) // la colonna si adatti automaticamente al contenuto
		.setHeader("Lingua").setSortable(true).setKey("lingua");

		grid.addColumn(new ComponentRenderer<>(doc -> {
			Span span = new Span(doc.getNote());
			if (doc.getStato().equals(StatiDocumento.ELIMINATO))
				span.addClassName("riga-cancellata");
			return span;
		})).setResizable(true) // L'utente può allargarla
		.setFlexGrow(0) // evita che venga ridimensionata automaticamente
		.setAutoWidth(true) // la colonna si adatti automaticamente al contenuto
		.setHeader("Note").setSortable(true).setKey("note");

		// colonna custom per i tag
		grid.addColumn(p -> {
			if (p.getTags() == null)
				return "";
			return p.getTags().stream().map(TagDto::getNomeTag) // prendi solo il nome
					.collect(Collectors.joining(", "));
		}).setResizable(true) // L'utente può allargarla
		.setFlexGrow(0) // evita che venga ridimensionata automaticamente
		.setAutoWidth(true) // la colonna si adatti automaticamente al contenuto
		.setHeader("Tags")
		/** per il ritorno a capo del valore */
		.setRenderer(new ComponentRenderer<>(item -> {
			Div div = new Div();
			div.setText(item.getTags().stream().map(TagDto::getNomeTag) // prendi solo il nome
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


		grid.addColumn(new ComponentRenderer<>(doc -> {
			Checkbox checkbox = new Checkbox(doc.isPreferito());
			checkbox.setReadOnly(true); // evita modifiche da parte dell'utente
			if (doc.getStato().equals(StatiDocumento.ELIMINATO))
				checkbox.addClassName("riga-cancellata");
			return checkbox;
		})).setResizable(true) // L'utente può allargarla
		.setFlexGrow(0) // evita che venga ridimensionata automaticamente
		.setAutoWidth(true) // la colonna si adatti automaticamente al contenuto
		.setHeader("Preferito").setSortable(true).setKey("preferito");

		grid.addColumn(new ComponentRenderer<>(doc -> {
			Span span = new Span(doc.getRating() != null ? doc.getRating().toString() : "0");
			if (doc.getStato().equals(StatiDocumento.ELIMINATO))
				span.addClassName("riga-cancellata");
			return span;
		})).setResizable(true) // L'utente può allargarla
		.setFlexGrow(0) // evita che venga ridimensionata automaticamente
		.setAutoWidth(true) // la colonna si adatti automaticamente al contenuto
		.setHeader("Valutazione").setSortable(true).setKey("rating");

		grid.addColumn(new ComponentRenderer<>(doc -> {
			Span span = new Span(String.valueOf(doc.getVisualizzazioni()));
			if (doc.getStato().equals(StatiDocumento.ELIMINATO))
				span.addClassName("riga-cancellata");
			return span;
		})).setResizable(true) // L'utente può allargarla
		.setFlexGrow(0) // evita che venga ridimensionata automaticamente
		.setAutoWidth(true) // la colonna si adatti automaticamente al contenuto
		.setHeader("Visual").setSortable(true).setKey("visualizzazioni");

		grid.addColumn(new ComponentRenderer<>(doc -> {
			Checkbox checkbox = new Checkbox(doc.isBackup());
			checkbox.setReadOnly(true); // evita modifiche da parte dell'utente
			if (doc.getStato().equals(StatiDocumento.ELIMINATO))
				checkbox.addClassName("riga-cancellata");
			return checkbox;
		})).setResizable(true) // L'utente può allargarla
		.setFlexGrow(0) // evita che venga ridimensionata automaticamente
		.setAutoWidth(true) // la colonna si adatti automaticamente al contenuto
		.setHeader("Backup").setSortable(true).setKey("backup");

		grid.addComponentColumn(document -> {
			HorizontalLayout actions = new HorizontalLayout();

			Anchor edit = new Anchor("documents-form/" + document.getIdDocumento() + "/false", "Modifica");
			Anchor delete = new Anchor("documents", "cancella");
			delete.getElement().addEventListener("click", ev -> {
				conferma(document.getIdDocumento(), "Sei sicuro di voler cancellare questo elemento ?");
			});
			Anchor recovery = new Anchor("documents", "ripristino");
			recovery.getElement().addEventListener("click", ev -> {
				conferma(document.getIdDocumento(), "Stai ripristinando questo elemento. Sei sicuro di volerlo fare ?");
			});

			
			recovery.setVisible(document.getStato().equals(StatiDocumento.ELIMINATO));
			delete.setVisible(!document.getStato().equals(StatiDocumento.ELIMINATO));

			actions.add(edit, delete, recovery);
			return actions;
		}).setResizable(true) // L'utente può allargarla
		.setFlexGrow(0) // evita che venga ridimensionata automaticamente
		.setAutoWidth(true) // la colonna si adatti automaticamente al contenuto
		.setHeader("Azioni");

		// riordina le colonne manualmente
		//        grid.setColumnReorderingAllowed(true);
		grid.getColumns().forEach(col -> col.setAutoWidth(true)); // adatta alla pagina
		// Abilita il ridimensionamento per tutte le colonne
		//        grid.getColumns().forEach(column -> column.setResizable(true));

		// rendi la tabella interattiva
		grid.addItemClickListener(e -> getUI()
				.ifPresent(ui -> ui.navigate("documents-form/" + e.getItem().getIdDocumento() + "/true")));

	}

	/*
	 * private void addNew() { RouteParameters params = new RouteParameters("id",
	 * String.valueOf("0")); QueryParameters queryParams =
	 * QueryParameters.simple(Map.of("view", "false"));
	 * 
	 * getUI().ifPresent(ui -> ui.navigate(Form.class, params, queryParams)); }
	 * 
	 * private Component createPaginationControls() { HorizontalLayout pagination =
	 * new HorizontalLayout(previousButton, pageLabel, nextButton);
	 * pagination.setAlignItems(Alignment.CENTER);
	 * pagination.setJustifyContentMode(JustifyContentMode.CENTER); return
	 * pagination; }
	 */

	@Override
	protected void navigateToForm(Long id) {
		// Gestisci la navigazione al form (nuovo o modifica)
		String route = "documents-form/" + (id != null ? id : "0") + "/false";
		getUI().ifPresent(ui -> ui.navigate(route));
	}

}
