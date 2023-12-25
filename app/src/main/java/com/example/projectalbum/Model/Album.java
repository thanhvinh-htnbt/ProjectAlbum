package com.example.projectalbum.Model;

public class Album {
    private String id;
    private String imgResource;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getName() {
        return name;
    }

    public int getCount(){return count;}
    public void setCount(int count){this.count = count;}

    public void setName(String Name) {
        this.name = name;
    }
    public boolean isSelected() {
        return IsSelected;
    }

    public void setSelected(boolean selected) {
        IsSelected = selected;
    }
}
