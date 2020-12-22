package com.iastate.edu.coms309.sb4.getit.client.ui_elements;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.MotionEvent;

public class Scrollbar extends CanvasUiElement {

    private Canvas lastUsedCanvas;

    public Scrollbar (CanvasUiElement parent, double x, double y, double width, double height) {
        super (parent, x, y, width, height);
    }

    @Override
    public void draw(Canvas canvas, double x, double y) {
        //Set last used canvas
        lastUsedCanvas = canvas;

        //Calculate scroll position
        double scroll = ((NoteArea)getParent ()).getScroll ();

        //Get page count
        int pages = ((NoteArea)getParent ()).getPages ();
        double barHeight = ((double)1) / pages;

        //Draw scroll thingy
        Paint pt = new Paint ();
        pt.setColor (0xFF808080);
        pt.setStyle (Paint.Style.FILL);
        canvas.drawRect (getPixelX (x), getPixelY (y + scroll), getPixelX (x + getWidth ()), getPixelY (y + scroll + barHeight), pt);

        //Draw scrollbar outline
        pt = new Paint();
        pt.setColor (0xFF000000);
        pt.setStyle (Paint.Style.STROKE);
        pt.setStrokeWidth (5f);
        canvas.drawRect (getPixelX (x), getPixelY (y), getPixelX (x + getWidth ()), getPixelY (y + getHeight ()), pt);
    }

    @Override
    public void onClickEvent() {

    }

    @Override
    public void onTouchEvent(double x, double y, MotionEvent e) {
        //Get page count
        if (lastUsedCanvas != null) {
            int pages = ((NoteArea) getParent()).getPages();
            double viewportHeight = getUi ().getProportionHeight ();

            double barHeight = ((double) viewportHeight) / pages; //TODO implement height
            double scroll = y - barHeight / 2; //TODO implement height
            if (scroll < 0) {
                scroll = 0;
            } else if (scroll > viewportHeight - barHeight) { //TODO implement height
                scroll = viewportHeight - barHeight; //TODO implement height
            }
            ((NoteArea) getParent()).setScroll(scroll);
        }
    }
}
