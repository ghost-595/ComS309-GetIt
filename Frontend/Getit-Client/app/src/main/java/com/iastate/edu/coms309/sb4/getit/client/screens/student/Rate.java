package com.iastate.edu.coms309.sb4.getit.client.screens.student;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.iastate.edu.coms309.sb4.getit.client.R;

public class Rate extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate);

        //Configure back button
        Button backButton = (Button) findViewById(R.id.nav_back_rate_course);
        BackButtonListener backListener = new BackButtonListener();
        backButton.setOnClickListener(backListener);
    }

    public void goBack() {
        this.finish();
    }

    private class BackButtonListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            goBack();
        }

    }
}
