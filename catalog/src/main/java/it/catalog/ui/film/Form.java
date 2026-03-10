package it.catalog.ui.film;

import java.util.List;
import java.util.Optional;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.Location;
import com.vaadin.flow.router.OptionalParameter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.QueryParameters;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteParameters;

import it.catalog.ui.common.MainLayout;

@Route(value="film-form/:id", layout = MainLayout.class)
@PageTitle("Modulo Film")
public class Form extends VerticalLayout implements BeforeEnterObserver,HasUrlParameter<Integer> {

	private boolean viewMode = true;
	
	
    @Override
    public void beforeEnter(BeforeEnterEvent event) {
    	RouteParameters routeParams = event.getRouteParameters();
        Optional<String> idParam = routeParams.get("id");


		/*
		 * if (idParam.isPresent()) { try { Integer id=Integer.parseInt(idParam.get());
		 * videoService.findById(id).ifPresentOrElse( v -> { this.video = v;
		 * 
		 * }, () -> { this.video =new VideoDto();
		 * //event.forwardTo("Nesun video trovato")
		 * 
		 * } ); //bindFieldsFromVideo(); bindFieldsFromObject(this.video);
		 * updateFieldState(); // aggiorna i campi e visibilità del pulsante
		 * 
		 * } catch (NumberFormatException ex) { event.forwardTo("not-found"); } } else {
		 * event.forwardTo("not-found"); }
		 */
    }
	
	  @Override
	    public void setParameter(BeforeEvent event, @OptionalParameter Integer id) {
	        Location location = event.getLocation();
	        QueryParameters queryParams = location.getQueryParameters();
	        String viewParam = queryParams.getParameters()
	            .getOrDefault("view", List.of("true"))
	            .get(0);

	        viewMode = Boolean.parseBoolean(viewParam);
	        // Usa id e viewMode per caricare i dati e impostare la modalità
	    }
}

