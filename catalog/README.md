# Catalog


##Versioni

- **0.0.1-SNAPSHOT**  implementazione del metodo di ricerca valido solo per i file audio
- **1.0.0-SNAPSHOT**  centralizzazione del sistema di ricerca da riutilizzare sulle altre index
- **1.0.1-SNAPSHOT**  bugFix su ricerca dentro ui.audio.index e salvataggio dentro ui.audio.form
- **1.0.2-SNAPSHOT**  bugFix su indirizzamento verso ui.documenti.index e ui.documenti.form
- **1.0.3-SNAPSHOT**  bugFix su documenti.index e imagini.index
- **1.1.0-SNAPSHOT**  introduzione della index per i film
- **1.1.1-SNAPSHOT**  bugFix per l'index dei video e dei video chitarra
- **1.1.2-SNAPSHOT**  bugFix sul salvataggio dei tags sul Document.Form
- **1.1.3-SNAPSHOT**  bugFix sul salvataggio degli audio e riordino dei campi per documenti e audio
- **1.1.4-SNAPSHOT**  bugFix sul form degli audio
- **1.2.0-SNAPSHOT**  Aggiunta del form per i Film
- **2.0.0-SNAPSHOT**  Passaggio alla Struttura a Framework dove la classe AbstractCommonFileForm governa il layout dei Form
- **2.1.0-SNAPSHOT**  Introduzione delle descrizioni per le enumeration che vengono mostrate come ToolTip sulle ComboBox. Aggiunte Le enumeration per le estensioni dei Video, Film e delle immagini. Aggiunta l'enumeration ImageType per il tipo di immagine da gestire.
Aggiunta l'enumeration Livello per descrivere il livello di difficoltà richiesto per le video-lezioni di Chitarra.
- **2.2.0-SNAPSHOT**  Aggiunta dell'autocompletamento per i campi del form per i Film
- **3.0.0-SNAPSHOT**  Aggiunta la gestione dei video e la navigazione rapida direttamente dalle pagine dei form
===================================================================================================================================

##TODO

- [VERIFICA]: sulle entity la gestione dei formati và fatta con le sole stringhe come per AudioFile,Video o Film oppure basta la sola definizione del tipo enumeration senza l'annotation @Enumerated(EnumType.STRING) ???

- [VERIFICA]: sul form dei video và agganciato playerImage un immagine di abbelimento e testata la funzionalità incrementaVisualizzazioni() ogni volta che si clicca sull'immagine

- [Sviluppo]:Aggiunto il controllo dei duplicati sui tipi di file ✅

- [Sviluppo]: sui form dovrebbe essere possibile spostarsi da una voce all'altra senza tornare indietro ✅

- [Sviluppo]: Introduzione di una login per la protezione dei dati (Vedi Persone)

- [Sviluppo]: Manca la gestione dei Tag e delle Emozioni (CRUD)
- [Bug]: l'ordine di comparsa delle colonne nella tabelle della pagina index deve rispecchiare quello del relativo Form

- [Bug]: sui form il torna indietro (o l'annulla in modifica) deve tornare alla pagina dell'index che ho lasciato non alla prima pagina ✅
- [Sviluppo]: Introduzione dell'autocompletamento dei campi sui form (vedi TMDB per i film e MusicBrainz o iTunes Search API per l'audio)

			- autocompletamento per Film.Form ✅
			- autocompletamento per Audio.Form
			- [Bug]: Nel caso ci siano 2 o più film/audio con lo stesso titolo

- Immagini.form: ordine  e obbligatorietà da rivedere
- Video.form e Chitarra.form: [Bug] errore sull'accesso alla pagina 
- Film.form: da vedere comletamente
- Bug Audio.Index: 
			1. paginazione da sistemare non funziona per come dovrebbe ✅
			2. non funziona la ricerca per tags ✅
			3. la sequenza della prima colonna non si aggiorna con la paginazione ✅
			4. refresh dei risultati da verificare ✅
