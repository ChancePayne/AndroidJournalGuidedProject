package com.lambdaschool.journalguidedproject.views;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;

import com.lambdaschool.journalguidedproject.R;


// S04M04-1 Create class which extends a view (in this case, edit text)
public class LinedEditText extends android.support.v7.widget.AppCompatEditText {
    private static final String TAG = "LinedEditText";

    Rect  rect;
    Paint paint;

    int offset;
    int previousColor;

    // S04M04-2 Added necessary constructors
    public LinedEditText(Context context) {
        super(context);
        init(null);
    }

    public LinedEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public LinedEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    // S04M04-4 be sure to include an init method to initialize all necessary objects
    private void init(AttributeSet attrs) {
        rect = new Rect();
        paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);

//        S04M04-8 pass in attribute set and if not null, retrieve the data
        if (attrs != null) {
            TypedArray typedArray = getContext().obtainStyledAttributes(
                    attrs,
                    R.styleable.LinedEditText);

            offset = typedArray.getInt(
                    R.styleable.LinedEditText_line_offset,
                    10);

            paint.setStrokeWidth(
                    typedArray.getFloat(
                            R.styleable.LinedEditText_line_thickness,
                            2));

            paint.setColor(
                    typedArray.getColor(
                            R.styleable.LinedEditText_line_color,
                            Color.RED));
        } else {
            paint.setStrokeWidth(5);
            paint.setColor(Color.GREEN);
            offset = 10;
        }
    }
    // S04M04-11B - override other methods to provide custom behavior
    @Override
    protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect);
        if(focused) {
            previousColor = getColor();
            setColor(Color.RED);
        } else {
            setColor(previousColor);
        }
    }

    // S04M04-9 add getters and setters for any xml parameters as well as other data which should be retrievable
    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public void setColor(int Color) {
        paint.setColor(Color);
    }

    public int getColor() {
        return paint.getColor();
    }

    public void setThickness(float thickness) {
        paint.setStrokeWidth(thickness);
    }

    public float getThickness() {
        return paint.getStrokeWidth();
    }

    // S04M04-3 override the ondraw method (this is called each time a new frame is drawn
    @Override
    protected void onDraw(Canvas canvas) {
        // return the number of lines of text in the view
        int count = getLineCount();

//        long start = System.nanoTime();

        for (int i = 0; i < count; ++i) {
            int baseLine = getLineBounds(i, rect);
            // this will draw a line below each line of text
            canvas.drawLine(rect.left, baseLine + offset, rect.right, baseLine + offset, paint);
        }

//        Log.i(TAG, "Draw Time: " + (System.nanoTime() - start));

        super.onDraw(canvas);
    }
}
