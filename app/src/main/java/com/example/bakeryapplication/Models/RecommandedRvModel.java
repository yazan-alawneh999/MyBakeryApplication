package com.example.bakeryapplication.Models;

import java.io.Serializable;

public class RecommandedRvModel implements Serializable {
    private String itemName, pic, description;
    private Double fee;
    private int stars;
    private int time;
    private int calories;

    private int countItemInCart  ;




    public int getCountItemInCart() {
        return countItemInCart;
    }

    public void setCountItemInCart(int countItemInCart) {
        this.countItemInCart = countItemInCart;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getFee() {
        return fee;
    }

    public void setFee(Double fee) {
        this.fee = fee;
    }

    public int getStars() {
        return stars;
    }

    public void setStars(int stars) {
        this.stars = stars;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getCalories() {
        return calories;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }

    public RecommandedRvModel() {
    }

    public RecommandedRvModel(String itemName, String pic, String description, Double fee, int stars, int time, int calories) {
        this.itemName = itemName;
        this.pic = pic;
        this.description = description;
        this.fee = fee;
        this.stars = stars;
        this.time = time;
        this.calories = calories;
            
    }
}
