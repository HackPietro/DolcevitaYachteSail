package com.progetto.ingsw.Model;

public record Barca(String id, String name, String description, String price, String category) {
    @Override
    public String toString() {
        return id + ";" + name + ";" + description + ";" + price  + ";" + category;
    }
}
