package it.catalog.ui;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import it.catalog.ui.common.MainLayout;

@Route(value="", layout = MainLayout.class)
@PageTitle("Home")
//@Theme("principal") // deve corrispondere al nome della cartella
@CssImport("./styles/styles.css")
public class HomeView extends VerticalLayout {


    public HomeView() {

    	setAlignItems(Alignment.CENTER);
//         setSpacing(true);

    	 HorizontalLayout paddindlayout = new HorizontalLayout();
    	 paddindlayout.getStyle().set("padding-top", "40px");

    	
         HorizontalLayout row1 = new HorizontalLayout();
//       row1.setWidthFull();
         row1.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
       row1.setJustifyContentMode(JustifyContentMode.BETWEEN);
         
       row1.add(createCategoryButton("Immagini", "images"),createCategoryButton("Audio", "audio"));

       
       HorizontalLayout row2 = new HorizontalLayout();
       row2.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
       row2.setJustifyContentMode(JustifyContentMode.BETWEEN);
       
       row2.add(createCategoryButton("Documenti", "documents"));
       
       HorizontalLayout row3 = new HorizontalLayout();
//       row2.setWidthFull();
//       row2.setAlignItems(Alignment.START);
       row3.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
       row3.setJustifyContentMode(JustifyContentMode.BETWEEN);

       row3.add(createCategoryButton("Video","video"),createCategoryButton("Film", "documents"));
       
       
       add(paddindlayout,row1,row2,row3);
    }
   
    private Button createCategoryButton(String label, String route) {
        Button button = new Button(label);
        
        if ("Film".equals(label)) {
        	
        	button.getStyle().set("--lumo-button-primary-background-color", "red");
        	button.getStyle().set("--lumo-button-primary-text-color", "white");
        	button.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        }

        button.addClassName("multiline-button");
        button.addClickListener(e -> {
            // Puoi usare un service condiviso o un ViewModel per passare i dati
            getUI().ifPresent(ui -> ui.navigate(route));
        });

        return button;
    }
    
    
}