package com.example.projectalbum.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.projectalbum.Interface.AdapterListener;
import com.example.projectalbum.Model.Album;
import com.example.projectalbum.R;


import java.util.ArrayList;
import java.util.List;

public class Album_Adapter extends RecyclerView.Adapter<Album_Adapter.AlbumViewHolder> {
    private List<Album> listAlbum;
    private Context context;
    private AdapterListener listener;
    private boolean isShowCheck;

    public Album_Adapter(Context context) {
        this.context = context;
    }

    public void setData(List<Album> listAlbum)
    {
        this.listAlbum = listAlbum;
//        notifyDataSetChanged();
    }

    //
    public void setListener(AdapterListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public AlbumViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_album_item, parent, false);
        //xủa lý sự kiện click
        final AlbumViewHolder viewHolder = new AlbumViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull AlbumViewHolder holder, int position) {
        Album album = this.listAlbum.get(position);
        if(album == null)
        {
            return;
        }
//    //////sai
//        Picasso.get()
//                .load(album.getImageUrl())
//                .into(holder.imgv_album);
//        holder.imgv_album.setImageResource(album.getImgResource());
        try {
            Glide.with(context).load(album.getImgResource()).into(holder.imgv_album);
        }catch (Exception e)
        {
            //todo
        }
//        holder.imgv_album.setImageURI();
        holder.tv_album.setText(album.getName());
        holder.cv_item.setBackgroundResource(R.drawable.custom_item_album);
        holder.img_check.setBackgroundResource(0);
//
//        if(this.isShowCheck)
//        {
//            if(album.IsSelected)
//            {
//                Toast.makeText(context, "set background resource", Toast.LENGTH_SHORT).show();
//                holder.cv_item.setBackgroundResource(R.drawable.custom_item_album_selected);
//                holder.img_check.setBackgroundResource(R.drawable.drawable_checked);
//            }
//            else {
//                Toast.makeText(context, "set background resource1", Toast.LENGTH_SHORT).show();
//                holder.cv_item.setBackgroundResource(R.drawable.custom_item_album);
//                holder.img_check.setBackgroundResource(R.drawable.drawable_unchecked);
//            }
//        }
//        else{
//            Toast.makeText(context, "set background resource2", Toast.LENGTH_SHORT).show();
//            holder.cv_item.setBackgroundResource(R.drawable.custom_item_album);
//            holder.img_check.setBackgroundResource(0);
//        }


        holder.cv_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isShowCheck) {
                    int position = holder.getAdapterPosition();
                    String message = "Bạn đã click vào item ở vị trí " + listAlbum.get(position).getName();
                    Toast.makeText(v.getContext(), message, Toast.LENGTH_SHORT).show();
                    //xử lý hiện danh sách ảnh của album ở đây
                    if (listener != null) {
                        listener.onItemClick(listAlbum.get(position).getName(),listAlbum.get(position).getId());
                    }
                }
                else{
                    album.IsSelected = !album.IsSelected;
                    holder.cv_item.setBackgroundResource(album.IsSelected ?
                            R.drawable.custom_item_album_selected : R.drawable.custom_item_album);
                    holder.img_check.setBackgroundResource(album.IsSelected ?
                            R.drawable.drawable_checked : 0);
                    if(album.IsSelected)
                    {
                        listener.onItemShowActionSelected(true);
                    }
                    else if(getSelectedAlbumItem().size() == 0)
                    {
                        listener.onItemShowActionSelected(false);
                    }
                }
            }
        });
        holder.cv_item.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                listener.onItemShowActionSelected(true);
                if(!isShowCheck) {
                    album.IsSelected = true;
                    isShowCheck = true;
                    holder.cv_item.setBackgroundResource(R.drawable.custom_item_album_selected);
                    holder.img_check.setBackgroundResource(R.drawable.drawable_checked);
                    Toast.makeText(v.getContext(), "long click", Toast.LENGTH_SHORT).show();
                    return true;
                }
                return false;
            }
        });

    }

    @Override
    public int getItemCount() {
        if(this.listAlbum != null)
        {
            return  this.listAlbum.size();
        }
        return 0;
    }

    public List<Album> getSelectedAlbumItem()
    {
        List<Album> selectedListAlbum = new ArrayList<>();
        for(Album album : this.listAlbum)
        {
            if(album.IsSelected)
            {
                selectedListAlbum.add(album);
            }
        }

        return selectedListAlbum;
    }

    public class AlbumViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgv_album, img_check;
        LinearLayout cv_item;
        private TextView tv_album;

        public AlbumViewHolder(@NonNull View itemView) {
            super(itemView);
            this.imgv_album = (ImageView) itemView.findViewById(R.id.imgv_album);
            this.img_check = (ImageView) itemView.findViewById(R.id.img_check);
            this.tv_album = (TextView) itemView.findViewById(R.id.tv_name_album);
            this.cv_item = (LinearLayout) itemView.findViewById(R.id.cv_item);
        }
    }

    public void setIsShowCheck(boolean isShowCheck){this.isShowCheck = isShowCheck;}
}
