package com.iastate.edu.coms309.sb4.getit.client.ui_elements;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.util.Log;
import android.view.MotionEvent;

import androidx.annotation.RequiresApi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import com.iastate.edu.coms309.sb4.getit.client.model.Course;

public class NoteArea extends CanvasUiElement {

    private double scroll;
    private int pages = 4;

    private Scrollbar scrollbar;
    private WriteArea writeArea;
    private NoteSelectMenu noteSelectMenu;

    public NoteArea (CanvasUi parent) {
        super (null, 0, 0, 1, 1);
        setUi (parent);
    }

    @Override
    public void draw (Canvas canvas, double x, double y) {

        //Background color
        Paint pt = new Paint ();
        pt.setColor (0xFFE0E0E0);
        pt.setStyle (Paint.Style.FILL);
        canvas.drawRect (0, 0, getPixelX (1), getPixelY (getDrawAreaHeight (canvas)), pt);

        //Outline
        pt = new Paint ();
        pt.setColor (0xFF000000);
        pt.setStrokeWidth (5f);
        pt.setStyle (Paint.Style.STROKE);
        canvas.drawRect (0, 0, getPixelX (1), getPixelY (getDrawAreaHeight (canvas)), pt);
    }

    @Override
    public void onClickEvent() {
        //TODO auto-generated stub
    }

    @Override
    public void onTouchEvent(double x, double y, MotionEvent e) {
        //TODO auto-generated stub
    }

    void setScroll (double scroll) {
        this.scroll = scroll;
    }

    double getScroll () {
        return scroll;
    }

    void addPages () {
        pages += 4;
    }

    void setPages (int pages) {
        this.pages = pages;
    }

    int getPages () {
        return pages;
    }

    @Override
    public void setup (Canvas canvas) {
        //Create subcomponents
        //getProportionHeight is currently non-functional
        writeArea = new WriteArea (this, 0, 0, .95, getUi ().getProportionHeight ());
        noteSelectMenu = new NoteSelectMenu (this, 0, 0, .95, getUi ().getProportionHeight ());
        writeArea.show ();
        noteSelectMenu.hide ();
        scrollbar = new Scrollbar (this, .95, 0, .5, getUi ().getProportionHeight ());
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public JSONObject exportNotes () {
        //See openNotes for tag specifications
        JSONObject jobj = new JSONObject ();
        try {
            //Fill dummy tags
            //jobj.put ("class", "TEST");
            jobj.put ("data", "4/20/2069");
            jobj.put ("text", "TEST");

            //Actually put the class and date in
            jobj.put ("course", Course.getCurrentCourse ().getName ());
            LocalDateTime d = LocalDateTime.now ();
            String date = d.getMonth ().getValue () + "/" + d.getDayOfMonth () + "/" + d.getYear ();
            jobj.put ("date", date);

            //Put the current number of pages
            jobj.put("pages", getPages());

            //Store points in a JSON arraay
            ArrayList<ArrayList<WriteArea.Point>> pathData = writeArea.getPaths();
            JSONArray paths = new JSONArray ();
            fillPointArray (paths, pathData);

            //Put the JSON array in the object to return
            jobj.put ("data", paths.toString ());
            return jobj;
        } catch (JSONException e) {
            e.printStackTrace ();
        }
        //Should be unreachable code. Change my mind.
        return null;
    }

    private void fillPointArray (JSONArray paths, ArrayList<ArrayList<WriteArea.Point>> pathData) throws JSONException {
        for (int i = 0; i < pathData.size (); i ++) {
            ArrayList<WriteArea.Point> ptsData = pathData.get (i);
            JSONArray pts = new JSONArray ();
            for (int c = 0; c < ptsData.size (); c ++) {
                JSONObject point = new JSONObject ();
                point.put ("x", ptsData.get (c).x);
                point.put ("y", ptsData.get (c).y);
                pts.put (point);
            }
            paths.put (pts);
        }
    }

    /**
     * Imports notes from the given JSONObject
     * @param notesObject
     */
    public void openNotes (JSONObject notesObject) {
        /**
         * Tag specifications:
         * course: the class these notes are for. Currently a dummy tag.
         * date: the date these notes were taken. Currently a dummy tag.
         * text: raw text included with the notes. Currently a dummy tag.
         * pages: the number of pages in this notes object as an integer.
         * data: a two-dimensional array of vertex data. Verticies are in the form {x, y}, where x and y are double-percision floating points.
         *
         * Example:
         * {
         * "class" : "Fursuiting 101"
         * "date" : "4/20/2069"
         * "text" : "DO NOT TAKE FURSUITING 101 I AM CURRENTLY SITTING IN CLASS REGRETTING MY LIFE CHOICES PLEASE SEND HELP"
         * "pages" : 4,
         * "data" : [
         *    [
         *        {"x" : 0.42, "y" : 0.42},
         *        {"x" : 0.14159, "y" : 0.71828},
         *    ],
         *    [
         *        {"x" : 0.0, "y" : 0.1}
         *        {"x" : 0.56, "y" : 0.33}
         *        {"X" : 0.2, "y" : 0.2}
         *    ]
         * ]
         * }
         */

        try {

            //Set the number of pages
            int numPages = notesObject.getInt ("pages");
            setPages (numPages);

            //Read and place the vertex data
            JSONArray data = new JSONArray (notesObject.getString ("data"));
            for (int i = 0; i < data.length(); i ++) {
                JSONArray verticies = data.getJSONArray (i);
                for (int c = 0; c < verticies.length (); c ++) {
                    JSONObject working = verticies.getJSONObject (c);
                    writeArea.addPoint (working.getDouble("x"), working.getDouble("y"));
                }
                writeArea.endPath ();
            }
        } catch (JSONException e) {
            e.printStackTrace ();
        }
        this.getUi().refresh ();
    }
}
