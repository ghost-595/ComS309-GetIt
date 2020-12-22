package com.iastate.edu.coms309.sb4.getit.client.ui_elements;

import android.graphics.Canvas;
import android.view.MotionEvent;

import java.util.ArrayList;

public abstract class CanvasUiElement {

    private CanvasUiElement parent;
    private ArrayList<CanvasUiElement> children;

    private double relativeX;
    private double relativeY;
    private double width;
    private double height;

    private boolean mouseIn = false;

    private boolean protectChildrenFromDrag = true;
    private CanvasUiElement eventFor;

    private CanvasUi ui;

    private boolean hidden = false;

    public CanvasUiElement (CanvasUiElement parent, double x, double y, double width, double height) {
        //Setup hierarchial relations
        this.parent = parent;
        if (parent != null) {
            parent.addChild(this);
            setUi (parent.getUi ());
        }

        //Create the list to hold children
        children = new ArrayList<CanvasUiElement> ();

        //Set dimensions
        this.relativeX = x;
        this.relativeY = y;
        this.width = width;
        this.height = height;
    }

    protected void addChild (CanvasUiElement child) {
        if (child != null) {
            children.add(child);
            child.ui = ui;
        }
    }

    protected void setProperties (double relativeX, double relativeY, double width, double height) {
        this.relativeX = relativeX;
        this.relativeY = relativeY;
        this.width = width;
        this.height = height;
    }

    //Call to draw stuffs
    protected abstract void draw (Canvas canvas, double x, double y);

    protected void render (Canvas canvas, double x, double y) {
        //Component draws itself
        draw (canvas, x, y);
        //Calls the draw method for all its children
        for (int i = 0; i < children.size (); i ++) {
            CanvasUiElement current = children.get (i);
            if (!current.hidden ()) {
                current.render(canvas, x + current.getRelativeX(), y + current.getRelativeY());
            }
        }
    }

    protected CanvasUiElement getParent () {
        return parent;
    }

    protected double getRelativeX () {
        return relativeX;
    }

    protected double getRelativeY () {
        return relativeY;
    }

    protected double getWidth () {
        return width;
    }

    protected double getHeight () {
        return height;
    }

    protected void setMouseIn (boolean inside) {
        mouseIn = inside;
    }

    protected boolean mouseInside () {
        return mouseIn;
    }

    public abstract void onClickEvent ();
    public abstract void onTouchEvent (double x, double y, MotionEvent e);

    public void handleClickEvent () {
        eventFor = null;
        //Handle click event for this component
        onClickEvent ();
        //Handle click event for children
        for (int i = 0; i < children.size (); i ++) {
            CanvasUiElement current = children.get (i);
            //Check if event is inside the child
            if (!current.hidden () && current.mouseInside ()) {
                current.handleClickEvent ();
            }
        }
    }

    public void handleTouchEvent (double x, double y, MotionEvent e) {
        //Handle touch event for children
        boolean childTriggered = false;
        for (int i = 0; i < children.size (); i ++) {
            CanvasUiElement current = children.get (i);
            if (!current.hidden ()) {
                //Check if event is inside the child
                if (x >= current.getRelativeX() && x <= current.getRelativeX() + current.getWidth() && y >= current.getRelativeY() && y <= current.getRelativeY() + current.getHeight()) {
                    childTriggered = true;
                    if (eventFor == null) {
                        eventFor = current;
                    }
                    if (eventFor == current && protectChildrenFromDrag) {
                        current.setMouseIn(true);
                        current.handleTouchEvent(x - current.getRelativeX(), y - current.getRelativeY(), e);
                    } else if (!protectChildrenFromDrag) {
                        current.setMouseIn(true);
                        current.handleTouchEvent(x - current.getRelativeX(), y - current.getRelativeY(), e);
                    }
                } else {
                    current.setMouseIn(false);
                }
            } else {
                onTouchEvent (x, y, e);
            }
        }
        if (!childTriggered) {
            //Handle touch event for this component
            onTouchEvent (x, y, e);
        }
    }

    protected int getPixelX(double xProportion) {
        return (int)(getUi ().getMaxWidth () * xProportion);
    }

    protected int getPixelY(double yProportion) {
        return (int)(getUi ().getMaxHeight () * yProportion);
    }

    protected static double getDrawAreaHeight (Canvas canvas) {
        return ((double)canvas.getHeight ()) / canvas.getWidth ();
    }

    protected CanvasUi getUi () {
        return ui;
    }

    protected void setUi (CanvasUi ui) {
        //DO NOT CALL THIS METHOD OUTSIDE OF A CONSTRUCTOR
        this.ui = ui;
    }

    protected void setProtectChildrenFromDrag (boolean protect) {
        this.protectChildrenFromDrag = protect;
    }

    protected boolean getProtectChildrenFromDrag () {
        return protectChildrenFromDrag;
    }

    protected void hide () {
        hidden = true;
    }

    protected void show () {
        hidden = false;
    }

    protected boolean hidden () {
        return hidden;
    }

    void setup (Canvas canvas) {
        //Only use for the top-level component; called by CanvasUi before the first draw
    }
}
