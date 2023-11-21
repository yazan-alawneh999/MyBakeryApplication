package com.example.bakeryapplication.Models;

import java.util.ArrayList;

public class OrderModel {
    private String customerName ,customerPhone ;
    private ArrayList<CartModel> cartItems ;

    private String customerId ;
    private int id ;

    public OrderModel() {
    }

    public OrderModel(String customerName, String customerPhone, ArrayList<CartModel> cartItems , String customerId) {
        this.customerName = customerName;
        this.customerPhone = customerPhone;
        this.cartItems = cartItems;
        this.customerId = customerId ;
        this.id = id++ ;

    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

    public ArrayList<CartModel> getCartItems() {
        return cartItems;
    }

    public void setCartItems(ArrayList<CartModel> cartItems) {
        this.cartItems = cartItems;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
