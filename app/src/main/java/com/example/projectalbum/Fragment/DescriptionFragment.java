package com.example.projectalbum.Fragment;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.projectalbum.R;


public class DescriptionFragment extends DialogFragment {

    TextView tv_predes;
    EditText et_newdes;
    Button btn_close,btn_add;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState){
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_description, null);

        tv_predes=view.findViewById(R.id.tv_description);
        et_newdes=view.findViewById(R.id.et_newdes);
        btn_close=view.findViewById(R.id.btn_close_des);
        btn_add=view.findViewById(R.id.btn_add_des);

        Bundle data=getArguments();
        final String[] predes = {data.getString("description")};
        tv_predes.setText(predes[0]);


        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String add=et_newdes.getText().toString();

                if(predes[0] !=null)
                {
                    predes[0] = predes[0] + add;
                }
                else predes[0] = add;

                tv_predes.setText(predes[0]);

                et_newdes.setText("");
            }
        });


        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Đóng fragment khi nút close được nhấn
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction().remove(DescriptionFragment.this).commit();
            }
        });


        return new Dialog(getActivity());
    }
}