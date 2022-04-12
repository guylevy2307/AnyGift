package com.example.anygift.Retrofit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CardType {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("categories")
    @Expose
    private List<String> categories = null;
    @SerializedName("id")
    @Expose
    private String id;

    @Override
    public String toString() {
        return "CardType{" +
                "name='" + name + '\'' +
                ", categories=" + categories +
                ", id='" + id + '\'' +
                '}';
    }

    public CardType(String name, List<String> categories, String id) {
        this.name = name;
        this.categories = categories;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getCategories() {
        return categories;
    }

    public void setCategories(List<String> categories) {
        this.categories = categories;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}