package com.example.projectalbum.Activity;

import static com.example.projectalbum.Database.DB.getListPhoto;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.projectalbum.Model.Photo;
import com.example.projectalbum.R;

import java.util.ArrayList;
import java.util.List;

public class SlideShowActivity extends AppCompatActivity {
    Context context = null;
    Button btnBack,btnStop,btnContinue;
    List<Photo> photoList = new ArrayList<>();
    ImageView imgSoloPhoto;
    TextView txtSoloMsg;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context =this;

        //AlertDialog.Builder preSlideShow=new AlertDialog.Builder(SlideShow.this);


        photoList=getListPhoto(context);

        setContentView(R.layout.activity_slideshow);
        txtSoloMsg = (TextView) findViewById(R.id.txtSoloMsg_SlideShow);
        imgSoloPhoto = (ImageView) findViewById(R.id.imgSolo_SlideShow);
        // set caption-and-large picture
        //txtSoloMsg.setText(" Position= "  + " " + photoList.get(1).getName());
        //imgSoloPhoto.setImageResource( photoList.get(1).getLargeImages() );
        // set GO BACK button to return to layout1 (GridView)
        btnBack = (Button) findViewById(R.id.btnBack_SlideShow);
        btnStop=(Button) findViewById(R.id.btnStop_SlideShow);
        btnContinue=(Button) findViewById(R.id.btnContinue_SlideShow);

        final int[] index = {-1};
        final boolean[] running = {true};
        btnContinue.setEnabled(false);


        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                running[0] =!running[0];
                btnContinue.setEnabled(true);
                btnStop.setEnabled(false);

            }
        });
        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                running[0] =!running[0];
                btnContinue.setEnabled(false);
                btnStop.setEnabled(true);

            }
        });

        Handler handler=new Handler();
        Runnable runnable=new Runnable() {
            @Override
            public void run() {
                if(running[0])
                {
                    if(index[0] ==photoList.size()-1)
                    {
                        finish();
                    } else {
                        index[0] = index[0] + 1;
                    }
                    txtSoloMsg.setText(" Position= " + index[0] + " " + photoList.get(index[0]).getName());


                    // Nhận giá trị kiểu string từ Intent trước
                    String imagePath = photoList.get(index[0]).getFilePath();
                    // set caption-and-large picture
                    txtSoloMsg.setText(" Position= " + 1);
                    //truyền ảnh vào
                    Glide.with(context).load(imagePath).into(imgSoloPhoto);


                }
                // Lặp lại sau mỗi 5 giây, đã sửa thành 1 giây để test
                handler.postDelayed(this, 1000);

            }
        };
        // Lặp lại sau mỗi 5 giây
        handler.postDelayed(runnable, 1000);
    }


}
