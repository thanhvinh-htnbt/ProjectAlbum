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
        //context để lấy ảnh
        context=this.context;
        setContentView(R.layout.activity_solo_image);

        photoList=getListPhoto(context);
        txtSoloMsg = (TextView) findViewById(R.id.txtSoloMsg);
        imgSoloPhoto = (ImageView) findViewById(R.id.imgSolo);



        // Truyền vị trí kiểu integer qua Intent dưới dạng extra ở intent trước
        //intent.putExtra("POSITION", data);



        // Nhận giá trị kiểu integer từ Intent trước
        int position = getIntent().getIntExtra("POSITION", 0);

        // set caption-and-large picture
        txtSoloMsg.setText(" Position= " + position + " " + photoList.get(position).getName());
        //truyền ảnh vào
        imgSoloPhoto.setImageResource( photoList.get(position).getLargeImages() );

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
                //BitmapDrawable bitmapDrawable = (BitmapDrawable) imgSoloPhoto.getDrawable();
                //Bitmap bitmap = bitmapDrawable.getBitmap();
                //shareImageAndText(bitmap);
            }

        });
    }

}