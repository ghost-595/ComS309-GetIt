package com.iastate.edu.coms309.sb4.getit.client.screens.entry;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.viewpager.widget.PagerAdapter;

import com.iastate.edu.coms309.sb4.getit.client.R;

public class SliderAdapter extends PagerAdapter {

    Context context;
    LayoutInflater layoutInflater;

    public SliderAdapter(Context context) {
        this.context = context;
    }

    public int[] slide_Pages = {
            R.drawable.welcome,
            R.drawable.student,
            R.drawable.professor
    };

    public String[] slide_Headings = {
            "Welcome!",
            "To the Student...",
            "To the Professor..."
    };

    public String[] slide_desc = {
            "Welcome to Getit! This is an app that allows you to track academic progress, classes, and much more. It is intended to help you get the " +
                    "most from school with the most simplicity",
            "Thank you for taking the time to use our app! As a student, you are able to view, add, rate, and even take notes in classes you are enrolled in." +
                    "I hope you enjoy all the possibilities! ",
            "Thank you for taking the time to use our app! The Getit app is great for professors because professors are able to assign attendance, view ratings, and more. " +
                    "In order to become a professor, you must first register into the app and recieve approval from an admin. " +
                    "Once that is done, you will then be able to enroll in courses for future use."
    };

    @Override
    public int getCount() {
        return slide_Headings.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object o) {
        return view == (RelativeLayout) o;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.scroll_layout, container, false);

        ImageView scrollImageView = (ImageView) view.findViewById(R.id.scrollImage);
        TextView scrollHeading = (TextView) view.findViewById(R.id.heading);
        TextView scrollDescription = (TextView) view.findViewById(R.id.description);

        scrollImageView.setImageResource(slide_Pages[position]);
        scrollHeading.setText(slide_Headings[position]);
        scrollDescription.setText((slide_desc[position]));

        container.addView(view);

        return view;


    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((RelativeLayout) object);
    }
}
