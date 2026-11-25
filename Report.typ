#set document(title: "JBriscola — Relazione sullo stato del progetto")
#set text(lang: "it", size: 11pt)
#set page(paper: "a4", margin: 2.2cm, numbering: "1")
#set par(justify: true)
#set heading(numbering: "1.")

#align(center)[
  #text(size: 21pt, weight: "bold")[JBriscola]

  #text(size: 14pt)[Relazione sullo stato del progetto]

  Versione di riferimento: commit `8273773` — “feat(view)!: add console view”.
]

= Obiettivo e stato di avanzamento

JBriscola è un progetto Java per la realizzazione del gioco della briscola,
sviluppato nell’ambito del corso di Metodologie di Programmazione. L’obiettivo
attuale è costruire un modello del gioco indipendente dall’interfaccia, sul quale
aggiungere progressivamente l’interazione con giocatori umani e strategie
automatiche più articolate.

La versione descritta permette di simulare partite complete tra bot, risolvere
le prese, assegnare le carte vinte alle squadre e calcolare i punteggi. Una vista
testuale consente di osservare l’evoluzione della partita. L’interazione umana
è predisposta nel modello, ma non è ancora completata. Questa relazione è una
base di lavoro aggiornabile e riguarda esclusivamente il codice presente nel
commit indicato.

= Struttura del progetto

Il codice è organizzato in tre package, secondo una separazione delle
responsabilità ispirata a Model–View–Controller:

- `model` contiene gli oggetti del dominio, le regole, la costruzione della
  partita e il suo avanzamento;
- `controller` configura la partita attraverso il builder, registra la vista
  e avvia il ciclo di aggiornamento;
- `view` riceve lo stato del modello. `View` conserva l’ultimo stato ricevuto,
  mentre `ConsoleView` lo stampa sul terminale.

Il progetto utilizza Java 25, Maven per la compilazione e la gestione delle
dipendenze, e JUnit Jupiter per i test. La documentazione comprende Javadoc e un
diagramma delle classi in PlantUML, che contiene anche elementi previsti per gli
sviluppi successivi.

= Modellazione del dominio

La carta è rappresentata dal record `Card`, composto da un rango (`Rank`) e un
seme (`Suit`). I quattro semi e i dieci ranghi generano le 40 carte del mazzo.
Ogni rango conserva separatamente il punteggio e la forza nella presa: questa
distinzione permette di confrontare anche carte con lo stesso valore in punti.

`Deck` gestisce la costruzione, il mescolamento e la pesca delle carte.
`Hand` raccoglie le carte di un giocatore e offre operazioni di inserimento,
rimozione e ricerca. La lista restituita dalla mano non è modificabile
dall’esterno.

La classe astratta `Player` contiene nome, squadra e mano. Le sue sottoclassi
sono `HumanPlayer` e `AIPlayer`. La rimozione di una carta dalla mano avviene in
`Player.playCard(Card)`, che rifiuta una carta non posseduta. L’aggiunta della
carta al tavolo è invece responsabilità di `Game`.

`Team` conserva i giocatori registrati e le carte vinte. I membri di una squadra
condividono la stessa istanza di `Team`, così le prese confluiscono in un unico
mazzetto. L’uguaglianza e il codice hash delle squadre dipendono dal loro
identificatore.

`Table` associa ciascun giocatore alla carta giocata usando una `LinkedHashMap`.
L’ordine di inserimento conserva l’ordine della presa, necessario per individuare
il seme della prima carta e il giocatore che ha aperto.

= Regole, strategie e costruzione della partita

Le regole sono separate dalla gestione della partita mediante l’interfaccia
`GameRules`. L’implementazione `BriscolaRules` determina la carta vincente e
somma i punti delle carte raccolte. Nella risoluzione di una presa prevale la
briscola; in sua assenza si confrontano le carte del seme iniziale. Tra carte
dello stesso seme prevale il rango più forte.

