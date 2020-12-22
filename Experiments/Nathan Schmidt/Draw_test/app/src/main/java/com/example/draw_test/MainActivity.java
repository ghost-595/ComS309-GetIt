package com.example.draw_test;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        v = new DrawableView (this);
        LinearLayout ly = findViewById (R.id.layout);
        ly.addView (v);

        //Setup touch listener
        CanvasTouchListener listener = new CanvasTouchListener ();
        CanvasClickListener cl = new CanvasClickListener ();
        ly.setOnTouchListener (listener);
        ly.setOnClickListener (cl);
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
            pts = new ArrayList<Point> ();
        }

        @Override
        public void onDraw (Canvas c) {
            //c.drawARGB (255, 0, 0, 255);
            for (int i = 0; i < pts.size (); i ++) {
                if (i != 0) {
                    Point from = pts.get (i - 1);
                    Point to = pts.get (i);
                    c.drawLine (from.x, from.y, to.x, to.y, cp);
                }
            }
            //c.drawLine (0, 0, 256, 256, cp);
            //c.drawRect (new Rect (0, 0, 32, 32), cp);
        }
    }

    public class CanvasTouchListener implements View.OnTouchListener {

        @Override
        public boolean onTouch (View vi, MotionEvent e) {
            v.addPoint ((int)e.getX (), (int)e.getY ());
            return false;
        }
    }

    public class CanvasClickListener implements View.OnClickListener {

        @Override
        public void onClick (View vi) {
            Log.d ("UNIQUETAG", "message");
            v.invalidate ();
        }
    }
}
