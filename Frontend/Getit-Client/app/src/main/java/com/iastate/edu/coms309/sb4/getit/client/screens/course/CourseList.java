package com.iastate.edu.coms309.sb4.getit.client.screens.course;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.iastate.edu.coms309.sb4.getit.client.R;
import com.iastate.edu.coms309.sb4.getit.client.model.Course;
import com.iastate.edu.coms309.sb4.getit.client.screens.professor.ProfessorCourse;
import com.iastate.edu.coms309.sb4.getit.client.screens.recyclerView.RecylcerViewAdapter;
import com.iastate.edu.coms309.sb4.getit.client.screens.student.StudentCourse;
import com.iastate.edu.coms309.sb4.getit.client.state.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/* Course List class
 *
 * this screen will present the student with all classes they ae currently enrolled in and allows them to select a certain course or search/add a course
 */

public class CourseList extends AppCompatActivity implements RecylcerViewAdapter.ItemClickListener {

    private ArrayList<String> courseNames = new ArrayList<>();
    private ArrayList<Integer> courseIds = new ArrayList<>();
    private ArrayList<Course> courseList = new ArrayList<>();
    private RecylcerViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_list);

        //Configure back button
        Button backButton = findViewById(R.id.nav_back_course_list);
        BackButtonListener backListener = new BackButtonListener();
        backButton.setOnClickListener(backListener);

        //Create buttons
        Button searchCoursesButton = findViewById(R.id.searchCourseButton);

        //Create listeners
        SearchCoursesButtonListener searchCoursesListener = new SearchCoursesButtonListener();

        //Assign listeners to buttons
        searchCoursesButton.setOnClickListener(searchCoursesListener);

        getUsersCourses();

    }

    private void getUsersCourses() {
        RequestQueue queue;
        queue = Volley.newRequestQueue(this);
        String url = "http://coms-309-sb-4.cs.iastate.edu:8080/user/courses";
        JSONObject jsonBody = new JSONObject();

        final AppCompatActivity thisActivity = this;
        final RecylcerViewAdapter.ItemClickListener thisListener = this;

        try {
            jsonBody.put("userID", User.getId());
        } catch (JSONException e) {
            Log.e("Get User Courses", e.toString());
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, url, jsonBody, new Response.Listener<JSONObject>() {

                    private ArrayList<String> courseNames = new ArrayList<>();
                    private ArrayList<Integer> courseIds = new ArrayList<>();

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray result = response.getJSONArray("result");
                            for (int i = 0; i < result.length(); i++) {
                                JSONObject course = result.getJSONObject(i);
                                courseList.add(new Course(course)); //Hi this is Nathan, this line is important I promise
                                String courseCategory = course.getString("category");
                                String courseNumber = Integer.toString(course.getInt("number"));
                                int courseId = course.getInt("id");
                                courseNames.add(courseCategory + " " + courseNumber);
                                courseIds.add(courseId);
                            }

                            getCourseData(courseNames, courseIds);

                        } catch (JSONException error) {
                            Log.e("Course List", error.toString());
                            error.printStackTrace();
                        }
                        adapter = new RecylcerViewAdapter(thisActivity, courseNames, R.layout.course_layout, R.id.studentCourse);
                        RecyclerView recyclerView = findViewById(R.id.courseListRecyclerView);
                        recyclerView.setLayoutManager(new LinearLayoutManager(thisActivity));
                        adapter.setClickListener(thisListener);
                        recyclerView.setAdapter(adapter);
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Get User Courses", error.toString());
                        error.printStackTrace();
                    }
                });

        queue.add(jsonObjectRequest);
    }

    private void getCourseData(ArrayList<String> courseNamesData, ArrayList<Integer> courseIdsData) {
        courseNames.addAll(courseNamesData);
        courseIds.addAll(courseIdsData);
    }

    //Navigation functions
    private void goBack() {
        this.finish();
    }

    @Override
    public void onItemClick(View view, int position) {
        Intent intent;
        if (User.getRole() == 0) {
            intent = new Intent(getApplicationContext(), StudentCourse.class);
        } else {
            intent = new Intent(getApplicationContext(), ProfessorCourse.class);
        }
        intent.putExtra("courseName", courseNames.get(position));
        intent.putExtra("courseId", courseIds.get(position));

        Course.setCurrentCourse(courseList.get(position));
        startActivity(intent);
    }

    private class BackButtonListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            goBack();
        }
    }

    /* do search courses
     * Presents screen for the student to search for a course they are not already enrolled in
     */
    private void doSearchCourses() {
        Intent intent = new Intent(this, SearchCourse.class);
        startActivity(intent);
        this.finish();
    }

    private class SearchCoursesButtonListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            doSearchCourses();
        }

    }
}
