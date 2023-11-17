package com.example.projectalbum.Fragment;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.projectalbum.Adapter.Category_Adapter;
import com.example.projectalbum.Database.DB;
import com.example.projectalbum.Model.Photo;
import com.example.projectalbum.R;

import java.util.ArrayList;
import java.util.List;

public class all_photo_album_fragment extends Fragment {
    List<Photo> photoList = new ArrayList<>();
    private static final String ID = "id";
    private String id = "";
    private Context context;
    LinearLayout  layout_show_all_photo;

    public all_photo_album_fragment() {
        // Required empty public constructor
    }

    public static all_photo_album_fragment newInstance(Context context, String id) {
        all_photo_album_fragment fragment = new all_photo_album_fragment();
        fragment.context = context;
        Bundle args = new Bundle();
        args.putString(ID, id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            id = getArguments().getString(ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        layout_show_all_photo = (LinearLayout) inflater.inflate(R.layout.fragment_show_all_photo, container, false);
        //Kiểm tra quyền truy cập bộ nhớ
        if(ContextCompat.checkSelfPermission(context,
                android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions((Activity) context,
                    new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},101);
        }
        else
        {
            loadImage(layout_show_all_photo);
        }


        // Inflate the layout for this fragment
        return layout_show_all_photo;
    }

    private void loadImage(LinearLayout layout) {

        //Chuẩn bị recyclerView
        RecyclerView recyclerView = layout.findViewById(R.id.recyclerViewImg);

        //Set up layout
        Category_Adapter categoryAdapter = new Category_Adapter(getContext());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);

        categoryAdapter.setData(DB.getListCategory(context, id));
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