package com.mobile.apps.proyectofinalappsmoviles.Classes;

import com.fasterxml.jackson.annotation.*;
import java.time.OffsetDateTime;

public class Note {
    private String id;
    private String title;
    private String desc;
    private OffsetDateTime createdAt;

    @JsonProperty("id")
    public String getID() { return id; }
    @JsonProperty("id")
    public void setID(String value) { this.id = value; }

    @JsonProperty("title")
    public String getTitle() { return title; }
    @JsonProperty("title")
    public void setTitle(String value) { this.title = value; }

    @JsonProperty("desc")
    public String getDesc() { return desc; }
    @JsonProperty("desc")
    public void setDesc(String value) { this.desc = value; }

    @JsonProperty("createdAt")
    public OffsetDateTime getCreatedAt() { return createdAt; }
    @JsonProperty("createdAt")
    public void setCreatedAt(OffsetDateTime value) { this.createdAt = value; }
}