package com.example.projectalbum.Database;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;


import androidx.annotation.NonNull;

import com.example.projectalbum.Model.Album;
import com.example.projectalbum.Model.Category;
import com.example.projectalbum.Model.Photo;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class DB {

//    public static List<Photo> photoList = new ArrayList<>();

    public static List<Album> getAlbums(Context context) {
        ContentResolver contentResolver = context.getContentResolver();
        Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        String[] projection = new String[]{
                MediaStore.Images.Media.BUCKET_ID,
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
                MediaStore.Images.Media._ID,
                MediaStore.Images.Media.DATA
        };
        String orderBy = MediaStore.Images.Media.DATE_TAKEN + " DESC";
        Cursor cursor = contentResolver.query(uri, projection, null, null, orderBy);

        HashMap<String, Album> albumMap = new HashMap<>();
        List<Album> albumList = new ArrayList<>();

        if (cursor != null) {
            int bucketIdColumn = cursor.getColumnIndex(MediaStore.Images.Media.BUCKET_ID);
            int bucketNameColumn = cursor.getColumnIndex(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);
            int imageIdColumn = cursor.getColumnIndex(MediaStore.Images.Media._ID);
            int dataColumn = cursor.getColumnIndex(MediaStore.Images.Media.DATA);

            while (cursor.moveToNext()) {
                String bucketId = cursor.getString(bucketIdColumn);
                String bucketName = cursor.getString(bucketNameColumn);
                String imageId = cursor.getString(imageIdColumn);
                String data = cursor.getString(dataColumn);

                Album album = albumMap.get(bucketId);
                if (album == null) {
                    album = new Album(bucketId, bucketName, 0, data);
                    albumMap.put(bucketId, album);
                    albumList.add(album);
                }
                album.setCount(album.getCount() + 1);
            }
            cursor.close();
        }
        return albumList;
    }

    public static void createNewAlbum(Context context,String albumName) {
    //todo
    }


//=========
    public static List<Photo> getListPhoto(Context context)
    {
        List<Photo> photoList = new ArrayList<>();
//
//        List<String> pathImg = new ArrayList<>();

        int columnIndexData, thumb, dateIndex, descriptionIndex;

        String thumbnail = null;
        Long dateTaken = null;
        String imagePath = null;
        Long size;
        String description="";

        //Cột của bảng
        String[] projection = {MediaStore.MediaColumns.DATA,
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
                MediaStore.Images.Media.DATE_TAKEN,
                MediaStore.Images.Media.DESCRIPTION
        };



        String orderBy = MediaStore.Images.Media.DATE_TAKEN;

        Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,projection,
                null,null,orderBy + " DESC");

        columnIndexData = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        //thumb = cursor.getColumnIndexOrThrow(MediaStore.Images.Thumbnails.DATA);
        dateIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_TAKEN);

        descriptionIndex=cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DESCRIPTION);



        Calendar myCal = Calendar.getInstance();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

        if (cursor != null){
            while (cursor.moveToNext()){
                //thumbnail = cursor.getString(thumb);
                dateTaken = cursor.getLong(dateIndex);
                imagePath = cursor.getString(columnIndexData);

                myCal.setTimeInMillis(dateTaken);
                String dateText = formatter.format(myCal.getTime());

                File f=new File(imagePath);
                size=f.length();

                description=cursor.getString(descriptionIndex);

                Photo image = new Photo();

                image.setDateTaken(dateText);
                image.setFilePath(imagePath);
                image.setSize(size);
                image.setDescription(description);


                photoList.add(image);

            }

        }
        cursor.close();
        return photoList;
    }
    public static List<Photo> getListPhotoOfIdAlbum(Context context, String id)
    {
        List<Photo> photoList = new ArrayList<>();

        int columnIndexData, thumb, dateIndex;

        String thumbnail = null;
        Long dateTaken = null;
        String imagePath = null;
        Long size;

        //Cột của bảng
        String[] projection = {MediaStore.MediaColumns.DATA,
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
                MediaStore.Images.Media.DATE_TAKEN
        };

        String orderBy = MediaStore.Images.Media.DATE_TAKEN;

        // Thêm điều kiện để chỉ lấy hình ảnh thuộc album có ID được truyền vào
        String selection = MediaStore.Images.Media.BUCKET_ID + " = ?";
        String[] selectionArgs = { id };

        Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,projection,
                selection,selectionArgs,orderBy + " DESC");

        columnIndexData = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        //thumb = cursor.getColumnIndexOrThrow(MediaStore.Images.Thumbnails.DATA);
        dateIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_TAKEN);

        Calendar myCal = Calendar.getInstance();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

        if (cursor != null){
            while (cursor.moveToNext()){
                //thumbnail = cursor.getString(thumb);
                dateTaken = cursor.getLong(dateIndex);
                imagePath = cursor.getString(columnIndexData);

                myCal.setTimeInMillis(dateTaken);
                String dateText = formatter.format(myCal.getTime());

                File f=new File(imagePath);
                size=f.length();

                Photo image = new Photo();

                image.setDateTaken(dateText);
                image.setFilePath(imagePath);
                image.setSize(size);

                photoList.add(image);

            }

        }
        cursor.close();
        return photoList;
    }

    @NonNull
    public static List<Category> getListCategory(Context context) {
        List<Category> categoryList = new ArrayList<>();
        int categoryCount = 0;
       List<Photo> photoList = getListPhoto(context);

        try {
            categoryList.add(new Category(photoList.get(0).getDateTaken(), new ArrayList<>()));
            categoryList.get(categoryCount).addListPhoto(photoList.get(0));
            for (int i = 1; i  <photoList.size(); i++) {
                if (!photoList.get(i).getDateTaken().equals(photoList.get(i - 1).getDateTaken())) {
                    categoryList.add(new Category(photoList.get(i).getDateTaken(), new ArrayList<>()));
                    categoryCount++;
                }
                categoryList.get(categoryCount).addListPhoto(photoList.get(i));
            }
            return categoryList;
        } catch (Exception e) {
            return null;
        }

    }

    @NonNull
    public static List<Category> getListCategory(Context context, String id) {
        List<Category> categoryList = new ArrayList<>();
        int categoryCount = 0;
        List<Photo> photoList = new ArrayList<>();
        if(id!="")
        {
            photoList = getListPhotoOfIdAlbum(context,id);
        }else{
            photoList = getListPhoto(context);
        }


        try {
            categoryList.add(new Category(photoList.get(0).getDateTaken(), new ArrayList<>()));
            categoryList.get(categoryCount).addListPhoto(photoList.get(0));
            for (int i = 1; i  <photoList.size(); i++) {
                if (!photoList.get(i).getDateTaken().equals(photoList.get(i - 1).getDateTaken())) {
                    categoryList.add(new Category(photoList.get(i).getDateTaken(), new ArrayList<>()));
                    categoryCount++;
                }
                categoryList.get(categoryCount).addListPhoto(photoList.get(i));
            }
            return categoryList;
        } catch (Exception e) {
            return null;
        }

    }

}
