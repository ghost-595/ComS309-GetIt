package com.iastate.edu.coms309.sb4.getit.client.screens.entry;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.iastate.edu.coms309.sb4.getit.client.R;

public class GettingStarted extends AppCompatActivity {

    private ViewPager mSlideSideViewPager;
    private LinearLayout mDotLayout;
    private TextView[] mDots;
    private int currentPageIndex;

    private SliderAdapter sliderAdapter;

    private Button mNextBtn;
    private Button mPrevBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_getting_started);

        //Initialize Main Components to Getting Started Screen

        mNextBtn = (Button) findViewById(R.id.next);
        mPrevBtn = (Button) findViewById(R.id.prev);

        mSlideSideViewPager = (ViewPager) findViewById(R.id.scrollViewPager);
        mDotLayout = (LinearLayout) findViewById(R.id.dotsLayout);

        sliderAdapter = new SliderAdapter(this);
        mSlideSideViewPager.setAdapter(sliderAdapter);

        findCurrentPage(0);

        mSlideSideViewPager.addOnPageChangeListener(viewListener);

        //On Click Listeners for Next and Prev Buttons
        mNextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentPageIndex < mDots.length) {
                    mSlideSideViewPager.setCurrentItem(currentPageIndex++);
                }
                startActivity(new Intent(GettingStarted.this, CreateAccount.class));
            }
        });

        //On Click Listeners for Next and Prev Buttons
        mPrevBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSlideSideViewPager.setCurrentItem(currentPageIndex--);
            }
        });

        //Initialize Animations
        RelativeLayout relativeLayout = findViewById(R.id.relativeLayout);
        AnimationDrawable animationDrawable = (AnimationDrawable) relativeLayout.getBackground();
        animationDrawable.setEnterFadeDuration(2000);
        animationDrawable.setExitFadeDuration(4000);
        animationDrawable.start();

    }

    private void findCurrentPage(int position) {
        mDots = new TextView[3];
        mDotLayout.removeAllViews();

        for (int i = 0; i < mDots.length; i++) {
            mDots[i] = new TextView(this);
            mDots[i].setText(Html.fromHtml("&#8226"));
            mDots[i].setTextSize(35);
            mDots[i].setTextColor(getResources().getColor(R.color.colorWhiteTransparent));

            mDotLayout.addView(mDots[i]);

        }

        if (mDots.length > 0) {
            mDots[position].setTextColor(getResources().getColor(R.color.colorWhite));
        }
    }

    ViewPager.OnPageChangeListener viewListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int i, float v, int j) {

        }

        @Override
        public void onPageSelected(int i) {
            findCurrentPage(i);
            //When current page is open program will update the index to i
            currentPageIndex = i;

            if (i == 0) {
                mNextBtn.setEnabled(true);
                mPrevBtn.setEnabled(false);
                mPrevBtn.setVisibility(View.INVISIBLE);

                mNextBtn.setText("Next");
                mPrevBtn.setText("");

            } else if (i == mDots.length - 1) {
                mNextBtn.setEnabled(true);
                mPrevBtn.setEnabled(true);
                mPrevBtn.setVisibility(View.VISIBLE);

                mNextBtn.setText("Finish");
                mPrevBtn.setText("Prev");
            } else {
                mNextBtn.setEnabled(true);
                mPrevBtn.setEnabled(true);
                mPrevBtn.setVisibility(View.VISIBLE);

                mNextBtn.setText("Next");
                mPrevBtn.setText("Back");
            }


        }

        @Override
        public void onPageScrollStateChanged(int i) {

        }

    };
}
