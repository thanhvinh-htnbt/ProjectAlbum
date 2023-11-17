package com.example.projectalbum.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.projectalbum.Fragment.ListAlbumFragment;
import com.example.projectalbum.Fragment.show_all_photo_fragment;
import com.example.projectalbum.Interface.MainActivityListener;
import com.example.projectalbum.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity implements MainActivityListener {


    BottomNavigationView bottomNavigationView;

    Fragment selectedFragment = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                switch (itemId){
                    case R.id.nav_photo:
                        selectedFragment = new show_all_photo_fragment();
                        break;
                    case R.id.nav_album:
//                        Intent intent = new Intent(MainActivity.this, ListAlbumActivity.class);
//                        startActivity(intent);
                        selectedFragment = ListAlbumFragment.newInstance(MainActivity.this);
                        break;
                }

                if(selectedFragment != null){
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            selectedFragment).commit();
                }
                return true;
            }
        });

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new show_all_photo_fragment()).commit();
    }


    @Override
    public void onItemAlbumClick(String name, String id) {
        Toast.makeText(this, "id=" + id.toString(), Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(MainActivity.this, PhotoAlbumActivity.class);
        intent.putExtra("ID", id);
        intent.putExtra("NAME", name);
        startActivity(intent);
    }
}