package com.example.projectalbum.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.projectalbum.Adapter.MyImageAdapter;
import com.example.projectalbum.Database.DB;
import com.example.projectalbum.Model.Photo;
import com.example.projectalbum.R;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    GridView gridview;
    TextView txtSoloMsg;
    ImageView imgSoloPhoto;
    Button btnSoloBack, btnDelete, btn_album;
    Bundle myOriginalMemoryBundle;
//    String[] items={"Image 1","Image 2","Image 3","Image 4"};
//    Integer[] thumbnails = {R.drawable.lake_1, R.drawable.lake_2, R.drawable.lake_3, R.drawable.mountain_1};
//    Integer[] largeImages = {R.drawable.lake_1, R.drawable.lake_2, R.drawable.lake_3, R.drawable.mountain_1};
    List<Photo>photoList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            Integer value = extras.getInt("id");
            // Sử dụng giá trị nhận được
            photoList = DB.getListPhotoOfIdAlbum(value);
        }
        else
        {
            photoList = DB.getListPhoto();
        }


        setContentView(R.layout.activity_main);
        gridview = (GridView) findViewById(R.id.gv_ListItems);
        gridview.setAdapter(new MyImageAdapter(this, photoList));
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) { showBigScreen(position); }
        });
        this.btn_album = (Button) findViewById(R.id.btn_album);
        this.btn_album.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ListAlbumActivity.class);
                startActivity(intent);
            }
        });
    }
    private void showBigScreen(int position) {
        // show the selected picture as a single frame in the second layout
        setContentView(R.layout.solo_image_layout);
        // plumbing – second layout
        txtSoloMsg = (TextView) findViewById(R.id.txtSoloMsg);
        imgSoloPhoto = (ImageView) findViewById(R.id.imgSolo);
        // set caption-and-large picture
        txtSoloMsg.setText(" Position= " + position + " " + photoList.get(position).getName());
        imgSoloPhoto.setImageResource( photoList.get(position).getLargeImages() );
        // set GO BACK button to return to layout1 (GridView)
        btnSoloBack = (Button) findViewById(R.id.btnSoloBack);
        btnDelete=(Button) findViewById(R.id.btnSoloDelete);
        btnSoloBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // redraw the main screen showing the GridView
                onCreate(myOriginalMemoryBundle);
            }
        });
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
}