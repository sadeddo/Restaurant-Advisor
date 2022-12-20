package com.restaurantadvisor.myapplication;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Menus {
    @SerializedName("id")
    @Expose
    private Integer id;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("description")
    @Expose
    private String description;

    @SerializedName("price")
    @Expose
    private Float price;

    @SerializedName("restaurant_id")
    @Expose
    private Integer restaurant_id;

    public Integer getIdMenu() {
        return id;
    }
    public void setIdMenu(Integer id) {
        this.id = id;
    }

    public String getNameMenu() {
        return name;
    }
    public void setNameMenu(String name) {
        this.name = name;
    }

    public String getDescriptionMenu() {
        return description;
    }
    public void setDescriptionMenu(String description) {
        this.description = description;
    }

    public Float getPriceMenu() {
        return price;
    }
    public void setPriceMenu(Float price) {
        this.price = price;
    }

    public Integer getIdRest() {
        return restaurant_id;
    }
    public void setIdRest(Integer restaurant_id) {
        this.restaurant_id = restaurant_id;
    }
}
