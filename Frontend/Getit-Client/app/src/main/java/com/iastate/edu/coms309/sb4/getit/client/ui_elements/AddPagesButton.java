package com.iastate.edu.coms309.sb4.getit.client.ui_elements;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.util.Log;
import android.view.MotionEvent;

import androidx.annotation.RequiresApi;

public class AddPagesButton extends CanvasUiElement {

    public static final double BUTTON_WIDTH = .15;
    public static final double BUTTON_HEIGHT = .15;
    public static final double BUTTON_MARGIN_BOTTOM = .05;

    public static final double SYMBOL_STROKE_WIDTH = .05;
    public static final double SYMBOL_MARGIN = .3;

    public static final double SYMBOL_WIDTH = BUTTON_WIDTH - SYMBOL_MARGIN * 2;

    public AddPagesButton (WriteArea parent) {
        super (parent, 0, 0, BUTTON_WIDTH, BUTTON_HEIGHT);
    }

    @Override
    public void draw(Canvas canvas, double x, double y) {
        //Set x and y to parent origin
        x -= getRelativeX ();
        y -= getRelativeY ();
        WriteArea parent = (WriteArea)getParent ();
        int pages = parent.getPages ();
        double scroll = parent.getScroll ();

        //Calculate button coords and move button
        double buttonX = getParent ().getWidth () / 2 - BUTTON_WIDTH / 2;
        double buttonY = pages - BUTTON_HEIGHT - BUTTON_MARGIN_BOTTOM - scroll;
        setProperties (buttonX, buttonY, getWidth (), getHeight ());

        //Set x and y to their proper values
        x += buttonX;
        y += buttonY;

        //Background color
        Paint pt = new Paint();
        pt.setColor(0xFF3BA89A);
        pt.setStyle(Paint.Style.FILL);

        //Draw the button outline
        double radius = BUTTON_WIDTH / 2;
        int radiusPx = getPixelX (radius);
        canvas.drawCircle (getPixelX (x) + radiusPx, getPixelY (y) + radiusPx, radiusPx, pt);

        //Calculate symbol metrics
        double symbolStrokeWidth = BUTTON_WIDTH * SYMBOL_STROKE_WIDTH;
        double symbolMargin = BUTTON_WIDTH * SYMBOL_MARGIN;
        double symbolWidth = BUTTON_WIDTH * SYMBOL_WIDTH;

        //Calculate symbol coords (plus sign)
        //Putting this in a separate method seems like a bad idea
        double symbolHTop = radius - symbolStrokeWidth / 2;
        double symbolHBottom = symbolHTop + symbolStrokeWidth;
        double symbolHLeft = radius - symbolWidth / 2;
        double symbolHRight = symbolHLeft + symbolWidth;
        double symbolVTop = radius - symbolWidth / 2;
        double symbolVBottom = symbolVTop + symbolWidth;
        double symbolVLeft = radius - symbolStrokeWidth / 2;
        double symbolVRight = symbolVLeft + symbolStrokeWidth;

        //Draw the symbol
        pt.setColor (0xFFFFFFFF); //White
        canvas.drawRect (getPixelX (x + symbolHLeft), getPixelY (y + symbolHTop), getPixelX (x + symbolHRight), getPixelY (y + symbolHBottom), pt);
        canvas.drawRect (getPixelX (x + symbolVLeft), getPixelY (y + symbolVTop), getPixelX (x + symbolVRight), getPixelY (y + symbolVBottom), pt);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onClickEvent() {
        //Calculate initial scroll
        NoteArea noteArea = ((NoteArea)getParent ().getParent ());
        double scroll = noteArea.getScroll ();
        int pages = noteArea.getPages ();
        double scaledScroll = scroll * pages;

        //Add new pages
        noteArea.addPages ();

        //Calculate and set new scroll value
        double newPages = noteArea.getPages ();
        double newScroll = (pages * scroll) / newPages;
        noteArea.setScroll (newScroll);
    }

    @Override
    public void onTouchEvent(double x, double y, MotionEvent e) {

    }
}
