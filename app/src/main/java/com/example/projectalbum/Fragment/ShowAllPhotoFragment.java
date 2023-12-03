package com.example.projectalbum.Fragment;


import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.projectalbum.Activity.MainActivity;
import com.example.projectalbum.Adapter.Category_Adapter;
import com.example.projectalbum.Database.DB;
import com.example.projectalbum.Model.Photo;
import com.example.projectalbum.R;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ShowAllPhotoFragment# newInstance} factory method to
 * create an instance of this fragment.
 */
public class ShowAllPhotoFragment extends Fragment {
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_TAKE_PHOTO = 1;
    private static final int CAMERA_REQUEST = 1888;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    private static final int PICTURE_RESULT = 1;
    MainActivity main;
    Context context = null;
//    Button btn_album;

    LinearLayout  layout_show_all_photo;
    TextView txtSoloMsg;
    ImageButton ibtn_camera;
    ImageView imgSoloPhoto;
    Button btnSoloBack, btnDelete, btnShare;
    Bundle myOriginalMemoryBundle;
    List<Photo>photoList = new ArrayList<>();
    Category_Adapter categoryAdapter;

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
            String value = extras.getString("id");
            // Sử dụng giá trị nhận được
            photoList = DB.getListPhotoOfIdAlbum(context,value);
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


        ibtn_camera = (ImageButton) layout_show_all_photo.findViewById(R.id.ibtn_camera);
        ibtn_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takenImg();
            }
        });


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

//    private void dispatchTakePictureIntent() {
//        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        // Ensure that there's a camera activity to handle the intent
//        if (takePictureIntent.resolveActivity(context.getPackageManager()) != null) {
//            // Create the File where the photo should go
//            File photoFile = null;
//            try {
//                photoFile = createImageFile();
//            } catch (IOException ex) {
//                // Error occurred while creating the File
//            }
//            // Continue only if the File was successfully created
//            if (photoFile != null) {
//                Uri photoURI = FileProvider.getUriForFile(context,
//                        "com.example.android.projectalbum",
//                        photoFile);
//                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
//                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
//            }
//        }
//    }
//
//    String currentPhotoPath;
//
//    private File createImageFile() throws IOException {
//        // Create an image file name
//        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
//        String imageFileName = "JPEG_" + timeStamp + "_";
//        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
//        File image = File.createTempFile(
//                imageFileName,  /* prefix */
//                ".jpg",         /* suffix */
//                storageDir      /* directory */
//        );
//
//        // Save a file: path for use with ACTION_VIEW intents
//        currentPhotoPath = image.getAbsolutePath();
//        return image;
//    }
    private Uri imageUri;
    private void takenImg() {
        int permissionCheckStorage = ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.CAMERA);
        if (permissionCheckStorage != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA},MY_CAMERA_PERMISSION_CODE );
        } else {
            ContentValues values = new ContentValues();
            values.put(MediaStore.Images.Media.TITLE, "New Picture");
            values.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera");
            imageUri = getActivity().getApplicationContext().getContentResolver().insert(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            startActivityForResult(intent, PICTURE_RESULT);
            // TODO Simply append one image to the allImages list. No need to loop through it.
    //        GetAllPhotoFromGallery.updateNewImages();
    //        GetAllPhotoFromGallery.refreshAllImages();
            categoryAdapter.setData(DB.getListCategory(main));
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICTURE_RESULT) {
            if (resultCode == RESULT_OK) {
                // Hình ảnh đã được lưu vào imageUri, cập nhật giao diện người dùng tại đây
                // TODO: Cập nhật dữ liệu
                categoryAdapter.setData(DB.getListCategory(main));
            } else if (resultCode == RESULT_CANCELED) {
                // Người dùng hủy hoạt động chụp ảnh
            } else {
                // Lỗi khác
            }
        }
    }


}