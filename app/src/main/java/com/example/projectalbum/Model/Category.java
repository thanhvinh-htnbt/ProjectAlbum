package com.example.projectalbum.Model;

import java.util.List;

public class Category {
    private String nameCategory;
    private List<Photo> listPhoto;

    public String getNameCategory() {
        return nameCategory;
    }

    public void setNameCategory(String nameCategory) {
        this.nameCategory = nameCategory;
    }
    public Category(List<Photo> listPhoto) {
        this.listPhoto = listPhoto;
    }
    public List<Photo> getListPhoto() {
        return listPhoto;
    }

    public void setListPhoto(List<Photo> listPhoto) {
        this.listPhoto = listPhoto;
    }

    public void addListPhoto(Photo img){this.listPhoto.add(img);}

    public Category(String nameCategory, List<Photo> listPhoto) {
        this.nameCategory = nameCategory;
        this.listPhoto = listPhoto;
    }


}
