package edu.odu.ece486.hm_app;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;

import java.util.Formatter;

/**
 * Created by Andrew on 10/7/2014.
 */
public class TestView extends View {

    // Values for the Rectangle
    private int xMax;
    private int yMax;
    private float width = 100;
    private float left = xMax - width;
    private float right = xMax;
    private float top = yMax - 100;
    private float bottom = yMax;
    private Paint paint;

    private int index = 0;
    private int flag = 0;

    // Debugging rectangle text output
    private StringBuilder status = new StringBuilder(0);
    private Formatter formatter = new Formatter(status);

    // Bluetooth data information
    private StringBuilder blueToothData = new StringBuilder(0);
    private Formatter blueToothFormatter = new Formatter(blueToothData);


    public TestView(Context context) {
        super(context);
        paint = new Paint();

        paint.setTypeface(Typeface.SANS_SERIF);
        paint.setTextSize(30);
    }

    public TestView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void onDraw(Canvas canvas) {
        if(index == 0) {
            bottom = canvas.getHeight();
            width = 100;
            left = canvas.getWidth() - width;
            right = canvas.getWidth();
            top = canvas.getHeight() - 100;
        }

        // Drawing the background "tube" for the air pressure meter.
        paint.setColor(Color.DKGRAY);
        canvas.drawRect(left, 0.0f, right, bottom, paint);

        // If the rectangle crosses the threshold, turn green.
        if(top <= (canvas.getHeight() / 4))
            paint.setColor(Color.GREEN);
        else
            paint.setColor(Color.RED);

        // Draw the dynamic rectangle.
        paint.setStrokeWidth(3);
        canvas.drawRect(left, top, right, bottom, paint);

        // Debugging rectangle top location
        paint.setColor(Color.CYAN);
        canvas.drawText(status.toString(), 10, 30, paint);

        // Drawing bluetooth data message
        paint.setColor(Color.YELLOW);
        canvas.drawText(blueToothData.toString(), 10, 100, paint);

        // Drawing threshold barrier line.
        paint.setColor(Color.WHITE);
        paint.setStrokeWidth(8);
        canvas.drawLine(canvas.getWidth() - 150, canvas.getHeight() / 4, canvas.getWidth(), canvas.getHeight() / 4, paint);

        // Update the rectangle
        update(index);

        // Delay
        try {
            Thread.sleep(15);
        } catch (InterruptedException e) { }

        index = 1;
        invalidate(); // Force a re-draw
    }

    // Update the size of the rectangle.
    private void update(int i) {
        if(flag == 0) {
            if(top > 20 && top < (bottom + 50))
                top -= bottom / 30;
            else {
                flag = 1;
            }
        } else if(flag == 1) {
            if(top <= (bottom - 50))
                top += bottom / 30;
            else
                flag = 0;
        }

        // Build status message
        status.delete(0, status.length());
        formatter.format("Y Value: (%3.0f)", top);

        // Build blueTooth message
        blueToothData.delete(0, blueToothData.length());
        blueToothFormatter.format("BlueTooth Data: ");
    }

    @Override
    public void onSizeChanged(int w, int h, int oldW, int oldH) {
        xMax = w - 1;
        yMax = h - 1;
    }
}
