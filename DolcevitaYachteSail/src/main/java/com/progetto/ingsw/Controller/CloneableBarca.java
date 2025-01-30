package com.progetto.ingsw.Controller;

import com.progetto.ingsw.Model.Barca;

interface CloneableBarca {
    CloneableBarca clone();
    void updateDetails(Barca barca);
}

