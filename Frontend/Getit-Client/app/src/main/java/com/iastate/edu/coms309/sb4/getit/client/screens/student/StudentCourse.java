package com.iastate.edu.coms309.sb4.getit.client.screens.student;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.iastate.edu.coms309.sb4.getit.client.R;

/* Student Course Class
 * This class is the main page for a single class that a student is enrolled in
 *
 */

public class StudentCourse extends AppCompatActivity {

    private String courseName = " ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_course);

        //set course name
        Intent myIntent = getIntent();
        courseName = myIntent.getStringExtra("courseName");
        TextView courseText = findViewById(R.id.studentCourseName);
        courseText.setText(courseName);

        //Configure back button
        Button backButton = findViewById(R.id.nav_back_student_course);
        BackButtonListener backListener = new BackButtonListener();
        backButton.setOnClickListener(backListener);

        //Create buttons
        Button attendanceButton = findViewById(R.id.nav_student_attendance);
        Button rateCourseButton = findViewById(R.id.nav_rate_course);
        Button notesButton = findViewById(R.id.nav_notes);

        //Create listeners
        AttendanceButtonListener attendanceListener = new AttendanceButtonListener();
        RateCourseButtonListener rateCourseListener = new RateCourseButtonListener();
        NotesButtonListener notesListener = new NotesButtonListener();

        //Assign listeners to buttons
        attendanceButton.setOnClickListener(attendanceListener);
        rateCourseButton.setOnClickListener(rateCourseListener);
        notesButton.setOnClickListener(notesListener);
    }

    private void goBack() {
        this.finish();
    }

    private class BackButtonListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            goBack();
        }

    }

    /**
     * starts new intent with passed in class
     *
     * @param classToLaunch
     */
    private void launchNewIntent(Class classToLaunch) {
        Intent intent = new Intent(this, classToLaunch);
        intent.putExtra("courseName", courseName);
        startActivity(intent);
        this.finish();
    }

    private class AttendanceButtonListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            launchNewIntent(StudentAttendance.class);
        }

    }

    private class RateCourseButtonListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            launchNewIntent(Rate.class);
        }

    }

    private class NotesButtonListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            launchNewIntent(Notes.class);
        }

    }
}
