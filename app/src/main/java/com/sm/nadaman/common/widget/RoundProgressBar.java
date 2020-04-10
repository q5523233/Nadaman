package com.sm.nadaman.common.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.sm.nadaman.R;


/**
 * Created by Administrator on 2015/12/16.
 */
public class RoundProgressBar extends View {
    private Paint paint;
    //圆环的颜色
    private int roundColor;
    //圆环进度的颜色
    private int roundProgressColor;
    //圆环的宽度
    private float roundWidth;
    //最大进度
    private int max;
    //当前进度
    private int progress;
    //进度的风格，实心或者是空心
    private int style;
    public static final int STROKE = 0;
    public static final int FILL = 1;
    public RoundProgressBar(Context context) {
        this(context, null);
    }

    public RoundProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RoundProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        paint = new Paint();
        TypedArray mTypedArray = context.obtainStyledAttributes(attrs, R.styleable.RoundProgressBar);
        roundColor = mTypedArray.getColor(R.styleable.RoundProgressBar_roundColor, Color.RED);
        roundProgressColor = mTypedArray.getColor(R.styleable.RoundProgressBar_roundProgressColor,Color.GREEN);
        roundWidth = mTypedArray.getDimension(R.styleable.RoundProgressBar_roundWidth, 7);
        max = mTypedArray.getInteger(R.styleable.RoundProgressBar_max, 100);
        style = mTypedArray.getInt(R.styleable.RoundProgressBar_style, 0);
        mTypedArray.recycle();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float centre = (float) ((getWidth() /2.0)/2.0); //获取圆心的x坐标
        float radius = (float) ((int)(centre + centre - roundWidth / 2) / 1.3); //圆环的半径
//        float radius = (float) (getWidth() / 2.0) - 70;
        paint.setColor(roundColor); //设置圆环的颜色
        paint.setStyle(Paint.Style.STROKE); //设置空心
        paint.setStrokeWidth(roundWidth); //设置圆环的宽度
        paint.setAntiAlias(true); //消除锯齿
        canvas.drawCircle(centre+centre, getHeight() / 2, radius, paint);
        //画圆环画圆弧
        paint.setStrokeWidth(roundWidth); // 设置圆环的宽度
        paint.setColor(roundProgressColor); //设置进度的颜色
        RectF oval = new RectF(roundWidth,roundWidth,getWidth()-roundWidth,getHeight()-roundWidth); // 圆弧形状和大小界限
//        RectF oval = new RectF((float) ((getHeight() / 1.85) - radius), (float) ((getWidth() / 2.18) - radius), (float) ((getHeight() / 1.85) + radius), (float) ((getWidth() / 2.18) + radius)); // 圆弧形状和大小界限
        switch (style){
            case STROKE:{
                paint.setStyle(Paint.Style.STROKE);
                canvas.drawArc(oval,270,360 * progress / max,false,paint);
                break;
            }
            case FILL:{
                paint.setStyle(Paint.Style.FILL_AND_STROKE);
                if (progress != 0)
                    canvas.drawArc(oval,270,360 * progress / max,true,paint);
                break;
            }
        }
    }
    public synchronized int getMax(){
        return max;
    }
    public synchronized void setMax(int max){
        this.max = max;
    }
    public synchronized int getProgress(){
        return progress;
    }
    public synchronized void setProgress(int progress){
        if (progress > max)
            progress = max;
        if (progress <= max){
            this.progress = progress;
            postInvalidate();
        }
    }

    public int getRoundColor() {
        return roundColor;
    }

    public void setRoundColor(int roundColor) {
        this.roundColor = roundColor;
    }

    public int getRoundProgressColor() {
        return roundProgressColor;
    }

    public void setRoundProgressColor(int roundProgressColor) {
        this.roundProgressColor = roundProgressColor;
    }

    public float getRoundWidth() {
        return roundWidth;
    }

    public void setRoundWidth(float roundWidth) {
        this.roundWidth = roundWidth;
    }
}
