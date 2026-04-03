package it.catalog.ui.film;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteParameters;
import com.vaadin.flow.theme.lumo.LumoUtility.Gap;

import it.catalog.service.dto.FilmDto;
import it.catalog.service.impl.FilmServiceImpl;
import it.catalog.ui.common.MainLayout;

@Route(value="film-form/:id/:view", layout = MainLayout.class)
@PageTitle("Modulo Film")
public class Form extends Composite<VerticalLayout> implements BeforeEnterObserver{

    private final FilmServiceImpl service;
    private final Binder<FilmDto> binder = new Binder<>(FilmDto.class);

    private final Button save = new Button("Salva");
    private final Anchor cancel = new Anchor("images", "Indietro");
	

	 private FilmDto dto=new FilmDto();
	 
	 private boolean viewMode = true;

    public Form(FilmServiceImpl service) {
        
		 this.service = service; 
		
		VerticalLayout layoutColumn2 = new VerticalLayout();
        H1 h1 = new H1();
        HorizontalLayout layoutRow = new HorizontalLayout();
        TextField textField = new TextField();
        TextField textField2 = new TextField();
        ComboBox comboBox = new ComboBox();
        HorizontalLayout layoutRow2 = new HorizontalLayout();
        TextField textField3 = new TextField();
        TextField textField4 = new TextField();
        TextField textField5 = new TextField();
        HorizontalLayout layoutRow3 = new HorizontalLayout();
        TextField textField6 = new TextField();
        TextField textField7 = new TextField();
        Checkbox checkbox = new Checkbox();
        HorizontalLayout layoutRow4 = new HorizontalLayout();
        TextField textField8 = new TextField();
        ComboBox comboBox2 = new ComboBox();
        HorizontalLayout layoutRow5 = new HorizontalLayout();
        TextField textField9 = new TextField();
        DateTimePicker dateTimePicker = new DateTimePicker();
        HorizontalLayout layoutRow6 = new HorizontalLayout();
        TextField textField10 = new TextField();
        DateTimePicker dateTimePicker2 = new DateTimePicker();
        HorizontalLayout layoutRow7 = new HorizontalLayout();
        DateTimePicker dateTimePicker3 = new DateTimePicker();
        Checkbox checkbox2 = new Checkbox();
        TextArea textArea = new TextArea();
        getContent().setWidth("100%");
        getContent().getStyle().set("flex-grow", "1");
        layoutColumn2.setWidthFull();
        getContent().setFlexGrow(1.0, layoutColumn2);
        layoutColumn2.setWidth("100%");
        layoutColumn2.getStyle().set("flex-grow", "1");
        h1.setText("Modulo Film");
        h1.setWidth("max-content");
        layoutRow.setWidthFull();
        layoutColumn2.setFlexGrow(1.0, layoutRow);
        layoutRow.addClassName(Gap.MEDIUM);
        layoutRow.setWidth("100%");
        layoutRow.setHeight("120px");
        textField.setLabel("Text field");
        textField.setWidth("min-content");
        textField2.setLabel("Text field");
        textField2.setWidth("min-content");
        comboBox.setLabel("Combo Box");
        comboBox.setWidth("min-content");
        setComboBoxSampleData(comboBox);
        layoutRow2.setWidthFull();
        layoutColumn2.setFlexGrow(1.0, layoutRow2);
        layoutRow2.addClassName(Gap.MEDIUM);
        layoutRow2.setWidth("100%");
        layoutRow2.getStyle().set("flex-grow", "1");
        textField3.setLabel("Text field");
        textField3.setWidth("min-content");
        textField4.setLabel("Text field");
        textField4.setWidth("min-content");
        textField5.setLabel("Text field");
        textField5.setWidth("min-content");
        layoutRow3.setWidthFull();
        layoutColumn2.setFlexGrow(1.0, layoutRow3);
        layoutRow3.addClassName(Gap.MEDIUM);
        layoutRow3.setWidth("100%");
        layoutRow3.getStyle().set("flex-grow", "1");
        textField6.setLabel("Text field");
        textField6.setWidth("min-content");
        textField7.setLabel("Text field");
        textField7.setWidth("min-content");
        checkbox.setLabel("Checkbox");
        checkbox.setWidth("min-content");
        layoutRow4.setWidthFull();
        layoutColumn2.setFlexGrow(1.0, layoutRow4);
        layoutRow4.addClassName(Gap.MEDIUM);
        layoutRow4.setWidth("100%");
        layoutRow4.getStyle().set("flex-grow", "1");
        textField8.setLabel("Text field");
        textField8.setWidth("min-content");
        comboBox2.setLabel("Combo Box");
        comboBox2.setWidth("min-content");
        setComboBoxSampleData(comboBox2);
        layoutRow5.setWidthFull();
        layoutColumn2.setFlexGrow(1.0, layoutRow5);
        layoutRow5.addClassName(Gap.MEDIUM);
        layoutRow5.setWidth("100%");
        layoutRow5.getStyle().set("flex-grow", "1");
        textField9.setLabel("Text field");
        textField9.setWidth("min-content");
        dateTimePicker.setLabel("Date time picker");
        dateTimePicker.setWidth("min-content");
        layoutRow6.setWidthFull();
        layoutColumn2.setFlexGrow(1.0, layoutRow6);
        layoutRow6.addClassName(Gap.MEDIUM);
        layoutRow6.setWidth("100%");
        layoutRow6.getStyle().set("flex-grow", "1");
        textField10.setLabel("Text field");
        textField10.setWidth("min-content");
        dateTimePicker2.setLabel("Date time picker");
        dateTimePicker2.setWidth("min-content");
        layoutRow7.setWidthFull();
        layoutColumn2.setFlexGrow(1.0, layoutRow7);
        layoutRow7.addClassName(Gap.MEDIUM);
        layoutRow7.setWidth("100%");
        layoutRow7.getStyle().set("flex-grow", "1");
        dateTimePicker3.setLabel("Date time picker");
        dateTimePicker3.setWidth("min-content");
        checkbox2.setLabel("Checkbox");
        checkbox2.setWidth("min-content");
        textArea.setLabel("Text area");
        textArea.setWidth("100%");
        getContent().add(layoutColumn2);
        layoutColumn2.add(h1);
        layoutColumn2.add(layoutRow);
        layoutRow.add(textField);
        layoutRow.add(textField2);
        layoutRow.add(comboBox);
        layoutColumn2.add(layoutRow2);
        layoutRow2.add(textField3);
        layoutRow2.add(textField4);
        layoutRow2.add(textField5);
        layoutColumn2.add(layoutRow3);
        layoutRow3.add(textField6);
        layoutRow3.add(textField7);
        layoutRow3.add(checkbox);
        layoutColumn2.add(layoutRow4);
        layoutRow4.add(textField8);
        layoutRow4.add(comboBox2);
        layoutColumn2.add(layoutRow5);
        layoutRow5.add(textField9);
        layoutRow5.add(dateTimePicker);
        layoutColumn2.add(layoutRow6);
        layoutRow6.add(textField10);
        layoutRow6.add(dateTimePicker2);
        layoutColumn2.add(layoutRow7);
        layoutRow7.add(dateTimePicker3);
        layoutRow7.add(checkbox2);
        layoutColumn2.add(textArea);
    }

