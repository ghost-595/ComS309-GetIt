package com.iastate.edu.coms309.sb4.getit.client.screens.course;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.iastate.edu.coms309.sb4.getit.client.R;
import com.iastate.edu.coms309.sb4.getit.client.screens.recyclerView.RecylcerViewAdapter;
import com.iastate.edu.coms309.sb4.getit.client.state.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SearchCourse extends AppCompatActivity implements RecylcerViewAdapter.ItemClickListener {

    //List of Courses to Search
    private List<String> coursesList;
    private RecylcerViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_course);

        coursesList = new ArrayList<>();

        getAllCourses();

        //Initialize the Recycler View for Searching Courses
        RecyclerView recyclerView = findViewById(R.id.searchCourseRecyclerView);
        adapter = new RecylcerViewAdapter(this, coursesList, R.layout.activity_search_course, R.id.textView);

        recyclerView.setAdapter(adapter);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);
    }

    private void getAllCourses() {

        final AppCompatActivity thisActivity = this;
        final RecylcerViewAdapter.ItemClickListener thisListener = this;

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://coms-309-sb-4.cs.iastate.edu:8080/course/list";
        JSONObject jsonBody = new JSONObject();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, jsonBody, new Response.Listener<JSONObject>() {

                    private ArrayList<String> allCourses = new ArrayList<>();

                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            JSONArray result = response.getJSONArray("result");

                            for (int i = 0; i < result.length(); i++) {
                                JSONObject course = result.getJSONObject(i);
                                String courseCat = course.getString("category");
                                String courseNum = Integer.toString(course.getInt("number"));
                                String courseName = courseCat + " " + courseNum;
                                allCourses.add(courseName);
                            }

                            getCourseData(allCourses);

                        } catch (JSONException e) {
                            Log.e("Get All Course JsonBody", e.toString());
                            e.printStackTrace();
                        }
                        adapter = new RecylcerViewAdapter(thisActivity, coursesList, R.layout.row_item, R.id.textView);
                        RecyclerView recyclerView = findViewById(R.id.searchCourseRecyclerView);
                        recyclerView.setLayoutManager(new LinearLayoutManager(thisActivity));
                        adapter.setClickListener(thisListener);
                        recyclerView.setAdapter(adapter);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Get All Courses", error.toString());
                        error.printStackTrace();
                    }
                });

        queue.add(jsonObjectRequest);
    }

    private void getCourseData(ArrayList<String> data) {
        coursesList.addAll(data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_course_main_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onItemClick(View view, int position) {
        String courseToAdd = coursesList.get(position);

        if (courseToAdd.equals("Add New Course")) {
            launchNewIntent(AddCourse.class);
        } else {
            addExistingCourse(courseToAdd);
            launchNewIntent(CourseList.class);
        }
    }

    /**
     * starts new intent with passed in class
     *
     * @param classToLaunch
     */
    private void launchNewIntent(Class classToLaunch) {
        Intent intent = new Intent(this, classToLaunch);
        startActivity(intent);
        this.finish();
    }

    private void addExistingCourse(String courseName) {
        final String courseCat;
        final int courseNumber;

        String[] splitCourseName = courseName.split(" ");

        if (splitCourseName[1].equals("S")) {
            courseCat = "COM S";
            courseNumber = Integer.parseInt(splitCourseName[2]);
        } else {
            courseCat = splitCourseName[0];
            courseNumber = Integer.parseInt(splitCourseName[1]);
        }

        RequestQueue queue;
        queue = Volley.newRequestQueue(this);
        String url = "http://coms-309-sb-4.cs.iastate.edu:8080/user/join";

        try {
            JSONObject jsonBody = new JSONObject();

            jsonBody.put("userID", User.getId());
            jsonBody.put("category", courseCat);
            jsonBody.put("number", courseNumber);

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                    (Request.Method.POST, url, jsonBody, new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {

                            try {
                                JSONObject result = response.getJSONObject("result");
                                JSONArray courses = result.getJSONArray("courses");
                                for (int i = 0; i < courses.length(); i++) {
                                    JSONObject testCourse = courses.getJSONObject(i);
                                    String testCourseCat = testCourse.getString("category");
                                    int testCourseNum = testCourse.getInt("number");
                                    if (testCourseCat.equals(courseCat) && testCourseNum == courseNumber) {
                                        if (User.getRole() == 1) {
                                            assignProfToCourse(testCourse.getInt("id"));
                                        }
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("Join Course", error.toString());
                            error.printStackTrace();
                        }
                    });

            queue.add(jsonObjectRequest);

        } catch (JSONException error) {
            Log.e("Join Course JsonBody", error.toString());
            error.printStackTrace();
        }
    }

    private void assignProfToCourse(int courseId) {
        RequestQueue queue;
        queue = Volley.newRequestQueue(this);
        String url = "http://coms-309-sb-4.cs.iastate.edu:8080/professor/assign";

        try {
            JSONObject jsonBody = new JSONObject();

            jsonBody.put("profID", User.getId());
            jsonBody.put("courseID", courseId);

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                    (Request.Method.POST, url, jsonBody, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                        }
                    }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("Prof Assign", error.toString());
                            error.printStackTrace();
                        }
                    });

            queue.add(jsonObjectRequest);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
