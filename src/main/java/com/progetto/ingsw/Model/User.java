package com.progetto.ingsw.Model;

public record User(String email, String name, String surname, Boolean isAdmin) {

    @Override
    public String toString() {
        return email + ";" + name + ";" + surname + ";" + isAdmin;
    }

}