    record SampleItem(String value, String label, Boolean disabled) {
    }

    private void setComboBoxSampleData(ComboBox comboBox) {
        List<SampleItem> sampleItems = new ArrayList<>();
        sampleItems.add(new SampleItem("first", "First", null));
        sampleItems.add(new SampleItem("second", "Second", null));
        sampleItems.add(new SampleItem("third", "Third", Boolean.TRUE));
        sampleItems.add(new SampleItem("fourth", "Fourth", null));
        comboBox.setItems(sampleItems);
        comboBox.setItemLabelGenerator(item -> ((SampleItem) item).label());
    }
	
	
	@Override
    public void beforeEnter(BeforeEnterEvent event) {
  
        RouteParameters routeParams = event.getRouteParameters();
        Optional<String> idParam = routeParams.get("id");
        viewMode = routeParams.get("view").map(v -> v.equalsIgnoreCase("true")).orElse(true);

        if (idParam.isPresent()) {
            try {
//            	binder.bindInstanceFields(this); // associa automaticamente i campi del form alle proprietà del DTO basandosi sul nome.
            	this.dto =service.findById(Long.parseLong(idParam.get()));
//            	bindFieldsFromObject(this.dto);
//                updateFieldState(); // aggiorna i campi e visibilità del pulsante
            	binder.readBean(dto); // Popola automaticamente i campi. IMPORTANTE: prima si definiscono i binding, poi si chiama readBean().               
            } catch (NumberFormatException ex) {
            	event.forwardTo("not-found");
            }
        } else
            event.forwardTo("not-found");        
        
    }
}
