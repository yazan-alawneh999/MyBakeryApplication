package com.example.bakeryapplication.Models;

import java.io.Serializable;

public class CartModel implements Serializable {
    String id ;
    String name;
    double fee;
    int quantity;
    double totalPrice;
    String pic;
    public CartModel(String name, double fee, int quantity, double totalPrice, String pic) {

        this.name = name;
        this.fee = fee;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
        this.pic = pic;
    }
    public CartModel() {
    }


    public void setQuantity(int quantity) {
        this.quantity = quantity;
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

    public double getFee() {
        return fee;
    }


    public int getQuantity() {
        return quantity;
    }


    public double getTotalPrice() {
        return totalPrice;
    }


    public String getPic() {
        return pic;
    }


    public void setFee(double fee) {
        this.fee = fee;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }
}



