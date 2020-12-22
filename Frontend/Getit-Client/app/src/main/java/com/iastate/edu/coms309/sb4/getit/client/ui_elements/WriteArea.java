package com.iastate.edu.coms309.sb4.getit.client.ui_elements;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;

import org.json.JSONObject;

import java.util.ArrayList;

public class WriteArea extends CanvasUiElement {

    public static final double PATH_RESOLUTION = .002;

    private ArrayList<ArrayList<Point>> paths;

    public WriteArea (CanvasUiElement parent, double x, double y, double width, double height) {
        super (parent, x, y, width, height);
        paths = new ArrayList<ArrayList<Point>> ();
        paths.add (new ArrayList<Point> ());

        //Create page add button
        new AddPagesButton (this);
    }

    @Override
    public void draw(Canvas canvas, double x, double y) {
        //Setup paint
        Paint pt = new Paint ();
        pt.setColor (0xFF000000);
        pt.setStyle (Paint.Style.STROKE);
        pt.setStrokeWidth (8f);

        //Get scroll
        double scroll = getScroll ();

        //Draw paths
        for (int pathIndex = 0; pathIndex < paths.size (); pathIndex ++) {
            ArrayList<Point> current = paths.get (pathIndex);
            for (int pointIndex = 1; pointIndex < current.size (); pointIndex ++) {
                int startX = getPixelX (x + current.get (pointIndex - 1).x);
                int startY = getPixelY (y + current.get (pointIndex - 1).y - scroll);
                int endX = getPixelX (x + current.get (pointIndex).x);
                int endY = getPixelY (y + current.get (pointIndex).y - scroll);
                //TODO lines clip out early; find a better solution
                if (/*y + current.get (pointIndex).y - scroll < 1 && y + current.get (pointIndex - 1).y - scroll < 1*/true) {
                    canvas.drawLine(startX, startY, endX, endY, pt);
                }
            }
        }
    }

    void addPoint(double x, double y) {
        paths.get (paths.size () - 1).add (new Point (x, y));
    }

    void endPath() {
        paths.add (new ArrayList<Point> ());
    }

    private Point getLastPoint() {
        ArrayList<Point> pts = paths.get (paths.size () - 1);
        if (pts.size () != 0) {
            return pts.get(pts.size() - 1);
        } else {
            return null;
        }
    }

    @Override
    public void onClickEvent() {
        endPath ();
    }

    @Override
    public void onTouchEvent(double x, double y, MotionEvent e) {

        //Get scroll
        double scroll = getScroll ();

        //Add point to path
        Point last = getLastPoint ();
        if (last == null) {
            addPoint (x, y + scroll);
        } else if (getDistance (x, y, last.x, last.y) >= PATH_RESOLUTION) {
            addPoint (x, y + scroll);
        }
    }

    private double getDistance (double x1, double y1, double x2, double y2) {
        return Math.sqrt ((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
    }

    int getPages() {
        return ((NoteArea)getParent ()).getPages ();
    }

    double getScroll() {
        return ((NoteArea)getParent ()).getScroll () * getPages ();
    }

    ArrayList<ArrayList<Point>> getPaths() {
        return paths;
    }

    class Point {

        public double x;
        public double y;

        public Point(double x, double y) {
            this.x = x;
            this.y = y;
        }
    }
}
