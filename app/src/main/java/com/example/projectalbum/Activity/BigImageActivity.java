package com.example.projectalbum.Activity;

import static com.example.projectalbum.Database.DB.getListPhoto;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;


import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.app.RecoverableSecurityException;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.example.projectalbum.Database.DB;
import com.example.projectalbum.Model.Photo;
import com.example.projectalbum.R;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

public class BigImageActivity extends AppCompatActivity {
    Context context = null;
    TextView txtSoloMsg, tv_Description;
    EditText et_Description;
    ImageView imgSoloPhoto;
    Button btnSoloBack, btnDelete, btnShare, btnDetail,btnAddDescription;
    List<Photo> photoList = new ArrayList<>();
    Bundle myOriginalMemoryBundle;

    String imagePath;
    String imageDate;
    Long imageSize;
    private static final int WRITE_EXTERNAL_STORAGE_REQUEST = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solo_image);

        //context để lấy ảnh
        context =this;

        photoList=getListPhoto(context);
        txtSoloMsg = (TextView) findViewById(R.id.txtSoloMsg);
        imgSoloPhoto = (ImageView) findViewById(R.id.imgSolo);
        tv_Description=(TextView) findViewById(R.id.tv_Description);
        et_Description=(EditText)findViewById(R.id.et_Description);

        // Nhận giá trị kiểu string từ Intent trước
        imagePath = getIntent().getStringExtra("imagePath");
        imageDate = getIntent().getStringExtra("imageDate");
        imageSize = getIntent().getLongExtra("imageSize",0);
        final String[] imageDescription = {getIntent().getStringExtra("imageDescription")};

        // set caption-and-large picture
        txtSoloMsg.setText(" Position= " + 1);
        tv_Description.setText(imageDescription[0]);
        //truyền ảnh vào
        Glide.with(context).load(imagePath).into(imgSoloPhoto);



        btnSoloBack = (Button) findViewById(R.id.btnSoloBack);
        btnDelete= (Button) findViewById(R.id.btnSoloDelete);
        btnShare = (Button) findViewById(R.id.btn_share_image);
        btnDetail=(Button)findViewById(R.id.btn_detail);
        btnAddDescription=(Button)findViewById(R.id.btn_Add_Description);

        btnSoloBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //quay trở lại intent trước
                finish();
            }
        });
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
            }
        });

        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Lấy ảnh để share
                BitmapDrawable bitmapDrawable = (BitmapDrawable) imgSoloPhoto.getDrawable();
                Bitmap bitmap = bitmapDrawable.getBitmap();
                shareImageAndText(bitmap);
            }

        });
        btnDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Tạo và hiển thị Fragment
                //DetailFragment fragment = new DetailFragment();
                AlertDialog.Builder detailDialog= new AlertDialog.Builder(BigImageActivity.this);

                detailDialog.setTitle("Chi tiết");
                detailDialog.setMessage("Dung lượng: "+imageSize);
                detailDialog.setMessage("Ngày tạo: "+imageDate);
                detailDialog.setMessage("Đường dẫn: "+imagePath);

                detailDialog.setPositiveButton("Close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                detailDialog.create().show();

                /*

                Bundle data=new Bundle();
                data.putLong("imageSize",imageSize);
                data.putString("imageDate",imageDate);
                data.putString("imagePath",imagePath);
                fragment.setArguments(data);

                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.add(R.id.fragmentDetail, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

                 */

            }
        });
        btnAddDescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String add=et_Description.getText().toString();

                if(imageDescription[0] !=null)
                {
                    imageDescription[0] = imageDescription[0] + add;
                }
                else imageDescription[0] = add;



                tv_Description.setText(imageDescription[0]);
                et_Description.setText("");
                /*
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.DESCRIPTION,imageDescription[0]);
                Uri imageUri = Uri.parse(imagePath);
                int rowsUpdated = getContentResolver().update(imageUri, values, null, null);

                 */

            }
        });



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
        File file = new File(imagePath);
        Uri contentUri = getImageContentUri(file);

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
        intent.putExtra(Intent.EXTRA_STREAM, "Image Subject");

        intent.setType("image/*");

        startActivity(Intent.createChooser(intent, "Share via"));
    }


    //Lấy ảnh để share
    private Uri getImageToShare(Bitmap bitmap) {
        File folder = new File(getCacheDir(),"images");
        Uri uri = null;

        try {
            folder.mkdirs();
            File file = new File(folder,"image.jpg");
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fileOutputStream);

            fileOutputStream.flush();
            fileOutputStream.close();

            uri = FileProvider.getUriForFile(context,"com.example.projectalbum", file);
        } catch (Exception e){
            e.printStackTrace();
        }
        return uri;
    }

    public void deleteMediaFile(Uri uri){
        try {
            getContentResolver().delete(uri, null,null);
        } catch (SecurityException e){
            PendingIntent pendingIntent = null;
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
                ArrayList<Uri> uris = new ArrayList<>();
                uris.add(uri);
                pendingIntent = MediaStore.createDeleteRequest(getContentResolver(),uris);
            } else {
                if (e instanceof RecoverableSecurityException){
                    RecoverableSecurityException exception = (RecoverableSecurityException) e;
                    pendingIntent = exception.getUserAction().getActionIntent();
                }
            }

            if(pendingIntent != null){
                IntentSender intentSender = pendingIntent.getIntentSender();
                try {
                    startIntentSenderForResult(intentSender,100,null,0,0,0);
                } catch (IntentSender.SendIntentException ex) {
                    ex.printStackTrace();
                }
            }
        }


    }


}