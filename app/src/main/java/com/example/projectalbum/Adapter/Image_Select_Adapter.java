package com.example.projectalbum.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.projectalbum.Model.Photo;

import java.util.ArrayList;
import java.util.List;

public class Image_Select_Adapter extends BaseAdapter {
    private Context context;
    private List<Photo> photos;

    private List<Boolean> selected;  // Danh sách để lưu trạng thái chọn của mỗi hình ảnh


    public Image_Select_Adapter(Context context, List<Photo> photos) {
        this.context = context;
        this.photos = photos;
        this.selected = new ArrayList<>(photos.size());
        for (int i = 0; i < photos.size(); i++) {
            selected.add(false);
        }
    }

    @Override
    public int getCount() {
        return photos.size();
    }

    @Override
    public Object getItem(int position) {
        return photos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            imageView = new ImageView(context);
            imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 200));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(8, 8, 8, 8);
        } else {
            imageView = (ImageView) convertView;
        }

        // Load image from file path
        String filePath = photos.get(position).getfilePath();
        //truyền ảnh vào ImageView
        Glide.with(context).load(filePath).into(imageView);
        // You need to replace this with your own image loading logic
        // For example, you can use libraries like Picasso or Glide to load image from file path
        // Picasso.get().load(new File(filePath)).into(imageView);

        // Set border color based on selection state
        if (selected.get(position)) {
            imageView.setBackgroundColor(Color.BLUE);  // Change this to the color you want for selected images
        } else {
            imageView.setBackgroundColor(Color.TRANSPARENT);  // Change this to the default color
        }

        // Add click listener
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toggle selection state
                selected.set(position, !selected.get(position));

                // Update border color
                if (selected.get(position)) {
                    imageView.setBackgroundColor(Color.BLUE);  // Change this to the color you want for selected images
                } else {
                    imageView.setBackgroundColor(Color.TRANSPARENT);  // Change this to the default color
                }
            }
        });

        return imageView;
    }

    // Phương thức để lấy danh sách các hình ảnh đã được chọn
    public List<Photo> getSelectedImages() {
        List<Photo> selectedImages = new ArrayList<>();
        for (int i = 0; i < photos.size(); i++) {
            if (selected.get(i)) {
                selectedImages.add(photos.get(i));
            }
        }
        return selectedImages;
    }
}
