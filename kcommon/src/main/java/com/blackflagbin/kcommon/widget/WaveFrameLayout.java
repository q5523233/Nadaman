package com.blackflagbin.kcommon.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.*;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.widget.FrameLayout;

public class WaveFrameLayout extends FrameLayout {
    private Paint mPaint;
    private Paint mPathPaint;
    private Path mPath;
    private Bitmap mMaskBitmap;
    private static final Xfermode mXfermode = new PorterDuffXfermode(PorterDuff.Mode.DST_IN);
    private float mRectFWidth;
    private int mWidth;
    private int mHeight;
    private GradientDrawable mBackShadowDrawableLR;

    public WaveFrameLayout(Context context) {
        this(context, null);
    }

    public WaveFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(final Context context, final AttributeSet attrs) {
        mPaint = new Paint();
        mPath = new Path();
        mPathPaint = new Paint();
        mPathPaint.setAntiAlias(true);
        mPathPaint.setColor(0xff000000);
        mPathPaint.setStyle(Paint.Style.FILL);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        // 获取图片宽度和高度
        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();
        mRectFWidth = mWidth / 20.0f;
        mRectFWidth = mRectFWidth - mRectFWidth % 1;//去掉小数部分


    }


    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        // 拿到Drawable
        Drawable drawable = getBackground();
        if (drawable == null){
            drawable = new ColorDrawable(Color.WHITE);
        }
        if (mMaskBitmap == null || mMaskBitmap.isRecycled()) {
            // 创建bitmap
            Bitmap bitmap = Bitmap.createBitmap(mWidth, mHeight, Bitmap.Config.ARGB_8888);
            // 创建画布
            Canvas drawCanvas = new Canvas(bitmap);
            drawable.draw(drawCanvas);
            mPaint.setXfermode(mXfermode);
            mMaskBitmap = getMaskBitmap();
            drawCanvas.drawBitmap(mMaskBitmap, 0, 0, mPaint);
            canvas.drawBitmap(bitmap, 0, 0, null);
            setBackground(new BitmapDrawable(bitmap));
        }
    }

    /**
     * 绘制形状
     *
     * @return
     */
    public Bitmap getMaskBitmap() {
        Bitmap bitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
//        Bitmap bitmap = Bitmap.createBitmap(mWidth, mHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);

        RectF oval = new RectF(0, 0, mRectFWidth, mRectFWidth);
        mPath.addArc(oval, 0, 180);
        mPath.lineTo(0, -(mHeight - mRectFWidth));
        mPath.lineTo(mRectFWidth, -(mHeight - mRectFWidth));
        mPath.lineTo(mRectFWidth, 0);
//        canvas.drawRGB(255, 255, 255);
        canvas.translate(0, mHeight - mRectFWidth);

        canvas.drawPath(mPath, mPathPaint);

        float sum = mRectFWidth;
        while (sum < mWidth) {
            canvas.translate(mRectFWidth, 0);
            canvas.drawPath(mPath, mPathPaint);
            sum += mRectFWidth;
        }
        return bitmap;
    }

}
