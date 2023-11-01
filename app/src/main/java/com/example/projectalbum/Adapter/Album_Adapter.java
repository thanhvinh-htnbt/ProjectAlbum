package com.example.projectalbum.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projectalbum.Interface.AdapterListener;
import com.example.projectalbum.Model.Album;
import com.example.projectalbum.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class Album_Adapter extends RecyclerView.Adapter<Album_Adapter.AlbumViewHolder> {
    private List<Album> listAlbum;
    private Context context;
    private AdapterListener listener;

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
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = viewHolder.getAdapterPosition();
                String message = "Bạn đã click vào item ở vị trí " + listAlbum.get(position).getName();
                Toast.makeText(view.getContext(), message, Toast.LENGTH_SHORT).show();
                //xử lý hiện danh sách ảnh của album ở đây
                if (listener != null) {
                    listener.onItemClick(listAlbum.get(position).getId());
                }

            }
        });
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
        holder.imgv_album.setImageResource(album.getImgResource());
//        holder.imgv_album.setImageURI();
        holder.tv_album.setText(album.getName());

    }

    @Override
    public int getItemCount() {
        if(this.listAlbum != null)
        {
            return  this.listAlbum.size();
        }
        return 0;
    }

    public class AlbumViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgv_album;
        private TextView tv_album;
        public AlbumViewHolder(@NonNull View itemView) {
            super(itemView);
            this.imgv_album = (ImageView) itemView.findViewById(R.id.imgv_album);
            this.tv_album = (TextView) itemView.findViewById(R.id.tv_name_album);
        }
    }
}
