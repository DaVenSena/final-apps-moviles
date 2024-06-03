package com.mobile.apps.proyectofinalappsmoviles.Classes;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Note implements Parcelable {
    public static final Creator<Note> CREATOR = new Creator<Note>() {
        @Override
        public Note createFromParcel(Parcel in) {
            return new Note(in);
        }

        @Override
        public Note[] newArray(int size) {
            return new Note[size];
        }
    };
    private String id;
    private String title;
    private String desc;
    private Date createdAt;

    public Note(String id, String title, String desc, String createdAt) throws ParseException {
        this.id = id;
        this.title = title;
        this.desc = desc;
        this.createdAt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(createdAt);
    }

    protected Note(Parcel in) {
        id = in.readString();
        title = in.readString();
        desc = in.readString();
        createdAt = new Date(in.readLong());
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) throws ParseException {
        this.createdAt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(createdAt);
        ;
    }

    @NonNull
    @Override
    public String toString() {
        return "Note{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", desc='" + desc + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(title);
        dest.writeString(desc);
    }
}