Per i bot è adottato il pattern *Strategy*: `AIPlayer` delega la scelta a
`AIStrategy`. L’implementazione disponibile, `RandomStrategy`, restituisce una
carta casuale della mano senza rimuoverla; il giocatore esegue poi la giocata.
Questa separazione permette di introdurre altre strategie mantenendo invariata
la gestione della mano.

`GameBuilder` prepara il numero di giocatori, le due squadre condivise, gli
umani indicati e gli eventuali observer. `fillPlayers()` completa i posti delle
squadre con bot. Prima di costruire `Game`, `build()` verifica che il numero
effettivo dei giocatori coincida con quello richiesto. Mazzo e tavolo vengono
creati da `Game` durante l’inizializzazione.

= Avanzamento della partita e osservazione dello stato

`Game.updateGame()` elabora una fase per chiamata. L’enum `Phase` distingue
inizializzazione, turno, attesa della scelta umana, raccolta della presa,
distribuzione e fine partita. `GameStatus` indica invece se il gioco può
proseguire automaticamente (`READY`) o deve fermarsi (`NOTREADY`).

L’inizializzazione distribuisce tre carte a ciascun giocatore e rivela la
briscola. La carta scoperta viene reinserita all’indice zero del mazzo:
poiché la pesca avviene dall’estremità opposta, sarà pescata per ultima.
Quando tutti hanno giocato, le carte sono assegnate alla squadra vincente,
il tavolo viene svuotato e l’ordine dei giocatori viene ruotato per far
iniziare il vincitore. Lo stesso ordine viene usato per la pesca successiva.
Quando il mazzo è vuoto si continua con le carte in mano, fino al loro
esaurimento.

`EmptyDeckException` segnala una pesca senza carte disponibili:
nella distribuzione iniziale viene convertita in un errore di stato, mentre
nelle distribuzioni successive consente di proseguire senza pescare.
`TurnEndedException` segnala il raggiungimento della fine dell’ordine di turno.

Il collegamento tra modello e vista usa il pattern *Observer*.
`GameObserver.update(Game)` riceve la notifica e può richiedere `getState()`.
I record `GameState` e `PlayerState` descrivono una fotografia dei dati pubblici:
fase, stato, giocatore corrente, briscola, carte residue, giocatori e punteggi.
Il tavolo è esposto come mappa carta–stato del giocatore, in ordine di giocata.
Le collezioni sono copiate e rese non modificabili, evitando di esporre alla
vista gli oggetti mutabili del modello. Le mani degli avversari sono
rappresentate soltanto dal numero di carte.

`ConsoleView` stampa questi dati a ogni notifica. Il controller predefinito
registra ancora `View`; la vista console è collegata esplicitamente nel test
dedicato.

= Verifica e aspetti da completare

La suite comprende 18 test e verifica mazzo, mano, costruzione della partita,
stati immutabili, notifiche e simulazioni tra bot. Il test con quattro bot e
`ConsoleView` esegue la partita fino a `ENDGAME`, controllando che mani, tavolo
e mazzo siano vuoti, che la vista sia aggiornata e che i punteggi sommino a 120.
Un limite di tempo e di aggiornamenti rileva eventuali cicli che non terminano.
La suite del commit di riferimento è stata eseguita con esito positivo.

Le simulazioni confermano il funzionamento del ciclo automatico, ma non
costituiscono ancora una verifica completa delle regole. Restano da affrontare:

- l’acquisizione della scelta umana e la ripresa della partita dopo l’attesa;
- l’alternanza delle squadre nell’ordine iniziale: il builder aggiunge ancora
  i bot prima di una squadra e poi dell’altra;
- il pareggio, che il metodo di selezione del vincitore non distingue ancora;
- una validazione più completa della configurazione e la definizione del
  passaggio tra `ENDGAME` ed `ENDED`;
- la rimozione delle notifiche duplicate, attualmente inviate sia da `Game`
  sia dal ciclo del controller, e test mirati sui casi di risoluzione delle prese.

Il risultato raggiunto è quindi un motore di gioco osservabile e verificato
nelle simulazioni automatiche, con una base su cui completare l’interazione
e consolidare le regole.
