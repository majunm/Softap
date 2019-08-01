package com.example.tst.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.annotation.RequiresApi;

import com.example.tst.R;

import java.util.List;

/**
 * 自定义公告栏view
 */
public class INoticeBoardView extends FrameLayout {
    private ValueAnimator anim;
    public int animDuration = 5000;
    // LinearLayout.VERTICAL 时生效
    int offsetX = 100;
    public int vertical_duration_step = 10;
    public int mScreenWidth;
    private int direction = LinearLayout.HORIZONTAL;
    int maxWidth = 0;

    public void init(Context context, AttributeSet attrs) {
        mInflater = LayoutInflater.from(context);
        mScreenWidth = context.getResources().getDisplayMetrics().widthPixels;
        if (attrs != null) {
            TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.INoticeBoardView, 0, 0);
            animDuration = (int) typedArray.getInt(R.styleable.INoticeBoardView_INoticeBoardView_AnimTime, animDuration);
            offsetX = (int) typedArray.getInt(R.styleable.INoticeBoardView_INoticeBoardView_Vertical_OffsetX, offsetX);
            direction = (int) typedArray.getInt(R.styleable.INoticeBoardView_INoticeBoardView_Direction, direction);
            vertical_duration_step = (int) typedArray.getInt(R.styleable.INoticeBoardView_INoticeBoardView_Vertical_Duration_Step, vertical_duration_step);
            typedArray.recycle();
        }
    }

    public INoticeBoardView(Context context) {
        super(context);
        init(context, null);
    }

    public INoticeBoardView(Context context,  AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public INoticeBoardView(Context context,  AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public INoticeBoardView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    /**
     * 不足一屏幕宽的view 强制指定为为屏幕宽度
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int defSize = getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec);
        int defWidth = getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec);
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            LayoutParams lp = (LayoutParams) child.getLayoutParams();
            int wSpec = MeasureSpec.makeMeasureSpec(lp.width, MeasureSpec.UNSPECIFIED);
//            int hSpec = MeasureSpec.makeMeasureSpec(lp.height, MeasureSpec.UNSPECIFIED);
            int hSpec = MeasureSpec.makeMeasureSpec(defSize, MeasureSpec.EXACTLY);
            child.measure(wSpec, hSpec);
            int childWidth = child.getMeasuredWidth();
            switch (direction) {
                case LinearLayout.HORIZONTAL:
                    int meta = childWidth % mScreenWidth;
                    int adjustSize = (meta == 0 ? childWidth / mScreenWidth : childWidth / mScreenWidth + 1) * mScreenWidth;
                    if (childWidth < adjustSize) {
                        wSpec = MeasureSpec.makeMeasureSpec(adjustSize, MeasureSpec.EXACTLY);
                    }
                    child.measure(wSpec, hSpec);
                    break;
                default:
                    break;
            }
            if (childWidth > maxWidth) {
                maxWidth = childWidth;
            }
        }
//        setMeasuredDimension(maxWidth, defSize);
        setMeasuredDimension(defWidth, defSize);
    }

    public static abstract class INoticeAdapt<T> {
        public List<T> data;

        public INoticeAdapt(List<T> data) {
            this.data = data;
        }

        public T getItem(int newIdx) {
            return data.get(getRealSize(newIdx));
        }

        public int mCurrentIndex = 0;

        public int getRealSize(int newIdx) {
            return (newIdx + data.size()) % data.size();
        }

        public int getItemCount() {
            return data == null ? 0 : data.size();
        }

        public abstract void onBindViewHolder(View itemView, T itemData);

        protected abstract View onCreateViewHolder(LayoutInflater inflater, ViewGroup parent, int viewType);
    }

    LayoutInflater mInflater;
    public View mCurrentView;
    public View mSecondView;
    public INoticeAdapt mAdapt;

    public INoticeBoardView setAdapter(INoticeAdapt adapt) {
        if (adapt != null && adapt.getItemCount() > 0) {
            mAdapt = adapt;
            removeAllViews();
            mCurrentView = adapt.onCreateViewHolder(mInflater, INoticeBoardView.this, 0);
            adapt.onBindViewHolder(mCurrentView, adapt.getItem(0));
            if (adapt.getItemCount() > 1) {
                mSecondView = adapt.onCreateViewHolder(mInflater, INoticeBoardView.this, 0);
                adapt.onBindViewHolder(mSecondView, adapt.getItem(1));
            } else {
                mSecondView = adapt.onCreateViewHolder(mInflater, INoticeBoardView.this, 0);
                adapt.onBindViewHolder(mSecondView, adapt.getItem(0));
            }
            addChild(mCurrentView);
            addChild(mSecondView);
            autoRun();
        }
        return this;
    }

    public void addChild(View view) {
        if (view != null && view.getParent() == null) {
            addView(view);
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            pause();
        } else if (ev.getAction() == MotionEvent.ACTION_UP || ev.getAction() == MotionEvent.ACTION_CANCEL) {
            resume();
        }
        super.dispatchTouchEvent(ev);
        return true;
    }

    public INoticeBoardView resume() {
        if (anim != null && anim.isPaused()) {
            anim.resume();
        }
        return this;
    }

    public INoticeBoardView pause() {
        if (anim != null && anim.isRunning()) {
            anim.pause();
        }
        return this;
    }

    public INoticeBoardView autoRun() {
        post(new Runnable() {
            @Override
            public void run() {
                loop();
            }
        });
        return this;
    }

    public int widgetWidth;
    public int widgetHeight;

    private void loop() {
        if (mCurrentView == null || mSecondView == null) {
            return;
        }
        widgetWidth = mCurrentView.getWidth();
        widgetHeight = mCurrentView.getHeight();
        int page = widgetWidth % mScreenWidth;
        int duration = (page == 0 ? widgetWidth / mScreenWidth : widgetWidth / mScreenWidth + 1) * animDuration;
        switch (direction) {
            case LinearLayout.HORIZONTAL:
                anim = ValueAnimator.ofInt(0, -widgetWidth);
                mCurrentView.setX(0);
                mSecondView.setX(widgetWidth);
                anim.setDuration(duration);
                anim.setInterpolator(new LinearInterpolator());
                anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        int animatedValue = (int) animation.getAnimatedValue();
                        mCurrentView.setX(animatedValue);
                        mSecondView.setX(animatedValue + widgetWidth);
                    }
                });
                anim.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        int mCurrentIndex = mAdapt.mCurrentIndex = mAdapt.getRealSize(mAdapt.mCurrentIndex) + 1;
                        tryPrepareNextView();
                        mCurrentView.setX(0);
                        mSecondView.setX(widgetWidth);
                        mAdapt.onBindViewHolder(mCurrentView, mAdapt.getItem(mCurrentIndex));
                        mAdapt.onBindViewHolder(mSecondView, mAdapt.getItem(mCurrentIndex + 1));
                        loop();
                    }
                });
                break;
            case LinearLayout.VERTICAL:
            default:
                int width = getWidth();
                int moveDistance = 0;
                if (widgetWidth > width) {
                    moveDistance = widgetWidth - getWidth() + offsetX;
                }
                page = moveDistance % width;
                duration = (page == 0 ? moveDistance / width : moveDistance / width + 1) * animDuration;
                duration = Math.max(duration, animDuration);
                anim = ValueAnimator.ofInt(0, -moveDistance);
                mCurrentView.setX(0);
                mCurrentView.setY(0);
                mSecondView.setY(widgetHeight);
                anim.setDuration(duration);
                anim.setInterpolator(new LinearInterpolator());
                anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        int animatedValue = (int) animation.getAnimatedValue();
                        mCurrentView.setX(animatedValue);
                    }
                });
                anim.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        ObjectAnimator translationY1 = ObjectAnimator.ofFloat(mCurrentView, "translationY", 0, -widgetHeight);
                        ObjectAnimator translationY2 = ObjectAnimator.ofFloat(mSecondView, "translationY", widgetHeight, 0);
                        AnimatorSet animatorSet = new AnimatorSet();
                        animatorSet.playTogether(translationY1, translationY2);
//                        animatorSet.playTogether(translationY2);
                        animatorSet.setDuration(animDuration / vertical_duration_step);
                        animatorSet.setInterpolator(new LinearInterpolator());
//                        animatorSet.setDuration(animDuration);
                        animatorSet.addListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);
                                int mCurrentIndex = mAdapt.mCurrentIndex = mAdapt.getRealSize(mAdapt.mCurrentIndex) + 1;
                                tryPrepareNextView();
                                mCurrentView.setX(0);
                                mCurrentView.setY(0);
                                mSecondView.setY(widgetHeight);
                                mAdapt.onBindViewHolder(mCurrentView, mAdapt.getItem(mCurrentIndex));
                                mAdapt.onBindViewHolder(mSecondView, mAdapt.getItem(mCurrentIndex + 1));
                                loop();
                            }
                        });
                        animatorSet.setStartDelay(500); //停顿500毫秒
                        //开始执行
                        animatorSet.start();
                    }
                });
                break;
        }
        anim.start();
    }

    public void tryPrepareNextView() {
        removeView(mCurrentView);
        mCurrentView = mSecondView;
        mSecondView = mAdapt.onCreateViewHolder(mInflater, this, 0);
        addChild(mSecondView);
    }

    public final String TAG = getClass().getSimpleName();

}
