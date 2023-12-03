package com.example.projectalbum.Model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
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

    public static void sortByDateAscending(List<Category> categoryList) {
        Collections.sort(categoryList, new Comparator<Category>() {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

            @Override
            public int compare(Category category1, Category category2) {
                try {
                    Date date1 = dateFormat.parse(category1.getNameCategory());
                    Date date2 = dateFormat.parse(category2.getNameCategory());
                    return date1.compareTo(date2);
                } catch (ParseException e) {
                    e.printStackTrace();
                    return 0;
                }
            }
        });
    }

    // Sắp xếp theo ngày giảm dần
    public static void sortByDateDescending(List<Category> categoryList) {
        Collections.sort(categoryList, new Comparator<Category>() {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

            @Override
            public int compare(Category category1, Category category2) {
                try {
                    Date date1 = dateFormat.parse(category1.getNameCategory());
                    Date date2 = dateFormat.parse(category2.getNameCategory());
                    return date2.compareTo(date1);
                } catch (ParseException e) {
                    e.printStackTrace();
                    return 0;
                }
            }
        });
    }

}
