package it.catalog.ui.video;

import java.util.EnumSet;
import java.util.List;
import java.util.Map;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.OptionalParameter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import it.catalog.common.enums.CategorieVideo;
import it.catalog.service.dto.VideoDto;
import it.catalog.service.interfaces.IVideoService;
import it.catalog.ui.common.MainLayout;
import it.catalog.ui.video.base.AbstractVideoForm;
import it.catalog.ui.video.Index;

@Route(value="video-form", layout = MainLayout.class)
@PageTitle("Video - Form")
public class Form extends AbstractVideoForm<VideoDto> implements BeforeEnterObserver,HasUrlParameter<Integer> {

	private final Button salvaButton = new Button("Salva");
  private final Button backButton = new Button("← Indietro");

//Variabili sentinella
  private boolean isReadOnly = false;
  private Long currentId = null; // Se null = INSERT, se valorizzato = EDIT/VIEW
  
//Il service ci serve sia per leggere che per scrivere
  private final IVideoService videoService;

    public Form(IVideoService videoService) {
    	// Passiamo "Modulo Video" come terzo parametro al padre
        super(VideoDto.class, videoService, "Modulo Video");
        this.videoService = videoService;
        
        // Impostiamo nella combo solo le categorie che NON sono CHITARRA
        // I video di chitarra verranno gestiti nella sezione dedicata
        categoria.setItems(EnumSet.of(
        		CategorieVideo.spezzoni, 
        		CategorieVideo.documentario, 
        		CategorieVideo.musica, 
        		CategorieVideo.sport,
        		CategorieVideo.guida
        ));
        
        
        HorizontalLayout buttons = new HorizontalLayout(backButton, salvaButton);
        partialFormLout.addComponentAtIndex(0, buttons);
        
        salvaButton.addClickListener(e -> saveVideo());
        backButton.addClickListener(e ->  getUI().ifPresent(ui -> ui.navigate(Index.class)));
    }

 // ============================================================
    // PARTE 1: LETTURA DAL DB (Load)
    // ============================================================
    // Questo metodo viene chiamato AUTOMATICAMENTE da Vaadin quando entri nella pagina.
    // Esempio URL: .../video/form/5 (dove 5 è il parametro)
    @Override
    public void setParameter(BeforeEvent event, @OptionalParameter Integer videoId) {
        if (videoId != null) {
            // CASO MODIFICA: Cerco nel DB
           videoService.findById(videoId).ifPresentOrElse(
                    v -> {
                       // Popolo il form (e il binder) con i dati letti dal DB
                       setBean(v); 
//                        configureButton();
                    },
                    () ->   {
                    	setBean(new VideoDto());
                    	//event.forwardTo("Nesun video trovato")
                    }
                );
        } else {
            // CASO NUOVO INSERIMENTO: Creo un DTO vuoto
            setBean(new VideoDto());
        }
    }
    
    
    
    // ============================================================
    // PARTE 2: SCRITTURA SU DB (Save)
   // ============================================================
    private void saveVideo() {
        if (binder.validate().isOk()) {
            // 2. Recupero il DTO aggiornato dal Binder
             // (Contiene i dati digitati dall'utente + l'ID se era una modifica)
             VideoDto dtoDaSalvare = binder.getBean();

             try {
                 // 3. Chiamata al Service per scrivere su DB
                 videoService.save(dtoDaSalvare);

                 Notification.show("Video salvato correttamente!",3000, Notification.Position.TOP_CENTER);
                 getUI().ifPresent(ui -> ui.navigate(Index.class)); // Torna alla lista eventualmente
             } catch (Exception e) {
                 Notification.show("Errore durante il salvataggio: " + e.getMessage(),3000, Notification.Position.TOP_CENTER);
             }
         }
    }
    
    // 2. Gestione Query Parameters (URL: /video-chitarra/form/123?view=true)
    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        Map<String, List<String>> params = event.getLocation().getQueryParameters().getParameters();
        
        // Controllo la variabile sentinella "view"
        if (params.containsKey("view") && "true".equals(params.get("view").get(0))) {
            this.isReadOnly = true;
        } else {
            this.isReadOnly = false;
        }

        applyMode();
    }
    
    private void applyMode() {
        // Applica il readonly al form padre
        setReadOnlyMode(isReadOnly);
        
        // Nascondi il bottone salva se siamo in visualizzazione
        salvaButton.setVisible(!isReadOnly);
        
        if (isReadOnly) {
        	backButton.setText("Indietro");
        }
    }
  
}
