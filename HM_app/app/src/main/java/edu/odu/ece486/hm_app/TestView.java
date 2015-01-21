package edu.odu.ece486.hm_app;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;


/**
 * This custom view is created to display the animation of the pressure bar.
 * The view only takes a small part of the Test Activity screen, and the rest of
 * the activity will use the activity_test.xml.
 */
public class TestView extends View {
    private String mExampleString; // TODO: use a default from R.string...
    private int mExampleColor = Color.RED; // TODO: use a default from R.color...
    private float mExampleDimension = 0; // TODO: use a default from R.dimen...
    private Drawable mExampleDrawable;

    // Rectangle boundaries variables.
    private float top;
    private float bottom;
    private float left;
    private float right;

    // Screen size variables
    private int screenHeight;
    private int screenWidth;

    // Pressure bar scaling interval
    private float pressureIncrement;

    // Index variable used for initialization
    private int index = 0;

    // Flag used to alternate direction.
    private int direction = 0;              // 0 = Up; 1 = down;

    private TextPaint mTextPaint;

    //private int passingPressureHeight =  getHeight() / 3;
    private Paint paint = new Paint();

    // Pressure sensor object used to received data.
    private PressureSensor pressureSensor;
    private int pressure = 0;

/****************************************************************************************/

    public TestView(Context context) {
        super(context);
        init(null, 0);
        pressureSensor = new PressureSensor();
    }

    public TestView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
        pressureSensor = new PressureSensor();
    }

    public TestView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
        pressureSensor = new PressureSensor();
    }

/****************************************************************************************/

    private void init(AttributeSet attrs, int defStyle) {
        // Load attributes
        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.TestView, defStyle, 0);

        //mExampleString = a.getString(
              //  R.styleable.TestView_exampleString);
        mExampleColor = a.getColor(
                R.styleable.TestView_exampleColor,
                mExampleColor);
        // Use getDimensionPixelSize or getDimensionPixelOffset when dealing with
        // values that should fall on pixel boundaries.
        mExampleDimension = a.getDimension(
                R.styleable.TestView_exampleDimension,
                mExampleDimension);

        if (a.hasValue(R.styleable.TestView_exampleDrawable)) {
            mExampleDrawable = a.getDrawable(
                    R.styleable.TestView_exampleDrawable);
            mExampleDrawable.setCallback(this);
        }

        a.recycle();

        // Set up a default TextPaint object
        mTextPaint = new TextPaint();
        mTextPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setTextAlign(Paint.Align.LEFT);

        // Update TextPaint and text measurements from attributes
        //invalidateTextPaintAndMeasurements();
    }

/****************************************************************************************/

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int contentWidth = getWidth();
        int contentHeight = getHeight();
        screenWidth = contentWidth;
        screenHeight = contentHeight;

        // Set passing pressure bar at 1/3 from the top of the screen.
        int passingPressureHeight = contentHeight / 3;

        // Have the maximum pressure be 30 mmHg.
        pressureIncrement = contentHeight / 30;

        // Initialization of Rectangle boundaries.
        if(index == 0) {
            left = 0;
            top = contentHeight - (contentHeight / 20);
            right = contentWidth;
            bottom = contentHeight;
        }

        // Avoid repeat initializing.
        index = 1;

        // TODO: consider storing these as member variables to reduce
        // allocations per draw cycle.
        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        int paddingRight = getPaddingRight();
        int paddingBottom = getPaddingBottom();

        // Draw the example drawable on top of the text.
        if (mExampleDrawable != null) {
            mExampleDrawable.setBounds(paddingLeft, paddingTop,
                    paddingLeft + contentWidth, paddingTop + contentHeight);
            mExampleDrawable.draw(canvas);
        }

        // Drawing rectangle the appropriate color.
        if(top < passingPressureHeight)
            paint.setColor(Color.GREEN);
        else
            paint.setColor(Color.RED);
        canvas.drawRect(left, top, right, bottom, paint);

        // Draw the minimum passing pressure line.
        paint.setColor(Color.WHITE);
        paint.setStrokeWidth(8.0f);
        canvas.drawLine(0, passingPressureHeight, contentWidth, passingPressureHeight, paint);


        // Animate the pressure bar.
        update();

        // Delay
        try {
            Thread.sleep(15);
        } catch (InterruptedException e) { }

        invalidate();       // Force a re-draw.
        requestLayout();    // Maintain stability with layout.
    }

/****************************************************************************************/

    /**
     * Update function used to animate the pressure bar based upon the values received
     * from the air pressure sensor.
     */
    private void update() {
        // Make the Rectangle grow and shrink vertically.
        /*if(direction == 0) {
            if(top > 20 && top < (bottom + 50))
                top -= bottom / 30;
            else
                direction = 1;
        } else if(direction == 1) {
            if(top <= (bottom - 50))
                top += bottom / 30;
            else
                direction = 0;
        }*/

        // Value should be between 0 and 30
        pressure = pressureSensor.getPressure();

        // Move the top of the rectangle up towards the top of the screen '-'
        top = screenHeight - (pressure * pressureIncrement);

    }

/****************************************************************************************/

    /**
     * Gets the example string attribute value.
     * @return The example string attribute value.
     */
    /*public String getExampleString() {
        return mExampleString;
    }*/

    /**
     * Sets the view's example string attribute value. In the example view, this string
     * is the text to draw.
     * @param exampleString The example string attribute value to use.
     */
    /*public void setExampleString(String exampleString) {
        mExampleString = exampleString;
        invalidateTextPaintAndMeasurements();
    }*/

    /**
     * Gets the example color attribute value.
     * @return The example color attribute value.
     */
    /*public int getExampleColor() {
        return mExampleColor;
    }*/

    /**
     * Sets the view's example color attribute value. In the example view, this color
     * is the font color.
     * @param exampleColor The example color attribute value to use.
     */
    /*public void setExampleColor(int exampleColor) {
        mExampleColor = exampleColor;
        //invalidateTextPaintAndMeasurements();
    }*/

    /**
     * Gets the example dimension attribute value.
     * @return The example dimension attribute value.
     */
    /*public float getExampleDimension() {
        return mExampleDimension;
    }*/

    /**
     * Sets the view's example dimension attribute value. In the example view, this dimension
     * is the font size.
     * @param exampleDimension The example dimension attribute value to use.
     */
    /*public void setExampleDimension(float exampleDimension) {
        mExampleDimension = exampleDimension;
        //invalidateTextPaintAndMeasurements();
    }*/

    /**
     * Gets the example drawable attribute value.
     * @return The example drawable attribute value.
     */
    /*public Drawable getExampleDrawable() {
        return mExampleDrawable;
    }*/

    /**
     * Sets the view's example drawable attribute value. In the example view, this drawable is
     * drawn above the text.
     * @param exampleDrawable The example drawable attribute value to use.
     */
    /*public void setExampleDrawable(Drawable exampleDrawable) {
        mExampleDrawable = exampleDrawable;
    }*/
}
