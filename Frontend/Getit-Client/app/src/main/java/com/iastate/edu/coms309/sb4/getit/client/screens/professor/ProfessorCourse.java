package com.iastate.edu.coms309.sb4.getit.client.screens.professor;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.iastate.edu.coms309.sb4.getit.client.R;

public class ProfessorCourse extends AppCompatActivity {

    private String courseName = " ";
    private int courseId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_professor_course);

        Intent myIntent = getIntent();
        courseName = myIntent.getStringExtra("courseName");
        courseId = myIntent.getIntExtra("courseId", -1);

        TextView courseText = findViewById(R.id.professorCourseName);
        courseText.setText(courseName);

        //Configure back button
        Button backButton = findViewById(R.id.nav_back_professor_course);
        BackButtonListener backListener = new BackButtonListener();
        backButton.setOnClickListener(backListener);

        //Create buttons
        Button attendanceButton = findViewById(R.id.nav_professor_attendance);

        //Create listeners
        AttendanceButtonListener attendanceListener = new AttendanceButtonListener();

        //Assign listeners to buttons
        attendanceButton.setOnClickListener(attendanceListener);
    }

    //Navigation functions
    private void goBack() {
        this.finish();
    }

    private class BackButtonListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            goBack();
        }

    }

    private void doAttendance() {
        Intent intent = new Intent(this, ProfessorAttendance.class);
        intent.putExtra("courseName", courseName);
        intent.putExtra("courseId", courseId);
        startActivity(intent);
    }

    private class AttendanceButtonListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            doAttendance();
        }

    }
}
