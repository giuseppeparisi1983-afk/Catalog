package it.catalog.ui.video;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

import it.catalog.service.dto.VideoDto;
import it.catalog.service.dto.search.DtoFilter;
import it.catalog.service.impl.ImageFileServiceImpl;
import it.catalog.service.interfaces.SearchService;
import it.catalog.ui.common.MainLayout;
import it.catalog.ui.video.base.AbstractVideoForm;
import lombok.extern.slf4j.Slf4j;

@Route(value = "video-form", layout = MainLayout.class)
@Slf4j
public class Form extends AbstractVideoForm<VideoDto> {

    public Form(SearchService<VideoDto, DtoFilter> service) {
        super("Modulo Video", service, VideoDto.class);
        buildLayout(); // Costruisce il layout definito nel padre
        binder.bindInstanceFields(this); // Bind dei campi rimanenti
    }
    
 // Implementazione dei metodi della logica
 	@Override
 	protected VideoDto loadBean(Long id) {

 		VideoDto dto = null;

 		try {
//           	binder.bindInstanceFields(this); // associa automaticamente i campi del form alle proprietà del DTO basandosi sul nome.
 			dto = service.findById(id);
 			if (dto != null) {
 				binder.readBean(dto); // Popola automaticamente i campi. IMPORTANTE: prima si definiscono i binding,
 										// poi si chiama readBean().
 			}
 		} catch (NumberFormatException ex) {

 		}

 		return dto;

 	}
    
    
    @Override protected void saveBean(VideoDto bean) { 
    	
    	log.info("Valore Categoria: " + bean.getCategoria());
    	
    	service.save(bean); }
    @Override protected VideoDto createNewBean() { return new VideoDto(); }
    @Override protected String getReturnRoute() { return "video"; }
}