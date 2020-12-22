package com.iastate.edu.coms309.sb4.getit.client.ui_elements;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.MotionEvent;

import java.util.ArrayList;

public class NoteSelectMenu extends CanvasUiElement {

    /**
     * UNUSED FEATURE
     */

    private static final ArrayList<String> noteDates = new ArrayList<String>();

    public NoteSelectMenu (CanvasUiElement parent, double x, double y, double width, double height) {
        super (parent, x, y, width, height);
        noteDates.add ("4/20/2069");
        noteDates.add ("9/9/1999");
        noteDates.add ("11/11/1111");
    }

    @Override
    public void draw(Canvas canvas, double x, double y) {
        Paint pt = new Paint();
        pt.setStyle (Paint.Style.STROKE);
        Paint.FontMetrics fm = pt.getFontMetrics ();
        for (int i = 0; i < noteDates.size (); i ++) {
            canvas.drawText (noteDates.get (i), 20, fm.ascent + fm.ascent * i, pt);
        }
    }

    @Override
    public void onClickEvent() {

    }

    @Override
    public void onTouchEvent(double x, double y, MotionEvent e) {

    }
}
