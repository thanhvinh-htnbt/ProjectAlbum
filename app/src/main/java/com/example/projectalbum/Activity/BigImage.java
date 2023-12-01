package com.example.projectalbum.Activity;

import static com.example.projectalbum.Database.DB.getListPhoto;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;



import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.example.projectalbum.Fragment.DescriptionFragment;
import com.example.projectalbum.Model.Photo;
import com.example.projectalbum.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class BigImage extends AppCompatActivity {
    Context context = null;
    TextView txtSoloMsg;
    EditText et_Description;
    ImageView imgSoloPhoto;

    DescriptionFragment descriptionFragment;

    List<Photo> photoList = new ArrayList<>();
    Bundle myOriginalMemoryBundle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solo_image);

        //context để lấy ảnh
        context =this;

        photoList=getListPhoto(context);
        txtSoloMsg = (TextView) findViewById(R.id.txtSoloMsg);
        imgSoloPhoto = (ImageView) findViewById(R.id.imgSolo);


        // Nhận giá trị kiểu string từ Intent trước
        String imagePath = getIntent().getStringExtra("imagePath");
        String imageDate = getIntent().getStringExtra("imageDate");
        Long imageSize = getIntent().getLongExtra("imageSize",0);
        final String[] imageDescription = {getIntent().getStringExtra("imageDescription")};


        //truyền ảnh vào
        Glide.with(context).load(imagePath).into(imgSoloPhoto);


        AlertDialog.Builder detailDialog= new AlertDialog.Builder(BigImage.this);

        Bundle des=new Bundle();
        des.putString("description",imageDescription[0]);


        BottomNavigationView navigation_picture=(BottomNavigationView)findViewById(R.id.bottomPictureNavigationView);
        navigation_picture.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.share_pic:
                        //Lấy ảnh để share
                        BitmapDrawable bitmapDrawable = (BitmapDrawable) imgSoloPhoto.getDrawable();
                        Bitmap bitmap = bitmapDrawable.getBitmap();
                        shareImageAndText(bitmap);
                        return true;
                    case R.id.detail_pic:_pic:

                        detailDialog.setTitle("Chi tiết");
                        String sizeunit=" bytes";
                        Long size=imageSize;
                        if(size>1024)
                        {
                            size=size/1024;
                            sizeunit=" KB";
                        }
                        if(size>1024)
                        {
                            size=size/1024;
                            sizeunit=" MB";
                        }
                        detailDialog.setMessage("Dung lượng: "+size + sizeunit+"\n"+"Ngày tạo: "+imageDate+"\n"+"Đường dẫn: "+imagePath);
                        detailDialog.setPositiveButton("Close", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                        detailDialog.create().show();
                        return true;
                    case R.id.delete_pic:
                        return true;

                    case R.id.edit_pic:
                        return true;

                    case R.id.description_pic:_pic:

                        descriptionFragment=new DescriptionFragment();
                        descriptionFragment.setArguments(des);
                        descriptionFragment.show(getSupportFragmentManager(), "dialog");

                        return true;

            }
                return false;
        }
        });

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
}