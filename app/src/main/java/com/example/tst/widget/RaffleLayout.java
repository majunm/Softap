package com.example.tst.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.RelativeLayout;

import com.example.tst.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 抽奖布局
 */
public class RaffleLayout extends RelativeLayout {

    private ValueAnimator mRaffleAnim;

    public void setShowAssist(boolean showAssist) {
        isShowAssist = showAssist;
        if (isShowAssist) {
            safeDelChildView(mArrowChild);
            safeDelChildView(mCoverChild);
        }
        postInvalidate();
    }

    public void safeDelChildView(View mArrowChild) {
        if (mArrowChild != null && mArrowChild.getParent() != null) {
            removeView(mArrowChild);
        }
    }

    // 显示辅助
    public boolean isShowAssist = false;
    public boolean isTst = false;
    public Paint mBgPaint;
    public Paint mCoverPaint;
    public Paint mCoverPaint2;
    public float mStrokeWidth = 2;
    public int mBgLayerCount = 8;
    public List<Layer> mBgLayers = new ArrayList<>();
    public List<ResourceLayer> mResourceLayers = new ArrayList<>();
    public List<OutCircleLayer> mOutCircleLayers = new ArrayList<>();
    public List<BallLayer> mBallLayers = new ArrayList<>();
    int[] balls = {
            R.mipmap.ball1_icon,
            R.mipmap.ball2_icon,
            R.mipmap.ball3_icon,
            R.mipmap.ball4_icon,
            R.mipmap.ball5_icon,
            R.mipmap.ball6_icon,
    };
    int[] ballLights = {
            R.mipmap.ball1_light_icon,
            R.mipmap.ball2_light_icon,
            R.mipmap.ball3_light_icon,
            R.mipmap.ball4_light_icon,
            R.mipmap.ball5_light_icon,
            R.mipmap.ball6_light_icon,
    };
    public int[] mDefArcBgColors = {
            Color.parseColor("#3B7EF9"),
            Color.parseColor("#45BEEF"),
            Color.parseColor("#5ED4B0"),
            Color.parseColor("#FDD000"),
            Color.parseColor("#FD9E05"),
            Color.parseColor("#F46440"),
            Color.parseColor("#E439A1"),
            Color.parseColor("#831EB5"),
    };
    public Bitmap mArrowBmp;

    public void init(Context context, AttributeSet attrs) {
        if (attrs != null) {

        }
        mBgPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mCoverPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mCoverPaint.setStrokeWidth(mStrokeWidth);
        mCoverPaint.setColor(Color.WHITE);

        mCoverPaint2 = new Paint(Paint.ANTI_ALIAS_FLAG);
        mCoverPaint2.setColor(Color.WHITE);
        mCoverPaint2.setStrokeWidth(20);

        mBgPaint.setStyle(Paint.Style.STROKE);
        mBgPaint.setStrokeWidth(mStrokeWidth);
        for (int i = 0; i < mBgLayerCount; i++) {
            mBgLayers.add(new Layer(mDefArcBgColors[i % mDefArcBgColors.length]));
            mResourceLayers.add(new ResourceLayer(context, R.layout.child));
            mBallLayers.add(new BallLayer(context, R.layout.ball, balls[i % balls.length], ballLights[i % ballLights.length]));
        }
        for (ResourceLayer mResourceLayer : mResourceLayers) {
            if (mResourceLayer.mResView != null) {
                addView(mResourceLayer.mResView);
            }
        }
        for (BallLayer mResourceLayer : mBallLayers) {
            if (mResourceLayer.mResView != null) {
                addView(mResourceLayer.mResView);
            }
        }
        if (!isShowAssist) {
            addView(mCoverChild = LayoutInflater.from(context).inflate(R.layout.cover, this, false));
            addView(mArrowChild = View.inflate(context, R.layout.arrow, null));
        }
        mArrowBmp = BitmapFactory.decodeResource(getResources(), R.mipmap.arrow);
        setWillNotDraw(false);
    }

    public View mArrowChild;
    public View mCoverChild;

    public RaffleLayout(Context context) {
        super(context);
        init(context, null);
    }

