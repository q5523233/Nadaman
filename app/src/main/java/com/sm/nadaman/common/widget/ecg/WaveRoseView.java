package com.sm.nadaman.common.widget.ecg;

import android.content.Context;
import android.graphics.*;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.Nullable;
import com.sm.nadaman.common.ecg.WaveInfo;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by sm on 2018/4/13.
 */

public class WaveRoseView extends View {
    public WaveRoseView(Context context) {
        super(context);
        init();
    }


    public WaveRoseView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public WaveRoseView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    private int width;
    int height;
    private WaveInfo mWaveInfo;
    private float heightMultiple = 0.1f;//高度倍数，1f下取值-100到100

    private Paint paint = new Paint();
    private int color;

    private int lineNum;//一行可以显示多少个数据

    private int wipeNum;//擦除的数量

    private int startOffset;
    private Bitmap mBitmap;
    private Bitmap mBitmap_bak;

    private Canvas mCanvas;
    private int index;

    public void setNumber(int number) {
        this.number = number;
    }

    public void setStartOffset(int startOffset) {
        this.startOffset = startOffset;
    }

    public int number = 250;//每秒收集次数

    List<Integer> mListAll = new ArrayList<>();//全部数据

    public void initStyle() {
        color = Color.BLACK;
    }

    @Override
    protected void onDraw(Canvas canvas) {
//        super.onDraw(canvas);
        synchronized (this) {
            toCollectDraw(canvas);
        }
    }

    private void toBufferDraw() {
        if (index < 2 || lineNum == 0) {//一个点
            return;
        }
        int startX = (index) % lineNum + 0;
        if (startX == 0) {
            if (mBitmap_bak != null) {
                Bitmap temp = mBitmap;
                mBitmap = mBitmap_bak;
                mBitmap_bak = temp;
                mCanvas.setBitmap(mBitmap);
//                mCanvas = new Canvas(mBitmap);
                mCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
            }
        } else if (startX == lineNum - wipeNum) {
            mBitmap_bak = Bitmap.createBitmap(mWaveInfo.getRectRight(), height, Bitmap.Config.ARGB_8888);
        }
        try {
            if (startX >= 1)
                toDrawLine(mCanvas, startX - 1, mListAll.get(mListAll.size() - 2), startX, mListAll.get(mListAll.size() - 1));
        } catch (Exception e) {
            reset();
        }
    }

    /**
     * 添加数据时绘制
     *
     * @param canvas
     */
    private void toCollectDraw(Canvas canvas) {
        if (index == 0 || lineNum == 0 || mBitmap == null)
            return;
        int startX = index % lineNum;
        RectF dst;
        Rect src;
        if (startX >= lineNum - wipeNum) {
            int sx = (int) getAmendWidth(wipeNum - (lineNum - startX)) + mWaveInfo.getRectLeft();
            src = new Rect(sx, 0, mBitmap.getWidth(), mBitmap.getHeight());
            dst = new RectF(sx, 0, mBitmap.getWidth(), mBitmap.getHeight());
            canvas.drawBitmap(mBitmap, src, dst, paint);
        } else if (startX < lineNum - wipeNum) {
            dst = new RectF(mWaveInfo.getRectLeft(), 0, mBitmap.getWidth(), mBitmap.getHeight());
            src = new Rect(mWaveInfo.getRectLeft(), 0, mBitmap.getWidth(), mBitmap.getHeight());
            canvas.drawBitmap(mBitmap, src, dst, paint);
            if (mBitmap_bak != null && startX + wipeNum < lineNum) {
                int sx = (int) getAmendWidth(startX + wipeNum) + mWaveInfo.getRectLeft();
                dst = new RectF(sx, 0, mBitmap_bak.getWidth(), mBitmap_bak.getHeight());
                src = new Rect(sx, 0, mBitmap_bak.getWidth(), mBitmap_bak.getHeight());
                canvas.drawBitmap(mBitmap_bak, src, dst, paint);
            }
        }
    }

//    ------------------------------------------------------------
//    ------------


