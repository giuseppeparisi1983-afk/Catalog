package it.catalog.ui.video.chitarra;

import java.util.Map;

import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.QueryParameters;
import com.vaadin.flow.router.Route;

import it.catalog.service.dto.GuitarDto;
import it.catalog.service.impl.GuitarServiceImpl;
import it.catalog.ui.common.MainLayout;
import it.catalog.ui.video.base.AbstractVideoIndex;

@Route(value = "chitarra", layout = MainLayout.class)
@PageTitle("Video Chitarra")
public class Index extends AbstractVideoIndex<GuitarDto> {

	public Index(GuitarServiceImpl guitarService) {
		super(guitarService, GuitarDto.class, "Archivio Video Chitarra");

		Anchor videoLink = new Anchor("video", "📹 Torna a Video Generici");
		addComponentAsFirst(videoLink);
	}

	@Override
	protected void addExtraColumns(Grid<GuitarDto> grid) {
		grid.addColumn(new ComponentRenderer<>(g -> {
			Checkbox cb = new Checkbox(g.getTodo() != null && g.getTodo());
			cb.setReadOnly(true);
			return cb;
		})).setHeader("Fatto").setKey("todo").setSortable(true);

		grid.addColumn(g -> g.getDifficolta() != null ? g.getDifficolta().getLabel() : "").setHeader("Difficoltà")
				.setKey("difficolta").setSortable(true);

		grid.addColumn(GuitarDto::getAutore).setHeader("Autore").setKey("autore").setSortable(true);

		grid.addColumn(new ComponentRenderer<>(g -> {
			Checkbox cb = new Checkbox(g.getVisto() != null && g.getVisto());
			cb.setReadOnly(true);
			return cb;
		})).setHeader("Visto").setKey("visto").setSortable(true);
	}

	@Override
	protected void navigateToForm(Integer id, boolean viewMode) {
		// Usa idGuitar se necessario, o id standard ereditato
		QueryParameters qp = QueryParameters.simple(Map.of("view", String.valueOf(viewMode)));
		String path = (id == null) ? "chitarra-form" : "chitarra-form/" + id;
		getUI().ifPresent(ui -> ui.navigate(path, qp));
	}

	@Override
	protected void navigateToForm(Long id) {
		navigateToForm(id != null ? id.intValue() : null, false);
	}
}