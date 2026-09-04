package it.catalog.ui.video;

import java.util.HashMap;
import java.util.Map;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import it.catalog.service.dto.VideoDto;
import it.catalog.service.dto.search.DtoFilter;
import it.catalog.service.interfaces.SearchService;
import it.catalog.ui.common.AbstractBaseForm;
import it.catalog.ui.common.MainLayout;
import it.catalog.ui.video.base.AbstractVideoIndex;

@Route(value = "video", layout = MainLayout.class)
@PageTitle("Archivio Video")
public class Index extends AbstractVideoIndex<VideoDto> {

	public Index(SearchService<VideoDto, DtoFilter> service) {
		super(service, VideoDto.class, "Archivio Video");
	}

	@Override
	protected void addExtraColumns(Grid<VideoDto> grid) {
		// I video base non hanno colonne extra oltre a quelle comuni
	}

	/**
	 * restituisce il percorso della route a cui tornare quando si chiude il form. 
	 * Il metodo è richiamato da navigateBack() dentro la classe padre AbstractBaseForm
	 * */
	@Override
	protected String getReturnRoute() {
		return "video";
	}

	// Specifichiamo quale form aprire
	@Override
	protected Class<? extends Component> getFormClass() {
		return Form.class;
	}

}