package com.example.calendarexpirment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class CalendarActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        Button btn = findViewById(R.id.backButton);
        btn.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                Intent intentBack = new Intent(CalendarActivity.this, MainActivity.class);
                CalendarActivity.this.startActivity(intentBack);
            }
        });
    }
}
