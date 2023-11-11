package com.example.projectalbum.Adapter;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.projectalbum.Activity.BigImage;
import com.example.projectalbum.Model.Category;
import com.example.projectalbum.Model.Photo;
import com.example.projectalbum.R;

import java.util.List;

public class Photo_Adapter extends RecyclerView.Adapter<Photo_Adapter.ViewHolder> {

    private Context context;
    private List<Photo> photoList;
    private List<Category> listCategory;

//    public Photo_Adapter(Context context, List<Photo> photoList, AdapterListener adapterListener) {
//        this.context = context;
//        this.photoList = photoList;
//       // this.adapterListener = adapterListener;
//    }
    public Photo_Adapter(Context context, List<Photo> photoList) {
        this.context = context;
        this.photoList = photoList;
       // this.adapterListener = adapterListener;
    }


    public void setListCategory(List<Category> listCategory) {
        this.listCategory = listCategory;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.custom_image_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String imagePath = photoList.get(position).getfilePath();

        //truyền ảnh vào ImageView
        Glide.with(context).load(imagePath).into(holder.imageView);

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(context !=null) {
                    Intent intent = new Intent(context, BigImage.class);
                    intent.putExtra("imagePath", imagePath);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return photoList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        ImageView imageView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.imageView);
        }
    }

}
