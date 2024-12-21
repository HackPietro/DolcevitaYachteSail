package com.progetto.ingsw.DataBase;

import com.progetto.ingsw.Model.Barca;
import com.progetto.ingsw.Model.Prenotazione;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;


public class ProxyPrenotazione {
    private static ProxyPrenotazione instance = null;
    public static ProxyPrenotazione getInstance(){
        if (instance == null){
            instance = new ProxyPrenotazione();
        }
        return instance;
    }

    public CompletableFuture<ArrayList<Prenotazione>> getPrenotazioni() {
        if (Authentication.getInstance().isAdmin()) {
            return DBConnection.getInstance().getPrenotazioniAdmin();
        }
        else{
            return DBConnection.getInstance().getPrenotazione(Authentication.getInstance().getUser().email());
        }
    }

}
