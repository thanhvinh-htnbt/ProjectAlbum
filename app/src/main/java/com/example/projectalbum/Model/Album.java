package com.example.projectalbum.Model;

public class Album {
    private int id;
    private Integer imgResource;
//    private String imageUrl;
    private String name;

    public Integer getImgResource() {
        return imgResource;
    }

    public Album(int id, Integer imgResource, String name) {
        this.id = id;
        this.imgResource = imgResource;
        this.name = name;
    }
//    public Album(int id, String name, String imageUrl) {
//        this.id = id;
//        this.imageUrl = imageUrl;
//        this.name = name;
//    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

//    public String getImageUrl() {
//        return imageUrl;
//    }


    public String getName() {
        return name;
    }

}