- Bug Audio.Form:
			1. non funziona l'aggiornamento dei tag
			2. grafica da sistemare ✅
			3. manca la validazione dei campi
			
- Export dei dati
- Dashboard con le statistiche. Analisi sui vari framework
- Bug sulla ricerca nella Index: è necessario dare la possibilità di scegliere qualsiasi campo come criterio di ricerca. Le label devono essere uguali alle intestazioni delle colonne del grid. [DA VERIFICARE] ✅
- Audio.Index: da verificare l'errore sull'intervallo delle date [DA VERIFICARE] ✅
- Documenti.Index: Non funziona la ricerca, inoltre quando si cancella il campo search deve resettare anche la select del criterio
- Immagini.Index: Le label devono essere uguali alle intestazioni delle colonne del grid. Quando si cancellano i campi di ricerca bisogna restituire una findAll ✅
- Video.Index: la ricerca funziona solo per titolo. Inoltre, quando si cancellano i campi di ricerca bisogna restituire una findAll
- Chitarra.Index: ci sono solo i campi di GuitarDto ma dovrebbero comparire anche gli altri di VideoDto. Inoltre, quando si cancellano i campi di ricerca bisogna restituire una findAll. La ricerca non funziona.
- Documenti.form:non funziona il sistema della view true | false
- Documenti.form: aggiungi l'anteprima o l'immagine con il link dell'apertura
- Bug Chitarra.form: stile da sistemare
- Ricerca: Aggiungere le seguenti funzionalità

				- supporto per ordinamento dinamico
				
				- supporto per filtri multipli combinati con AND/OR
				
				- supporto per debounce (evita troppe query mentre l’utente digita)
				
				- supporto per ricerca full-text
				  
				- ottimizzazione delle query per tags (vedi problema N+1 query)  

- Tasto torna indietro sull'index e sui form di tutte le entity coinvolte
- [Ambiente]: Installazione di AS locale in modo da poter accedere in lettura alle risorse in locale
- [Ambiente]: Se la fase di lettura và bene, introdurre su ogni form uno spazio che tramite Drag&Drop permetta l'upload del file fisico da salvare nel path specificato. Note Salva il file anche sull'HD di backup
- [Ambiente]: Sulla classe main CatalogApplication estrarre dall'hd tutti i metadati già presenti e aggiornare il db
- [Film]: metodo per il recupero di tutti i metadati dal file excel al momento non ancora disponibile e aggiorna i dati in tabella. Il file copertina, lo copi su src/resources/copertine/film e sul db memorizzi solo il nome del file. 
- [Film]: è necessario dare la possibilità di ricercare i film in base al proprio stato d'animo, da inserire come per i tags opzionalmente in fase di inserimento o aggiornamento di un film e selezionare l'emotion appropriata sulla pagina di ricerca

===================================================================================================================================

##FATTO

- [Bug]: il footer si sposta se provo a ingrandire o ridurre la pagina ✅
- Audio.form: [Bug] Non funziona il salvataggio e ordine e obbligastorietà dei campi da rivedere ✅
Manca il titolo del form la gestione del campo cancelled e attenzione alla gestione del campo Formato che sulla view è duplicato con il campo estensione ✅
- Documenti.form:manca il titolo del form ✅
- [Bug]: Film.Index non funziona la ricerca ✅
- Documenti.form: ordine e obbligatorietà da rivedere ✅
- Tradotti i menù dei componenti DatePicker e DateTimePicker
- Bug: Documenti.Form non funziona il salvataggio dei tags ✅
- rimappa l'entity dei video e controlla il mapping de campi sui film nella index ✅
- Aggiungi la parte dei tags ai video ✅
- [Ambiente]: vedi procedura per creare il repository su GitLab e un ambiente di test. e il db come faccio a portarlo online ✅
- BE dei film fatto. FE da fare. Aggiungere nuovi dati con i campi aggiunti ✅
- da aggiungere il footer con il Copyright ✅
- Bug Audio.Index:

				1) Le label della select devono essere uguali alle intestazioni delle colonne del grid. ✅
				2) Quando si cancellano i campi di ricerca bisogna restituire una findAll [OK]. ✅
				3) Non funziona quando viene cambiato il criterio di ricerca [OK]. ✅
				3.1) gestione del caso not found [OK]. ✅
				4) ricerca per tags ✅
				5) quando faccio una ricerca per data vorrei un campo devo poter scegliere una data su un campo di tipo 
				  com.vaadin.flow.component.datepicker.DatePicker non un TextField ✅
				
