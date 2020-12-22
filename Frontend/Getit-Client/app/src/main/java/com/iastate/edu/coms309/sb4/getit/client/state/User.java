package com.iastate.edu.coms309.sb4.getit.client.state;

import com.iastate.edu.coms309.sb4.getit.client.model.Course;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class User {

    private static boolean valid = false;

    private static JSONObject userData;

    private static int userId;
    private static int userRole;
    private static int userType;
    private static String name;
    private static String email;
    private static ArrayList<Course> courses;

    public static void importData (JSONObject data) {
        userData = data;
        try {
            email = data.getString("email");
            name = data.getString("name");
            userId = data.getInt ("id");
            userRole = data.getInt("role");
        } catch (JSONException e) {
            valid = false;
            return;
        }
        valid = true;
        return;
    }

    /**
     * Returns the user-course id for the given course, associated with the current user
     * @param course the course to use in ID generation
     * @return a long with the bits 63-32 set to the user ID, and bits 31-0 set to the course ID
     */
    public static int generateUserCourseId (Course course) {
        return ((userId & 0xFFFF) << 16) | (course.getId () & 0xFFFF);
    }

    public static JSONObject getData () {
        return userData;
    }

    public static  boolean isValid () {
        return valid;
    }

    public static int getId () {
        return userId;
    }

    public static int getRole () { return userRole; }

    public static int getType () {
        return userType;
    }

    public static String getName () { return name; }

    public static String getEmail () {
        return email;
    }
}
