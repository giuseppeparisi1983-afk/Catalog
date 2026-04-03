package it.catalog.ui.common;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.contextmenu.ContextMenu;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.shared.Tooltip;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import com.vaadin.flow.theme.lumo.LumoUtility;


public class MainLayout extends AppLayout {

    private final VerticalLayout wrapper = new VerticalLayout();
    private final Div footer = new Div();
	 
    public MainLayout() {
    	 setPrimarySection(Section.DRAWER);
    	createHeader();
        createDrawer();
        createFooter();
    }

    private void createHeader() {
    	DrawerToggle toggle = new DrawerToggle();
        toggle.setAriaLabel("Menu toggle");
    	
    	H1 logo = new H1("File Manager");
        logo.getStyle().set("font-size", "1.5em").set("margin", "0");
logo.addClassNames(LumoUtility.FontSize.LARGE, LumoUtility.Margin.NONE);
        Avatar avatar = new Avatar("Utente");
        ContextMenu userMenu = new ContextMenu(avatar);
        userMenu.setOpenOnClick(true);
        userMenu.addItem("Profilo", e -> {/* naviga al profilo */});
        userMenu.addItem("Logout", e -> {/* logout simulato */});

        HorizontalLayout header = new HorizontalLayout(logo, new Spacer(), avatar);
        header.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        header.setWidthFull();
        header.setPadding(true);
        header.setSpacing(true);
//H1 heading1 = new H1("Heading 1");
//header.add(heading1);

        addToNavbar(true, toggle,header);
    }

    private void createDrawer() {

    	 // Creo il SideNav
        SideNav nav = new SideNav();

        // Root "Home"
        SideNavItem home = new SideNavItem("Home", it.catalog.ui.HomeView.class, VaadinIcon.HOME.create());
        Tooltip.forComponent(home).setText("Vai alla Home");
        
        // Figli diretti di Home
        SideNavItem documenti = new SideNavItem("Documenti", it.catalog.ui.documenti.Index.class, VaadinIcon.FILE_TEXT.create());
        Tooltip.forComponent(documenti).setText("Gestisci i tuoi documenti");
        SideNavItem immagini  = new SideNavItem("Immagini", it.catalog.ui.image.Index.class, VaadinIcon.PICTURE.create());
        Tooltip.forComponent(immagini).setText("Archivia e visualizza le tue immagini");
        SideNavItem video     = new SideNavItem("Video",it.catalog.ui.video.Index.class, VaadinIcon.FILE_MOVIE.create());
        Tooltip.forComponent(video).setText("Sezione video");
        SideNavItem musica    = new SideNavItem("Audio", it.catalog.ui.audio.Index.class, VaadinIcon.MUSIC.create());
        Tooltip.forComponent(musica).setText("Archivia e ascolta la tua musica");
        SideNavItem film      = new SideNavItem("Film", it.catalog.ui.film.Index.class, VaadinIcon.FILM.create());
        Tooltip.forComponent(film).setText("Archivia e guarda i tuoi film");

        // Figlio di "Video"
        Image customIcon = new Image("images/guitar-icon.png", "Chitarra");
        customIcon.setWidth("25px");
        customIcon.setHeight("25px");
     // Applica la classe CSS
        customIcon.addClassName("png-icon");
        
        SideNavItem chitarra  = new SideNavItem("Chitarra", it.catalog.ui.video.chitarra.Index.class, customIcon);
        Tooltip.forComponent(chitarra).setText("Sezione dedicata alla chitarra");

        // Associo i figli
        video.addItem(chitarra);
        home.addItem(documenti);
        home.addItem(immagini);
        home.addItem(video);
        home.addItem(musica);
        home.addItem(film);

        // Aggiungo al menu
        nav.addItem(home);
        
        // Definizione dei toolTip

        Tooltip videoTip = Tooltip.forComponent(video); 
        videoTip.setText("Sezione video");
        
        // Inserisco nel drawer dell’AppLayout
        addToDrawer(nav);
    }
    
    
    private void createFooter() {
        
        footer.getStyle().set("text-align", "center");
        footer.getStyle().set("padding", "var(--lumo-space-m)");
        footer.getStyle().set("color", "var(--lumo-secondary-text-color)");
        footer.getStyle().set("font-size", "var(--lumo-font-size-s)");
    	footer.setText("© 2026 Giuseppe – Tutti i diritti riservati");

    }
    
    @Override
    protected void afterNavigation() {
        super.afterNavigation();

        // Ogni volta che cambia la view, aggiorno il contenuto principale
//        Component content = getContent();
//        wrapper.removeAll();
//
//        wrapper.add(content, footer);
//        wrapper.setFlexGrow(1, content);
    }

    @Override
    public void setContent(Component content) {
        if (content == null) {
            super.setContent(null);
            return;
        }

        // Svuotiamo il wrapper per evitare accumuli di vecchie View
        wrapper.removeAll();
        wrapper.setSizeFull();
        wrapper.setPadding(false);
        wrapper.setSpacing(false);

        // Costruiamo la gerarchia: 
        // 1. La View dinamica (es. HomeView) sopra
        // 2. Il Footer statico sotto
        wrapper.add(content, footer);
        
        // Diciamo alla View di espandersi per occupare tutto lo spazio verticale utile
        wrapper.setFlexGrow(1, content);

        // Passiamo il wrapper completo al vero setContent dell'AppLayout
        super.setContent(wrapper);
    }
    
    
    // Utility per allineare gli elementi
    private static class Spacer extends Div {
        public Spacer() {
            getStyle().set("flex-grow", "1");
        }
    }
}

