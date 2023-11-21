package com.example.bakeryapplication.Models;

public class CategoriesFragmentModel {
    String name , description , imageUrl ,type;

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

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public CategoriesFragmentModel(String name, String description, String disCount, String imageUrl, String type) {
        this.name = name;
        this.description = description;
        this.imageUrl = imageUrl;
        this.type = type ;
    }

    public CategoriesFragmentModel() {
    }
}
