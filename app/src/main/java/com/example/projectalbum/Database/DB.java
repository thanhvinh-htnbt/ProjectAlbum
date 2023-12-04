package com.example.projectalbum.Database;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.app.RecoverableSecurityException;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.projectalbum.Model.Album;
import com.example.projectalbum.Model.Category;
import com.example.projectalbum.Model.Photo;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class DB {
    public static final int REQUEST_CODE_PERMISSION = 1;
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

    public static void createNewAlbum(Context context, String albumName, List<Photo> images) {
        // Kiểm tra quyền truy cập bộ nhớ ngoài
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // Yêu cầu quyền truy cập bộ nhớ ngoài nếu chưa có
            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        } else {
            // Tạo thư mục mới với tên album
            File albumDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES +"/"+ albumName);
            if (!albumDir.exists()) {
                albumDir.mkdirs();
            }
            else {
                Toast.makeText(context, "Album đã tồn tại", Toast.LENGTH_SHORT).show();
                return;
            }

            // Duyệt qua danh sách các ảnh
            for (Photo image : images) {
                // Kiểm tra xem hình ảnh có tồn tại không
                File imageFile = new File(image.getFilePath());
                if (imageFile.exists()) {
                    // Sao chép hình ảnh vào thư mục album
                    File newImageFile = new File(albumDir, imageFile.getName());
                    try {
                        Files.copy(imageFile.toPath(), newImageFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                    } catch (IOException e) {
                        Log.e("CreateNewAlbum", "Failed to copy image file: " + image.getFilePath(), e);
                    }
                    // Tạo một đối tượng ContentValues để đóng gói các thông tin về ảnh
                    ContentValues imageValues = new ContentValues();
                    imageValues.put(MediaStore.Images.Media.TITLE, imageFile.getName());

                    imageValues.put(MediaStore.Images.Media.DATE_TAKEN, image.getDateTaken2());
                    imageValues.put(MediaStore.Images.Media.BUCKET_ID, albumName.hashCode());
                    imageValues.put(MediaStore.Images.Media.BUCKET_DISPLAY_NAME, albumName);
//                    imageValues.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
                    imageValues.put(MediaStore.Images.Media.DESCRIPTION, image.getDescription());
                    imageValues.put(MediaStore.Images.Media.DATA, newImageFile.getPath());
//                    imageValues.put(MediaStore.Images.Media._ID, image.getId());

                    // Tìm URI của hình ảnh trong MediaStore
                    String[] projection = { MediaStore.Images.Media._ID };
                    String selection = MediaStore.Images.Media.DATA + "=?";
                    String[] selectionArgs = new String[] { newImageFile.getPath() };

                    Cursor cursor = context.getContentResolver().query(
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                            projection,
                            selection,
                            selectionArgs,
                            null
                    );

                    if (cursor != null && cursor.moveToFirst()) {
                        int idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID);
                        long id = cursor.getLong(idColumn);
                        Uri imageUri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id);


                        // Chèn ảnh vào MediaStore
                        context.getContentResolver().update(imageUri, imageValues, null, null);
                    }
                    if (cursor != null) {
                        cursor.close();
                    }
                } else {
                    Log.e("CreateNewAlbum", "Image file does not exist: " + image.getFilePath());
                }
            }
        }
        DB.deletePhotos(context, images);
    }
    public static void insertAlbum(Context context, String albumName, List<Photo> images) {
        // Kiểm tra quyền truy cập bộ nhớ ngoài
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // Yêu cầu quyền truy cập bộ nhớ ngoài nếu chưa có
            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        } else {
            // Tạo thư mục mới với tên album
            File albumDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES +"/"+ albumName);
            if (!albumDir.exists()) {
                Toast.makeText(context, "Album không tồn tại", Toast.LENGTH_SHORT).show();
                return;
            }

            // Duyệt qua danh sách các ảnh
            for (Photo image : images) {
                // Kiểm tra xem hình ảnh có tồn tại không
                File imageFile = new File(image.getFilePath());
                if (imageFile.exists()) {
                    // Sao chép hình ảnh vào thư mục album
                    File newImageFile = new File(albumDir, imageFile.getName());
                    try {
                        Files.copy(imageFile.toPath(), newImageFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                    } catch (IOException e) {
                        Log.e("CreateNewAlbum", "Failed to copy image file: " + image.getFilePath(), e);
                    }
                    // Tạo một đối tượng ContentValues để đóng gói các thông tin về ảnh
                    ContentValues imageValues = new ContentValues();
                    imageValues.put(MediaStore.Images.Media.TITLE, image.getName());

                    imageValues.put(MediaStore.Images.Media.DATE_TAKEN, image.getDateTaken2());
                    imageValues.put(MediaStore.Images.Media.BUCKET_ID, albumName.hashCode());
                    imageValues.put(MediaStore.Images.Media.BUCKET_DISPLAY_NAME, albumName);
//                    imageValues.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
                    imageValues.put(MediaStore.Images.Media.DESCRIPTION, image.getDescription());
                    imageValues.put(MediaStore.Images.Media.DATA, newImageFile.getPath());
//                    imageValues.put(MediaStore.Images.Media._ID, image.getId());

                    // Tìm URI của hình ảnh trong MediaStore
                    String[] projection = { MediaStore.Images.Media._ID };
                    String selection = MediaStore.Images.Media.DATA + "=?";
                    String[] selectionArgs = new String[] { newImageFile.getPath() };

                    Cursor cursor = context.getContentResolver().query(
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                            projection,
                            selection,
                            selectionArgs,
                            null
                    );

                    if (cursor != null && cursor.moveToFirst()) {
                        int idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID);
                        long id = cursor.getLong(idColumn);
                        Uri imageUri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id);


                        // Chèn ảnh vào MediaStore
                        context.getContentResolver().update(imageUri, imageValues, null, null);
                    }
                    if (cursor != null) {
                        cursor.close();
                    }
                } else {
                    Log.e("CreateNewAlbum", "Image file does not exist: " + image.getFilePath());
                }
            }
        }
        DB.deletePhotos(context, images);
    }
    public static void deleteEmptyAlbum(String albumName) {
        File albumDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES +"/" + albumName);
        if (albumDir.exists() && albumDir.isDirectory()) {
            File[] files = albumDir.listFiles();
            if (files != null && files.length == 0) {
                // Thư mục rỗng, xóa nó
                if (albumDir.delete()) {
                    Log.i("DeleteAlbum", "Deleted empty album: " + albumName);
                } else {
                    Log.e("DeleteAlbum", "Failed to delete empty album: " + albumName);
                }
            }
        }
    }
    public static void deleteAllEmptyAlbums() {
        File picturesDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        if (picturesDir.exists() && picturesDir.isDirectory()) {
            File[] albums = picturesDir.listFiles();
            if (albums != null) {
                for (File album : albums) {
                    if (album.isDirectory()) {
                        deleteEmptyAlbum(album.getName());
                    }
                }
            }
        }
    }
    public static Uri getImageUri(Context context, Photo image)
    {
        // Tìm URI của hình ảnh trong MediaStore
        String[] projection = { MediaStore.Images.Media._ID };
        String selection = MediaStore.Images.Media.DATA + "=?";
        String[] selectionArgs = new String[] { image.getFilePath() };

        Cursor cursor = context.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                projection,
                selection,
                selectionArgs,
                null
        );

        Uri imageUri = null;
        if (cursor != null && cursor.moveToFirst()) {
            int idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID);
            long id = cursor.getLong(idColumn);
            imageUri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id);
        } else {
            Log.e("DeleteImage", "Failed to find URI for image: " + image.getFilePath());
        }

        if (cursor != null) {
            cursor.close();
        }

        return imageUri;
    }
    @RequiresApi(api = Build.VERSION_CODES.Q)
    public static void deletePhoto(Context context, Photo image)
    {
        Uri imageUri = getImageUri(context, image);
        if (imageUri != null) {
            try {
                int rowsDeleted = context.getContentResolver().delete(imageUri, null, null);
                if (rowsDeleted > 0) {
                    Log.i("DeleteImage", "Deleted image: " + image.getFilePath());
                } else {
                    Log.e("DeleteImage", "Failed to delete image: " + image.getFilePath());
                }
            } catch (RecoverableSecurityException e) {
                PendingIntent pendingIntent = e.getUserAction().getActionIntent();

                try {
                    ((Activity) context).startIntentSenderForResult(
                            pendingIntent.getIntentSender(),
                            REQUEST_CODE_PERMISSION,
                            null,
                            0,
                            0,
                            0,
                            null
                    );
                } catch (IntentSender.SendIntentException ex) {
                    Log.e("DeleteImage", "Failed to send intent: ", ex);
                }

                // Tạo một Handler để thực hiện lại yêu cầu xóa sau khi cấp quyền
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        deletePhoto(context, image);
                    }
                }, 1000);  // Thời gian chờ là 1000ms (1 giây)
            }
        } else {
            Log.e("DeleteImage", "Failed to find URI for image: " + image.getFilePath());
        }
    }
    public static void deletePhotos(Context context, List<Photo> images)
    {
        List<Uri> urisToDelete = new ArrayList<>();

        for (Photo image: images)
        {
            Uri imageUri = getImageUri(context, image);
            if (imageUri != null) {
                urisToDelete.add(imageUri);
            }
        }

        if (!urisToDelete.isEmpty()) {
            PendingIntent pendingIntent = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
                pendingIntent = MediaStore.createDeleteRequest(context.getContentResolver(), urisToDelete);
            }

            try {
                Activity activity = (Activity)context;
                activity.startIntentSenderForResult(
                        pendingIntent.getIntentSender(),
                        REQUEST_CODE_PERMISSION,
                        null,
                        0,
                        0,
                        0,
                        null
                );
            } catch (IntentSender.SendIntentException e) {
                Log.e("DeleteImage", "Failed to send intent: ", e);
            }
        }
        DB.deleteAllEmptyAlbums();
    }

    public static List<Photo> getListPhoto(Context context)
    {
        List<Photo> photoList = new ArrayList<>();

        int columnIndexData, nameIndex, dateIndex, descriptionIndex;

        String name = null;
        Long dateTaken = null;
        String imagePath = null;
        Long size;
        String description="";

        //Cột của bảng
        String[] projection = {MediaStore.MediaColumns.DATA,
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
                MediaStore.Images.Media.DATE_TAKEN,
                MediaStore.Images.Media.DESCRIPTION,
                MediaStore.Images.Media.DISPLAY_NAME
        };



        String orderBy = MediaStore.Images.Media.DATE_TAKEN;

        Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,projection,
                null,null,orderBy + " DESC");

        columnIndexData = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        nameIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME);
        dateIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_TAKEN);

        descriptionIndex=cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DESCRIPTION);



        Calendar myCal = Calendar.getInstance();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

        if (cursor != null){
            while (cursor.moveToNext()){
                name = cursor.getString(nameIndex);
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
                image.setName(name);

                photoList.add(image);

            }

        }
        cursor.close();
        return photoList;
    }
    public static List<Photo> getListPhotoExcludeAlbum(Context context, String albumName)
    {
        List<Photo> photoList = new ArrayList<>();

        int columnIndexData, dateIndex, descriptionIndex;
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

        //Thêm điều kiện để loại trừ album có tên là 'name'
        String selection = MediaStore.Images.Media.BUCKET_DISPLAY_NAME + " != ?";
        String[] selectionArgs = new String[] { albumName };

        Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,projection,
                selection,selectionArgs,orderBy + " DESC");

        columnIndexData = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        dateIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_TAKEN);
        descriptionIndex=cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DESCRIPTION);

        Calendar myCal = Calendar.getInstance();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

        if (cursor != null){
            while (cursor.moveToNext()){
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

        int columnIndexData, dateIndex, nameIndex;

        String name = null;
        Long dateTaken = null;
        String imagePath = null;
        Long size;

        //Cột của bảng
        String[] projection = {MediaStore.MediaColumns.DATA,
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
                MediaStore.Images.Media.DATE_TAKEN,
                MediaStore.Images.Media.DISPLAY_NAME
        };

        String orderBy = MediaStore.Images.Media.DATE_TAKEN;

        String selection = MediaStore.Images.Media.BUCKET_ID + " = ?";
        String[] selectionArgs = { id };

        Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,projection,
                selection,selectionArgs,orderBy + " DESC");

        columnIndexData = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        nameIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME);
        dateIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_TAKEN);

        Calendar myCal = Calendar.getInstance();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

        if (cursor != null){
            while (cursor.moveToNext()){
                name = cursor.getString(nameIndex);
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
                image.setName(name);

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
