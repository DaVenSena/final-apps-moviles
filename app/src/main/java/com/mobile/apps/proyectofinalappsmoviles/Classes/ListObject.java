package com.mobile.apps.proyectofinalappsmoviles.Classes;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class ListObject implements Parcelable {
    private long quantity;
    private String imageURL;
    private String name;
    private String id;

    public ListObject(String id, long quantity, String imageURL, String name) {
        this.id = id;
        this.quantity = quantity;
        this.imageURL = imageURL;
        this.name = name;
    }

    public ListObject(long quantity, String imageURL, String name) {
        this.quantity = quantity;
        this.imageURL = imageURL;
        this.name = name;
    }

    protected ListObject(Parcel in) {
        quantity = in.readLong();
        imageURL = in.readString();
        name = in.readString();
    }

    public static final Creator<ListObject> CREATOR = new Creator<ListObject>() {
        @Override
        public ListObject createFromParcel(Parcel in) {
            return new ListObject(in);
        }

        @Override
        public ListObject[] newArray(int size) {
            return new ListObject[size];
        }
    };

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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "ListObject{" +
                "quantity=" + quantity +
                ", imageURL='" + imageURL + '\'' +
                ", name='" + name + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeLong(quantity);
        dest.writeString(imageURL);
        dest.writeString(name);
    }
}
