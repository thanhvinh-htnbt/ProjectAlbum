package com.example.projectalbum.Activity;

import static com.example.projectalbum.Database.DB.getListPhoto;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.example.projectalbum.Model.Photo;
import com.example.projectalbum.R;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

public class BigImage extends AppCompatActivity {
    Context context = null;
    TextView txtSoloMsg;
    ImageView imgSoloPhoto;
    Button btnSoloBack, btnDelete, btnShare;
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



        // Truyền vị trí kiểu string qua Intent dưới dạng extra ở intent trước
        //Nếu cần thêm data gì thì thêm vào intent
        //intent.putExtra("imagePath", data);



        // Nhận giá trị kiểu string từ Intent trước
        String imagePath = getIntent().getStringExtra("imagePath");
        // set caption-and-large picture
        txtSoloMsg.setText(" Position= " + 1);
        //truyền ảnh vào
        Glide.with(context).load(imagePath).into(imgSoloPhoto);



        btnSoloBack = (Button) findViewById(R.id.btnSoloBack);
        btnDelete= (Button) findViewById(R.id.btnSoloDelete);
        btnShare = (Button) findViewById(R.id.btn_share_image);
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