    private void toDrawLine(Canvas canvas, float startX, float startY, float stopX, float stopY) {
        startX = getAmendWidth(startX);
        startY = getAmendHeight(startY);
        stopX = getAmendWidth(stopX);
        stopY = getAmendHeight(stopY);
        canvas.drawLine(startX, startY, stopX, stopY, paint);
    }

    private void init() {
        mWaveInfo = WaveInfo.getInstance();
        setLayoutParams(new ViewGroup.LayoutParams(0, 0));
        initStyle();
    }

    public void setParams(int width, int height) {
        ViewGroup.LayoutParams param = getLayoutParams();
        if (param == null) {
            param = new ViewGroup.LayoutParams(0, 0);
        }
        param.width = width;
        param.height = height;
        setLayoutParams(param);
    }


    private void initConfig() {
        changeWaveSpeed();
        changeWaveGain();
    }

    public void changeWaveSpeed() {
        lineNum = (int) (mWaveInfo.getGridNum(mWaveInfo.getRectRight()) / mWaveInfo.getSpeed() * number) - startOffset;
        maxHolderNum = lineNum * 4;
        if (mWaveInfo.getWaveSpeed() == WaveInfo.WaveSpeed.WAVE_SPEED_12_5) {
            wipeNum = (int) (number / 12.5) / 2;
        } else if (mWaveInfo.getWaveSpeed() == WaveInfo.WaveSpeed.WAVE_SPEED_25) {
            wipeNum = (number / 25) / 2;
        } else {
            wipeNum = (number / 50) / 2;
        }
    }

    public void changeWaveGain() {
        if (WaveInfo.getInstance().getWaveGain() == WaveInfo.WaveGain.WAVE_GAIN_5) {
            heightMultiple = 0.025f;
        } else if (WaveInfo.getInstance().getWaveGain() == WaveInfo.WaveGain.WAVE_GAIN_10) {
            heightMultiple = 0.05f;
        } else {
            heightMultiple = 0.1f;
        }
//        drawAgain();
    }

    private int maxHolderNum;

    public void length(int num) {

        if (getVisibility() == GONE) //没有显示
            return;
        if (getWidth() == 0)
            return;
        synchronized (this) {
            if (index == 0 || getWidth() != width) {
                width = getWidth();
                height = getHeight();

                if (width == 0 || height == 0)
                    return;
                initConfig();
                initBuffer();
            }
            if (index >= maxHolderNum) {
                mListAll.remove(0);
            }
            mListAll.add(num);
            index++;
            toBufferDraw();
            postInvalidate();
        }

    }


    private void initBuffer() {
        mBitmap = Bitmap.createBitmap(mWaveInfo.getRectRight(), height, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);
        mCanvas.drawColor(Color.TRANSPARENT);
        paint.setStyle(Paint.Style.STROKE);
        paint.setAntiAlias(true);
        paint.setStrokeWidth(2f);
        paint.setColor(color);
        paint.setTextSize(10f);
    }

    /**
     * 保留数据 重画
     */
    public void drawAgain() {

        width = getWidth();
        height = getHeight();
        if (width == 0 || height == 0)
            return;

        initConfig();

    }


    private float getAmendHeight(float num) {

        float x = (height - height / 12) * heightMultiple;
        num = num * (x / 200.0f);
//        Log.e("----", "getAmendHeight: "+num );
        if (num > 0) {
            return 0 - num + height / 2;
        } else {
            return Math.abs(num) + height / 2;
        }
    }

    private float getAmendWidth(float num) {
        float x = (mWaveInfo.getRectRight() - 1f - startOffset) / lineNum;
        return x * num + startOffset;
    }


    /**
     * 重置
     */
    public void reset() {
        synchronized (this) {
            index = 0;
            mListAll.clear();
            height = 0;
            width = 0;
            lineNum = 0;
            mBitmap_bak = null;
            mBitmap = null;
            mCanvas = null;
        }

        try {
            finalize();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        reset();
        mWaveInfo = null;
        super.onDetachedFromWindow();
    }
}
