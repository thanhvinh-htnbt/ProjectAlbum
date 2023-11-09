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
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.projectalbum.Activity.ListAlbumActivity;
import com.example.projectalbum.Activity.MainActivity;
import com.example.projectalbum.Adapter.MyImageAdapter;
import com.example.projectalbum.Database.DB;
import com.example.projectalbum.Interface.AdapterListener;
import com.example.projectalbum.Model.Photo;
import com.example.projectalbum.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link show_all_photo# newInstance} factory method to
 * create an instance of this fragment.
 */
public class show_all_photo extends Fragment {

    MainActivity main;
    Context context = null;
    Button btn_album;
    List<String> imagesPath;
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

        this.btn_album = (Button) layout_show_all_photo.findViewById(R.id.btn_album);
        this.btn_album.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(main, ListAlbumActivity.class);
                startActivity(intent);
            }
        });
        return layout_show_all_photo;
    }
    private void loadImage(LinearLayout layout) {

        imagesPath = DB.getImgpath(main);

        RecyclerView recyclerView = layout.findViewById(R.id.recyclerViewImg);
        recyclerView.setLayoutManager(new GridLayoutManager(main,3));
        MyImageAdapter adapterClass = new MyImageAdapter(main, imagesPath, new AdapterListener() {
            @Override
            public void onItemClick(Integer data) {
                // Code ấn vào ảnh ở đây
            }
        });
        recyclerView.setAdapter(adapterClass);
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

    private void showBigScreen(int position) {
        // show the selected picture as a single frame in the second layout
        main.setContentView(R.layout.solo_image_layout);
        // plumbing – second layout
        txtSoloMsg = (TextView) main.findViewById(R.id.txtSoloMsg);
        imgSoloPhoto = (ImageView) main.findViewById(R.id.imgSolo);
        // set caption-and-large picture
        txtSoloMsg.setText(" Position= " + position + " " + photoList.get(position).getName());
        //truyền ảnh vào
        imgSoloPhoto.setImageResource( photoList.get(position).getLargeImages() );
        // set GO BACK button to return to layout1 (GridView)
        btnSoloBack = (Button) main.findViewById(R.id.btnSoloBack);
        btnDelete= (Button) main.findViewById(R.id.btnSoloDelete);
        btnShare = (Button) main.findViewById(R.id.btn_share_image);
        btnSoloBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCreate(myOriginalMemoryBundle);
            }
        });
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BitmapDrawable bitmapDrawable = (BitmapDrawable) imgSoloPhoto.getDrawable();
                Bitmap bitmap = bitmapDrawable.getBitmap();
                shareImageAndText(bitmap);
            }

        });
    }
    private void shareImageAndText(Bitmap bitmap) {
        Uri uri = getImageToShare(bitmap);

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        intent.putExtra(Intent.EXTRA_TEXT, "Image Text");
        intent.putExtra(Intent.EXTRA_STREAM, "Image Subject");

        intent.setType("image/*");

        startActivity(Intent.createChooser(intent, "Share via"));
    }

    private Uri getImageToShare(Bitmap bitmap) {
        File folder = new File(main.getCacheDir(),"images");
        Uri uri = null;

        try {
            folder.mkdirs();
            File file = new File(folder,"image.jpg");
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fileOutputStream);

            fileOutputStream.flush();
            fileOutputStream.close();

            uri = FileProvider.getUriForFile(context,"com.example.projectalbum", file);
        } catch (Exception e){
            e.printStackTrace();
        }
        return uri;
    }
}