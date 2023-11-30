package com.example.projectalbum.Activity;

import static com.example.projectalbum.Database.DB.getListPhoto;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;



import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.example.projectalbum.Fragment.DetailFragment;
import com.example.projectalbum.Model.Photo;
import com.example.projectalbum.R;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

public class BigImage extends AppCompatActivity {
    Context context = null;
    TextView txtSoloMsg, tv_Description;
    EditText et_Description;
    ImageView imgSoloPhoto;
    Button btnSoloBack, btnDelete, btnShare, btnDetail,btnAddDescription,btnEdit;
    List<Photo> photoList = new ArrayList<>();
    Bundle myOriginalMemoryBundle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solo_image);

        //context để lấy ảnh
        context =this;

        photoList=getListPhoto(context);
        txtSoloMsg = (TextView) findViewById(R.id.txtSoloMsg);
        imgSoloPhoto = (ImageView) findViewById(R.id.imgSolo);
        tv_Description=(TextView) findViewById(R.id.tv_Description);
        et_Description=(EditText)findViewById(R.id.et_Description);

        // Nhận giá trị kiểu string từ Intent trước
        String imagePath = getIntent().getStringExtra("imagePath");
        String imageDate = getIntent().getStringExtra("imageDate");
        Long imageSize = getIntent().getLongExtra("imageSize",0);
        final String[] imageDescription = {getIntent().getStringExtra("imageDescription")};

        // set caption-and-large picture
        txtSoloMsg.setText(" Position= " + 1);
        tv_Description.setText(imageDescription[0]);
        //truyền ảnh vào
        Glide.with(context).load(imagePath).into(imgSoloPhoto);



        btnSoloBack = (Button) findViewById(R.id.btnSoloBack);
        btnDelete= (Button) findViewById(R.id.btnSoloDelete);
        btnShare = (Button) findViewById(R.id.btn_share_image);
        btnDetail=(Button)findViewById(R.id.btn_detail);
        btnEdit=(Button)findViewById(R.id.btn_edit);

        btnAddDescription=(Button)findViewById(R.id.btn_Add_Description);

        btnSoloBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //quay trở lại intent trước
                finish();
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
                //Lấy ảnh để share
                BitmapDrawable bitmapDrawable = (BitmapDrawable) imgSoloPhoto.getDrawable();
                Bitmap bitmap = bitmapDrawable.getBitmap();
                shareImageAndText(bitmap);
            }

        });
        btnDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Tạo và hiển thị Fragment
                AlertDialog.Builder detailDialog= new AlertDialog.Builder(BigImage.this);

                detailDialog.setTitle("Chi tiết");
                String sizeunit=" bytes";
                Long size=imageSize;
                if(size>1024)
                {
                    size=size/1024;
                    sizeunit=" KB";
                }
                if(size>1024)
                {
                    size=size/1024;
                    sizeunit=" MB";

                }
                detailDialog.setMessage("Dung lượng: "+size + sizeunit+"\n"+"Ngày tạo: "+imageDate+"\n"+"Đường dẫn: "+imagePath);


                detailDialog.setPositiveButton("Close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                detailDialog.create().show();



            }
        });
        btnAddDescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String add=et_Description.getText().toString();

                if(imageDescription[0] !=null)
                {
                    imageDescription[0] = imageDescription[0] + add;
                }
                else imageDescription[0] = add;



                tv_Description.setText(imageDescription[0]);
                et_Description.setText("");


            }
        });
    }

    //Share dữ liệu
    private void shareImageAndText(Bitmap bitmap) {
        Uri uri = getImageToShare(bitmap);

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        intent.putExtra(Intent.EXTRA_TEXT, "Image Text");
        intent.putExtra(Intent.EXTRA_STREAM, "Image Subject");

        intent.setType("image/*");

        startActivity(Intent.createChooser(intent, "Share via"));
    }


    //Lấy ảnh để share
    private Uri getImageToShare(Bitmap bitmap) {
        File folder = new File(getCacheDir(),"images");
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