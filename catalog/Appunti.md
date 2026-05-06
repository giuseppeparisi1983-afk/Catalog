# Catalog

###Appunti


`**URL d'accesso**` `LOCAL: http://localhost:8081/ 		OnLine: https://catalogws.onrender.com`

`**Comando per l'export del db da lanciare da C:\xampp\mysql\bin **` `mysqldump -u root -p --single-transaction --quick --default-character-set=utf8mb4 catalog > "C:\Users\giuse\git\repository\catalog\src\main\resources\db\gestionale_dump.sql"`



### 1. Aggiunta logo  

Per aggiungere il logo (favicon) al tuo portale Vaadin con Spring Boot, basta copiare il file favicon.ico dentro src/main/resources/static. Spring Boot serve i file statici automaticamente da src/main/resources/static all’utente finale.

I browser cercano automaticamente favicon.ico nella root del sito. 
Se il file è lì, lo useranno come icona del sito, senza bisogno di annotazioni o configurazioni aggiuntive.

### 2. Automazione realizzazione del FrontEnd

`>` Dopo la definizione della View dentro `https://start.vaadin.com/app`

	#### Index

1. Per accedere ai dati reali dal backend iniettare o definire il mio Service
2. Popolare la griglia con i dati reali in modo da sostituire il backend fittizio con i dati reali.
3. gestire i listener per gli Anchor modifica e cancella
		`*` modifica: naviga verso il Form con ID
		`*` cancella: mostra un Dialog di conferma, imposta cancelled = true, salva e aggiorna la griglia.

4. ricerca di tutti i dati
aggiungi al metodo setGridSampleData() l'istruzione 
````grid.setItems(query -> samplePersonService.trovaTutti(VaadinSpringDataHelpers.toSpringPageRequest(query)).stream());````
5. ricerca puntuale.
La classe SpecificationFactory<T> è stata aggiunta come supporto per questa funzionalità. Serve per tipizzare il criterio di ricerca sull'oggetto org.springframework.data.jpa.domain.Specification<T> in modo da applicare un operatore diverso alla where condition in base al tipo di campo scelto come criterio di ricerca (vedi metodo build()). Se si tratta di una stringa sulla query verrà usato il like, con i tipi date invece si userà il between, infine per i boolean e i tipi number(come integer o double) useremo l'uguaglianza. Nel metodo trovaConFiltro() di PersonaSerice viene passato un oggetto di supporto di tipo DtoFilter che ragruppa il criterio di ricerca (searchFieldSelector) con il valore da trovare (searchField). Nel metodo viene inoltre utilizzata una findAll passando l'oggetto org.springframework.data.jpa.domain.Specification<T> che permette di definire criteri di ricerca dinamici usando l'API Criteria di JPA. Spring Data JPA usa il Specification per generare una Predicate con l'API Criteria che verrà applicata alla query JPA. L'altro parametro che viene passato è org.springframework.data.domain.Pageable che serve per specificare la paginazione e l'ordinamento (che viene applicato tramite Order nella clausola ORDER BY.). Include informazioni come numero di pagina, dimensione della pagina, e ordinamento

	#### Form

1. Per salvare o caricare i dati iniettare o definire il Service
2. definizione dell'oggetto Dto e del binder corrispondente
3. Perchè ````nomeBinder.bindInstanceFields(this);```` funzioni è necessario che i campi del form devono essere dichiarati come variabili di istanza (non locali dentro il costruttore). Inoltre i loro nomi devono corrispondere esattamente alle proprietà del DTO. Esempio: TextField name ↔ getName() / setName() nel DTO questo vale per tutti i componenti supportati da Vaadin Binder per i tipi Integer c'è IntegerField per i double c'è NumberField con le String o degli oggetti custom posso utilizzare le Combobox anche se per il tipo String si preferisce TextField. Infine, il Binder deve essere inizializzato DOPO che i campi sono stati creati.
4. Per collegare i campi del form ai dati del dto sull'override del metodo beforeEnter() basta fare
		- nomeBinder.bindInstanceFields(this);
		- findById(id) Se l’ID è nella route, viene trovato e caricato il dto da modificare (se presente)
		- nomeBinder.readBean(dto);
