package com.sm.nadaman.common.widget.ecg;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;

import com.blankj.utilcode.util.ScreenUtils;
import com.scwang.smartrefresh.layout.util.DensityUtil;

import org.greenrobot.greendao.annotation.NotNull;


/**
 * Created by sm on 2018/6/25.
 */

public class WaveBg extends Drawable {

    private int color;
//    private float lineNum;

    private int yNumber = 24 * 5;//y轴格子数量
    private int xNumber = 0;//x轴格子数量
    private int colors1;
    private int colors2;
    private Paint mPaint = new Paint();
    private RectF mRect = new RectF();//绘图区域
    private float gridWidth;//最小格子宽度
    private float leftSpace;
    private float rightSpace;
    private float topSpace = DensityUtil.dp2px(2);

    void setyNumber(int yNumber) {
        this.yNumber=yNumber;
    }


    public WaveBg() {
        initStyle();
    }

    @Override
    public void setBounds(@NotNull Rect bounds) {
        super.setBounds(bounds);
    }

    @Override
    protected void onBoundsChange(Rect bounds) {
        super.onBoundsChange(bounds);
        int width = getBounds().width();
        int height = getBounds().height();
        gridWidth = (height * 1f - topSpace * 2) / yNumber;
        xNumber = (int) (width / gridWidth);
//        float totalXSpace = xNumber % 5 * gridWidth;
//        leftSpace = rightSpace = totalXSpace / 2;
//        xNumber = xNumber - xNumber % 5 ;
        startX = mRect.left = leftSpace;
        mRect.right = mRect.left + xNumber * gridWidth;
        startY = mRect.top = topSpace;
        mRect.bottom = mRect.top + yNumber * gridWidth;
    }

    public float getRightSpace() {
        return rightSpace;
    }


    public RectF getDrawRect() {
        return mRect;
    }

    /**
     * 初始化主题样式
     */
    private void initStyle() {
//        colors1 = Color.rgb(28, 28, 28);
//        colors2 = Color.rgb(48, 48, 48);
        colors1 = Color.argb(16, 0, 0, 0);
        colors2 = Color.argb(16 * 8, 255, 0, 0);
        color = Color.WHITE;
        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth(ScreenUtils.getScreenDensity());
    }

    private float startX;
    private float startY;

    public void setColors1(int colors1) {
        this.colors1 = colors1;
    }

    public void setColors2(int colors2) {
        this.colors2 = colors2;
    }

    public void setColor(int color) {
        this.color = color;
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        startX = mRect.left;
        startY = mRect.top;
        canvas.drawColor(color);
        for (int i = 0; i <= xNumber; i++) {
            //画竖线
            mPaint.setColor(i % 5 == 0 ? colors2 : colors1);
            canvas.drawLine(startX, startY, startX, mRect.bottom, mPaint);
            startX += gridWidth;
        }
        for (int i = 0; i <= yNumber; i++) {
            //画横线
            mPaint.setColor(i % 5 == 0 ? colors2 : colors1);
            canvas.drawLine(mRect.left, startY, mRect.right, startY, mPaint);
            startY += gridWidth;
        }
    }

    public float getTopSpace() {
        return topSpace;
    }

    @Override
    public void setAlpha(int alpha) {

    }

    @Override
    public void setColorFilter(ColorFilter colorFilter) {

    }

    @Override
    public int getOpacity() {
        return PixelFormat.OPAQUE;
    }

    float getGridWidth() {
        return gridWidth;
    }
}
