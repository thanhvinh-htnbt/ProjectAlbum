package com.example.projectalbum.Activity;

import static com.example.projectalbum.Database.DB.getListPhoto;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;


import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.RecoverableSecurityException;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import androidx.annotation.NonNull;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.dsphotoeditor.sdk.activity.DsPhotoEditorActivity;
import com.dsphotoeditor.sdk.utils.DsPhotoEditorConstants;
import com.example.projectalbum.Adapter.ImagePager_Adapter;
import com.example.projectalbum.Model.Photo;
import com.example.projectalbum.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class BigImageActivity extends AppCompatActivity {
    Context context = null;
    List<Photo> photoList = new ArrayList<>();

    String imagePath, imageDate, imageName;
    Long imageSize;
    private int currentItem = 0;
    private static final String TAG = "MyApp";
    final String[] imageDescription = {""};

    int width;
    int height;

    BitmapFactory.Options options;
    Bitmap bitmap;
    Toolbar toolbar;
    private static final int WRITE_EXTERNAL_STORAGE_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solo_image);


        toolbar = findViewById(R.id.toolbar);
        Toolbar toolbar = findViewById(R.id.toolbar);

        toolbar.setNavigationIcon(R.drawable.drawable_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onBackPressed(); // This will simulate the back button press
            }
        });


        context = this;

        photoList = getListPhoto(context);

        // Nhận giá trị kiểu string từ Intent trước
        imagePath = getIntent().getStringExtra("imagePath");
        imageDate = getIntent().getStringExtra("imageDate");
        imageSize = getIntent().getLongExtra("imageSize", 0);

        imageDescription[0] = getIntent().getStringExtra("imageDescription");
        imageName = getIntent().getStringExtra("imageName");

        toolbar.setTitle(imageName);


        final String[] imageDescription = {getIntent().getStringExtra("imageDescription")};

        ViewPager2 viewPager = (ViewPager2) findViewById(R.id.viewPager);
        ImagePager_Adapter adapter = new ImagePager_Adapter(context, photoList);
        viewPager.setAdapter(adapter);
        for (int i = 0; i < photoList.size(); i++) {
            if (photoList.get(i).getFilePath().equals(imagePath)) {
                currentItem = i;
                viewPager.setCurrentItem(i, false);
                break;
            }
        }


        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                currentItem = position;
                updateToolbarWithImageInfo(photoList.get(position));
            }
        });

        // Khởi tạo currentItem và cập nhật thanh công cụ với thông tin từ ImageModel đầu tiên
        //currentItem = 0;
        updateToolbarWithImageInfo(photoList.get(currentItem));


        BitmapFactory.Options options = new BitmapFactory.Options();
        Bitmap bitmap = BitmapFactory.decodeFile(imagePath, options);
        width = options.outWidth;
        height = options.outHeight;


        AlertDialog.Builder detailDialog = new AlertDialog.Builder(BigImageActivity.this);
        Bundle des = new Bundle();
        des.putString("description", imageDescription[0]);


        BottomNavigationView navigation_picture = (BottomNavigationView) findViewById(R.id.bottomPictureNavigationView);
        navigation_picture.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.share_pic:
                        shareImageAndText(bitmap);
                        return true;
                    case R.id.edit_pic:
                        Intent editIntent = new Intent(BigImageActivity.this, DsPhotoEditorActivity.class);

                        File file = new File(imagePath);
                        Uri contentUri = getImageContentUri(file);

                        editIntent.setData(contentUri);

                        editIntent.putExtra(DsPhotoEditorConstants.DS_PHOTO_EDITOR_OUTPUT_DIRECTORY, "Nhom 1");

                        editIntent.putExtra(DsPhotoEditorConstants.DS_TOOL_BAR_BACKGROUND_COLOR, Color.parseColor("#00000000"));

                        editIntent.putExtra(DsPhotoEditorConstants.DS_MAIN_BACKGROUND_COLOR, Color.parseColor("#FF000000"));

                        startActivity(editIntent);
                        return true;
                    case R.id.detail_pic:
                        detailDialog.setTitle("Chi tiết");
                        String sizeunit = " bytes";
                        Long size = imageSize;
                        if (size > 1024) {
                            size = size / 1024;
                            sizeunit = " KB";
                        }
                        if (size > 1024) {
                            size = size / 1024;
                            sizeunit = " MB";
                        }
                        detailDialog.setMessage("Dung lượng: " + size + sizeunit + "\n" + "Kích thước: " + width + " x " + height + "\n" + "Ngày tạo: " + imageDate + "\n" + "Đường dẫn: " + imagePath);
                        detailDialog.setPositiveButton("Close", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                        detailDialog.create().show();
                        return true;
                    case R.id.delete_pic:

                        AlertDialog.Builder builder = new AlertDialog.Builder(BigImageActivity.this);
                        Uri targetUri = Uri.parse(imagePath);
                        builder.setTitle("Confirm");
                        builder.setMessage("Do you want to delete this image?");

                        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int which) {

                                if (ContextCompat.checkSelfPermission(context,
                                        Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                                    ActivityCompat.requestPermissions((Activity) context,
                                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_EXTERNAL_STORAGE_REQUEST);
                                } else {
                                    // Nếu đã có quyền, thực hiện xóa ảnh
                                    deleteImage(imagePath);
                                }
                                finish();
                                dialog.dismiss();
                                Intent intent = new Intent(BigImageActivity.this, MainActivity.class);
                                startActivity(intent);
                            }

                        });

                        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                // Do nothing
                                dialog.dismiss();
                            }
                        });

                        AlertDialog alert = builder.create();
                        alert.show();
                        return true;

                    case R.id.description_pic:
                        showDescriptionDialog();
                        return true;
                }
                return false;
            }
        });

    }


    private void updateToolbarWithImageInfo(Photo imageModel) {

        imagePath = imageModel.getFilePath();
        imageDate = imageModel.getDateTaken();
        imageSize = imageModel.getSize();
        imageDescription[0] = imageModel.getDescription();
        options = new BitmapFactory.Options();
        bitmap = BitmapFactory.decodeFile(imagePath, options);
        width = options.outWidth;
        height = options.outHeight;
        imageName = imageModel.getName();
        toolbar.setTitle(imageName);
    }


    public void showDescriptionDialog() {

        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.dialog_description, null);
        Dialog desdialog = new Dialog(this);
        desdialog.setContentView(dialogView);
        desdialog.setCancelable(false);

        TextView textView = (TextView) desdialog.findViewById(R.id.tv_description);
        EditText editText = (EditText) desdialog.findViewById(R.id.et_newdes);
        Button buttonClose = dialogView.findViewById(R.id.btn_close_des);
        Button buttonAdd = dialogView.findViewById(R.id.btn_add_des);
        textView.setText(imageDescription[0]);
        buttonClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Đóng dialog
                imageDescription[0] = textView.getText().toString();
                File file = new File(imagePath);
                Uri imageUri = getImageContentUri(file);
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.DESCRIPTION, textView.getText().toString());
                getContentResolver().update(imageUri, values, null, null);
                desdialog.dismiss();
            }
        });
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newdes = editText.getText().toString();
                editText.setText("");
                String olddes = textView.getText().toString();
                textView.setText(olddes + "\n" + newdes);
            }
        });
        desdialog.show();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == WRITE_EXTERNAL_STORAGE_REQUEST) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Quyền được cấp, thực hiện xóa ảnh
                deleteImage(imagePath);
            } else {
                // Quyền không được cấp, thông báo cho người dùng
                Toast.makeText(this, "Permission denied. Cannot delete image.", Toast.LENGTH_SHORT).show();
            }
        }
    }


    //Xóa file
    private void deleteImage(String imagePath) {
        //Firebase
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReference();

        StorageReference ref = storageReference.child("images/"+ UUID.randomUUID().toString());

        File file = new File(imagePath);
        Uri contentUri = getImageContentUri(file);

        ref.putFile(contentUri);

        if (contentUri != null) {
            // Kiểm tra mức độ API trước khi xóa
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                // Android 11 (API level 30) trở lên
                deleteImageAndroid11AndAbove(contentUri);
            } else {
                // Android 10 (API level 29) và dưới
                deleteImageAndroid10AndBelow(file, contentUri);
            }
        }
    }

    private Uri getImageContentUri(File imageFile) {
        String filePath = imageFile.getAbsolutePath();
        String[] projection = {MediaStore.Images.Media._ID};
        String selection = MediaStore.Images.Media.DATA + "=?";
        String[] selectionArgs = new String[]{filePath};

        Cursor cursor = getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                projection,
                selection,
                selectionArgs,
                null
        );

        if (cursor != null && cursor.moveToFirst()) {
            int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID);
            long mediaId = cursor.getLong(columnIndex);
            cursor.close();
            return Uri.withAppendedPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, String.valueOf(mediaId));
        }

        // Nếu không tìm thấy, trả về null
        return null;
    }

    private void deleteImageAndroid11AndAbove(Uri contentUri) {
        try {
            ContentResolver contentResolver = getContentResolver();
            contentResolver.delete(contentUri, null, null);
        } catch (RecoverableSecurityException e) {
            try {
                IntentSender intentSender = e.getUserAction().getActionIntent().getIntentSender();
                startIntentSenderForResult(intentSender, 1, null, 0, 0, 0, null);
            } catch (IntentSender.SendIntentException ex) {
                ex.printStackTrace();
            }
        }
    }

    private void deleteImageAndroid10AndBelow(File file, Uri contentUri) {
        // Xóa file
        if (file.exists()) {
            if (file.delete()) {
                Toast.makeText(BigImageActivity.this, "Delete successfully: " + file.getPath(), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(BigImageActivity.this, "Delete failed: " + file.getPath(), Toast.LENGTH_SHORT).show();
            }
        }

        // Xóa thông tin trong MediaStore
        getContentResolver().delete(contentUri, null, null);
    }

    //Share dữ liệu
    private void shareImageAndText(Bitmap bitmap) {
        Uri uri = getImageToShare(bitmap);

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        intent.putExtra(Intent.EXTRA_TEXT, "Image Text");

        intent.setType("image/*");

        startActivity(Intent.createChooser(intent, "Share via"));
    }


    //Lấy ảnh để share
    private Uri getImageToShare(Bitmap bitmap) {
        File folder = new File(getCacheDir(), "images");
        Uri uri = null;

        try {
            folder.mkdirs();
            File file = new File(folder, "image.jpg");
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fileOutputStream);

            fileOutputStream.flush();
            fileOutputStream.close();

            uri = FileProvider.getUriForFile(context, "com.example.projectalbum", file);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return uri;
    }


}