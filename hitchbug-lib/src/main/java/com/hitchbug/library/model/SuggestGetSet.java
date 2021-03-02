package com.hitchbug.library.model;

public class SuggestGetSet {

    String id, name, price;

    public SuggestGetSet(String id, String name, String price) {
        this.setId(id);
        this.setName(name);
        this.price = price;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

}