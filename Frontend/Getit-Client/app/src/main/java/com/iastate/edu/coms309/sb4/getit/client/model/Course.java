package com.iastate.edu.coms309.sb4.getit.client.model;

import org.json.JSONException;
import org.json.JSONObject;

public class Course {

    private boolean valid;

    private int id;
    private String category;
    private int number;

    private String professorName;
    private String professorEmail;

    private static Course currentCourse;

    public Course(int id, String category, int number) {
        this.id = id;
        this.category = category;
        this.number = number;
        this.professorName = "null";
        this.professorEmail = "null";
        valid = true;
    }

    public Course(int id, String category, int number, String professorName, String professorEmail) {
        this.id = id;
        this.category = category;
        this.number = number;
        this.professorName = professorName;
        this.professorEmail = professorEmail;
        valid = true;
    }

    public Course(JSONObject courseObject) {
        try {
            this.id = courseObject.getInt("id");
            this.category = courseObject.getString("category");
            this.number = courseObject.getInt("number");
            this.professorName = courseObject.getString("professorName");
            this.professorEmail = courseObject.getString("professorEmail");
        } catch (JSONException e) {
            valid = false;
            return;
        }
        valid = true;
    }

    public boolean isValid() {
        return valid;
    }

    public int getId() {
        return id;
    }

    public String getCategory() {
        return category;
    }

    public int getNumber() {
        return number;
    }

    public String getName() {
        return category + " " + number;
    }

    public JSONObject toJSONObject() {
        if (!valid) {
            //This might happen
            return null;
        }
        JSONObject jobj = new JSONObject();
        try {
            jobj.put("id", id);
            jobj.put("category", category);
            jobj.put("number", number);
        } catch (JSONException e) {
            //This should literally never happen
            return null;
        }
        return jobj;
    }

    public static void setCurrentCourse(Course c) {
        currentCourse = c;
    }

    public static Course getCurrentCourse() {
        return currentCourse;
    }
}
