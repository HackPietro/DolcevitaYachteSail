# Dolcevita Yacht e Sail

Applicazione desktop in **Java e JavaFX** per un concessionario di barche usate. I clienti sfogliano le barche disponibili, le cercano per parola chiave o per categoria, le salvano in una wishlist e prenotano una visita in concessionario. L'amministratore gestisce gli annunci e controlla le prenotazioni.

Progetto universitario per il corso di **Ingegneria del Software** (CdS Informatica, Università della Calabria), focalizzato sull'applicazione dei **design pattern**. In particolare sono stati utilizzati i pattern **Proxy** e **Prototype**. L'interfaccia grafica è realizzata con **FXML e CSS**. Il repository include anche la documentazione di progetto.

---

## Funzionalità

### Tutti gli utenti

- **Home** con una panoramica delle barche attualmente in vendita
- **Ricerca** per parola chiave
- **Navigazione per categoria**: barche a motore, a vela, da pesca, sportive, tradizionali, catamarani, gommoni, trimarani
- **Scheda della barca** con foto, descrizione, prezzo e barche simili
- **Registrazione** con controllo dei campi: email valida e non già usata, password di almeno 6 caratteri, conferma password uguale
- **Login** e logout
- **Recupero password** dalla schermata di accesso

### Cliente registrato

- **Wishlist** personale: aggiunta e rimozione delle barche di interesse
- **Prenotazione di una visita**: scelta di giorno, mese e anno, con controllo che la data non sia nel passato
- **Le mie prenotazioni**: elenco delle visite prenotate, con possibilità di rimuoverle
- **Account**: modifica della password

### Amministratore

- **Aggiunta di un annuncio**: ID, nome, categoria, descrizione, parole chiave, prezzo e foto, che viene copiata tra le risorse dell'applicazione
- **Rimozione di un annuncio** tramite il suo ID, con eliminazione anche delle relative prenotazioni e voci di wishlist
- **Elenco di tutte le prenotazioni**, ordinate per data
- Nella scheda della barca i controlli di prenotazione non vengono mostrati

---

## Design pattern

| Pattern | Dove | Come |
|---|---|---|
| **Prototype** | `CloneableBarca`, `BarcaItem`, `BoxPageController` | `BarcaItem` implementa `CloneableBarca` (`clone()` e `updateDetails(Barca)`). Le card delle barche nelle pagine delle categorie vengono create clonando un elemento e aggiornandone i dettagli, invece di ricostruirle da zero |
| **Proxy** | `ProxyPrenotazione` | Punto di accesso unico alle prenotazioni: a seconda dell'utente autenticato restituisce tutte le prenotazioni (amministratore) oppure solo le proprie (cliente) |

Nel progetto compaiono anche classi **Singleton** (`DBConnection`, `Authentication`, `SceneHandler`, `BarcaHandler`).

---

## Tecnologie

| Ambito | Tecnologia |
|---|---|
| Linguaggio | Java 17 |
| Interfaccia | JavaFX 17.0.2, FXML, CSS, Ikonli (icone) |
| Database | SQLite (`sqlite-jdbc`) |
| Sicurezza | Password cifrate con BCrypt (`spring-security-core`) |
| Build | Maven |
| Test | JUnit 5 |

---

## Struttura del repository

```
DolcevitaYachteSail/
├── Documentazione.pdf                    # Documentazione di progetto
├── README.md
└── DolcevitaYachteSail/                  # Progetto Maven
    ├── pom.xml
    ├── database.db                       # Database SQLite con dati di esempio
    └── src/main/
        ├── java/com/progetto/ingsw/
        │   ├── Controller/               # Controller JavaFX (home, wishlist, prenotazioni, account...)
        │   │   └── Handler/              # Stato condiviso (barca selezionata)
        │   ├── DataBase/                 # Connessione, autenticazione, validazione, Proxy
        │   ├── Model/                    # Barca, Prenotazione, User
        │   └── View/                     # SceneHandler: gestione di scene e alert
        └── resources/com/progetto/ingsw/
            ├── fxmls/                    # Schermate FXML
            ├── immagini/                 # Foto delle barche
            ├── Loghi/                    # Icone e loghi
            └── fonts/Roboto/             # Font
```

---

## Database

Il database è un file SQLite, `database.db`, con quattro tabelle:

| Tabella | Contenuto |
|---|---|
| `utenti` | Nome, cognome, email (chiave primaria), password cifrata, flag `isAdmin` |
| `barche` | ID, nome, prezzo, descrizione, categoria, parole chiave per la ricerca |
| `wishlist` | Coppie utente e barca salvata |
| `prenotazioni` | Utente, barca, giorno, mese e anno della visita |

Il file incluso contiene 8 categorie con 6 barche ciascuna, per prezzi da 35.000 € a 4.000.000 €.

---

## Avvio del progetto

**Requisiti**: JDK 17 e Maven.

1. Clona il repository:

   ```bash
   git clone https://github.com/HackPietro/DolcevitaYachteSail.git
   cd DolcevitaYachteSail/DolcevitaYachteSail
   ```

2. Avvia l'applicazione dall'IDE (ad esempio IntelliJ IDEA) eseguendo la classe `com.progetto.ingsw.Main`.

> Il database viene aperto con il percorso relativo `database.db`, quindi l'applicazione va avviata dalla cartella che lo contiene (la cartella del progetto Maven). Anche le foto aggiunte dall'amministratore vengono salvate con un percorso relativo, in `src/main/resources/com/progetto/ingsw/immagini/`.

---

## Documentazione

La relazione [`Documentazione.pdf`](./Documentazione.pdf) contiene:

- analisi dei requisiti utente e diagramma dei casi d'uso
- schede dei casi d'uso: registrazione, aggiunta annuncio, wishlist, prenotazione visita
- modello di dominio
- diagrammi di sequenza di sistema e contratti delle operazioni
- architettura software a livelli: UI, Application e Domain

---

## Autori

- **Pietro Cimino** · [@HackPietro](https://github.com/HackPietro)
- **Andrea Dodaro**
