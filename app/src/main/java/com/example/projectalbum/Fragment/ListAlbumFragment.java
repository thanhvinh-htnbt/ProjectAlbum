package com.example.projectalbum.Fragment;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.example.projectalbum.Activity.ListAlbumActivity;
import com.example.projectalbum.Activity.MainActivity;
import com.example.projectalbum.Activity.PhotoAlbumActivity;
import com.example.projectalbum.Adapter.Album_Adapter;
import com.example.projectalbum.Database.DB;
import com.example.projectalbum.Interface.AdapterListener;
import com.example.projectalbum.Interface.MainActivityListener;
import com.example.projectalbum.Model.Album;
import com.example.projectalbum.R;

import java.io.File;
import java.util.List;

public class ListAlbumFragment extends Fragment implements AdapterListener, PopupMenu.OnMenuItemClickListener {
    private RecyclerView rcv_list_album;
    private ImageButton imgb_menu;
    private boolean isShowCheck = false;
    Album_Adapter albumAdapter;
    private List<Album> listAlbum;
    private Context context;
    private MainActivityListener listener;
    private static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 1;
    public ListAlbumFragment(Context context)
    {
        this.context = context;
    }
    public void setListener(MainActivityListener listener) {
        this.listener = listener;
    }
    public static ListAlbumFragment newInstance(Context context) {
        ListAlbumFragment fragment = new ListAlbumFragment(context);
//        setListener((MainActivityListener) context);
        fragment.setListener((MainActivityListener) context);
        fragment.context = context;
        return fragment;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_list_album, container, false);

        albumAdapter = new Album_Adapter(this.context);
        recyclerViewAlbum(view);

        this.imgb_menu = (ImageButton) view.findViewById(R.id.popup_menu);
        this.imgb_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopup(view);
            }
        });
        this.onPress();

        return view;
    }
    private void recyclerViewAlbum(View view) {
        GridLayoutManager layoutManager = new GridLayoutManager(this.context, 3);
        this.rcv_list_album = (RecyclerView) view.findViewById(R.id.rcv_list_album);
        this.rcv_list_album.setLayoutManager(layoutManager);

        albumAdapter.setListener(this);
        listAlbum = loadAlbum();
        albumAdapter.setData(listAlbum);
        this.rcv_list_album.setAdapter(albumAdapter);

    }

    private List<Album> loadAlbum() {
        List<Album> albumList = DB.getAlbums(this.context);

        return albumList;
    }


//    @Override
//    public void onBackPressed() {
//        if (isShowCheck) {
//            // Thoát khỏi trạng thái dấu tích
//            isShowCheck = false;
//            for (Album album : listAlbum) {
//                album.IsSelected = false;
//                this.albumAdapter.setIsShowCheck(false);
//            }
//            // Cập nhật giao diện
//            albumAdapter.notifyDataSetChanged();
//        } else {
//            super.onBackPressed();
//        }
//    }
    private void onPress()
    {
        // Thiết lập hành động cho nút Back
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // Thực hiện hành động của bạn tại đây
                if (isShowCheck) {
                    // Thoát khỏi trạng thái dấu tích
                    isShowCheck = false;
                    for (Album album : listAlbum) {
                        album.IsSelected = false;
                        albumAdapter.setIsShowCheck(false);
                    }
                    // Cập nhật giao diện
                    albumAdapter.notifyDataSetChanged();
                } else {
                    setEnabled(false);
                    requireActivity().onBackPressed();
                }
            }
        });
    }

    @Override
    public void onItemClick(String name, String id) {
        this.listener.onItemAlbumClick(name, id);
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
        PopupMenu popup = new PopupMenu(this.context, v);
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
        //yêu ầu quyền ghi
        if (ContextCompat.checkSelfPermission(this.context, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // Quyền chưa được cấp, yêu cầu quyền.
            ActivityCompat.requestPermissions((Activity) this.context,
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
                    Toast.makeText(this.context, "Không có quyền tạo album mới", Toast.LENGTH_SHORT).show();
                }
                return;
            }

        }
        ;
    }

    private void showCreateAlbumDialog() {
        // Hiển thị dialog hoặc EditText để người dùng nhập tên album mới.
        // Tạo một AlertDialog.Builder
        AlertDialog.Builder builder = new AlertDialog.Builder(this.context);

        // Đặt tiêu đề cho AlertDialog
        builder.setTitle("Nhập tên Album mới");

        // Tạo một EditText
        final EditText input = new EditText(this.context);

        // Đặt EditText vào AlertDialog
        builder.setView(input);

        // Đặt nút OK và xử lý sự kiện khi nút OK được nhấn
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String name = input.getText().toString();
                DB.createNewAlbum(context,name);
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

}