package com.example.projectalbum.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.projectalbum.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DetailFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    TextView tv_size,tv_date,tv_path;
    Button btn_close;




    public DetailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DetailFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DetailFragment newInstance(String param1, String param2) {
        DetailFragment fragment = new DetailFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_detail, container, false);

        tv_size=view.findViewById(R.id.tv_Size);
        tv_date=view.findViewById(R.id.tv_Date);
        tv_path=view.findViewById(R.id.tv_Path);

        Bundle data=getArguments();

        String imagePath = data.getString("imagePath");
        String imageDate = data.getString("imageDate");
        Long imageSize = data.getLong("imageSize",0);

        //Chuyen sang KB
        imageSize=imageSize/1024;
        if(imageSize>1024)
        {
            //Nếu chuyển được sang MB
            imageSize=imageSize/1024;
            tv_size.setText("Dung lượng: "+imageSize+" MB");
        }
        else{
            tv_size.setText("Dung lượng: "+imageSize+" KB");
        }

        tv_date.setText("Ngày tạo: "+imageDate);
        tv_path.setText("Đường dẫn: "+imagePath);


        btn_close = view.findViewById(R.id.btn_Close);
        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Đóng fragment khi nút close được nhấn
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction().remove(DetailFragment.this).commit();
            }
        });


        return view;
    }
}