    public RaffleLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public RaffleLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }


    /**
     * 保证是正方形,这样才是圆me
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }

    // 控件宽度
    public int mWidth;
    // 控件高度
    public int mHeight;
    // 中心点x
    public int mPivotX;
    // 中心点y
    public int mPivotY;
    // 半径
    public int mRadius;
    //RectF 圆弧矩形
    public RectF mOval;

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
        mRadius = mWidth / 2;
        mPivotX = mWidth / 2;
        mPivotY = mHeight / 2;
        /**
         * 多色渐变
         */
        int[] colors = {Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW, Color.MAGENTA};
        float[] position = {0f, 0.2f, 0.4f, 0.6f, 1.0f}; //颜色位置比例
        Shader shader1 = new LinearGradient(0, 0, mWidth, 0, colors, position, Shader.TileMode.CLAMP);
        mBgPaint.setShader(shader1);
        mOval = new RectF(0, 0, mWidth, mHeight);

        //#7C1693 ||#251BA2
//        mOutCircleLayers.add(new OutCircleLayer(0,26));
//        mOutCircleLayers.add(new OutCircleLayer(0,3));
        //#7C1693 ||#251BA2
        if (mOutCircleLayers.isEmpty()) {
            OutCircleLayer mOutCircleLayer1 = new OutCircleLayer() {
                @Override
                public float[] createPosition() {
                    float[] position = {0.0f, 1.0f}; //颜色位置比例
                    return position;
                }

                @Override
                public Shader createShader(int width) {
                    return new LinearGradient(0, 0, 0, width, createColors(), createPosition(), Shader.TileMode.CLAMP);
                }

                @Override
                public int[] createColors() {
                    int[] colors = new int[]{
                            Color.parseColor("#7C1693"),
                            Color.parseColor("#251BA2"),
                    };
                    return colors;
                }
            }.setStrokeWidth(36);
            OutCircleLayer mOutCircleLayer2 = new OutCircleLayer().setStrokeWidth(mOutCircleLayer1.mStrokeWidth + 3);
            OutCircleLayer mOutCircleLayer3 = new OutCircleLayer(Color.parseColor("#750F08"), mOutCircleLayer2.mStrokeWidth + 24);
            OutCircleLayer mOutCircleLayer4 = new OutCircleLayer(Color.WHITE, mOutCircleLayer3.mStrokeWidth + 1);
            OutCircleLayer mOutCircleLayer5 = new OutCircleLayer(Color.parseColor("#63000000"), mOutCircleLayer4.mStrokeWidth + 30);
            mOutCircleLayers.add(mOutCircleLayer1);
            mOutCircleLayers.add(mOutCircleLayer2);
            mOutCircleLayers.add(mOutCircleLayer3);
            mOutCircleLayers.add(mOutCircleLayer4);
            mOutCircleLayers.add(mOutCircleLayer5);
            mOutCircleLayer1.setShader(mOutCircleLayer1.createShader(mWidth));
            mOutCircleLayer2.setShader(mOutCircleLayer2.createShader(mWidth));
        }
    }

    public int step = 0;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (!isShowAssist) {
            return;
        }
        if (running) {
            canvas.rotate(animSytle ? degress : 0, mPivotX, mPivotY);
        }
        canvas.save();
        canvas.drawColor(Color.WHITE);
        if (isShowAssist && isTst) {
            canvas.drawColor(Color.RED);
        }
        int arc = 360 / mBgLayerCount;
        int bgLayerSize = mBgLayers.size();
        for (int i = 0; i < bgLayerSize; i++) {
            Layer layer = mBgLayers.get(i);
            canvas.drawArc(mOval, arc * i, arc, true, layer.mBgPaint);
            float radian = (float) Math.toRadians(arc * i);
            float x = mPivotX + (float) (Math.cos(radian) * mRadius);
            float y = mPivotY + (float) (Math.sin(radian) * mRadius);
            canvas.drawLine(mPivotX, mPivotY, x, y, mCoverPaint);
            if (isShowAssist && isTst) {
                // 绘制辅助线
                float angle = (float) Math.toRadians(arc * i + arc / 2);
                int xx = mPivotX + (int) (((mRadius / 2) * Math.cos(angle)));
                int yy = mPivotY + (int) (((mRadius / 2) * Math.sin(angle)));
                canvas.drawPoint(xx, yy, mCoverPaint);
            }
        }
        mBgPaint.setStyle(Paint.Style.STROKE);
        canvas.drawCircle(mPivotX, mPivotY, mRadius - mStrokeWidth / 2, mBgPaint);
        int outSize = mOutCircleLayers.size();
        for (int i = outSize - 1; i >= 0; i--) {
            OutCircleLayer mOutCircleLayer = mOutCircleLayers.get(i);
            canvas.drawCircle(mPivotX, mPivotY, mRadius - mOutCircleLayer.mStrokeWidth / 2, mOutCircleLayer.mBgPaint);
        }
