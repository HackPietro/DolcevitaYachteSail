package com.progetto.ingsw.Model;


public record Prenotazione(String id_utente, String id_barca, int giorno, int mese, int anno) {
        @Override
        public String toString() {
            return id_utente + ";" + id_barca + ";" + giorno + ";" + mese + ";" + anno;
        }
}
