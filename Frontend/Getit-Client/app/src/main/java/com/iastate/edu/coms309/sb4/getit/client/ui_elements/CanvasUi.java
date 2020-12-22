package com.iastate.edu.coms309.sb4.getit.client.ui_elements;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Region;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

public class CanvasUi extends View {

    private Canvas canvas;
    private CanvasUiElement content;

    private int maxWidth;
    private int maxHeight;

    private boolean firstDraw = true;

    public CanvasUi (Context ctx, int maxWidth, int maxHeight) {
        super (ctx);

        //Set max width and height
        this.maxWidth = maxWidth;
        this.maxHeight = maxHeight;

        //Create event listeners
        CanvasClickListener canvasClickListener = new CanvasClickListener ();
        CanvasTouchListener canvasTouchListener = new CanvasTouchListener ();

        //Set listeners
        setOnClickListener (canvasClickListener);
        setOnTouchListener (canvasTouchListener);
    }

    @Override
    public void onDraw (Canvas c) {
        //Setup content component
        c.clipRect (0, 0, maxWidth, maxHeight, Region.Op.INTERSECT);
        canvas = c;
        if (firstDraw) {
            content.setup(c);
        }

        if (content != null) {
            content.render(c, content.getRelativeX(), content.getRelativeY());
        }
        firstDraw = false;
    }

    public class CanvasTouchListener implements View.OnTouchListener {

        @Override
        public boolean onTouch (View vi, MotionEvent e) {
            if (content != null) {
                content.handleTouchEvent(toProportionCoord((int) e.getX()) - content.getRelativeX(), toProportionCoord((int) e.getY()) - content.getRelativeY(), e);
                vi.invalidate();
            }
            return false;
        }
    }

    public class CanvasClickListener implements View.OnClickListener {

        @Override
        public void onClick (View vi) {
            if (content != null) {
                content.handleClickEvent();
                vi.invalidate();
            }
        }
    }

    //Gets the coordinate proportional to the width of the canvas
    public double toProportionCoord (int coordinate) {
        if (canvas == null) {
            return ((double) coordinate) / getWidth();
        } else {
            return ((double) coordinate) / canvas.getWidth ();
        }
    }

    double getProportionHeight () {
        return ((double)(maxHeight)) / maxWidth;
    }

    public void setContent(CanvasUiElement content) {
        this.content = content;
    }

    double getMaxWidth () {
        return maxWidth;
    }

    double getMaxHeight () {
        return maxHeight;
    }

    void refresh () {invalidate ();}
}
