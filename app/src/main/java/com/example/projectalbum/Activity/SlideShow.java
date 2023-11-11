package com.example.projectalbum.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.projectalbum.Database.DB;
import com.example.projectalbum.Model.Photo;
import com.example.projectalbum.R;

import java.util.ArrayList;
import java.util.List;

public class SlideShow extends AppCompatActivity {
    Button btnBack,btnStop,btnContinue;
    List<Photo> photoList = new ArrayList<>();
    ImageView imgSoloPhoto;
    TextView txtSoloMsg;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            Integer value = extras.getInt("id");
            // Sử dụng giá trị nhận được
            photoList = DB.getListPhoto(this);
        } else {
            photoList = DB.getListPhoto(this);
        }

        setContentView(R.layout.activity_slideshow);
        txtSoloMsg = (TextView) findViewById(R.id.txtSoloMsg);
        imgSoloPhoto = (ImageView) findViewById(R.id.imgSolo);
        // set caption-and-large picture
        //txtSoloMsg.setText(" Position= "  + " " + photoList.get(1).getName());
        //imgSoloPhoto.setImageResource( photoList.get(1).getLargeImages() );
        // set GO BACK button to return to layout1 (GridView)
        btnBack = (Button) findViewById(R.id.btnBack);
        btnStop=(Button) findViewById(R.id.btnStop);
        btnContinue=(Button) findViewById(R.id.btnContinue);
        /*
        try {
            sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        */


        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // redraw the main screen showing the GridView
                //onCreate(myOriginalMemoryBundle);
            }
        });
        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        /*

        for(int i=0;i< photoList.size();i++)
        {
            txtSoloMsg = (TextView) findViewById(R.id.txtSoloMsg);
            imgSoloPhoto = (ImageView) findViewById(R.id.imgSolo);
            // set caption-and-large picture
            txtSoloMsg.setText(" Position= " + i + " " + photoList.get(i).getName());
            imgSoloPhoto.setImageResource( photoList.get(i).getLargeImages() );
            // set GO BACK button to return to layout1 (GridView)
            btnBack = (Button) findViewById(R.id.btnBack);
            btnStop=(Button) findViewById(R.id.btnStop);
            btnContinue=(Button) findViewById(R.id.btnContinue);
            try {
                sleep(5000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            btnBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // redraw the main screen showing the GridView
                    //onCreate(myOriginalMemoryBundle);
                }
            });
            btnStop.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
            btnStop.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
        } */

    }


}
