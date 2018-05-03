package com.example.punny.test;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.punny.test.fragment.Lesson_Detail;

import java.util.ArrayList;

/**
 * Created by PuNNy on 4/13/2018.
 */

public class LessonAdapter extends RecyclerView.Adapter<LessonAdapter.LessonViewHolder> {


    //Member variables
    private GradientDrawable mGradientDrawable;
    private static ArrayList<Lesson> mLessonsData;
    private Context mContext;

    /**
     * Constructor that passes in the sports data and the context
     * @param sportsData ArrayList containing the sports data
     * @param context Context of the application
     */
    public LessonAdapter(ArrayList<Lesson> sportsData, Context context) {
        this.mLessonsData = sportsData;
        this.mContext = context;

        //Prepare gray placeholder
        mGradientDrawable = new GradientDrawable();
        mGradientDrawable.setColor(Color.GRAY);

        //Make the placeholder same size as the images
        Drawable drawable = ContextCompat.getDrawable
                (mContext,R.drawable.surgalt1);
        if(drawable != null) {
            mGradientDrawable.setSize(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        }
    }


    /**
     * Required method for creating the viewholder objects.
     * @param parent The ViewGroup into which the new View is added after it is
     *               bound to an adapter position.
     * @param viewType The view type of the new View.
     * @return The newly create SportsViewHolder.
     */
    @Override
    public LessonViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new LessonViewHolder(mContext, LayoutInflater.from(mContext).
                inflate(R.layout.list_item, parent, false), mGradientDrawable);
    }

    /**
     * Required method that binds the data to the viewholder.
     * @param holder The viewholder into which the data should be put.
     * @param position The adapter position.
     */
    @Override
    public void onBindViewHolder(LessonViewHolder holder, int position) {

        //Get the current sport
        Lesson currentSport = mLessonsData.get(position);

        //Bind the data to the views
        holder.bindTo(currentSport);

    }


    /**
     * Required method for determining the size of the data set.
     * @return Size of the data set.
     */
    @Override
    public int getItemCount() {
        return mLessonsData.size();
    }


    /**
     * SportsViewHolder class that represents each row of data in the RecyclerView
     */
    static class LessonViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        //Member Variables for the holder data
        private TextView mTitleText;
        private TextView mInfoText;
        private TextView mTitles;
        private ImageView mLessonsImage;
        private Context mContext;
        private Lesson mCurrentSport;
        private GradientDrawable mGradientDrawable;

        /**
         * Constructor for the SportsViewHolder, used in onCreateViewHolder().
         * @param itemView The rootview of the list_item.xml layout file
         */
        LessonViewHolder(Context context, View itemView, GradientDrawable gradientDrawable) {
            super(itemView);

            //Initialize the views
            mTitleText = (TextView)itemView.findViewById(R.id.title);
            mInfoText = (TextView)itemView.findViewById(R.id.subTitle);
            mTitles = (TextView)itemView.findViewById(R.id.newsTitle);
            mLessonsImage = (ImageView)itemView.findViewById(R.id.sportsImage);

            mContext = context;
            mGradientDrawable = gradientDrawable;

            //Set the OnClickListener to the whole view
            itemView.setOnClickListener(this);
        }

        void bindTo(Lesson currentSport){
            //Populate the textviews with data
            mTitleText.setText(currentSport.getTitle());
            mTitles.setText(currentSport.getNews());
            mInfoText.setText(currentSport.getInfo());

            //Get the current sport
            mCurrentSport = currentSport;


            //Load the images into the ImageView using the Glide library
            Glide.with(mContext).load(currentSport.
                    getImageResource()).placeholder(mGradientDrawable).into(mLessonsImage);
        }

        @Override
        public void onClick(View view) {
            Toast.makeText(mContext,"Hello", Toast.LENGTH_LONG).show();
            Lesson currentLesson = mLessonsData.get(getAdapterPosition());
            Lesson_Detail fragment = new Lesson_Detail();
            FragmentManager fragmentManager = ((MainActivity)mContext).getSupportFragmentManager();

            Bundle bundle = new Bundle();
            bundle.putString("title",currentLesson.getTitle());
            bundle.putInt("image_resource",currentLesson.getImageResource());

            fragment.setArguments(bundle);

            fragmentManager.beginTransaction().replace(R.id.content_frame,fragment).commit();

        }
    }
}
