package it.catalog.ui.documenti;

import java.util.ArrayList;

import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

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
    private TextField estensione = new TextField("Estensione");
    private ComboBox<TipoDocumento> categoria = new ComboBox<>("Categoria");
    private ComboBox<StatiDocumento> stato = new ComboBox<>("Stato");

    public Form(SearchService<DocumentoDto, DtoFilter> service) {
        super(service, DocumentoDto.class);

        // Layout per campi specifici
        FormLayout specificLayout = new FormLayout();
        
 // --- MIXING PERSONALIZZATO ---
        
        // 1. Voglio il Nome (Comune) in alto
        specificLayout.add(nome); 
        
        // 2. Poi voglio l'Autore (Specifico) e la Categoria (Specifica)
        specificLayout.add(autore, categoria);
        specificLayout.add(lingua, versione, origine, estensione, stato);
        
         // 3. RICHIAMIAMO I METODI HELPER NELL'ORDINE VOLUTO
		
		 // Aggiungo il primo blocco del padre (Nome, Path)
        addIdentityFields(specificLayout); 

        addStatusFields(specificLayout); // Aggiungo il blocco dello stato (Comuni)
        
        addDateFields(specificLayout); // Aggiungo il blocco delle date (Comuni)
		
        // Poi aggiungo il blocco dei Tag e Descrizione (Comuni)
        addClassificationFields(specificLayout);
        
        
        // 4. Aggiungiamo il layout finito alla View
        add(specificLayout);
        
        // Lo aggiungiamo al form
        addComponentAtIndex(1, specificLayout); // Lo mettiamo prima del footer
        
        
        // --- Popolamento delle ComboBox PRIMA che il metodo setParameter venga eseguito.---
        categoria.setItems(TipoDocumento.values());
        categoria.setItemLabelGenerator(TipoDocumento::getLabel); // o .name()

        stato.setItems(StatiDocumento.values());
        stato.setItemLabelGenerator(StatiDocumento::getLabel);
        
        // Bind dei campi specifici
    	binder.forField(categoria).asRequired("Campo obbligatorio").bind(DocumentoDto::getCategoria,
				DocumentoDto::setCategoria);
        
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
