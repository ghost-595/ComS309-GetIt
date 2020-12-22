package com.themaxsmith.demochartapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        LineChart chart = findViewById(R.id.chart);

        genGraph(chart);

        setupAboutBTN();



    }

    void setupAboutBTN(){
        Button btn = findViewById(R.id.abtBtn);
        btn.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                Intent intentSec = new Intent(MainActivity.this, SecActivity.class);
                MainActivity.this.startActivity(intentSec);
            }
        });
    }


    void genGraph(LineChart chart){



        int[] data = { 1,5,7,10,4 };

        List<Entry> entries = new ArrayList<Entry>();

        for (int x = 0; x < data.length; x++){
            entries.add(new Entry(x, data[x]));
        }

        LineDataSet dataSet = new LineDataSet(entries, "Sample Data");

        LineData lineData = new LineData(dataSet);
        chart.setData(lineData);
        chart.invalidate();
    }
}
