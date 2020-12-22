package com.iastate.edu.coms309.sb4.getit.client.screens.course;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
import com.iastate.edu.coms309.sb4.getit.client.R;

import org.json.JSONException;
import org.json.JSONObject;

public class AddCourse extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_course);

        //Configure back button
        Button backButton = findViewById(R.id.nav_back_add_course);
        BackButtonListener backListener = new BackButtonListener();
        backButton.setOnClickListener(backListener);

        //Configure addCourse button
        Button addCourseButton = findViewById(R.id.add_course);
        AddCourseListener addCourseListener = new AddCourseListener();
        addCourseButton.setOnClickListener(addCourseListener);
    }

    private void addCourse() {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://coms-309-sb-4.cs.iastate.edu:8080/course/add";
        JSONObject jsonBody = new JSONObject();

        TextInputEditText courseNumberInput = findViewById(R.id.courseNumberInput);
        int courseNumber = Integer.parseInt(courseNumberInput.getEditableText().toString().trim());

        Spinner courseCategories = findViewById(R.id.courseCategories);
        String courseCat = courseCategories.getSelectedItem().toString();

        try {
            jsonBody.put("category", courseCat);
            jsonBody.put("number", courseNumber);
        } catch (JSONException e) {
            Log.e("Add Course JsonBody", e.toString());
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, url, jsonBody, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            boolean success = response.getBoolean("success");
                            if (!success) {
                                TextView addCourseOutput = findViewById(R.id.addCourseOutput);
                                addCourseOutput.setText("Course Already Exists");
                            } else {
                                goBack();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Add Course", error.toString());
                        error.printStackTrace();
                    }
                });
        queue.add(jsonObjectRequest);
    }

    private void goBack() {
        Intent intent = new Intent(this, SearchCourse.class);
        startActivity(intent);
        this.finish();
    }

    private class AddCourseListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            addCourse();
        }
    }

    private class BackButtonListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            goBack();
        }
    }
}
