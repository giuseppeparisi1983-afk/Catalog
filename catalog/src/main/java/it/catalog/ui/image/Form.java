package it.catalog.ui.image;

import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;

import it.catalog.common.enums.ImageFormat;
import it.catalog.common.enums.ImageType;
import it.catalog.service.dto.ImageDto;
import it.catalog.service.dto.search.DtoFilter;
import it.catalog.service.interfaces.SearchService;
import it.catalog.ui.common.AbstractCommonFileForm;
import it.catalog.ui.common.MainLayout;

/**
 * Visualizzazione e Salvataggio OK compresi i tags
 */
@Route(value = "images-form", layout = MainLayout.class)
@PageTitle("Immagini - Form")
public class Form extends AbstractCommonFileForm<ImageDto, SearchService<ImageDto, DtoFilter>, DtoFilter> {

	private ComboBox<ImageFormat> formato = new ComboBox<>("Formato");
	private ComboBox<ImageType> tipoFile = new ComboBox<>("Tipo File");
	private TextField locandina = new TextField("Locandina URL");
	private Image copertina = new Image();

	public Form(SearchService<ImageDto, DtoFilter> service) {
		super("Modulo Immagine", service, ImageDto.class, DtoFilter::new, "");

		// 1. COSTRUIAMO IL LAYOUT
		buildLayout();

		// 2. Configurazione componenti specifici
		setup();

		// 3. BINDER
		binder.forField(formato).asRequired("Campo obbligatorio").bind("formato");
		binder.forField(tipoFile).asRequired("Campo obbligatorio").bind("tipoFile");
		binder.bindInstanceFields(this);

	}

	private void setup() {

		tipoFile.setPlaceholder("Seleziona tipo ...");
		tipoFile.setWidth("170px");

		copertina.setWidth("160px");
		copertina.setHeight("200px");
		locandina.setValueChangeMode(ValueChangeMode.EAGER);
		locandina.addValueChangeListener(e -> {
			copertina.setVisible(!e.getValue().isBlank());
			copertina.setSrc(e.getValue());
		});

		setupCombo(formato, ImageFormat.values());
		setupCombo(tipoFile, ImageType.values());
	}

	@Override
	protected void buildLayout() {
		mainFormLayout.setSpacing(true);
		mainFormLayout.setPadding(false);
		mainFormLayout.getStyle().set("gap", "8px");

	    // --- Creazione barra di Navigazione in alto ---
	    addNavigationLayout(mainFormLayout); 
	    
		addSpecificTopLayout(mainFormLayout);

		// 2. Altro gancio
		addSpecificMiddleLayout(mainFormLayout);

		// 3. Blocchi comuni
		addClassificationFields(mainFormLayout);

		add(mainFormLayout);
	}

	@Override
	protected void addSpecificTopLayout(VerticalLayout mainLayout) {

		// RIGA 1: Nome, tipoFile
		nome.setWidth("280px");
		// tipoFile.setWidth("120px");
		HorizontalLayout row = new HorizontalLayout(nome, tipoFile);
		row.setAlignItems(Alignment.BASELINE);
		mainLayout.add(row);

	}

	@Override
	protected void addSpecificMiddleLayout(VerticalLayout mainLayout) {
		VerticalLayout middleRows = new VerticalLayout();
		middleRows.setPadding(false);
		middleRows.getStyle().set("gap", "8px");

		// RIGA 2: Il blocco Info File (Path, Formato, Dimensione) deciso dal Padre
		addInfoFileLayout(middleRows, formato);

		locandina.setWidth("850px");

		// RIGHE 3: Locandina
		middleRows.add(locandina);

		addDateFields(middleRows);

		addStatusFields(middleRows);
		// Sezione con anteprima Locandina
		HorizontalLayout section = new HorizontalLayout(middleRows, copertina);
		section.expand(middleRows);
		mainLayout.add(section);

	}

	// Implementazione dei metodi della logica
	@Override
	protected ImageDto loadBean(Long id) {

		ImageDto dto = null;

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
	protected void saveBean(ImageDto bean) {
		service.save(bean);
	}

	@Override
	protected ImageDto createNewBean() {
		return new ImageDto();
	}

	/**
	 * restituisce il percorso della route a cui tornare quando si chiude il form. 
	 * Questo viene richiamato dal metodo navigateBack() della classe padre AbstractBaseForm
	 * */
	@Override
	protected String getReturnRoute() {
		return "images";
	}

	@Override
	protected String getTagType() {
		return "Image";
	}
	
}