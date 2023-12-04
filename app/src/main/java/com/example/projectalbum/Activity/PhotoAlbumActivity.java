package com.example.projectalbum.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.projectalbum.Fragment.AllPhotoAlbumFragment;
import com.example.projectalbum.R;

public class PhotoAlbumActivity extends AppCompatActivity {
    private ImageButton imgb_back;
    private TextView tv_name_album;
    private ImageButton popup_menu;
    Fragment show_photo_album_fragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_album);

        Intent intent = getIntent();
        String id = intent.getStringExtra("ID");
        String name = intent.getStringExtra("NAME");

        this.imgb_back = (ImageButton) findViewById(R.id.imgb_back);
        this.tv_name_album = (TextView) findViewById(R.id.tv_name_album);
        this.popup_menu = (ImageButton) findViewById(R.id.popup_menu);

        this.tv_name_album.setText(name);

        this.show_photo_album_fragment = AllPhotoAlbumFragment.newInstance(this,id);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container_photo_album, this.show_photo_album_fragment)
                .commit();
        this.imgb_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

}