package com.example.projectalbum.Fragment;



import android.Manifest;
import android.app.DownloadManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import com.example.projectalbum.Activity.MainActivity;
import com.example.projectalbum.Activity.SeachImageActivity;
import com.example.projectalbum.Activity.SlideShowActivity;
import com.example.projectalbum.Adapter.Category_Adapter;
import com.example.projectalbum.Adapter.Image_Select_Adapter;
import com.example.projectalbum.Adapter.Photo_Adapter;
import com.example.projectalbum.Database.DB;
import com.example.projectalbum.Model.Category;
import com.example.projectalbum.Model.Photo;
import com.example.projectalbum.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;


public class ShowAllPhotoFragment extends Fragment {
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_TAKE_PHOTO = 1;
    private static final int CAMERA_REQUEST = 1888;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    private static final int PICTURE_RESULT = 1;
    MainActivity main;
    Context context = null;
    Toolbar toolbar_allphoto;

    Category_Adapter categoryAdapter;
    Photo_Adapter photoAdapter;

    LinearLayout  layout_show_all_photo;
    List<Photo>photoList = new ArrayList<>();
    List<Category> categoryList;
    int flagLayout, column;
    List<Photo> selectedImages;


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
    public void onResume() {
        super.onResume();
        Bundle extras = main.getIntent().getExtras();

        column = 4;
        flagLayout = 1;
        categoryList = DB.getListCategory(main);

        if (extras != null) {
            String value = extras.getString("id");
            // Sử dụng giá trị nhận được
            photoList = DB.getListPhotoOfIdAlbum(context,value);
        }
        else
        {
            photoList = DB.getListPhoto(context);
        }
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Bundle extras = main.getIntent().getExtras();

        column = 4;
        flagLayout = 1;
        categoryList = DB.getListCategory(main);

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

            loadImageCategoryList(layout_show_all_photo);
        }



        return layout_show_all_photo;
    }
    private void loadImageCategoryList(LinearLayout layout) {
        flagLayout = 1;

        //Chuẩn bị recyclerView
        RecyclerView recyclerView = layout.findViewById(R.id.recyclerViewImg);

        //Set up layout
        categoryAdapter = new Category_Adapter(getContext());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);

        categoryAdapter.setData(categoryList);
        categoryAdapter.setColumn(column);

        recyclerView.setAdapter(categoryAdapter);
    }

    private void loadImagePhotoList(LinearLayout layout) {

        flagLayout = 2;

        RecyclerView recyclerView = layout.findViewById(R.id.recyclerViewImg);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), column);
        recyclerView.setLayoutManager(gridLayoutManager);

        photoAdapter = new Photo_Adapter(getContext(), photoList);
        recyclerView.setAdapter(photoAdapter);
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
                loadImageCategoryList(layout_show_all_photo);
                categoryAdapter.setData(DB.getListCategory(main));
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
                    case R.id.restorePhoto:
                        showRestoreImageSelectionDialog(context);
                        break;

                    case R.id.slideShow:
                        Intent slideshow = new Intent(context, SlideShowActivity.class);
                        startActivity(slideshow);
                        break;
                    case R.id.mn_camera:
                        takenImg();
                        break;

                    case R.id.gridSetting:
                        if(column == 4){
                            menuItem.setIcon(R.drawable.ic_1grid);
                            column = 1;
                        }
                        else if(column == 1){
                            menuItem.setIcon(R.drawable.ic_2grid);
                            column = 2;
                        } else {
                            menuItem.setIcon(R.drawable.ic_4grid);
                            column = 4;
                        }
                        if(flagLayout == 1){categoryAdapter.setColumn(column);}
                        else {loadImagePhotoList(layout_show_all_photo);}
                        break;

                    case R.id.sortByDateAsc:
                        Category.sortByDateAscending(categoryList);
                        loadImageCategoryList(layout_show_all_photo);
                        break;

                    case R.id.sortByDateDec:
                        Category.sortByDateDescending(categoryList);
                        loadImageCategoryList(layout_show_all_photo);
                        break;

                    case R.id.sortByNameAsc:
                        Photo.sortByNameAscending(photoList);
                        loadImagePhotoList(layout_show_all_photo);
                        break;

                    case R.id.sortByNameDec:
                        Photo.sortByNameDescending(photoList);
                        loadImagePhotoList(layout_show_all_photo);
                        break;

                }
                return true;
            }
        });
    }
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
            values.put(MediaStore.Images.Media.BUCKET_DISPLAY_NAME, "Camera");
            imageUri = getActivity().getApplicationContext().getContentResolver().insert(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            startActivityForResult(intent, PICTURE_RESULT);

            categoryAdapter.setData(DB.getListCategory(main));
        }
    }


    private void showRestoreImageSelectionDialog(Context context) {
        // Tạo một AlertDialog.Builder
        AlertDialog.Builder builder = new AlertDialog.Builder(this.context);

        // Đặt tiêu đề cho AlertDialog
        builder.setTitle("Chọn hình ảnh phục hồi");

        // Khai báo thư viện Firebase Storage
        StorageReference mStorageRef;

        // Khởi tạo FirebaseStorage
        mStorageRef = FirebaseStorage.getInstance().getReference().child("images");


        List<Photo> lp = new ArrayList<>();

        // Tạo một danh sách để lưu trữ các đối tượng trong Firebase Storage
        List<StorageReference> storageReferences = new ArrayList<>();

        // Lấy danh sách tên của các đối tượng trong thư mục trên Firebase Storage
        mStorageRef.listAll().addOnSuccessListener(new OnSuccessListener<ListResult>() {
            @Override
            public void onSuccess(ListResult listResult) {
                // Lấy danh sách tất cả các đối tượng
                List<StorageReference> items = listResult.getItems();

                // Lưu trữ danh sách các đối tượng vào storageReferences
                storageReferences.addAll(items);

                // Tạo danh sách tác vụ lấy URL
                List<Task<Uri>> tasks = new ArrayList<>();

                // Lặp qua danh sách các đối tượng để tạo tác vụ lấy URL cho mỗi đối tượng
                for (StorageReference item : items) {
                    Task<Uri> urlTask = item.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String imageUrl = uri.toString();
                            //imageUrls.add(imageUrl);
                            Photo p = new Photo();
                            p.setFilePath(imageUrl);
                            lp.add(p);
                        }
                    });

                    // Thêm tác vụ vào danh sách
                    tasks.add(urlTask);
                }

                // Khi tất cả các tác vụ lấy URL hoàn thành
                Tasks.whenAllSuccess(tasks).addOnSuccessListener(new OnSuccessListener<List<Object>>() {
                    @Override
                    public void onSuccess(List<Object> objects) {
                        // Dữ liệu đã sẵn sàng, cập nhật GridView hoặc thực hiện các thao tác cần thiết
                        Image_Select_Adapter imageAdapter = new Image_Select_Adapter(context, lp);
                        GridView gridView = new GridView(context);
                        gridView.setAdapter(imageAdapter);
                        gridView.setNumColumns(3);

                        builder.setView(gridView);
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // Xử lý sự kiện khi nút OK được nhấn
                                // Bạn có thể lấy danh sách các hình ảnh đã được chọn từ ImageAdapter
                                selectedImages = imageAdapter.getSelectedImages();
                                // Xử lý danh sách các hình ảnh đã được chọn ở đây
                                for(int i=0;i<selectedImages.size();i++)
                                {
                                    DownloadManager.Request request= new DownloadManager.Request(Uri.parse(selectedImages.get(i).getFilePath()));

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
                });
            }
        });

    }

}