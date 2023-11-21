package com.example.bakeryapplication.Models;

public class HomeFragmentCategoriesModel {
    private String image ,title,type ;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public HomeFragmentCategoriesModel() {
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public HomeFragmentCategoriesModel(String image, String title, String type) {
        this.image = image;
        this.title = title;
        this.type= type ;
    }
}
