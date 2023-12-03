package com.example.projectalbum.Adapter;



import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projectalbum.Activity.BigImageActivity;
import com.example.projectalbum.Model.Category;
import com.example.projectalbum.R;


import java.util.List;

public class Category_Adapter extends RecyclerView.Adapter<Category_Adapter.CategoryViewHolder>{
    private Context context;
    private List<Category> listCategory;

    private int column;


    public Category_Adapter(Context context) {
        this.context = context;
        column = 4;
    }

    public void setData(List<Category> listCategory){
        this.listCategory = listCategory;
        notifyDataSetChanged();
    }

    public void setColumn(int column) {
        this.column = column;
        notifyDataSetChanged();
    }

    public int getColumn() {
        return column;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_category_item, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {

        Category category = listCategory.get(position);
        if (category == null)
            return;
        //Set ngày tháng
        holder.tvNameCategory.setText(category.getNameCategory());

        //Set up layout
        GridLayoutManager gridLayoutManager = new GridLayoutManager(context, column);
        holder.rcvPictures.setLayoutManager(gridLayoutManager);

        Photo_Adapter photoAdapter = new Photo_Adapter(context.getApplicationContext(),category.getListPhoto());
        photoAdapter.setListCategory(listCategory);
        holder.rcvPictures.setAdapter(photoAdapter);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Code ấn vào ảnh ở đây

                //Lấy imagePath của ảnh được click trong category

                String imagePath = category.getListPhoto().get(holder.getAdapterPosition()).getfilePath();
                String imageDate = category.getListPhoto().get(holder.getAdapterPosition()).getDateTaken();
                Long imageSize = category.getListPhoto().get(holder.getAdapterPosition()).getSize();
                String imageDescription=category.getListPhoto().get(holder.getAdapterPosition()).getDescription();


                // Truyền vị trí kiểu string qua Intent dưới dạng extra ở intent trước
                Intent intent = new Intent(context, BigImageActivity.class);

                intent.putExtra("imagePath", imagePath);
                intent.putExtra("imageDate", imageDate);
                intent.putExtra("imageSize", imageSize);
                intent.putExtra("imageDescription", imageDescription);

                context.startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        if (listCategory != null){
            return listCategory.size();
        }
        return 0;
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder{
        private TextView tvNameCategory;
        private RecyclerView rcvPictures;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);

            tvNameCategory = itemView.findViewById(R.id.tvNameCategory);
            rcvPictures = itemView.findViewById(R.id.rcvPictures);
        }
    }
}