//        isHalo();
        canvas.restore();
        drawIndicator(canvas);
        if (running) {
            start();
        }

    }

    /**
     * 绘制指示器
     *
     * @param canvas
     */
    private void drawIndicator(Canvas canvas) {
        canvas.save();
        canvas.rotate(animSytle ? -degress : degress, mPivotX, mPivotY);
        canvas.drawBitmap(mArrowBmp, mPivotX - mArrowBmp.getWidth() / 2, mPivotY - mArrowBmp.getHeight() / 3 * 2, null);
        canvas.restore();
    }

    private void isHalo() {
        for (BallLayer mResourceLayer : mBallLayers) {
            mResourceLayer.addHalo(false);
        }
        mBallLayers.get(step % mBallLayers.size()).addHalo(true);
        step += 1;
        step %= mBallLayers.size();
    }

    public int degress = 0;
    public boolean running = false;

    public void start() {
        if (mRaffleAnim != null) {
            return;
        }
        running = true;
        degress += 2;
        degress %= 360;
        postInvalidate();
    }

    //在动画开始的地方快然后慢
    public void startRaffle() {
        mRaffleAnim = ValueAnimator.ofInt(0, 360 * 7);
        mRaffleAnim.setInterpolator(new DecelerateInterpolator());
//        mRaffleAnim.setInterpolator(new LinearInterpolator());
        mRaffleAnim.setDuration(animSytle ? 21000 : 6000);
        mRaffleAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int mCurrDegress = (int) valueAnimator.getAnimatedValue();
                running = true;
//                 degress = mCurrDegress % 360;
                degress = mCurrDegress;
                postInvalidate();
            }
        });
        mRaffleAnim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                animSytle = !animSytle;
                startRaffle();
            }
        });
        mRaffleAnim.start();
    }

    public boolean animSytle = true;

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        //start();
        startRaffle();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stop();
    }

    private void stop() {
        if (mRaffleAnim != null && mRaffleAnim.isRunning()) {
            mRaffleAnim.cancel();
            mRaffleAnim = null;
        }
        running = false;
        postInvalidate();
    }

    public int offsetX = 0;

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        int centerX = (right - left) / 2;
        int centerY = (bottom - top) / 2;
        int arc = 360 / mBgLayerCount;
        float mAngle = arc;
        int childCount = getChildCount();
        offsetX = mRadius / 10;
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            int btnWidth = child.getWidth();
            int btnHeight = child.getHeight();
            if (child.getTag() != null) {
                if ("BallLayer".equals(child.getTag())) {
                    float radian = (float) Math.toRadians(arc * i);
                    int x = centerX + (int) (Math.cos(radian) * (mRadius - 36 / 2));
                    int y = centerY + (int) (Math.sin(radian) * (mRadius - 36 / 2));
                    child.layout(x - btnWidth / 2, y - btnHeight / 2, x + btnWidth / 2, y + btnHeight / 2);
                    continue;
                }
                float angle = (float) Math.toRadians(arc * i + mAngle / 2);
                int x = centerX + (int) ((mRadius / 2 + offsetX) * Math.cos(angle));
                int y = centerY + (int) ((mRadius / 2 + offsetX) * Math.sin(angle));
                // 确定绘制图片的位置
                child.layout(x - btnWidth / 2, y - btnHeight / 2, x + btnWidth / 2, y + btnHeight / 2);
                child.setRotation(90 + arc * i + mAngle / 2);
            } else {
                if (mArrowChild == child) {
                    child.layout(centerX - btnWidth / 2, centerY - btnHeight / 3 * 2, centerX + btnWidth / 2, centerY + btnHeight / 3 * 1);
                } else {
                    child.layout(centerX - btnWidth / 2, centerY - btnHeight / 2, centerX + btnWidth / 2, centerY + btnHeight / 2);
                }
            }
        }
    }
}
