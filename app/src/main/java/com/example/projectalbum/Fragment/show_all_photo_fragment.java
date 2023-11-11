package com.example.projectalbum.Fragment;


import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.projectalbum.Activity.MainActivity;
import com.example.projectalbum.Adapter.Category_Adapter;
import com.example.projectalbum.Adapter.Photo_Adapter;
import com.example.projectalbum.Database.DB;
import com.example.projectalbum.Model.Photo;
import com.example.projectalbum.R;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link show_all_photo_fragment# newInstance} factory method to
 * create an instance of this fragment.
 */
public class show_all_photo_fragment extends Fragment {

    MainActivity main;
    Context context = null;
//    Button btn_album;

    LinearLayout  layout_show_all_photo;
    TextView txtSoloMsg;
    ImageView imgSoloPhoto;
    Button btnSoloBack, btnDelete, btnShare;
    Bundle myOriginalMemoryBundle;
    List<Photo>photoList = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }

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
            Integer value = extras.getInt("id");
            // Sử dụng giá trị nhận được
            photoList = DB.getListPhotoOfIdAlbum(value, context);
        }
        else
        {
            photoList = DB.getListPhoto(context);
        }

        layout_show_all_photo = (LinearLayout) inflater.inflate(R.layout.fragment_show_all_photo, container, false);

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
        Category_Adapter categoryAdapter = new Category_Adapter(getContext());
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



}