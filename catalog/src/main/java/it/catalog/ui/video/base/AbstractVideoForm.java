package it.catalog.ui.video.base;


import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.shared.Tooltip;
import com.vaadin.flow.component.textfield.TextField;

import it.catalog.common.enums.CategorieVideo;
import it.catalog.common.enums.VideoFormat;
import it.catalog.service.dto.GuitarDto;
import it.catalog.service.dto.VideoRecord;
import it.catalog.service.dto.search.DtoFilter;
import it.catalog.service.interfaces.SearchService;
import it.catalog.ui.common.AbstractCommonFileForm;
import it.catalog.ui.utility.AppConverters;

//T deve essere VideoDto o una sua sottoclasse
public abstract class AbstractVideoForm<T extends VideoRecord> 
extends AbstractCommonFileForm<T, SearchService<T, DtoFilter>, DtoFilter> {

    // Campi specifici per i video che non sono in CommonFileForm
	protected TextField durataMin = new TextField("Durata (min)");
	protected final ComboBox<CategorieVideo> categoria = new ComboBox<>("Categoria");
	private ComboBox<VideoFormat> estensione = new ComboBox<>("Formato");
    protected Anchor imageLink = new Anchor();
    protected Image playerImage = new Image("images/video-player.jpg", "Play Video");

//     private boolean viewMode = true;

    public AbstractVideoForm(String title, SearchService<T, DtoFilter> service, Class<T> beanType) {

    	// Passiamo tutto al nonno (AbstractCommonFileForm) decidendo il prefisso una volta per tutte
        super(title, service, beanType, DtoFilter::new, 
                beanType.equals(GuitarDto.class) ? "video." : "");
    	
     // 2. BINDA SOLO I CAMPI DI QUESTA CLASSE (Video)
        setupVideoBindings();
        
        // Configuriamo il player video
        setupVideoPlayer();
    
    }
    
    private void setupVideoBindings() {
        String p = beanType.equals(GuitarDto.class) ? "video." : "";

        setupCombo(estensione, VideoFormat.values());
        setupCombo(categoria, CategorieVideo.values());

        binder.forField(estensione).asRequired("Campo obbligatorio").bind(p + "estensione");
        binder.forField(categoria).asRequired("Campo obbligatorio").bind(p + "categoria");
        binder.forField(durataMin).asRequired("Campo obbligatorio")
        .withConverter(new AppConverters.StringToDoubleConverter())
        .bind(p + "durataMin");

    }
    
    private void setupVideoPlayer() {
        playerImage.setWidth("200px");
        playerImage.getStyle().set("cursor", "pointer");
        playerImage.addClickListener(e -> incrementaVisualizzazioni());
        imageLink.add(playerImage);
        Tooltip.forComponent(imageLink).setText("Play video");
        setupCombo(estensione, VideoFormat.values());
    }
    
    
    private void incrementaVisualizzazioni() {
        if (bean != null && bean.getId() != null) {
            
        	 VideoRecord record = (VideoRecord) bean;
        	
        	// Logica per aumentare i click (deve essere implementata via service)
        	 int attuali = (record.getVisualizzazioni() != null) ? record.getVisualizzazioni() : 0;
        	 record.setVisualizzazioni(attuali + 1);
        	 record.setLastView(java.time.Instant.now()); // Aggiorniamo anche l'ultima visione
             service.save(bean);
             binder.readBean(bean); // Notifichiamo il binder che i dati nel bean sono cambiati
            
             // Aggiorniamo l'href dell'immagine/link se necessario
             imageLink.setHref(record.getPath() != null ? record.getPath() : "");
             
             Notification.show("Visualizzazioni aggiornate: " + record.getVisualizzazioni());
        }
        else {
            Notification.show("Salva il record prima di avviare il video");
        }
    }
    
    
 // Qui implementiamo come i Video/Chitarre mostrano i loro campi comuni
    @Override
    protected void addSpecificTopLayout(VerticalLayout mainLayout) {
        nome.setWidth("400px");
        HorizontalLayout row = new HorizontalLayout(nome, categoria,durataMin);
        row.setAlignItems(Alignment.BASELINE);
        mainLayout.add(row);
    }

    @Override
    protected void addSpecificMiddleLayout(VerticalLayout mainLayout) {
       
        // Aggiungiamo il blocco info file (path, estensione, dimensione)
        // Nota: estensione sarà passata dal form figlio (combo o text)
        addInfoFileLayout(mainLayout, estensione);
    }
    
    @Override
    protected String getTagType() {
    	return "Video"; 
    }
    

}