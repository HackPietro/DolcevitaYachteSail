package com.progetto.ingsw.Controller.Handler;

import com.progetto.ingsw.Model.Barca;

public class BarcaHandler {
    private static BarcaHandler instance = null;
    private Barca barca = null;

    private BarcaHandler() {}

    public static BarcaHandler getInstance() {
        if(instance == null)
            instance = new BarcaHandler();
        return instance;
    }

    public void setBarca(Barca barca) {
        this.barca = barca;
    }

    public Barca getBarca() {
        return barca;
    }
    public void setNullBarca(){this.barca = null;}
}
