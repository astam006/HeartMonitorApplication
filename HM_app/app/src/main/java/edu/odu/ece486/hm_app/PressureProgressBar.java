package edu.odu.ece486.hm_app;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.ProgressBar;

/**
 * Created by Andrew on 2/4/2015.
 */
public class PressureProgressBar extends ProgressBar{
    Paint paint = new Paint();

    public PressureProgressBar(Context context) {
        super(context);
    }

    public PressureProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PressureProgressBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float passingPoint = 2 * (canvas.getWidth() / 3);
        paint.setColor(Color.WHITE);
        paint.setStrokeWidth(8);
        canvas.drawLine(passingPoint, 0, passingPoint, canvas.getHeight(), paint);
    }
}
