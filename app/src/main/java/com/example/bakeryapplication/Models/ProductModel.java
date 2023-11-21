package com.example.bakeryapplication.Models;

import java.io.Serializable;

public class ProductModel implements Serializable {

    private String  pic , name ,description ,type , id ;
    private int prepareTime;
    private int calories;
    private int quantity;
    private double fee , rate ;
    private boolean isRecommanded ;

    public ProductModel() {
    }

    public ProductModel(String pic, String name, String description, String type, int prepareTime, int calories, double fee, boolean isRecommanded) {
        this.pic = pic;
        this.name = name;
        this.description = description;
        this.type = type;
        this.prepareTime = prepareTime;
        this.calories = calories;
        this.fee = fee;
        this.isRecommanded = isRecommanded;
    }


    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getPrepareTime() {
        return prepareTime;
    }

    public void setPrepareTime(int prepareTime) {
        this.prepareTime = prepareTime;
    }

    public int getCalories() {
        return calories;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getFee() {
        return fee;
    }

    public void setFee(double fee) {
        this.fee = fee;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public boolean isRecommanded() {
        return isRecommanded;
    }

    public void setRecommanded(boolean recommanded) {
        isRecommanded = recommanded;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