- Home: Cambia la navigazione. Aggiungi nella index dei video il link per video Chitarra e togli il button dalla Home. ✅
- Sul form Stabilire un ordinamento dei campi da mostrare

    	1) categoria;
	2) titolo; durataMin;
    	3) rating;
    	4a) visualizzazioni; 4b) preferito;
	5) ultimaVisualizzazione;
    	6) percorsoFile;
	7) dataArchiviazione;	backup;
	8) note; cancelled;

Da risolvere il problema sul disallineamento dei campi ✅
- Bug Documenti.index: la paginazione non funziona perchè si vedono tutti i risultati in un unica pagina e così il menù di navigazione delle pagine non ha senso ✅
- Bug Documenti.index: ricerca istantanea da rivedere ✅
- Bug Documenti.form: da sistemare✅
- Bug Chitarra.index: da sistemare lo stile di visualizzazione e la cancellazione che non funziona✅
- il form Chitarra deve ereditare il form Video. Per cui su Modulo Chitarra devo poter vedere il form Video + le informazioni di Chitarra ✅
- Dopo il salva devo ritornare alla lista di valori ✅
- Ridefinizione dell'index e del form di Chitarra ✅
- Bug sul salvataggio dei video chitarra ✅
- Implementazione di una lista e un form con l'uso di https://start.vaadin.com/
e poi integrare sul mio BE ✅
- Index di Persone: controllo della findAll con Pageable per la ricerca con filtro ✅
- organizzazione delle classi con l'aggiunta del package per ogni dto. ListView diventa index e FormView diventa Form ✅
- Aggiunta alla home dei button Documenti e Film ✅
- Allineamento immagine con il form ✅
- Bug su ListChitarraView  ✅
- sostituire il logo di spring boot ✅
- Il campo rating và trasformato e mostrato con le stelline da 1 a 5. Questo solo sul form non nella lista di tutti i video ✅
- Aggiunta dell'immagine cliccabile per il play ✅
- Quando clicco sul video oltre la chiamata al play del video con il Path di riferimento deve partire un altra chiamata che aggiorna il numero di visualizzazioni e la data ultima visualizzazione ✅
- Problema sulle dipendenze per il logo ✅
- funzionalità del tasto cerca ✅
- il tasto Cancella non deve far scomparire il record ma cambiare lo stile della scritta che deve essere griggia ✅
- Sul form dei video La categoria deve essere una select non un campo text ✅
- Funzionamento dell'ordinamento e della paginazione ✅
- Implementazione del ripristino degli item se vengono cancellati ✅
- Ricerca dinamica. Possibilità di scegliere il campo di ricerca ✅
- VideoForm da rivedere completamente ✅
- Non funziona il pulsante Salva nel form del video. Problema sul mapping delle date ✅
- La checkbox preferito vorrei che si vedesse con l'immagine del mi piace. Se preferito è false l'immagine si vede sempre ma non è colorata. ✅
- Anche la checkbox backup trasformala in un immagine cliccabile che si colora quando il valore è true.✅
- Testing su inserimento nuovo video. ✅
- inserimento path su modifica o inserimento RISOLTO ✅
- Il Path del file deve arrivare con la root da aggiungere sull'interfaccia mapstruct. 
Questo campo và allargato in modo che si veda tutto e mostrato solo se view è false. ✅
