# Catalog


##Versioni

- **0.0.1-SNAPSHOT**  implementazione del metodo di ricerca valido solo per i file audio
- **1.0.0-SNAPSHOT**  centralizzazione del sistema di ricerca da riutilizzare sulle altre index
===================================================================================================================================

##TODO

- Bug Audio.Index: 
			1. paginazione da sistemare non funziona per come dovrebbe
			2. non funziona la ricerca per tags
			3. la sequenza della prima colonna non si aggiorna con la paginazione
- Bug Audio.Form:
			1. non funziona l'aggiornamento dei tag
			2. grafica da sistemare
			3. manca la validazione dei campi
			
- Gestione delle utenze Prendere spunto da Persone
- Export dei dati
- Dashboard con le statistiche. Analisi sui vari framework
- Manca la gestione dei film
- Bug sulla ricerca nella Index: è necessario dare la possibilità di scegliere qualsiasi campo come criterio di ricerca. Le label devono essere uguali alle intestazioni delle colonne del grid. [DA VERIFICARE]
- Audio.Index: da verificare l'errore sull'intervallo delle date [DA VERIFICARE]
- Documenti.Index: Non funziona la ricerca, inoltre quando si cancella il campo search deve resettare anche la select del criterio
- Immagini.Index: Le label devono essere uguali alle intestazioni delle colonne del grid. Quando si cancellano i campi di ricerca bisogna restituire una findAll
- Video.Index: la ricerca funziona solo per titolo. Inoltre, quando si cancellano i campi di ricerca bisogna restituire una findAll
- Chitarra.Inex: ci sono solo i campi di GuitarDto ma dovrebbero comparire anche gli altri di VideoDto. Inoltre, quando si cancellano i campi di ricerca bisogna restituire una findAll. La ricerca non funziona.

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
- [Ambiente]: vedi procedura per creare il repository su GitLab e un ambiente di test. e il db come faccio a portarlo online ?
- [Film]: metodo per il recupero di tutti i metadati dal file excel al momento non ancora disponibile e aggiorna i dati in tabella. Il file copertina, lo copi su src/resources/copertine/film e sul db memorizzi solo il nome del file. 

===================================================================================================================================

##FATTO

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
