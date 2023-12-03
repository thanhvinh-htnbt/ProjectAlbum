package com.example.projectalbum.Activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.example.projectalbum.Adapter.Album_Adapter;
import com.example.projectalbum.Database.DB;
import com.example.projectalbum.Interface.AdapterListener;
import com.example.projectalbum.Model.Album;
import com.example.projectalbum.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ListAlbumActivity extends AppCompatActivity  implements AdapterListener, PopupMenu.OnMenuItemClickListener {
    private RecyclerView rcv_list_album;
    private ImageButton imgb_menu;
    private boolean isShowCheck = false;
    Album_Adapter albumAdapter;
    private List<Album> listAlbum;
    private static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 1;

    //    private ImageButton imgb_popup;
//    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_album);
        albumAdapter = new Album_Adapter(this);
        recyclerViewAlbum();

        this.imgb_menu = (ImageButton) findViewById(R.id.popup_menu);
        this.imgb_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopup(view);
            }
        });
    }

    private void recyclerViewAlbum() {
        GridLayoutManager layoutManager = new GridLayoutManager(this, 3);
        this.rcv_list_album = (RecyclerView) findViewById(R.id.rcv_list_album);
        this.rcv_list_album.setLayoutManager(layoutManager);

        albumAdapter.setListener(this);
        listAlbum = loadAlbum();
        albumAdapter.setData(listAlbum);
        this.rcv_list_album.setAdapter(albumAdapter);

    }

    private List<Album> loadAlbum() {
        List<Album> albumList = DB.getAlbums(this);

        return albumList;
    }

    @Override
    public void onItemClick(String name, String id) {
        Toast.makeText(this, "id=" + id.toString(), Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(ListAlbumActivity.this, PhotoAlbumActivity.class);
        intent.putExtra("ID", id);
        intent.putExtra("NAME", name);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        if (isShowCheck) {
            // Thoát khỏi trạng thái dấu tích
            isShowCheck = false;
            for (Album album : listAlbum) {
                album.IsSelected = false;
                this.albumAdapter.setIsShowCheck(false);
            }
            // Cập nhật giao diện
            albumAdapter.notifyDataSetChanged();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onItemShowActionSelected(boolean isSelected) {

        if (isSelected) {
            this.isShowCheck = true;
        } else {
            this.isShowCheck = false;
            this.albumAdapter.setIsShowCheck(false);
        }
    }

    @Override
    public void onExitShowActionSelected() {
        //xử lý sự kiện thoát chọn
        //thoát thanh công cụ
    }

    public void showPopup(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        popup.setOnMenuItemClickListener(this);
        popup.inflate(R.menu.menu_album);
        popup.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.mi_them:
                //todo
                this.showInputCreateNewAlbum();
                return true;
            default:
                return false;
        }
    }

    private void showInputCreateNewAlbum() {
        //yêu cầu quyền ghi
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // Quyền chưa được cấp, yêu cầu quyền.
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
        } else {
            // Quyền đã được cấp. Hiển thị EditText để người dùng nhập tên và tạo album mới.
            showCreateAlbumDialog();
        }
        //
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Quyền đã được cấp. Hiển thị EditText để người dùng nhập tên và tạo album mới.
                    showCreateAlbumDialog();
                } else {
                    // Quyền đã bị từ chối. Hiển thị thông báo không có quyền tạo album mới.
                    Toast.makeText(getApplicationContext(), "Không có quyền tạo album mới", Toast.LENGTH_SHORT).show();
                }
                return;
            }

        }
        ;
    }

    private void showCreateAlbumDialog() {
        // Hiển thị dialog hoặc EditText để người dùng nhập tên album mới.
        // Tạo một AlertDialog.Builder
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // Đặt tiêu đề cho AlertDialog
        builder.setTitle("Nhập tên Album mới");

        // Tạo một EditText
        final EditText input = new EditText(this);

        // Đặt EditText vào AlertDialog
        builder.setView(input);

        // Đặt nút OK và xử lý sự kiện khi nút OK được nhấn
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String name = input.getText().toString();
                createNewAlbum(name);
                // Xử lý tạo album với tên đã nhập ở đây
                Album newAlbum = new Album("1", name, 0, "");
                listAlbum.add(newAlbum);
                albumAdapter.notifyDataSetChanged();
            }
        });

        // Đặt nút Cancel và xử lý sự kiện khi nút Cancel được nhấn
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        // Hiển thị AlertDialog
        builder.show();
    }
    public void createNewAlbum(String albumName) {
        File picturesDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File album = new File(picturesDir, albumName);
        if (!album.exists()) {
            boolean success = album.mkdirs();
            if (success) {
                Toast.makeText(getApplicationContext(), "Album created", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), "Failed to create album", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getApplicationContext(), "Album already exists", Toast.LENGTH_SHORT).show();
        }
    }
}