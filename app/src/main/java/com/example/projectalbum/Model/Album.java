package com.example.projectalbum.Model;

//public class Album {
//    private int id;
//    private Integer imgResource;
//    //    private String imageUrl;
//    private String name;
//
//    public boolean IsSelected;
//
//    public Integer getImgResource() {
//        return imgResource;
//    }
//
//    public Album(int id, Integer imgResource, String name) {
//        this.id = id;
//        this.imgResource = imgResource;
//        this.name = name;
//        this.IsSelected = false;
//    }
////    public Album(int id, String name, String imageUrl) {
////        this.id = id;
////        this.imageUrl = imageUrl;
////        this.name = name;
////    }
//
//    public int getId() {
//        return id;
//    }
//
//    public void setId(int id) {
//        this.id = id;
//    }
//
////    public String getImageUrl() {
////        return imageUrl;
////    }
//
//
//    public String getName() {
//        return name;
//    }
//
//}

public class Album {
    private String id;
    private String imgResource;
    //    private String imageUrl;
    private String name;

    private int count;

    public boolean IsSelected;

    public String getImgResource() {
        return imgResource;
    }

    public Album(String albumId, String albumName, int count, String albumCoverPath) {
        this.id = albumId;
        this.imgResource = albumCoverPath;
        this.name = albumName;
        this.IsSelected = false;
        this.count = count;
    }
//    public Album(int id, String name, String imageUrl) {
//        this.id = id;
//        this.imageUrl = imageUrl;
//        this.name = name;
//    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

//    public String getImageUrl() {
//        return imageUrl;
//    }


    public String getName() {
        return name;
    }

    public int getCount(){return count;}
    public void setCount(int count){this.count = count;}

    public void setName(String Name) {
        this.name = name;
    }
}
