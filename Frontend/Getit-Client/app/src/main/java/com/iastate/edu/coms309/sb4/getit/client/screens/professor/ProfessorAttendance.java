package com.iastate.edu.coms309.sb4.getit.client.screens.professor;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.iastate.edu.coms309.sb4.getit.client.R;
import com.iastate.edu.coms309.sb4.getit.client.state.User;
import com.iastate.edu.coms309.sb4.getit.client.websockets.ProfessorWebsocket;

import org.json.JSONException;
import org.json.JSONObject;

public class ProfessorAttendance extends AppCompatActivity {

    private ProfessorWebsocket professorWebsocket;
    private TextView attendingStudentsTextView;
    private TextView attendanceCodeView;

    private int courseId;
    private String courseName;

    /**
     * creates the ProfessorAttendance object
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_professor_attendance);

        Intent myIntent = getIntent();
        courseId = myIntent.getIntExtra("courseId", -1);
        courseName = myIntent.getStringExtra("courseName");

        //course name
        TextView courseText = findViewById(R.id.professorAttendanceCourseName);
        courseText.setText(courseName);

        //Configure back button
        Button backButton = findViewById(R.id.nav_back_professor_attendance);
        BackButtonListener backListener = new BackButtonListener();
        backButton.setOnClickListener(backListener);

        //attending students text view
        attendingStudentsTextView = findViewById(R.id.professorAttendanceOutput);

        professorWebsocket = new ProfessorWebsocket(this, User.getId());

        generateAttendanceCode();
    }

    /**
     * notifies the server to clear the attendance list and closes the websocket
     */
    private void finishProfessorAttendance() {
        //clear the attending student repository for this course
        professorWebsocket.sendMessage("clear " + courseName);
        attendingStudentsTextView.setText("");
        professorWebsocket.closeWebsocket();
        this.finish();
    }

    private class BackButtonListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            finishProfessorAttendance();
        }
    }

    /**
     * generates a new attendance code
     */
    private void generateAttendanceCode() {
        RequestQueue queue;
        queue = Volley.newRequestQueue(this);
        String url = "http://coms-309-sb-4.cs.iastate.edu:8080/professor/genAttendCode";
        try {
            JSONObject jsonBody = new JSONObject();

            jsonBody.put("profID", User.getId());
            jsonBody.put("courseID", courseId);

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                    (Request.Method.POST, url, jsonBody, new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                setAttendanceCode(response.get("result").toString());
                            } catch (JSONException e) {
                                Log.e("Gen Attendance Code", e.toString());
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("Gen Attendance Code", error.toString());
                            error.printStackTrace();
                        }
                    });

            queue.add(jsonObjectRequest);

        } catch (JSONException error) {
            Log.e("Gen Attendance Code", error.toString());
            error.printStackTrace();
        }
    }

    /**
     * set the attendance code to appear on the proffessorAttendanceCodeView
     *
     * @param code
     */
    private void setAttendanceCode(String code) {
        attendanceCodeView = findViewById(R.id.proffessorAttendanceCode);
        attendanceCodeView.setText(code);
    }

}
