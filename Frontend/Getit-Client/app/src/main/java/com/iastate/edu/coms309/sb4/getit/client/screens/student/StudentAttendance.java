package com.iastate.edu.coms309.sb4.getit.client.screens.student;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.iastate.edu.coms309.sb4.getit.client.R;
import com.iastate.edu.coms309.sb4.getit.client.state.User;
import com.iastate.edu.coms309.sb4.getit.client.websockets.StudentWebsocket;

public class StudentAttendance extends AppCompatActivity {

    private StudentWebsocket studentWebsocket;
    private String courseName;

    /**
     * creates the StudentAttendance object
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_attendance);

        //set course name
        Intent myIntent = getIntent();
        courseName = myIntent.getStringExtra("courseName");
        TextView courseText = findViewById(R.id.studentAttendanceCourseName);
        courseText.setText(courseName);

        //Configure back button
        Button backButton = findViewById(R.id.nav_back_student_attendance);
        BackButtonListener backListener = new BackButtonListener();
        backButton.setOnClickListener(backListener);

        //Configure submit button
        Button submitButton = findViewById(R.id.attendanceSubmit);
        SubmitButtonListener submitListener = new SubmitButtonListener();
        submitButton.setOnClickListener(submitListener);

        studentWebsocket = new StudentWebsocket(this, User.getId());
    }


    private void goBack() {
        this.finish();
    }

    private class BackButtonListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            studentWebsocket.closeWebsocket();
            goBack();
        }

    }

    /**
     * sends the attendance code and the corresponding course to the server
     *
     * @param codeAndCourse
     */
    private void submitAttendance(String codeAndCourse) {
        studentWebsocket.sendMessage(codeAndCourse);
    }

    private class SubmitButtonListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            //sends the submit attendance command to the server along with the inputted attendance code and courseName for the server to use to validate the code
            TextInputEditText attendanceInput = findViewById(R.id.attendanceInput);
            String code = attendanceInput.getEditableText().toString().trim();

            submitAttendance("submit attendance " + code + " " + courseName);
        }

    }


}
