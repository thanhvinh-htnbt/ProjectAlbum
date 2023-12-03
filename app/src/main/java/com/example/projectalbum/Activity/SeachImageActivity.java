package com.example.projectalbum.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.projectalbum.Adapter.Category_Adapter;
import com.example.projectalbum.Adapter.Photo_Adapter;
import com.example.projectalbum.Database.DB;
import com.example.projectalbum.Model.Category;
import com.example.projectalbum.Model.Photo;
import com.example.projectalbum.R;

import java.util.ArrayList;
import java.util.List;

public class SeachImageActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List<Photo> photoList;
    private  List<Category> categoryList;
    Photo_Adapter photoAdapter;

    Category_Adapter categoryAdapter;
    Toolbar toolbar_searchImage;

    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seach_image);

        photoList = DB.getListPhoto(this);
        categoryList = DB.getListCategory(this);

        searchView = findViewById(R.id.searchView);
        searchView.clearFocus();

        toolbar_searchImage = findViewById(R.id.toolbar_searchImage);
        toolbar_searchImage.inflateMenu(R.menu.search_image_menu);
        toolbar_searchImage.setTitle("Tìm kiếm ảnh");
        toolbar_searchImage.setNavigationIcon(R.drawable.drawable_back);
        toolbar_searchImage.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        toolbar_searchImage.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                int id = menuItem.getItemId();
                switch (id){
                    case R.id.search_byDate:
                        searchByDate();
                        break;
                    case R.id.search_byName:
                        searchByName();
                        break;
                }

                return true;
            }
        });

        searchByName();


    }

    private void searchByName() {
        recyclerView = findViewById(R.id.rv_searchResult);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 4);
        recyclerView.setLayoutManager(gridLayoutManager);

        photoAdapter = new Photo_Adapter(this, photoList);
        recyclerView.setAdapter(photoAdapter);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterListByName(newText);
                return true;
            }
        });
    }

    private void searchByDate() {
        categoryAdapter = new Category_Adapter(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);

        categoryAdapter.setData(categoryList);
        recyclerView.setAdapter(categoryAdapter);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterListByDate(newText);
                return true;
            }
        });

    }

    private void filterListByDate(String newText) {
        List<Category> filteredListCategory = new ArrayList<>();
        for(Category category : categoryList){
            if(category.getNameCategory().toLowerCase().contains(newText.toLowerCase())){
                filteredListCategory.add(category);
            }
        }

        if(filteredListCategory.isEmpty()){
            Toast.makeText(this,"Empty Result",Toast.LENGTH_SHORT).show();
        } else{
            categoryAdapter.setData(filteredListCategory);
        }
    }

    private void filterListByName(String newText) {
        List<Photo> filteredListPhoto = new ArrayList<>();
        for(Photo photo : photoList){
            if(photo.getName().toLowerCase().contains(newText.toLowerCase())){
                filteredListPhoto.add(photo);
            }
        }

        if(filteredListPhoto.isEmpty()){
            Toast.makeText(this,"Empty Result",Toast.LENGTH_SHORT).show();
        } else{
            photoAdapter.setPhotoList(filteredListPhoto);
        }

    }

}