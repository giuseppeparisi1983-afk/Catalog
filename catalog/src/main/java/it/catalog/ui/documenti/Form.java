package it.catalog.ui.documenti;

import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import it.catalog.common.enums.AudioFormat;
import it.catalog.common.enums.DocFormat;
import it.catalog.common.enums.StatiDocumento;
import it.catalog.common.enums.TipoDocumento;
import it.catalog.service.dto.DocumentoDto;
import it.catalog.service.dto.search.DtoFilter;
import it.catalog.service.interfaces.SearchService;
import it.catalog.ui.common.AbstractCommonFileForm;
import it.catalog.ui.common.MainLayout;

@Route(value = "documents-form", layout = MainLayout.class)
@PageTitle("Documento - Form")
public class Form extends AbstractCommonFileForm<DocumentoDto, SearchService<DocumentoDto, DtoFilter>> {

    private TextField autore = new TextField("Autore");
    private TextField lingua = new TextField("Lingua");
    private IntegerField versione = new IntegerField("Versione");
    private TextField origine = new TextField("Origine");
    private ComboBox<TipoDocumento> categoria = new ComboBox<>("Categoria");
    private ComboBox<StatiDocumento> stato = new ComboBox<>("Stato");
    private ComboBox<DocFormat> estensione = new ComboBox<>("Estensione");

    public Form(SearchService<DocumentoDto, DtoFilter>  service) {
        super("Modulo Documento", service, DocumentoDto.class);
        
        // 1. CONFIGURAZIONE ComboBox (Chiamiamo il padre)
        setupExtensionCombo(estensione, DocFormat.values());
        setupExtensionCombo(stato, StatiDocumento.values());
        setupExtensionCombo(categoria, TipoDocumento.values());
        
        // COSTRUIAMO IL LAYOUT
        buildLayout();
        
        // Binding specifici
//        binder.forField(categoria).asRequired().bind("categoria");
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

     // Implementazione dei metodi della logica
    @Override protected DocumentoDto loadBean(Long id) { 
    	
    	DocumentoDto dto = null;
    	
    	  try {
//          	binder.bindInstanceFields(this); // associa automaticamente i campi del form alle proprietà del DTO basandosi sul nome.
    		  dto =service.findById(id);
    		  if (dto != null) {
    			  binder.readBean(dto); // Popola automaticamente i campi. IMPORTANTE: prima si definiscono i binding, poi si chiama readBean().               
    	  }
    	  } catch (NumberFormatException ex) {

          }
    	
    	  return dto; 
    
    }
    
    @Override protected void saveBean(DocumentoDto bean) {service.save(bean); }
    @Override protected DocumentoDto createNewBean() { return new DocumentoDto(); }
    @Override protected String getReturnRoute() { return"documents";}
}