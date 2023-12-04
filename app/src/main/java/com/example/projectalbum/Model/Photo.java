package com.example.projectalbum.Model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Photo {

    private String dateTaken;
    private int id;
    private String name;
    //danh sách album chứa hình ảnh này
    private List<Integer> listIDAlbum = new ArrayList<>();
    private String filePath;
    private Long size;
    private String Description;


    public Photo(){}


    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public int getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public List<Integer> getListIDAlbum() {
        return listIDAlbum;
    }

    public String getFilePath() {
        return filePath;
    }



    public String getDateTaken() {
        return dateTaken;
    }

    public void setDateTaken(String dateTaken) {
        this.dateTaken = dateTaken;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }


    public String getDescription() {
        return Description;
    }

    public void setDescription(String Description) {
        this.Description = Description;
    }

    // Sắp xếp theo tên tăng dần
    public static void sortByNameAscending(List<Photo> photoList) {
        Collections.sort(photoList, new Comparator<Photo>() {
            @Override
            public int compare(Photo photo1, Photo photo2) {
                return photo1.getName().compareTo(photo2.getName());
            }
        });
    }

    // Sắp xếp theo tên giảm dần
    public static void sortByNameDescending(List<Photo> photoList) {
        Collections.sort(photoList, new Comparator<Photo>() {
            @Override
            public int compare(Photo photo1, Photo photo2) {
                return photo2.getName().compareTo(photo1.getName());
            }
        });
    }

}
