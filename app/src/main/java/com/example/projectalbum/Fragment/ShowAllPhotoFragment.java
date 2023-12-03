package com.example.projectalbum.Fragment;


import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import com.example.projectalbum.Activity.MainActivity;
import com.example.projectalbum.Activity.SeachImageActivity;
import com.example.projectalbum.Adapter.Category_Adapter;
import com.example.projectalbum.Database.DB;
import com.example.projectalbum.Model.Photo;
import com.example.projectalbum.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ShowAllPhotoFragment# newInstance} factory method to
 * create an instance of this fragment.
 */
public class ShowAllPhotoFragment extends Fragment {

    MainActivity main;
    Context context = null;
    Toolbar toolbar_allphoto;

    Category_Adapter categoryAdapter;

    LinearLayout  layout_show_all_photo;
    List<Photo>photoList = new ArrayList<>();

//    public static show_all_photo_fragment newInstance(String id)
//    {
//        Toast.makeText(this.getActivity(), "debug", Toast.LENGTH_SHORT).show();
//        show_all_photo_fragment fragment = new show_all_photo_fragment();
//        Bundle args = new Bundle();
//        args.putString("id", id);
//        fragment.setArguments(args);
//        return fragment;
//    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            context = getActivity();
            main = (MainActivity) getActivity();
        }
        catch (IllegalStateException e) {
            throw new IllegalStateException("MainActivity must implement callbacks");
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Bundle extras = main.getIntent().getExtras();

        if (extras != null) {
            String value = extras.getString("id");
            // Sử dụng giá trị nhận được
            photoList = DB.getListPhotoOfIdAlbum(context,value);
        }
        else
        {
            photoList = DB.getListPhoto(context);
        }

        layout_show_all_photo = (LinearLayout) inflater.inflate(R.layout.fragment_show_all_photo, container, false);

        SetupToolBar();

        //Kiểm tra quyền truy cập bộ nhớ
        if(ContextCompat.checkSelfPermission(main,
                android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(main,
                    new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},101);

        }
        else
        {

            loadImage(layout_show_all_photo);
        }


        return layout_show_all_photo;
    }
    private void loadImage(LinearLayout layout) {

        //Chuẩn bị recyclerView
        RecyclerView recyclerView = layout.findViewById(R.id.recyclerViewImg);

        //Set up layout
        categoryAdapter = new Category_Adapter(getContext());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);

        categoryAdapter.setData(DB.getListCategory(main));
        recyclerView.setAdapter(categoryAdapter);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode,permissions, grantResults);

        if(requestCode == 101)
        {
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(context,"Read external permission granted", Toast.LENGTH_SHORT).show();
                loadImage(layout_show_all_photo);
            }
            else
            {
                Toast.makeText(context,"Read external permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

        private void SetupToolBar() {
        toolbar_allphoto = layout_show_all_photo.findViewById(R.id.toolbar_allPhoto);
        toolbar_allphoto.inflateMenu(R.menu.top_photo_menu);
        toolbar_allphoto.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                int id = menuItem.getItemId();
                switch (id){
                    case R.id.searchPhoto:
                        Intent intent = new Intent(context, SeachImageActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.gridSetting:
                        if(categoryAdapter.getColumn() == 4){
                            menuItem.setIcon(R.drawable.ic_1grid);
                            categoryAdapter.setColumn(1);
                        }
                        else if(categoryAdapter.getColumn() == 1){
                            menuItem.setIcon(R.drawable.ic_2grid);
                            categoryAdapter.setColumn(2);
                        } else {
                            menuItem.setIcon(R.drawable.ic_4grid);
                            categoryAdapter.setColumn(4);
                        }
                        break;

                }
                return true;
            }
        });
    }

}