package it.catalog.ui.documenti;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Sort;

import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.QueryParameters;
import com.vaadin.flow.router.Route;

import it.catalog.common.enums.DocFormat;
import it.catalog.common.enums.StatiDocumento;
import it.catalog.common.enums.TipoDocumento;
import it.catalog.service.dto.DocumentoDto;
import it.catalog.service.dto.search.DtoFilter;
import it.catalog.service.dto.search.StringCriterion;
import it.catalog.service.interfaces.SearchService;
import it.catalog.ui.common.AbstractCommonFileForm;
import it.catalog.ui.common.MainLayout;

@Route(value = "documents-form", layout = MainLayout.class)
@PageTitle("Documento - Form")
public class Form extends AbstractCommonFileForm<DocumentoDto, SearchService<DocumentoDto, DtoFilter>, DtoFilter> {

	private TextField autore = new TextField("Autore");
	private TextField lingua = new TextField("Lingua");
	private IntegerField versione = new IntegerField("Versione");
	private TextField origine = new TextField("Origine");
	private ComboBox<TipoDocumento> categoria = new ComboBox<>("Categoria");
	private ComboBox<StatiDocumento> stato = new ComboBox<>("Stato");
	private ComboBox<DocFormat> estensione = new ComboBox<>("Estensione");

	public Form(SearchService<DocumentoDto, DtoFilter> service) {
		super("Modulo Documento", service, DocumentoDto.class,DtoFilter::new, "");

		// 1. COSTRUIAMO IL LAYOUT
		buildLayout();

		// 2. CONFIGURIAMO LA LOGICA SPECIFICA
		setup();

		// 3. Binding specifici
		binder.forField(stato).asRequired("Campo obbligatorio").bind("stato");
		binder.forField(estensione).asRequired("Campo obbligatorio").bind("formato");
		binder.forField(versione).asRequired("Campo obbligatorio").bind("versione");

		binder.bindInstanceFields(this);
	}

	@Override
	protected void addSpecificTopLayout(VerticalLayout mainLayout) {
		nome.setWidth("280px");
		autore.setWidth("280px");
		versione.setWidth("70px");
		HorizontalLayout row = new HorizontalLayout(nome, autore, categoria, versione);
		row.setAlignItems(Alignment.BASELINE);
		mainLayout.add(row);
	}

	@Override
	protected void addSpecificMiddleLayout(VerticalLayout mainLayout) {
		HorizontalLayout row = new HorizontalLayout(lingua, origine, stato);
		row.setAlignItems(Alignment.BASELINE);

		path.setWidth("62%"); // Imposta la larghezza del campo path
//        mainLayout.add(row, path); // Aggiungiamo anche il path qui
		addInfoFileLayout(mainLayout, estensione);
	}

	private void setup() {

		// CONFIGURAZIONE ComboBox (Chiamiamo il padre)
		setupCombo(estensione, DocFormat.values());
		setupCombo(stato, StatiDocumento.values());
		setupCombo(categoria, TipoDocumento.values());
	}

	// Implementazione dei metodi della logica
	@Override
	protected DocumentoDto loadBean(Long id) {

		DocumentoDto dto = null;

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
	protected void saveBean(DocumentoDto bean) {
		service.save(bean);
	}

	@Override
	protected DocumentoDto createNewBean() {
		return new DocumentoDto();
	}

	/**
	 * restituisce il percorso della route a cui tornare quando si chiude il form. 
	 * Questo viene richiamato dal metodo navigateBack() della classe padre AbstractBaseForm
	 * */
	@Override
	protected String getReturnRoute() {
		return "documents";
	}

	@Override
	protected String getTagType() {
		return "Documento";
	}
	
}