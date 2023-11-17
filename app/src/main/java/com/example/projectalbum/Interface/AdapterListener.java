package com.example.projectalbum.Interface;

public interface AdapterListener {
    void onItemClick(String name, String id);

    void onItemShowActionSelected(boolean isSelected);

    void onExitShowActionSelected();
}
