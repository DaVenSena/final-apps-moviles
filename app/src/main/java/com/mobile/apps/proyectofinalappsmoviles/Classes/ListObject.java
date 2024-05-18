package com.mobile.apps.proyectofinalappsmoviles.Classes;

public class ListObject {
    private long quantity;
    private String imageURL;
    private String name;

    public ListObject(long quantity, String imageURL, String name) {
        this.quantity = quantity;
        this.imageURL = imageURL;
        this.name = name;
    }

    public long getQuantity() {
        return quantity;
    }

    public void setQuantity(long value) {
        this.quantity = value;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String value) {
        this.imageURL = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String value) {
        this.name = value;
    }

    @Override
    public String toString() {
        return "ListObject{" +
                "quantity=" + quantity +
                ", imageURL='" + imageURL + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}