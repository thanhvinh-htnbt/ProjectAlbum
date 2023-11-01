package com.example.projectalbum.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.projectalbum.R;

public class TestActivity extends AppCompatActivity {
    Button btn_main, btn_testAlbum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        //
        btn_main = (Button) findViewById(R.id.btn_main);
        btn_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TestActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        //
        btn_testAlbum = (Button) findViewById(R.id.btn_testAlbum);
        btn_testAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TestActivity.this, ListAlbumActivity.class);
                startActivity(intent);
            }
        });

    }
}