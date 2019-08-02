package com.example.tst.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;

public class ResourceLayer extends Layer {
    public String mDesc;
    public Bitmap mResourceBmp;
    public final String TAG = getClass().getSimpleName();

    public ResourceLayer(Context mContext, int mResId, String mDesc) {
        this.mDesc = mDesc;
        mResourceBmp = BitmapFactory.decodeResource(mContext.getResources(), mResId);
    }

    public ResourceLayer(Context mContext, int mLayoutId) {
        this.mDesc = mDesc;
        mResView = LayoutInflater.from(mContext).inflate(mLayoutId, null);
        mResView.setTag(TAG);
    }

    public View mResView;
}
