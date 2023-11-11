package com.example.projectalbum.Database;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.projectalbum.Model.Album;
import com.example.projectalbum.Model.Category;
import com.example.projectalbum.Model.Photo;
import com.example.projectalbum.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class DB {
//    @NonNull
//    public static List<Album> getListAlbums(@NonNull Context context) {
//        List<Album> albums = new ArrayList<>();
//
//        if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_MEDIA_IMAGES}, PERMISSION_REQUEST_CODE);
//            // Xử lý việc yêu cầu quyền truy cập trong onRequestPermissionsResult()
//            return albums;
//        }
//        // Tạo URI để truy vấn danh sách album
////        Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
//        Uri uri = MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL);
//
//        // Các cột cần truy vấn
//        String[] projection = {MediaStore.Images.Media._ID, MediaStore.Images.Media.DISPLAY_NAME};
//
//        // Sắp xếp theo tên album (optional)
//        String sortOrder = MediaStore.Images.Media.DISPLAY_NAME + " ASC";
//
//        // Thực hiện truy vấn
//        ContentResolver contentResolver = context.getContentResolver();
//        Cursor cursor = contentResolver.query(uri, projection, null, null, sortOrder);
//
//        if (cursor != null) {
//            while (cursor.moveToNext()) {
//                int idColumnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID);
//                int nameColumnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME);
//
//                // Lấy thông tin album từ cursor
//                int id = cursor.getInt(idColumnIndex);
//                String name = cursor.getString(nameColumnIndex);
//                String imageUrl = getImageUrlFromId(contentResolver, id);
//
//                // Tạo đối tượng Album và thêm vào danh sách
//                albums.add(new Album(id, name, imageUrl));
//            }
//            cursor.close();
//        }
//
//        return albums;
//    }
//
//    @NonNull
//    private static String getImageUrlFromId(ContentResolver contentResolver, int imageId) {
//        Uri uri = Uri.withAppendedPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, String.valueOf(imageId));
//        return uri.toString();
//    }
//
//    // Xử lý kết quả yêu cầu quyền truy cập
//    private static final int PERMISSION_REQUEST_CODE = 1;
////    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        if (requestCode == PERMISSION_REQUEST_CODE) {
//            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                // Quyền đã được cấp, thực hiện lại getListAlbums() hoặc các hoạt động liên quan đến quyền truy cập
//            } else {
//                // Người dùng từ chối cung cấp quyền truy cập, xử lý tương ứng
//            }
//        }
//    }

    public static List<Album> getListAlbums()
    {
        List<Album> albumList = new ArrayList<>();
        albumList.add(new Album(1, getAvatarAlbum(1), "Tất cả"));
        albumList.add(new Album(2, getAvatarAlbum(2), "Camera"));
        albumList.add(new Album(3, getAvatarAlbum(3), "Screen short"));
        albumList.add(new Album(4, getAvatarAlbum(4), "Đã thích"));
        albumList.add(new Album(5, getAvatarAlbum(5), "Riêng tư"));
        return albumList;
    }

    public static List<Photo> getListPhoto(Context context)
    {
        List<Photo> photoList = new ArrayList<>();
//        List<String> filePathList = DB.getImgpath(context);
//
//        for(int i = 0; i < filePathList.size(); i++){
//            photoList.add(new Photo(i, "photo" + i, filePathList.get(i), R.drawable.lake_1));
//        }
////        photoList.add(new Photo(1, "photo1", R.drawable.lake_1, R.drawable.lake_1));
////        photoList.add(new Photo(2, "photo2", R.drawable.lake_2, R.drawable.lake_2));
////        photoList.add(new Photo(3, "photo3", R.drawable.lake_3, R.drawable.lake_3));
        List<String> pathImg = new ArrayList<>();

        int columnIndexData, thumb, dateIndex;

        String thumbnail = null;
        Long dateTaken = null;
        String imagePath = null;

        //Cột của bảng
        String[] projection = {MediaStore.MediaColumns.DATA,
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
                MediaStore.Images.Media.DATE_TAKEN
        };

        String orderBy = MediaStore.Images.Media.DATE_TAKEN;

        Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,projection,
                null,null,orderBy + " DESC");

        columnIndexData = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        //thumb = cursor.getColumnIndexOrThrow(MediaStore.Images.Thumbnails.DATA);
        dateIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_TAKEN);

        Calendar myCal = Calendar.getInstance();
        SimpleDateFormat formatter = new SimpleDateFormat("EEE, dd-MM-yyyy");

        if (cursor != null){
            while (cursor.moveToNext()){
                //thumbnail = cursor.getString(thumb);
                dateTaken = cursor.getLong(dateIndex);
                imagePath = cursor.getString(columnIndexData);

                myCal.setTimeInMillis(dateTaken);
                String dateText = formatter.format(myCal.getTime());
                Log.d("Check", dateText);

                Photo image = new Photo();

                image.setDateTaken(dateText);
                image.setFilePath(imagePath);

                photoList.add(image);

            }

        }
        cursor.close();
        return photoList;
    }
    public static List<Photo> getListPhotoOfIdAlbum(int id, Context context)
    {
        if(id == 1)
        {
            return getListPhoto(context);
        }
        Integer img = R.drawable.lake_1;
        if(id == 2)
        {
            img = R.drawable.lake_2;
        }else if(id ==3)
        {
            img = R.drawable.lake_3;
        }
        List<Photo> photoList = new ArrayList<>();
//        photoList.add(new Photo(1, "photo1", img, img));
//        photoList.add(new Photo(1, "photo1", img, img));
//        photoList.add(new Photo(1, "photo1", img, img));
        return photoList;
    }
    public static Integer getAvatarAlbum(int id)
    {
        if(id == 1)
        {
            return R.drawable.lake_1;
        }
        if(id == 2)
        {
            return R.drawable.lake_2;
        }
        if(id == 3)
        {
            return R.drawable.lake_3;
        }
        if(id == 4)
        {
            return R.drawable.lake_1;
        }
        return R.drawable.custom_item_album;
    }

    public static List<String> getImgpath(Context context){

        List<String> pathImg = new ArrayList<>();

        int columnIndexData, thumb, dateIndex;

        String thumbnail = null;
        Long dateTaken = null;

        //Cột của bảng
        String[] projection = {MediaStore.MediaColumns.DATA,
                                MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
                                MediaStore.Images.Media.DATE_TAKEN
        };

        String orderBy = MediaStore.Images.Media.DATE_TAKEN;

        Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,projection,
                null,null,orderBy + " DESC");

        columnIndexData = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        thumb = cursor.getColumnIndexOrThrow(MediaStore.Images.Thumbnails.DATA);
        dateIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_TAKEN);

        Calendar myCal = Calendar.getInstance();
        SimpleDateFormat formatter = new SimpleDateFormat("EEE, dd-MM-yyyy");

        if (cursor != null){
            while (cursor.moveToNext()){
                thumbnail = cursor.getString(thumb);
                dateTaken = cursor.getLong(dateIndex);

                myCal.setTimeInMillis(dateTaken);
                String dateText = formatter.format(myCal.getTime());
                Log.d("Check", dateText);

                Photo image = new Photo();

                image.setDateTaken(dateText);

                @SuppressLint("Range") String imagePath = cursor.getString
                        (cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                pathImg.add(imagePath);
            }

        }
        return pathImg;
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
    

}
