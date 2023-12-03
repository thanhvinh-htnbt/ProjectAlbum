package com.example.projectalbum.Adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.example.projectalbum.Model.Photo;
import com.example.projectalbum.R;

import java.util.List;

public class ImagePager_Adapter extends PagerAdapter {
    private Context context;
    private List<Photo> photoList;

    private int selectedIndex = 0;
    String imagePathx;

    private static final String TAG = "MyApp";
    public ImagePager_Adapter(Context context, List<Photo> photoList, String imagePath) {
        this.context = context;
        this.photoList = photoList;
        this.imagePathx=imagePath;
        for (int i = 0; i < photoList.size(); i++) {
            if (photoList.get(i).getFilePath().equals(imagePath)) {
                selectedIndex = i;
                break;
            }
        }
    }

    public void setSelectedIndex(int index) {
        selectedIndex = index;
    }



    @Override
    public int getCount() {
        return photoList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }



    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        Photo imageModel = photoList.get(position);
        // Cập nhật các view của bạn với thông tin từ ImageModel
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_image, container, false);
        ImageView imageView = view.findViewById(R.id.imageView);


        String imagePath = photoList.get(selectedIndex+position).getFilePath();


        Glide.with(context).load(imagePath).into(imageView);



        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}


