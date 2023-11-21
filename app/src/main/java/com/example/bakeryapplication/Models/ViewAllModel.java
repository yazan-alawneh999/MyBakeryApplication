package com.example.bakeryapplication.Models;

import java.io.Serializable;

public class ViewAllModel implements Serializable {
  String pic ,name ;
  double fee ;
  double time ,rate,calories;

  String description ;
  String type ;
  int quantity  ;



    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
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

    public double getFee() {
        return fee;
    }

    public void setFee(double fee) {
        this.fee = fee;
    }

    public double getTime() {
        return time;
    }

    public void setTime(double time) {
        this.time = time;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public double getCalories() {
        return calories;
    }

    public void setCalories(double calories) {
        this.calories = calories;
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

    public ViewAllModel(String pic, String name, double fee, double time, double rate, double calories, String description) {
        this.pic = pic;
        this.name = name;
        this.fee = fee;
        this.time = time;
        this.rate = rate;
        this.calories = calories;
        this.description = description;
    }

    public ViewAllModel() {
    }
}
