package com.sm.nadaman.common.widget.ecg;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import androidx.annotation.Nullable;
import com.sm.nadaman.R;
import com.sm.nadaman.common.ecg.Line;

import java.text.DecimalFormat;


/**
 * Created by sm on 2018/4/19.
 */

public class BackLayout extends View {
    private static String TAG = "BackLayout";
    private int mWorkWidth;

    public BackLayout(Context context) {
        super(context);
    }

    public BackLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public BackLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        if (hasWindowFocus){
            Log.e(TAG, "onWindowFocusChanged: onWindowFocusChanged" );
//            init();
//            postInvalidate();
        }

    }


    int bgColor = 16 * 1;
    int xNumber = 0;//x轴格子数量
    int yNumber = 50;//y轴格子数量
    float gridWidth;//小格子宽度
    public void setyNumber(int yNumber) {
        this.yNumber = yNumber;
    }

    public int getRectLeft() {
        return (int) mRect.left + 1;
    }

    public int getRectRight(){
        return (int) (mRect.right+1);
    }

    public int getRectTop() {
        return (int) mRect.top + 1;
    }

    Paint mPaint = new Paint();

    private Line mLine;

    public float getGridWidth() {
        return gridWidth;
    }
    public int getxNumber(){
        return xNumber;
    }

    private Rect mRect = new Rect();//画线范围的矩形

    public int getBlankHeight() {
        return (int) (getHeight() - gridWidth * (24 * 5 + 2));
    }

    public void init() {
        mPaint.setStrokeWidth(1f);

        float width = getWidth();
        float height = getHeight();
        gridWidth = getFloat(height / (yNumber+1));
        Log.e(TAG, "init: gridWidth = "+gridWidth );
        xNumber = (int) ((width - gridWidth) / gridWidth);
        int temp = xNumber % 5;
        xNumber -= temp;

        mRect.top = 1 ;
        mRect.bottom = getFloat(mRect.top + gridWidth * (yNumber));
        mRect.left = getFloat((width - xNumber * gridWidth) / 2);
        mRect.right = getFloat(mRect.left + gridWidth * xNumber);
        mWorkWidth = (int) (mRect.right - mRect.left);
        initStyle();
    }

    int colors1;
    int colors2;

    private int style;

    private void initStyle() {
        style = 2;
        if (style < 1 || style > 3)
            return;


        if (style == 2) {
            setBackgroundResource(R.color.white);
            colors1 = Color.argb(1 * bgColor, 0, 0, 0);
            colors2 = Color.argb(8 * bgColor, 255, 0, 0);
        } else {//1 3
            setBackgroundColor(Color.rgb(bgColor, bgColor, bgColor));
            colors1 = Color.rgb(24, 24, 24);
            colors2 = Color.rgb(48, 48, 48);
        }

    }

    /**
     * 保留一位小数
     *
     * @param f
     * @return
     */
    private float getFloat(float f) {
        DecimalFormat decimalFormat = new DecimalFormat("0.0");
        String t = decimalFormat.format(f);
        f = Float.parseFloat(t);
       /* String s = f + "";
        int index = s.indexOf(".");
        if (index < 0)
            return f;
        s = s.substring(0, index);
        f = Float.parseFloat(s);*/
        return f;
    }

    public void startDraw() {
        init();
        mLine = new Line();
        initStyle();
//        postInvalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(Color.TRANSPARENT);
        Log.e(TAG, "onDraw: " );
        if (mLine != null) {

            mPaint.setTextSize(10f);
            mPaint.setStrokeWidth(1f);
          /*  for (int i = 0; i <= yNumber; i++) {
                if (i % 5 == 0)
                    continue;
                mLine.setStartX(mRect.left);
                mLine.setEndX(mRect.right);
                mLine.setStartY(mRect.top + gridWidth * i);
                mLine.setEndY(mRect.top + gridWidth * i);

                drawLine(canvas);
            }

            for (int i = 0; i <= xNumber; i++) {
                if (i % 5 == 0)
                    continue;
                mLine.setStartX(mRect.left + gridWidth * i);
                mLine.setEndX(mRect.left + gridWidth * i);
                mLine.setStartY(mRect.top);
                mLine.setEndY(mRect.bottom);
                drawLine(canvas);
            }*/

            for (int i = 0; i <= yNumber; i++) {
                if (i % 5 == 0) {
                    if (style > 0 && style < 4)
                        mPaint.setColor(colors2);
                }else{
                    if (style > 0 && style < 4)
                        mPaint.setColor(colors1);
                }
                    mLine.setStartX(mRect.left);
                    mLine.setEndX(mRect.right);
                    mLine.setStartY(mRect.top + gridWidth * i);
                    mLine.setEndY(mRect.top + gridWidth * i);
                    drawLine(canvas);
            }

            for (int i = 0; i <= xNumber; i++) {
                if (i % 5 == 0) {
                    if (style > 0 && style < 4)
                        mPaint.setColor(colors2);
                }else{
                    if (style > 0 && style < 4)
                        mPaint.setColor(colors1);
                }
                    mLine.setStartX(mRect.left + gridWidth * i);
                    mLine.setEndX(mRect.left + gridWidth * i);
                    mLine.setStartY(mRect.top);
                    mLine.setEndY(mRect.bottom);
                    drawLine(canvas);
            }

        }
    }

    private void drawLine(Canvas canvas) {
        canvas.drawLine(mLine.getStartX(), mLine.getStartY(), mLine.getEndX(), mLine.getEndY(), mPaint);
    }

    public int getWorkWidth() {
        return mWorkWidth;
    }

    private class Rect {
        float left;
        float top;
        float right;
        float bottom;

        @Override
        public String toString() {
            return "Rect{" +
                    "left=" + left +
                    ", top=" + top +
                    ", right=" + right +
                    ", bottom=" + bottom +
                    '}';
        }

    }

}
