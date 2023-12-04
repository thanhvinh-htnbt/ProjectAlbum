package com.example.projectalbum.Activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.example.projectalbum.Adapter.Image_Select_Adapter;
import com.example.projectalbum.Database.DB;
import com.example.projectalbum.Fragment.AllPhotoAlbumFragment;
import com.example.projectalbum.Model.Photo;
import com.example.projectalbum.R;

import java.io.File;
import java.util.List;

public class PhotoAlbumActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {
    private ImageButton imgb_back;
    private TextView tv_name_album;
    private ImageButton popup_menu;
    private String id;
    private String name;
    Fragment show_photo_album_fragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_album);

        Intent intent = getIntent();
        id = intent.getStringExtra("ID");
        name = intent.getStringExtra("NAME");

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

        this.popup_menu = (ImageButton) findViewById(R.id.popup_menu);
        this.popup_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopup(view);
            }
        });
    }
    public void showPopup(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        popup.setOnMenuItemClickListener(this);
        popup.inflate(R.menu.menu_photoalbum);
        popup.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mi_them_anh:
                this.showImageSelectionDialog(this, name);
                return true;
            case R.id.mi_delete_album:
                DB.deletePhotos(this,DB.getListPhotoOfIdAlbum(this,id));
                onBackPressed();
                return true;
            case R.id.mi_rename_album:
                rename();
                return true;
            default:
                return false;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == DB.REQUEST_CODE_PERMISSION) {
            if (resultCode == Activity.RESULT_OK) {
                // Quyền đã được cấp, thực hiện lại yêu cầu xóa
                DB.deletePhotos(this, selectedImages);
                DB.deleteAllEmptyAlbums();
                AllPhotoAlbumFragment.loadImages();
            } else {
                // Người dùng từ chối cấp quyền
                Log.e("DeleteImage", "Permission denied by user");
            }
        }
    }
    List<Photo> selectedImages;
    private void showImageSelectionDialog(Context context, String albumName) {
        // Tạo một AlertDialog.Builder
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // Đặt tiêu đề cho AlertDialog
        builder.setTitle("Chọn hình ảnh");

        List<Photo> lp = DB.getListPhotoExcludeAlbum(context, name);

        // Tạo một adapter để hiển thị danh sách hình ảnh
        // Bạn cần thay đổi ImageAdapter để hỗ trợ việc đổi màu viền khi hình ảnh được chọn
        Image_Select_Adapter imageAdapter = new Image_Select_Adapter(context, lp);
        GridView gridView = new GridView(context);
        gridView.setAdapter(imageAdapter);
        gridView.setNumColumns(3);

        builder.setView(gridView);

        // Đặt nút OK và xử lý sự kiện khi nút OK được nhấn
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Xử lý sự kiện khi nút OK được nhấn
                // Bạn có thể lấy danh sách các hình ảnh đã được chọn từ ImageAdapter
                selectedImages = imageAdapter.getSelectedImages();
                DB.insertAlbum(context,albumName,selectedImages);
                // Xử lý danh sách các hình ảnh đã được chọn ở đây
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

    private void rename()
    {
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
                String nameinput = input.getText().toString();
                // Tạo thư mục mới với tên album
                File kt = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES +"/"+ nameinput);
                if (kt.exists()) {
                    Toast.makeText(PhotoAlbumActivity.this, "Album đã tồn tại", Toast.LENGTH_SHORT).show();
                    return;
                }
                File albumDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), name);
                File newAlbumDir = new File(albumDir.getParent(), nameinput);
                boolean success = albumDir.renameTo(newAlbumDir);
                if (success) {
                    // Thư mục đã được đổi tên thành công
                    Toast.makeText(PhotoAlbumActivity.this, "Đổi tên thành công", Toast.LENGTH_SHORT).show();
                    name = nameinput;
                    tv_name_album.setText(name);
                } else {
                    // Đổi tên thư mục thất bại
                    Toast.makeText(PhotoAlbumActivity.this, "Đổi tên thất bại", Toast.LENGTH_SHORT).show();
                }

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
}