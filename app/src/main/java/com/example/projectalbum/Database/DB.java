package com.example.projectalbum.Database;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.projectalbum.Model.Album;
import com.example.projectalbum.Model.Photo;
import com.example.projectalbum.R;

import java.util.ArrayList;
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

    public static List<Photo> getListPhoto()
    {
        List<Photo> photoList = new ArrayList<>();
        photoList.add(new Photo(1, "photo1", R.drawable.lake_1, R.drawable.lake_1));
        photoList.add(new Photo(2, "photo2", R.drawable.lake_2, R.drawable.lake_2));
        photoList.add(new Photo(3, "photo3", R.drawable.lake_3, R.drawable.lake_3));
        return photoList;
    }
    public static List<Photo> getListPhotoOfIdAlbum(int id)
    {
        if(id == 1)
        {
            return getListPhoto();
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
        photoList.add(new Photo(1, "photo1", img, img));
        photoList.add(new Photo(1, "photo1", img, img));
        photoList.add(new Photo(1, "photo1", img, img));
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
}
