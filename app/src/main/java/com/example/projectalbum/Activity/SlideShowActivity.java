package com.example.projectalbum.Activity;



import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.GenericTransitionOptions;
import com.bumptech.glide.Glide;
import com.example.projectalbum.Adapter.Image_Select_Adapter;
import com.example.projectalbum.Database.DB;
import com.example.projectalbum.Model.Photo;
import com.example.projectalbum.R;

import java.util.ArrayList;
import java.util.List;

public class SlideShowActivity extends AppCompatActivity {
    Context context = null;
    Button btnBack,btnStop,btnContinue;
    List<Photo> photoList = new ArrayList<>();
    ImageView imgSoloPhoto;
    TextView txtSoloMsg;
    List<Photo> selectedImages;
    final int[] index = {-1};
    final boolean[] running = {true};
    final int[] second = {1};

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context =this;

        showImageSelectionDialog(context);

        photoList=selectedImages;

        setContentView(R.layout.activity_slideshow);
        txtSoloMsg = (TextView) findViewById(R.id.txtSoloMsg_SlideShow);
        imgSoloPhoto = (ImageView) findViewById(R.id.imgSolo_SlideShow);
        // set caption-and-large picture

        // set GO BACK button to return to layout1 (GridView)
        btnBack = (Button) findViewById(R.id.btnBack_SlideShow);
        btnStop=(Button) findViewById(R.id.btnStop_SlideShow);
        btnContinue=(Button) findViewById(R.id.btnContinue_SlideShow);

        btnContinue.setEnabled(false);


        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                running[0] =!running[0];
                SlideShowActivity.this.finish();
            }
        });
        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                running[0] =!running[0];
                btnContinue.setEnabled(true);
                btnStop.setEnabled(false);

            }
        });
        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                running[0] =!running[0];
                btnContinue.setEnabled(false);
                btnStop.setEnabled(true);

            }
        });



    }

    private void showImageSelectionDialog(Context context) {
        // Tạo một AlertDialog.Builder
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // Đặt tiêu đề cho AlertDialog
        builder.setTitle("Chọn hình ảnh");

        List<Photo> lp = DB.getListPhoto(this);

        // Tạo một adapter để hiển thị danh sách hình ảnh
        // Bạn cần thay đổi ImageAdapter để hỗ trợ việc đổi màu viền khi hình ảnh được chọn
        Image_Select_Adapter imageAdapter = new Image_Select_Adapter(context, lp);
        GridView gridView = new GridView(context);
        gridView.setAdapter(imageAdapter);
        gridView.setNumColumns(3);

        builder.setView(gridView);

        // Đặt nút OK và xử lý sự kiện khi nút OK được nhấn
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Xử lý sự kiện khi nút OK được nhấn
                // Bạn có thể lấy danh sách các hình ảnh đã được chọn từ ImageAdapter
                selectedImages = imageAdapter.getSelectedImages();
                //DB.insertAlbum(context,albumName,selectedImages);
                // Xử lý danh sách các hình ảnh đã được chọn ở đây
                photoList=selectedImages;

                AlertDialog.Builder settingDialog= new AlertDialog.Builder(SlideShowActivity.this);
                settingDialog.setTitle("Cài đặt");
                LayoutInflater inflater = getLayoutInflater();
                View dialogLayout = inflater.inflate(R.layout.dialog_sildeshow, null);
                settingDialog.setView(dialogLayout);

                final EditText etInterval = dialogLayout.findViewById(R.id.et_numbersecond);
                final RadioGroup rgEffect = dialogLayout.findViewById(R.id.rg_effect);


                settingDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Get selected interval and effect
                        int interval = Integer.parseInt(etInterval.getText().toString());
                        int effect = rgEffect.getCheckedRadioButtonId();
                        second[0] =interval;

                        Handler handler=new Handler();
                        Runnable runnable=new Runnable() {
                            @Override
                            public void run() {
                                if(running[0])
                                {
                                    if(index[0] ==photoList.size()-1)
                                    {
                                        running[0] =!running[0];
                                        SlideShowActivity.this.finish();
                                    } else {
                                        index[0] = index[0] + 1;
                                    }

                                    String imagePath = photoList.get(index[0]).getFilePath();


                                    GenericTransitionOptions<Object> GenericTransitionOptions;
                                    GenericTransitionOptions=new GenericTransitionOptions<>().transition(R.anim.fade);

                                    if(effect==R.id.rb_effect1)
                                        GenericTransitionOptions=new GenericTransitionOptions<>().transition(R.anim.fade);
                                    else if(effect==R.id.rb_effect2)
                                        GenericTransitionOptions=new GenericTransitionOptions<>().transition(R.anim.rotate);
                                    else if(effect==R.id.rb_effect3)
                                        GenericTransitionOptions=new GenericTransitionOptions<>().transition(R.anim.right);

                                    //truyền ảnh vào
                                    Glide.with(context)
                                            .load(imagePath)
                                            .transition(GenericTransitionOptions)
                                            .into(imgSoloPhoto);

                                }

                                // Lặp lại sau mỗi 5 giây, đã sửa thành n giây để test
                                handler.postDelayed(this, second[0] *1000);

                            }
                        };
                        // Lặp lại sau mỗi n giây
                        handler.postDelayed(runnable, second[0] *1000);

                    }
                });
                settingDialog.create().show();

            }
        });

        // Đặt nút Cancel và xử lý sự kiện khi nút Cancel được nhấn
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                finish();
            }
        });

        // Hiển thị AlertDialog
        builder.show();
    }


}
