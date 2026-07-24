package it.catalog.ui.film;

import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import it.catalog.common.enums.FilmFormat;
import it.catalog.service.dto.FilmDto;
import it.catalog.service.dto.search.DtoFilter;
import it.catalog.service.interfaces.SearchService;
import it.catalog.ui.common.AbstractCommonFileForm;
import it.catalog.ui.common.MainLayout;
import it.catalog.ui.utility.AppConverters;

@Route(value = "film-form", layout = MainLayout.class)
@PageTitle("Film - Form")
public class Form extends AbstractCommonFileForm<FilmDto, SearchService<FilmDto, DtoFilter>> {

	private TextField regista = new TextField("Regista");
	private TextField duration = new TextField("Durata");
	private IntegerField anno = new IntegerField("Anno");
	private TextField genere = new TextField("Genere");
	private TextField locandina = new TextField("Locandina URL");
	private TextField trailer = new TextField("Trailer URL");
	private ComboBox<FilmFormat> estensione = new ComboBox<>("Formato");
	private Image copertina = new Image();
	private TextArea protagonisti = new TextArea("Protagonisti");

	public Form(SearchService<FilmDto, DtoFilter> service) {
		super("Modulo Film", service, FilmDto.class);

		// 1. COSTRUIAMO IL LAYOUT
		buildLayout();

		// 2. CONFIGURIAMO LA LOGICA SPECIFICA
		setupFilmLogic();

		// 3. BINDING
		binder.forField(nome).asRequired("Campo obbligatorio").bind("titolo");
		binder.forField(anno).asRequired("Campo obbligatorio").bind("anno");
		binder.forField(estensione).asRequired("Campo obbligatorio").bind("estensione");
		binder.forField(duration).asRequired("Campo obbligatorio")
				.withConverter(new AppConverters.StringToDoubleConverter()).bind("duration");
		binder.forField(locandina)
				.withValidator(
						value -> value == null || value.isBlank() || value.contains("\\") || value.contains("//"), // questo
																													// non
																													// è
																													// un
																													// campo
																													// obbligatorio,
																													// ma
																													// se
																													// l'utente
																													// scrive
																													// qualcosa,
																													// deve
																													// contenere
																													// almeno
																													// un
																													// carattere
																													// '\'
																													// o
																													// '/'
						"Il percorso del file deve contenere almeno un carattere '\\' o '/'" // Questo messaggio viene
																								// mostrato nel caso la
																								// validazione fallisce
																								// ovvero se tutte le
																								// condizioni della
																								// lambda restituiscono
																								// false
				).bind("locandina");
		/**
		 * Questo binda AUTOMATICAMENTE solo i campi non ancora bindati I campi comuni
		 * (nome, data, tags, ecc.) sono già stati "occupati" dalla classe base, quindi
		 * non vengono sovrascritti
		 */
		binder.bindInstanceFields(this);
	}

	@Override
	protected void addSpecificTopLayout(VerticalLayout mainLayout) {
		// RIGA 1: Nome, Regista, Durata
		regista.setWidth("280px");
		duration.setWidth("120px");
		HorizontalLayout row = new HorizontalLayout(nome, regista, duration);
		row.setAlignItems(Alignment.BASELINE);
		mainLayout.add(row);
	}

	@Override
	protected void addSpecificMiddleLayout(VerticalLayout mainLayout) {

		VerticalLayout middleRows = new VerticalLayout();
		middleRows.setPadding(false);
		middleRows.getStyle().set("gap", "8px");

		// RIGA 2: Genere, Anno
		HorizontalLayout r2 = new HorizontalLayout(genere, anno);
//        locandina.setWidthFull();
//        trailer.setWidthFull();

		locandina.setWidth("850px");
		trailer.setWidth("850px");

		// RIGHE 3 e 4: Locandina e Trailer
		middleRows.add(r2, locandina, trailer);

		// RIGA 5: Il blocco Info File (Path, Estensione, Dimensione) deciso dal Padre
		addInfoFileLayout(middleRows,estensione);

		// Sezione con anteprima Locandina
		HorizontalLayout section = new HorizontalLayout(middleRows, copertina);
		section.expand(middleRows);
		mainLayout.add(section);

	}

	// OVERRIDE: Personalizziamo l'ordine delle TextArea e Tags
	@Override
	protected void addClassificationFields(HasComponents container) {
		descrizione.setLabel("Trama");
		descrizione.setWidth("62%");
		protagonisti.setWidth("62%");
		note.setWidth("62%");

		// Ordine: Descrizione -> Protagonisti -> Note -> Tags
		container.add(descrizione, protagonisti, note, tags);
	}

	private void setupFilmLogic() {

		anno.setWidth("69px");
		anno.setWidth("120px");
		// Limiti anche lato componente (UI)
		anno.setMin(1940);
		anno.setMax(2080);
		anno.setStep(1);
		anno.setI18n(
				new IntegerField.IntegerFieldI18n().setMinErrorMessage("L'anno deve essere maggiore o uguale a 1940")
						.setMaxErrorMessage("L'anno non può superare il 2080"));

		copertina.setWidth("160px");
		copertina.setHeight("200px");
		// 1. Placeholder: Esempio visivo dentro il campo (scompare appena l'utente
		// digita)
		locandina.setPlaceholder("es. https://sito.it/cover.jpg oppure /images/copertina.jpg");

		// 2. HelperText: Spiegazione sintetica sempre visibile SOTTO il campo
		locandina.setHelperText(
				"Puoi inserire un URL remoto (http/https) o un percorso relativo del server (es. /images/...)");
		// Se l'immagine è vuota e NON vuoi mostrare neanche il rettangolo grigio:
		// 3. Fondamentale: dice a Vaadin di aggiornare il valore a OGNI singolo
		// carattere digitato
		locandina.setValueChangeMode(ValueChangeMode.EAGER);

		// 4. Gestiamo il cambio di valore nell'evento (scatta ogni volta che il testo
		// cambia)
		locandina.addValueChangeListener(e -> {
			String valore = e.getValue(); // Usiamo la variabile dell'evento!
			boolean haValore = valore != null && !valore.isBlank();

			copertina.setVisible(haValore);
			if (haValore) {
				copertina.setSrc(valore);
			}
		});

		copertina.setAlt("Cover preview");
		
//		estensione.setItems(FilmFormat.values());
//        estensione.setItemLabelGenerator(FilmFormat::getLabel);
        
        setupExtensionCombo(estensione, FilmFormat.values());
		
	}

	// Implementazione dei metodi della logica
	@Override
	protected FilmDto loadBean(Long id) {

		FilmDto dto = null;

		try {
//          	binder.bindInstanceFields(this); // associa automaticamente i campi del form alle proprietà del DTO basandosi sul nome.
			dto = service.findById(id);
			if (dto != null) {
				binder.readBean(dto); // Popola automaticamente i campi. IMPORTANTE: prima si definiscono i binding,
										// poi si chiama readBean().
			}
		} catch (NumberFormatException ex) {

		}

		return dto;

	}

	@Override
	protected void saveBean(FilmDto bean) {
		service.save(bean);
	}

	@Override
	protected FilmDto createNewBean() {
		return new FilmDto();
	}

	@Override
	protected String getReturnRoute() {
		return"film";
	}

}