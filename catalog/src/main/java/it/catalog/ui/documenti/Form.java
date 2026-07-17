package it.catalog.ui.documenti;

import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility.Gap;

import it.catalog.common.enums.StatiDocumento;
import it.catalog.common.enums.TipoDocumento;
import it.catalog.service.dto.DocumentoDto;
import it.catalog.service.dto.search.DtoFilter;
import it.catalog.service.interfaces.SearchService;
import it.catalog.ui.common.AbstractCommonFileForm;
import it.catalog.ui.common.MainLayout;

@Route(value = "documents-form", layout = MainLayout.class)
@PageTitle("Documento - Form")

/**
 * Estendiamo la classe AbstractCommonFileForm<T,S> che gestisce i campi comuni e qui aggiungiamo 
 * i campi che appartengono allo specifico DTO (in questo caso lingua, autore, versione, ecc.).
 * */

public class Form extends AbstractCommonFileForm<DocumentoDto, SearchService<DocumentoDto, DtoFilter>> {

    // Campi specifici di DocumentoDto
    private TextField autore = new TextField("Autore");
    private TextField lingua = new TextField("Lingua");
    private IntegerField versione = new IntegerField("Versione");
    private TextField origine = new TextField("Origine");
    private ComboBox<TipoDocumento> categoria = new ComboBox<>("Categoria");
    private ComboBox<StatiDocumento> stato = new ComboBox<>("Stato");

    public Form(SearchService<DocumentoDto, DtoFilter> service) {
        super("Modulo Documento",service, DocumentoDto.class);

        // Layout per campi specifici
        FormLayout specificLayout = new FormLayout();
        
        HorizontalLayout row1 = new HorizontalLayout();
//        row1.setAlignItems(FlexComponent.Alignment.CENTER); // Allinea verticalmente al testo
        row1.setAlignItems(FlexComponent.Alignment.BASELINE); // Allinea verticalmente al testo;
        
        row1.setSpacing(true);
        //row1.addClassName(Gap.MEDIUM);
        //row1.setWidth("100%");
        //row1.getStyle().set("flex-grow", "1");
        //row1.setAlignItems(Alignment.START);
        // row1.setJustifyContentMode(JustifyContentMode.CENTER);
        row1.setWidth("100%");
        //row1.setWidthFull();
        
        nome.setWidth("280px");
        autore.setWidth("280px");
        versione.setWidth("69px");
        
        row1.add(nome,autore,categoria,versione); 
        
        
        HorizontalLayout row2 = new HorizontalLayout();
      row2.setAlignItems(FlexComponent.Alignment.BASELINE);  
      row2.setSpacing(true);
      row2.setWidth("70%");
      
        row2.add(lingua,origine,stato);
                
		 // Aggiungo il primo blocco del padre (Nome, Path)
//      addIdentityFields(specificLayout); 
//      
//      specificLayout.add(estensione);

        VerticalLayout formLayout=new VerticalLayout();
        
        formLayout.setSpacing(true);
        formLayout.getStyle().set("padding", "0").set("margin", "0");
        formLayout.setPadding(false);
        formLayout.getStyle().set("gap", "8px"); // Riduce lo spazio tra righe
        
        
        formLayout.add(row1,row2); 
        
        addInfoFile(formLayout); // Aggiungo il blocco delle informazioni sul file (Comuni)

        addDateFields(formLayout); // Aggiungo il blocco delle date (Comuni)
        
      addStatusFields(formLayout); // Aggiungo il blocco dello stato (Comuni)


      // 4. Aggiungiamo il layout finito alla View
      specificLayout.add(formLayout);    
        
      // Poi aggiungo il blocco dei Tag e Descrizione (Comuni)
        addClassificationFields(formLayout);
        
        add(specificLayout);
        
        // Lo aggiungiamo al form
//        addComponentAtIndex(1, specificLayout); // Lo mettiamo prima del footer
        
        
        // --- Popolamento delle ComboBox PRIMA che il metodo setParameter venga eseguito.---
        categoria.setItems(TipoDocumento.values());
        categoria.setItemLabelGenerator(TipoDocumento::getLabel); // o .name()

        stato.setItems(StatiDocumento.values());
        stato.setItemLabelGenerator(StatiDocumento::getLabel);
        
        // Bind e obligatorietà dei campi specifici 
    	binder.forField(categoria).asRequired("Campo obbligatorio").bind(DocumentoDto::getCategoria,
				DocumentoDto::setCategoria);

    	binder.forField(stato).asRequired("Campo obbligatorio").bind(DocumentoDto::getStato,
    			DocumentoDto::setStato);

    	binder.forField(versione).asRequired("Campo obbligatorio").bind(DocumentoDto::getVersione,
    			DocumentoDto::setVersione);
    	
       /**Questo binda AUTOMATICAMENTE solo i campi non ancora bindati (autore, lingua)
         I campi comuni (nome, data, tags, ecc.) sono già stati "occupati" dalla classe base, quindi non vengono sovrascritti*/
        binder.bindInstanceFields(this);
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
    @Override protected void navigateBack() { getUI().ifPresent(ui -> ui.navigate("documents")); }
}
