package com.example.projectalbum.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.example.projectalbum.Adapter.Album_Adapter;
import com.example.projectalbum.Database.DB;
import com.example.projectalbum.Interface.AdapterListener;
import com.example.projectalbum.Model.Album;
import com.example.projectalbum.R;

import java.util.ArrayList;
import java.util.List;

public class ListAlbumActivity extends AppCompatActivity  implements AdapterListener {
    private RecyclerView rcv_list_album;

//    private ImageButton imgb_popup;
//    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_album);

        recyclerViewAlbum();

//        this.imgb_popup = (ImageButton) findViewById(R.id.popup_menu);
//        this.imgb_popup.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                showPopup(view);
//            }
//        });
    }

    private void recyclerViewAlbum() {
        GridLayoutManager layoutManager = new GridLayoutManager(this, 3);
        this.rcv_list_album = (RecyclerView) findViewById(R.id.rcv_list_album);
        this.rcv_list_album.setLayoutManager(layoutManager);

        Album_Adapter albumAdapter = new Album_Adapter(this);
        albumAdapter.setListener(this);
        albumAdapter.setData(loadAlbum());
        this.rcv_list_album.setAdapter(albumAdapter);

    }

    private List<Album> loadAlbum()
    {
        List<Album> albumList = DB.getListAlbums();

        return albumList;
    }

    @Override
    public void onItemClick(Integer id) {
        Toast.makeText(this, "id="+id.toString(), Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(ListAlbumActivity.this, MainActivity.class);
        intent.putExtra("id", id);
        startActivity(intent);
    }

//    public void showPopup(View v)
//    {
//        PopupMenu popup = new PopupMenu(this, v);
//        popup.setOnMenuItemClickListener(this);
//        popup.inflate(R.menu.popup_menu);
//        popup.show();
//    }

//    @Override
//    public boolean onMenuItemClick(MenuItem menuItem) {
////        switch (menuItem.getItemId())
////        {
////            case R.id.item1:
////                //todo
////                return true;
////            case R.id.item2:
////                //todo
////                return true;
////            default:
////                return false;
////        }
//        return false;
//    }
}