package com.sm.nadaman.common.widget.ecg;

import android.content.Context;
import android.graphics.*;
import android.util.AttributeSet;
import android.view.View;

import static android.graphics.Paint.Style.STROKE;

public class ReportPicViewTwo extends View {
    private double augmentNativeTag = 500.0;
    private  int pointArr[];


    private Canvas mCanvas;
    private Bitmap bitmap;

    private Path[] mPaths = new Path[15];

    //折线画笔

    private Paint textPaint;
    private Paint mPaint;
    //背景画笔
    private Paint mPaint2;

    //初始化x轴起始位置
    private float[] xNatives = new float[15];
    //y轴初始化
private float[] yNatives = new float[15];

    private int lineNum = 1250;

    public ReportPicViewTwo(Context context) {
        super(context);
        initView();

    }

    public ReportPicViewTwo(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public ReportPicViewTwo(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView();
    }

    int widthData;
    float heightData;
    boolean stopNum = true;

    private void initView() {

        setFocusable(true);
        setFocusableInTouchMode(true);

        //设置surfaceView透明
        textPaint = new Paint();
        textPaint.setTextSize(45.0f);

        this.setKeepScreenOn(true);
        for (int i = 0 ; i < mPaths.length; i++){
            mPaths[i] = new Path();
        }

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.BLACK);
        mPaint.setStyle(STROKE);
        mPaint.setStrokeWidth(2.5f);
        mPaint.setAntiAlias(true);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeJoin(Paint.Join.ROUND);

        //进行背景画笔的属性设置
        mPaint2 = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint2.setColor(Color.RED);
        mPaint2.setStyle(STROKE);
        mPaint2.setStrokeWidth(1.0f);
        mPaint2.setAntiAlias(true);
        mPaint2.setStrokeCap(Paint.Cap.ROUND);
        mPaint2.setStrokeJoin(Paint.Join.ROUND);

        widthData = getWidth() + 20;
        heightData = (float) (getHeight() / 650.0);
//        draw();
    }
    public void initData(int[] points){
        this.pointArr = points;
//        postInvalidate();
    }



    @Override
    protected void onDraw(Canvas canvas) {
        if(bitmap !=null){
            bitmap.recycle();
            bitmap = null;
        }
        bitmap = Bitmap.createBitmap(500, 500, Bitmap.Config.RGB_565);
        mCanvas = new Canvas(bitmap);
        mCanvas.drawColor(Color.WHITE);
        if (pointArr != null && pointArr.length != 0) {
            for (int i = 0; i < pointArr.length - 1; i++) {
                if (i < lineNum - 1) {
                    getXY(0,i);
                } else if (i < lineNum * 2 - 1 && i > lineNum - 1) {
                    getXY(1,i);
                } else if (i < lineNum * 3 - 1 && i > lineNum * 2 - 1) {
                    getXY(2,i);
                } else if (i < lineNum * 4 - 1 && i > lineNum * 3 - 1) {
                    getXY(3,i);
                } else if (i < lineNum * 5 - 1 && i > lineNum * 4 - 1) {
                    getXY(4,i);
                } else if (i < lineNum * 6 - 1 && i > lineNum * 5 - 1) {
                    getXY(5,i);
                } else if (i < lineNum * 7 - 1 && i > lineNum * 6 - 1) {
                    getXY(6,i);
                } else if (i < lineNum * 8 - 1 && i > lineNum * 7 - 1) {
                    getXY(7,i);
                } else if (i < lineNum * 9 - 1 && i > lineNum * 8 - 1) {
                    getXY(8,i);
                } else if (i < lineNum * 10 - 1 && i > lineNum * 9 - 1) {
                    getXY(9,i);
                } else if (i < lineNum * 11 - 1 && i > lineNum * 10 - 1) {
                    getXY(10,i);
                } else if (i < lineNum * 12 - 1 && i > lineNum * 11 - 1) {
                    getXY(11,i);
                } else if (i < lineNum * 13 - 1 && i > lineNum * 12 - 1) {
                    getXY(12,i);
                } else if (i < lineNum * 14 - 1 && i > lineNum * 13 - 1) {
                    getXY(13,i);
                } else if (i < lineNum * 15 - 1 && i > lineNum * 14 - 1) {
                    getXY(14,i);
                }

                for (int j = 0; j<mPaths.length ; j++){
                    if (yNatives[j] != 0)
                        mPaths[j].lineTo(xNatives[j], yNatives[j]);
                }
            }
        }
        float xxx = 0;
        float xxx2 = 0;
        int paintTag = 0;
        for (int x = 0; x < (bitmap.getWidth()) + 1; x++) {
            if (paintTag > 0 && paintTag <= 4) {
                mPaint2.setStrokeWidth(2.0f);
                mCanvas.drawLine(xxx, 0, xxx, bitmap.getHeight(), mPaint2);
                mPaint2.setStrokeWidth(0.5f);
                canvas.drawLine(xxx2, 0, xxx2, getHeight(), mPaint2);
            } else {
                paintTag = 0;
                mPaint2.setStrokeWidth(3.0f);
                mCanvas.drawLine(xxx, 0, xxx, bitmap.getHeight(), mPaint2);
                mPaint2.setStrokeWidth(1.0f);
                canvas.drawLine(xxx2, 0, xxx2, getHeight(), mPaint2);
            }
            paintTag++;
            xxx += 12.1653;
            xxx2 += getHeight() / 200.0;
        }
        xxx = 0;
        xxx2 = 0;
        paintTag = 0;
        for (int y = 0; y < (bitmap.getHeight()) + 1; y++) {
            if (paintTag > 0 && paintTag <= 4) {
                mPaint2.setStrokeWidth(2.0f);
                mCanvas.drawLine(0, xxx, bitmap.getWidth(), xxx, mPaint2);
                mPaint2.setStrokeWidth(0.5f);
                canvas.drawLine(0, xxx2, getWidth(), xxx2, mPaint2);
            } else {
                paintTag = 0;
                mPaint2.setStrokeWidth(3.0f);
                mCanvas.drawLine(0, xxx, bitmap.getWidth(), xxx, mPaint2);
                mPaint2.setStrokeWidth(1.0f);
                canvas.drawLine(0, xxx2, getWidth(), xxx2, mPaint2);
            }
            paintTag++;
            xxx += 12.1653;
            xxx2 += getHeight() / 200.0;
        }

        //画心电图
        for(int k = 0 ; k<mPaths.length; k++){
            canvas.drawPath(mPaths[k], mPaint);
        }

        stopNum = false;
    }

    private void getXY(int i,int position) {
        if (stopNum) {
            yNatives[i] = getHeight() / 16 * (i+2) - (float) ((getHeight() / 16) / augmentNativeTag * pointArr[position]);
            xNatives[i] += (getHeight() / 200.0) / 10;
        } else {
            if (lineNum > pointArr.length) {
                yNatives[i] = getHeight() / 16 * (i+2) -  (float) ((getHeight() / 16) / augmentNativeTag * pointArr[pointArr.length - 3]);
            } else {
                yNatives[i] = getHeight() / 16 * (i+2) -  (float) ((getHeight() / 16) / augmentNativeTag * pointArr[lineNum * 1 - 1 ]);
            }
        }
    }
}
