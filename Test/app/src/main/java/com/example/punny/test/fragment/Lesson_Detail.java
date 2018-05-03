package com.example.punny.test.fragment;


import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.punny.test.MainActivity;
import com.example.punny.test.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class Lesson_Detail extends Fragment {


    public Lesson_Detail() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_lesson__detail, container, false);

        FloatingActionButton floatingActionButton = (FloatingActionButton)view.findViewById(R.id.fab);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Lessons lessons = new Lessons();
                FragmentManager fragmentManager = ((MainActivity)getContext()).getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.content_frame,lessons).commit();
            }
        });
        //Initialize the views
        TextView sportsTitle = (TextView)view.findViewById(R.id.titleDetail);
        ImageView sportsImage = (ImageView)view.findViewById(R.id.sportsImageDetail);

        Bundle bundle = getArguments();

        sportsTitle.setText(""+bundle.getString("title"));

        Glide.with(getContext()).load(bundle.getInt("image_resource")).into(sportsImage);
        return view;
    }

}
