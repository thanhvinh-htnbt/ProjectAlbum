package com.example.projectalbum.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.projectalbum.Interface.AdapterListener;
import com.example.projectalbum.Model.Photo;
import com.example.projectalbum.R;

import java.util.ArrayList;
import java.util.List;

public class MyImageAdapter extends RecyclerView.Adapter<MyImageAdapter.ViewHolder> {

    private Context context;
    private List<String> imgpaths;

    protected AdapterListener adapterListener;

    public MyImageAdapter(Context context, List<String> imgpaths, AdapterListener adapterListener) {
        this.context = context;
        this.imgpaths = imgpaths;
        this.adapterListener = adapterListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.image_show,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String imagePath = imgpaths.get(position);
        //truyền ảnh vào ImageView
        Glide.with(context).load(imagePath).into(holder.imageView);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adapterListener.onItemClick(position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return imgpaths.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        ImageView imageView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.imageView);
        }
    }

}