Nota: 
l'istruzione nomeBinder.bindInstanceFields(this); funziona solo se i campi sono accessibili direttamente (non incapsulati in altri oggetti). Per cui ad esempio se abbiamo un UtenteDto che contiene un oggetto PersonaDto. I campi di PersonaDto come nome, cognome, active sono annidati dentro l'istanza contenuta su UtenteDto. In questo dunque binder.bindInstanceFields(this) non può automaticamente associare nome, cognome, active ai campi di persona (nome dell'istanza dentro UtenteDto). Per questi campi devi usare il binding manuale con lambda.
5. definizione del listener per il salvataggio  addButton.addClickListener(e -> addNew());
addNew() è un metodo privato che mette un id=0 in modo che la findById non restituisca nulla e che ritrovi il form vuoto


### 3. Scelta utilizzo dei componenti sulle View

Se le opzioni sono poche (meno di 5-6) e molto diverse (es. "Maschio/Femmina", "Si/No", "Privato/Azienda"):
vengono utilizzate le Select come nelle index sul campo criterio di ricerca.
Se le opzioni sono più di 6 o se i nomi sono lunghi (es. "In attesa di autorizzazione del responsabile"):
si utilizzano le ComboBox che offre all'utente la possibilità di digitare dentro. Se hai un enum con 20 opzioni (es. Regioni italiane), l'utente scrive "Lom" e trova "Lombardia". Con la Select deve scorrere la lista. La possibilità di digitare per filtrare ("...resp...") velocizza molto l'inserimento dati per gli utenti esperti. Se in futuro l'enum diventa una tabella DB con 1000 record, la ComboBox gestisce il caricamento graduale (la Select no).

###4. Gestione dei campi Enumerations

Soluzione adottata è utilizzare un JPA Attribute Converter.
Questo agisce come un "ponte":

Java -> DB: Prende la label dall'Enum e la scrive nel DB.
DB -> Java: Legge la stringa dal DB e ritrova l'Enum corrispondente. Per questo è stato introdotto il metodo fromLabel() in tutti gli enumerations per poter ritrovare l'Enum partendo dalla stringa letta dal database.
Le classi Converter che tramite la classe astratta AbstractLabelConverter che implementa AttributeConverter<E, String> di jakarta interca il salvataggio e la lettura.
L'annotation @Converter(autoApply = true) indica che il converter  si applicherà automaticamente ovunque usi l'Enum specifico.

Per coerenza dei dati su video.Form la combo categoria è stata settata con tutte le categorie TRANNE Chitarra in modo che sia impossibile selezionare questa voce. Dopo il salvataggio non avrai mai nel DB un record con categoria=CHITARRA con i campi di GuitarDto popolati.
I video della categoria chitarra verranno gestiti solo nella sezione dedicata. Per cui, allo stesso modo di prima, in video.chitarra.Form è impossibile selezionare la categoria "SPORT". Quindi non avrai mai un video con i campi di GuitarDto popolati ma categorizzati come SPORT.

####User experience (UX):

L'utente va su "Modulo Video" -> Vede le opzioni pertinenti.
L'utente va su "Modulo Chitarra" -> Vede che la categoria che è già preimpostata su "Chitarra" (magari grigetta/read-only) e si concentra sui campi specifici.
Questo perchè l'utente vuole dire: "Voglio inserire un nuovo video di chitarra" e fare tutto in una schermata.

Viene mantenuta un'unica classe base (AbstractVideoForm) per gestire il layout, il binding del nome, del path e il setup della combo.
Successivamente viene specializzato il comportamento della combo nei figli con due righe di codice (setItems e setReadOnly).

###5. Gestione delle view con i campi condivisi

dentro ui.video vengono gestiti anche i video che riguardano la chitatra che hanno dei campi specifici da dover gestire. Questo viene fatto applicando il Template Method Pattern. L'idea è che il padre crea i campi comuni, ma NON li aggiunge al layout nel suo costruttore. Lascia che sia il figlio a decidere l'ordine finale chiamandolo direttamente nel costruttore o in un metodo specifico.

####5.1 Gestione condivisa dei form

sui DTO entra in gioco l’ereditarietà e il Template Method Pattern:

	- Definizione una classe VideoDto con i campi comuni.

	- Definizione di GiutareDto extends VideoDTO con i campi specifici (todo, difficolta, ecc.).

	- Definizione di una classe astratta AbstractVideoForm (Vaadin) che costruisce i campi comuni. Questa viene richiamata sia da video.Form che da video.chitarra.Form per i campi comuni.

	- Definizione di video.chitarra.Form extends AbstractVideoForm<GuitarDto> che oltre a richiamare AbstractVideoForm aggiunge i campi specifici per la chitarra.

Così l’utente vede un unico form per “Modulo Chitarra”, senza dover passare da due schermate. Il salvataggio dietro le quinte rimane doppio (prima Video, poi Chitarra), ma l’utente non se ne accorge.

####5.2 Gestione condivisa delle Index

Lo stesso meccanismo viene utilizzato per le Index di video e chitarra.
Creando una classe base per le tue View (Index) che chiamo AbstractVideoIndex, centralizzi la logica di creazione della griglia e delle colonne comuni, esattamente come abbiamo fatto per il Form.
La Classe Base Astratta (ui.video._base.AbstractVideoIndex)
Questa classe prepara il terreno: crea la grid, imposta il layout e definisce il metodo configureCommonColumns.
Nota importante: Salviamo le colonne comuni in variabili protected (es. colNome), così i figli possono usarle dentro setColumnOrder.
In questo modo riusciamo ad avere le colonne comuni (nome, path, categoria) definite una volta sola e le colonne specifiche (difficolta,todo, ecc.) definite nel figlio.

####5.3 I vantaggi di questa struttura
1. Manutenibilità: Se domani decidi che la colonna Path deve essere larga 200px invece di 150px, lo cambi solo in AbstractVideoIndex e si aggiorna automaticamente in tutte le pagine (Video generici, Chitarra, etc.).
2. Flessibilità: video.chitarra.Index non è costretto a tenere le colonne nell'ordine in cui sono state create. Con setColumnOrder ha il controllo totale. (Non utilizzato [DA VEDERE])
3. Pulizia: Le classi figlie contengono solo la logica che le differenzia dalle altre, senza codice duplicato per configurare le colonne base.
È esattamente la filosofia che hai usato per i Form, applicata alle Grid.



###6. Gestione delle index

#####6.1 Ricerca per Tag

Ricerca in lista (Grid, tabella di tutte le persone) Qui conviene usare i tag come criterio di ricerca insieme ai campi come nome, cognome, data di nascita. Perché? Perché i tag ti permettono di filtrare più velocemente e in modo mirato, senza dover caricare ogni record e poi filtrare lato applicazione. → In questo caso la query può essere ottimizzata con indici su oggetto_tag (tipo_oggetto, id_tag).

Form di dettaglio (modifica/inserimento) Qui invece non conviene caricare i tag insieme alla persona se non servono, perché è un passaggio in più. → La findById della persona resta semplice e veloce (indicizzata su id). → Solo quando entri nel form e ti serve mostrare i tag, fai la query aggiuntiva su oggetto_tag.

👉 In parole semplici: leggere solo la persona è più veloce, leggere persona + tag richiede un passaggio in più. Per questo conviene caricare i tag solo quando servono (es. nel form di dettaglio), non sempre.


######6.2 Provisioning sui dai

Il popolamento delle tabelle sulle index viene gestito così:


		CallbackDataProvider<AudioDto, Void> dataProvider =
        	    	DataProvider.fromCallbacks(
        	        	// fetch callback
        	    			query -> {   	           
        	        	Pageable pageable = VaadinSpringDataHelpers.toSpringPageRequest(query);
        	        	
        	            Page<AudioDto> page = service.findPage(filtro, pageable);
        	            query.getPageSize(); // utile per ottimizzazioni

    	    			grid.setVisible(true);
    	    			pageInfo.setVisible(true);

        	            return page.getContent().stream();
        	        },

        	        // Count callback
        	        query -> {
        	        	
        	        	int total = (int) service.findPage(filtro, Pageable.unpaged()).getTotalElements();
        	        	     	
        	        	 if (total == 0) {
        	        			grid.setVisible(false);
            	    			pageInfo.setVisible(false);
            	    			Notification.show("Nessun dato trovato", 3000, Notification.Position.MIDDLE);
        	             }
        	        	 query.getPageSize(); // utile per ottimizzazioni
        	            
        	            return total;
        	        }
        	    );

        	grid.setDataProvider(dataProvider);
        	
Vaadin usa questo flusso:

Prima chiama SEMPRE la count callback. Serve per sapere quante righe totali ci sono. Poi decide se chiamare la fetch callback  
La fetch viene chiamata solo se: la count > 0 la griglia deve effettivamente caricare righe
			
👉 Se la count callback restituisce 0, Vaadin NON chiama la fetch callback.  
E quindi il tuo codice “not found” dentro la fetch non viene mai eseguito.

######6.3 Utilizzo degli Anchor per i link che rimangono sulla stessa pagina


	Anchor del = new Anchor("#", "cancella");

Quando imposti un Anchor di Vaadin con href="#", stai dicendo al browser: “naviga alla stessa pagina, ma vai all’anchor vuoto”.
Il problema è che il browser interpreta comunque # come una navigazione, e Vaadin intercetta quella navigazione come un navigation event interno. Questo può causare un refresh o addirittura una redirect alla root dell’applicazione, a seconda della configurazione del router.

Vaadin Flow gestisce la navigazione tramite il router. Quando clicchi un link con href="#":

 - il browser genera un navigation event

 - Vaadin prova a interpretarlo come una route

 - # non corrisponde a nessuna route valida

 - Vaadin ti manda alla route di default (spesso /, cioè la home)
 
 per evitare questo basta che sull'href aggiungi la route della pagina corrente 
 
	 Es.  Anchor del = new Anchor("audio", "cancella");
 
 
 in questo modo quando clicchi sull'anchor:

 - Il browser intercetta il click e prepara la navigazione verso l’href

 - Vaadin intercetta l'evento click e invoca il tuo listener Java

 - Subito dopo, il browser esegue la navigazione verso l’href

 - Vaadin ricarica la pagina SOLO se la route cambia o se la route è configurata per ricaricare

 - Se la route è la stessa, Vaadin non ricrea la view, quindi non richiama onAttach(), afterNavigation() o il costruttore

👉 Risultato: il tuo listener viene eseguito, ma la pagina non viene ricreata, quindi non vedi nessun aggiornamento automatico. Quando navighi verso la stessa route, Vaadin non ricostruisce la UI.
La view rimane la stessa istanza in memoria.

Quindi se nel Listener c'è un metodo che non aggiorna i dati Vaadin non ricarica la pagina solo perché clicchi un link che punta alla stessa route se sei già sulla stessa route non farà nulla.


### 7. Deploy Online

Ad ogni singolo deploy, Render distrugge il vecchio container e ne crea uno completamente nuovo partendo dal tuo Dockerfile.

Immagine Immutabile: Viene creata una nuova "immagine" (uno snapshot del sistema) che contiene il sistema operativo, Java 21 e il tuo nuovo file .jar.

Ambiente Pulito: Questo garantisce che non ci siano "residui" di vecchi test. Se l'app gira sul tuo PC con Docker, girerà identica su Render.

Niente aggiornamenti "parziali": Non viene mai caricato solo il .jar su un ambiente esistente. Viene sempre ricostruita l'intera "scatola" (container).






