package com.example.punny.test.fragment;


import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.punny.test.Lesson;
import com.example.punny.test.LessonAdapter;
import com.example.punny.test.MainActivity;
import com.example.punny.test.R;

import java.util.ArrayList;
import java.util.Collections;

/**
 * A simple {@link Fragment} subclass.
 */
public class Lessons extends Fragment {

    //Member variables
    private RecyclerView mRecyclerView;
    private ArrayList<Lesson> mSportsData;
    private LessonAdapter mAdapter;

    public Lessons() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ((MainActivity)getActivity()).getSupportActionBar().setTitle("Сургалтууд");
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_lessons, container, false);

        //Initialize the RecyclerView
        mRecyclerView = (RecyclerView)view.findViewById(R.id.recyclerView);

        //Set the Layout Manager
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


        //Initialize the ArrayList that will contain the data
        mSportsData = new ArrayList<>();

        //Initialize the adapter and set it ot the RecyclerView
        mAdapter = new LessonAdapter(mSportsData,getContext());
        mRecyclerView.setAdapter(mAdapter);

        //Get the data
        initializeData();

        return view;
    }
    /**
     * Method for initializing the sports data from resources.
     */
    private void initializeData() {
        //Get the resources from the XML file
        String[] sportsList = getResources().getStringArray(R.array.sports_titles);
        String[] sportsInfo = getResources().getStringArray(R.array.sports_info);
        String[] sportsTitle = getResources().getStringArray(R.array.sports_title);
        TypedArray sportsImageResources = getResources().obtainTypedArray(R.array.sports_images);
        //Clear the existing data (to avoid duplication)
        mSportsData.clear();


        //Create the ArrayList of Sports objects with the titles, images
        // and information about each sport
        for(int i=0; i<sportsList.length; i++){
            mSportsData.add(new Lesson(sportsList[i], sportsInfo[i],sportsTitle[i],
                    sportsImageResources.getResourceId(i,0)));
        }

        //Recycle the typed array
        sportsImageResources.recycle();

        //Notify the adapter of the change
        mAdapter.notifyDataSetChanged();
    }

    /**
     * onClick method for th FAB that resets the data
     * @param view The button view that was clicked
     */
    public void resetSports(View view) {
        initializeData();
    }
}
