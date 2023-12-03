package com.example.projectalbum.Model;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Photo {

    private String dateTaken;
    private int id;
    private String name;
    //danh sách album chứa hình ảnh này
    private List<Integer> listIDAlbum = new ArrayList<>();
    //để test sẽ thay đổi sau
    private String filePath;

    private Long size;

    private String Description;


    public Photo(){}

//    public Photo(int id, String name,  String filePath, Integer largeImages) {
//        this.id = id;
//        this.name = name;
////        this.listIDAlbum = listIDAlbum;
//        this.filePath = filePath;
//        this.largeImages = largeImages;


    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<Integer> getListIDAlbum() {
        return listIDAlbum;
    }

    public String getfilePath() {
        return filePath;
    }

//    public Integer getLargeImages() {
//        return largeImages;
//    }

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

    public long getDateTaken2() {
        long datelong = 0;
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        try {
            Date date = formatter.parse(dateTaken);
            datelong = date.getTime();
        } catch (ParseException e) {
            Log.e("DateConversion", "Failed to parse date: " + dateTaken, e);
        }
        return datelong;
    }
}
