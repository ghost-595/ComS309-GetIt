package com.example.draw_test_2;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    DrawableView v;
    Point clickedPoint = new Point (0, 0);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.wtf ("DMS", "Hello2");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        v = new DrawableView (this);
        LinearLayout ly = findViewById (R.id.layout);
        ly.addView (v);

        //Setup touch listener
        //CanvasTouchListener listener = new CanvasTouchListener ();
        //CanvasClickListener cl = new CanvasClickListener ();
        //ly.setOnTouchListener (listener);
        //ly.setOnClickListener (cl);
        /*Canvas cv = new Canvas();
        Paint pt = new Paint ();
        pt.setColor (0x000000);
        cv.drawRect (new Rect(0, 0, 17, 17), pt);
        cv.drawARGB (128, 255, 0, 0);
        View v = findViewById (R.id.view);
        v.draw(cv);*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public class DrawableView extends View {

        private Paint cp;
        private ArrayList<Point> pts;

        public ArrayList<Point> getPoints () {
            return pts;
        }

        public void addPoint (int x, int y) {
            pts.add (new Point (x, y));
        }

        public DrawableView (Context ctx) {
            super (ctx);
            cp = new Paint ();
            cp.setStyle (Paint.Style.STROKE);
            //cp.setColor (0x000000);
            cp.setStrokeWidth (5);

            Camera camera = new Camera (this, 30, 1, 50);
            camera.addToMesh (2, 0, 0);
            camera.addToMesh (2, -.5, -.5);
            camera.addToMesh (2, -.3, -.7);
            pts = camera.render ();
            //pts = new ArrayList<Point> ();

        }

        @Override
        public void onDraw (Canvas c) {
            //c.drawARGB (255, 0, 0, 255);
            for (int i = 0; i < pts.size (); i ++) {
                if (i != 0) {
                    Point from = pts.get(i - 1);
                    Point to = pts.get(i);
                    c.drawLine(from.x, from.y, to.x, to.y, cp);
                    if (i > 1) {
                        from = pts.get(i - 2);
                        c.drawLine(from.x, from.y, to.x, to.y, cp);
                    }
                }
            }
            //c.drawLine (0, 0, 256, 256, cp);
            //c.drawRect (new Rect (0, 0, 32, 32), cp);
        }
    }

    public class CanvasTouchListener implements View.OnTouchListener {

        @Override
        public boolean onTouch (View vi, MotionEvent e) {
            clickedPoint = new Point ((int)e.getX (), (int)e.getY ());
            return false;
        }
    }

    public class CanvasClickListener implements View.OnClickListener {

        @Override
        public void onClick (View vi) {
            Log.d ("UNIQUETAG", "message");
            v.addPoint (clickedPoint.x, clickedPoint.y);
            v.invalidate ();
        }
    }

    public class Point3d {

        public double x;
        public double y;
        public double z;

        public Point3d () {

        }

        public Point3d (double x, double y, double z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

    }

    public class Camera {

        private View v;
        private double fov;
        private double clipNear;
        private double clipFar;
        private ArrayList<Point3d> mesh;

        public Camera (View v, double fov, double clipNear, double clipFar) {
            this.v = v;
            this.fov = fov;
            this.clipNear = clipNear;
            this.clipFar = clipFar;
            mesh = new ArrayList<Point3d> ();
        }

        public ArrayList<Point> render () {
            Point3d tl = new Point3d (1, 1, -1);
            Point3d tr = new Point3d (1, 1, 1);
            Point3d bl = new Point3d (1, -1, -1);
            Point3d br = new Point3d (1, -1, 1);
            Point portTl = new Point (0, 0);
            Point portBr = new Point (v.getWidth (), v.getWidth ());

            //Render points
            double planeWidth = tr.x - tl.x;
            double planeHeight = tl.y - bl.y;
            ArrayList<Point> renderedPts = new ArrayList<Point> ();
            Log.d ("DEBUGMSG", "Hello");
            for (int i = 0; i < mesh.size (); i ++) {
                Point3d pt = mesh.get(i);
                Log.d ("DEBUGMSG", pt.x + " ");
                if (pt.x >= clipNear) {
                    double mzx = pt.z / pt.x;
                    double myx = pt.y / pt.x;
                    double planeX = mzx * clipNear;
                    double planeY = myx * clipNear;
                    String s = planeX + ", " + planeY;
                    Log.d ("DEBUGMSG", s);
                    int drawX = (int)(portBr.x / 2 + (planeX / planeWidth) * portBr.x);
                    int drawY = (int)(portBr.y / 2 + (planeY / planeHeight) * portBr.y);
                    renderedPts.add (new Point (drawX, drawY));
                }
            }

            return renderedPts;
        }

        public void setMesh (ArrayList<Point3d> mesh) {
            this.mesh = mesh;
        }

        public void addToMesh (double x, double y, double z) {
            mesh.add (new Point3d (x, y, z));
        }

    }
}
