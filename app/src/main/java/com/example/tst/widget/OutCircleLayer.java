package com.example.tst.widget;

import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;

public class OutCircleLayer extends Layer {
    public OutCircleLayer setStrokeWidth(float mStrokeWidth) {
        this.mStrokeWidth = mStrokeWidth;
        if (mBgPaint != null) {
            mBgPaint.setStrokeWidth(mStrokeWidth);
        }
        return this;
    }

    public OutCircleLayer(int mColor, float mStrokeWidth) {
        super(mColor);
        this.mStrokeWidth = mStrokeWidth;
        mBgPaint.setStyle(Paint.Style.STROKE);
        mBgPaint.setStrokeWidth(mStrokeWidth);
    }

    public OutCircleLayer() {
        super();
        mBgPaint.setStyle(Paint.Style.STROKE);
        mBgPaint.setStrokeWidth(mStrokeWidth);
//        Shader mShader;
//        if ((mShader = createShader()) != null) {
//            mBgPaint.setShader(mShader);
//        }
    }

    public OutCircleLayer setColors(int[] colors) {
        this.colors = colors;
        return this;
    }


    public OutCircleLayer setPosition(float[] position) {
        this.position = position;
        return this;
    }

    /**
     * 多色渐变
     */
    int[] colors = {Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW, Color.MAGENTA};
    float[] position = {0f, 0.2f, 0.4f, 0.6f, 1.0f}; //颜色位置比例

    public int[] createColors() {
        return colors;
    }

    public float[] createPosition() {
        return position;
    }

    // Shader mLinearGradient1 = new LinearGradient(0, 0, mWidth, 0, colors, position, Shader.TileMode.CLAMP);
    public Shader mShader;

    public Shader createShader(int width) {
        return mShader = new LinearGradient(0, 0, width, 0, createColors(), createPosition(), Shader.TileMode.CLAMP);
    }

    public float mStrokeWidth = 2;

    public void setShader(Shader createShader) {
        if (createShader != null) {
            mBgPaint.setShader(createShader);
        }
    }
}
