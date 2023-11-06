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
//    private Context context; // main activity’s context
//    List<Photo> photoList = new ArrayList<>();
//    public MyImageAdapter(Context mainActivityContext, List<Photo> photoList) { context = mainActivityContext; this.photoList = photoList; }
//    // how many entries are there in the data set?
//    public int getCount() { return photoList.size(); }
//    // what is in a given 'position' in the data set?
//    public Object getItem(int position) { return photoList.get(position); }
//    // what is the ID of data item in given 'position‘?
//    public long getItemId(int position) { return position; }
//    // create a view for each thumbnail in the data set, add it to gridview
//    public View getView(int position, View convertView, ViewGroup parent) {
//        ImageView imageView;
//// if possible, reuse (convertView) image already held in cache
//        if (convertView == null) {
//// no previous version of thumbnail held in the scrapview holder define entry in res/values/dimens.xml for grid height,width in dips <dimen name="gridview_size">100dp</dimen>
//// setLayoutParams will do conversion to physical pixels
//            imageView = new ImageView(context);
//            int gridSize = context.getResources().getDimensionPixelOffset(R.dimen.gridview_size);
//            imageView.setLayoutParams(new GridView.LayoutParams(gridSize, gridSize)); imageView.setScaleType(ImageView.ScaleType.FIT_XY); imageView.setPadding(5, 5, 5, 5);
//        }
//        else { imageView = (ImageView) convertView; }
//        imageView.setImageResource(photoList.get(position).getThumbnails()); imageView.setId(position);
//        return imageView;
//    }//getView
}
