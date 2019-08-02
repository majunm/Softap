package com.example.tst.widget;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.SweepGradient;

public class Layer {
    public int mColor = Color.RED;
    public Paint.Style mSytle = Paint.Style.FILL;
    public Paint mBgPaint;

    public Layer(int mColor) {
        this.mColor = mColor;
        mBgPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        if (mColor != 0) {
            mBgPaint.setColor(mColor);
        }
        mBgPaint.setStyle(mSytle);
    }

    public Layer() {
        mBgPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        if (mColor != 0) {
            mBgPaint.setColor(mColor);
        }
        mBgPaint.setStyle(mSytle);
    }
}
//    Paint paint = new Paint();
//    int[] colors = new int[]{Color.GREEN, Color.GREEN, Color.BLUE, Color.RED, Color.RED};
//    SweepGradient sweepGradient = new SweepGradient(240, 360, colors, null);
//                paint.setShader(sweepGradient);
//                paint.setStrokeWidth(30);
//                paint.setStyle(Paint.Style.STROKE);
//                canvas.drawCircle(mPivotX, mPivotY, mRadius - 30 / 2, paint);