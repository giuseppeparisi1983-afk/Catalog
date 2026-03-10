package it.catalog.ui.film;

import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import it.catalog.ui.common.MainLayout;

@Route(value="film", layout = MainLayout.class)
@PageTitle("Film")
@CssImport("./styles/styles.css")
public class Index extends VerticalLayout{

}
