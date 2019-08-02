package com.example.tst.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;
import android.widget.ImageView;

import androidx.palette.graphics.Palette;

import com.example.tst.R;

public class BallLayer extends ResourceLayer {
    public BallLayer(Context mContext, int mResId, String mDesc) {
        super(mContext, mResId, mDesc);
    }

    public BallLayer(Context mContext, int mLayoutId) {
        super(mContext, mLayoutId);
    }

    public int mResId;
    public int mLightResid;

    public BallLayer(Context mContext, int mLayoutId, int resId,int lightResid) {
        super(mContext, mLayoutId);
        mResId = resId;
        mLightResid = lightResid;
        if (mResView != null) {
            if (mResView instanceof ImageView) {
                mImageView = (ImageView) mResView;
                addHalo(true);
               // mImageView.setImageResource(resId);
            }
        }
    }

    public ImageView mImageView;

    public void addHaloToImage(final float radius) {
        addHaloToImage(mImageView, mImageView.getContext(), mResId, 0, radius);
    }

    public void addHalo(boolean hight) {
        if (mImageView != null) {
            if (hight) {
                mImageView.setImageResource(mLightResid);
                mImageView.setScaleX(1.5f);
                mImageView.setScaleY(1.5f);
            } else {
                mImageView.setImageResource(mResId);
                mImageView.setScaleX(1.0f);
                mImageView.setScaleY(1.0f);
            }
        }
    }

    /**
     * 给Image添加光晕
     *
     * @param context       上下文
     * @param imageId       图片id
     * @param shadowColorId 光晕颜色id
     * @param radius        （外围光晕宽度，也可以根据图片尺寸按照比例来，根据实际需求）
     * @return 加完光晕的图片
     */
    public static void addHaloToImage(final ImageView ivs, final Context context, final int imageId, final int shadowColorId, final float radius) {
        BitmapDrawable mBitmapDrawable = (BitmapDrawable) context.getResources().getDrawable(imageId);
        final Bitmap mBitmap = mBitmapDrawable.getBitmap();
        final int mBitmapWidth = mBitmap.getWidth();
        final int mBitmapHeight = mBitmap.getHeight();
        final int shadowRadius = (int) (context.getResources().getDisplayMetrics().density * radius);
        //创建一个比原来图片大2个radius的图片对象
        final Bitmap mHaloBitmap = Bitmap.createBitmap(mBitmapWidth + shadowRadius * 2, mBitmapHeight + shadowRadius * 2, Bitmap.Config.ARGB_8888);
        final Canvas mCanvas = new Canvas(mHaloBitmap);
        //设置抗锯齿
        mCanvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
        final Paint mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setFilterBitmap(true);
        Palette.from(mBitmap).generate(new Palette.PaletteAsyncListener() {
            @Override
            public void onGenerated(Palette palette) {
                Palette.Swatch vibrant = palette.getVibrantSwatch();
                if (vibrant == null) {
                    for (Palette.Swatch swatch : palette.getSwatches()) {
                        vibrant = swatch;
                        break;
                    }
                }
                try {
                    mPaint.setColor(context.getResources().getColor(shadowColorId));
                } catch (Exception e) {
                    mPaint.setColor(shadowColorId);
                }
                if (vibrant != null) {
                    int rbg = vibrant.getRgb();
                    int color = changeColor(rbg);
                    mPaint.setColor(color);
                }
                //外发光
                mPaint.setMaskFilter(new BlurMaskFilter(shadowRadius, BlurMaskFilter.Blur.OUTER));
                //从原位图中提取只包含alpha的位图
                Bitmap alphaBitmap = mBitmap.extractAlpha();
                //在画布上（mHaloBitmap）绘制alpha位图
                mCanvas.drawBitmap(alphaBitmap, shadowRadius, shadowRadius, mPaint);
                mPaint.reset();
                mPaint.setAntiAlias(true);
                mPaint.setFilterBitmap(true);
                mCanvas.drawBitmap(mBitmap, null, new Rect(shadowRadius + 1, shadowRadius + 1, shadowRadius + mBitmapWidth - 1, shadowRadius + mBitmapHeight - 1), null);
                //回收
                //mBitmap.recycle();
                //alphaBitmap.recycle();
                ivs.setImageBitmap(mHaloBitmap);
            }
        });
    }

    public static void dispose(final View mResView, boolean hight) {
        if (mResView != null) {
            if (mResView instanceof ImageView) {
                if (hight) {
//                    addHaloToImage((ImageView) mResView, mResView.getContext(), R.mipmap.pic, Color.BLACK, 20);
                    addHaloToImage((ImageView) mResView, mResView.getContext(), R.mipmap.ball1_icon, Color.BLACK, 2);
                } else {
                    ((ImageView) mResView).setImageResource(R.mipmap.pic);
                }
            }
        }
    }

    private static int changeColor(int rgb) {
        int red = rgb >> 16 & 0xFF;
        int green = rgb >> 8 & 0xFF;
        int blue = rgb & 0xFF;
        red = (int) Math.floor(red * (1 - 0.2));
        green = (int) Math.floor(green * (1 - 0.2));
        blue = (int) Math.floor(blue * (1 - 0.2));
        return Color.rgb(red, green, blue);
    }

    public void tst() {

    }
}
