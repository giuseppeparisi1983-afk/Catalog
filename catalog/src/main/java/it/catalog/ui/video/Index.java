package it.catalog.ui.video;

import java.util.Map;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.QueryParameters;
import com.vaadin.flow.router.Route;

import it.catalog.service.dto.VideoDto;
import it.catalog.service.impl.VideoService;
import it.catalog.ui.common.MainLayout;
import it.catalog.ui.video.base.AbstractVideoIndex;

@Route(value="video", layout = MainLayout.class)
@PageTitle("Video")
//@CssImport("./styles/styles.css")
public class Index extends AbstractVideoIndex<VideoDto> {

    public Index(VideoService videoService) {
        super(videoService, VideoDto.class, "Archivio Video");
        
        Anchor guitarLink = new Anchor("chitarra", "🎸 Vai a Video Chitarra");
        addComponentAsFirst(guitarLink);
    }

    @Override
    protected void addExtraColumns(Grid<VideoDto> grid) {
        // Nessuna colonna extra per i video comuni
    }

    @Override
    protected void navigateToForm(Integer id, boolean viewMode) {
        QueryParameters qp = QueryParameters.simple(Map.of("view", String.valueOf(viewMode)));
        String path = (id == null) ? "video-form" : "video-form/" + id;
        getUI().ifPresent(ui -> ui.navigate(path, qp));
    }

    @Override
    protected void navigateToForm(Long id) {
        // Implementazione obbligatoria per il bottone "Nuovo" di AbstractSearchView
        navigateToForm(id != null ? id.intValue() : null, false);
    }
}