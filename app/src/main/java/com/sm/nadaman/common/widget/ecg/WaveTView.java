package com.sm.nadaman.common.widget.ecg;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.blankj.utilcode.util.SizeUtils;

import java.util.ArrayList;
import java.util.List;

public class WaveTView extends View {
    private int[] colors;
    private String[] titles;
    private Paint mPathPaint;
    private Paint mTitlePaint;
    protected float gridWidth;//最小格子的大小
    protected float heightDensity = 0.25f;//高度密度
    protected float widthDensity;//宽度密度
    private int wipeNum;//擦除的数量
    private int wipeWidth;//擦除的数量
    private int lineNum;//屏幕能显示的数量
    private int maxNum = WaveViewConfig.valuePerMV;//心电最大数值
    private int color = Color.GREEN;//画心电的颜色
    private int color_pace = Color.WHITE;//画左边title的画笔颜色
    private int titleHeight;//和增益有关
    private int titleWidth;
    private int width;
    private float childHeight;
    private float gain;//增益
    private float titleLeftMargin;//画增益线的左边距
    private float pathLeftMargin;//画心电线的左边距
    private float paddingLeft;//网格背景距离左侧的边缘距离
    private float padddingTop;///网格背景距离上侧的边缘距离
    WaveBg waveBg;
    Bitmap bitmapBg;
    private int height;
    Thread thread;//绘制bitmap的线程
    private volatile boolean hasData;
    private int frame = 30;
    private int[] pix;
    private Canvas canvasTmp;
    private Bitmap bitmap;
    private boolean isAvaiable;
    private boolean needDrawTitle = true;
    private boolean touchAble;
    List<WaveInfo> waveInfos = new ArrayList<>();
    int lastIndex;
    int total;

    private class PropertyHolder {
        List<WaveInfo> waveInfos = new ArrayList<>();
        int lastIndex;
        int total;
        private boolean needDrawTitle = true;
        private boolean touchAble;
    }

    public WaveTView(Context context) {
        super(context);
        init();
    }

    public WaveTView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public WaveTView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPathPaint = new Paint();
        mPathPaint.setAntiAlias(true);
        mPathPaint.setStrokeWidth(SizeUtils.dp2px(1.2f));
        mPathPaint.setStyle(Paint.Style.STROKE);
        mTitlePaint = new Paint();
        mTitlePaint.setAntiAlias(true);
        mTitlePaint.setStyle(Paint.Style.STROKE);
        post(new Runnable() {
            @Override
            public void run() {
                if (listener != null) {
                    listener.onReady();
                }
            }
        });
    }

    private void setWidthDensity(float speed, int secCount) {
        this.widthDensity = gridWidth * speed / secCount;
        wipeNum = (int) (gridWidth * 5 / widthDensity) + 3;
        wipeWidth = (int) (wipeNum * widthDensity);
        lineNum = (int) ((width - pathLeftMargin - waveBg.getRightSpace()) / widthDensity);
    }


    private void setGain(float gain) {
        this.gain = gain;
        heightDensity = gridWidth * gain / maxNum;
        titleWidth = (int) (gridWidth * 5);
        titleHeight = (int) (gridWidth * gain);
        titleLeftMargin = SizeUtils.dp2px(5) + paddingLeft;
        pathLeftMargin = titleLeftMargin + titleWidth + SizeUtils.dp2px(6);
    }

    private void drawTitle(Canvas canvas, int index, String title) {
        mTitlePaint.setColor(color_pace);
        mTitlePaint.setTextSize(SizeUtils.sp2px(12));
        mTitlePaint.setStrokeWidth(SizeUtils.dp2px(1));
        float top = (childHeight - titleHeight) / 2.0f;
        float bottom = (childHeight + titleHeight) / 2.0f;
        float startX = titleLeftMargin + paddingLeft;
        float offset = index * childHeight + padddingTop;
        bottom += offset;
        top += offset;
        Path path = new Path();
        path.moveTo(startX, bottom);
        startX += titleWidth / 4.0f;
        path.lineTo(startX, bottom);
        path.lineTo(startX, top);
        startX += titleWidth * 0.5f;
        path.lineTo(startX, top);
        path.lineTo(startX, bottom);
        startX += titleWidth / 4.0f;
        path.lineTo(startX, bottom);
        canvas.drawPath(path, mTitlePaint);
        if (title != null) {
            mTitlePaint.setStrokeWidth(1);
            float textWidth = mTitlePaint.measureText(title);
            float textHeiht = mTitlePaint.getTextSize();
            canvas.drawText(title, (titleWidth - textWidth) / 2 + titleLeftMargin + paddingLeft, offset + childHeight / 2 + textHeiht / 2, mTitlePaint);
        }
    }


    public void setWaveInfos(String[] titles) {
//        int maxSize = WaveViewConfig.secCount * 10 > lineNum ? WaveViewConfig.secCount * 10 : lineNum;
//        if (maxSize == lineNum) {
//            maxSize = (int) (lineNum * 1.5f);
//        }
        if (waveInfos.size() == 0) {
            for (String title : titles) {
                WaveInfo e = new WaveInfo(title);
//                e.setMaxReMainSize(maxSize);
                waveInfos.add(e);
            }
        } else {
            for (int i = 0; i < waveInfos.size(); i++) {
                waveInfos.get(i).title = titles[i];
            }
        }

    }


    public void add(short[] arr) {
        if (!isBuild) {
            return;
        }
        for (int i = 0; i < waveInfos.size(); i++) {
            WaveInfo waveInfo = waveInfos.get(i);
            waveInfo.addNum(arr[i]);
        }
        hasData = true;
        total++;
    }

    /**
     * @param startIndex 不包含 该像素不会被擦除
     * @param now        包含
     */
    private void wipeBitmap(int startIndex, int now) {
        int startX;
        if (startIndex != 0) {
            startX = (int) (getStartX(startIndex)) + 1;
        } else {
            startX = (int) (getStartX(0) - 1 / widthDensity);
        }
//        int startX = (int) getStartX(startIndex) ;
        int width = (int) (wipeWidth + (now - startIndex) * widthDensity);
        if (startX + width > bitmap.getWidth()) {
            width = Math.min(bitmap.getWidth() - startX, width);
        }
        if (width > 0) {
            if (pix.length < width * height) {
                pix = new int[width * height];
            }
            bitmap.setPixels(pix, 0, width, startX, 0, width, height);
        }
    }

//    int max;
//    int min;

    private float getStartY(int data, float childIndex) {

        float maxHeight = gain * gridWidth * 2;
        float baseY = (childIndex * childHeight + childHeight / 2) + padddingTop;
        float ry = heightDensity * data;
        if (ry > 0) {
            ry = Math.min(maxHeight, ry);
        } else {
            ry = Math.max(-maxHeight, ry);
        }
        return baseY - ry;
    }

    private float getStartX(int index) {
        int i = index % lineNum;
        return i * widthDensity + pathLeftMargin;
    }

    public void setColors(int[] defaultColors) {
        this.colors = defaultColors;
        color_pace = defaultColors[3];
        color = defaultColors[4];
    }

    /**
     * 一页的话显示12条导联，支持双击放大；
     * 2页就没必要了
     *
     * @param config
     */
    private void initConfig(WaveViewConfig config) {
        this.width = getMeasuredWidth();
        this.height = getMeasuredHeight();
        if (width == 0 || height == 0) {
            return;
        }
        pix = new int[0];
        setColors(WaveViewConfig.defaultColors);//1设置背景色
        if (config != null)
            touchAble = config.getPagesize() == 1;
        createBitmapBg(titles.length);//2 创建背景bitmap
        if (config != null) {
            setWidthDensity(config.getSpeed(), config.getSecCount());//4 设置一行显示数和密度
            setGain(config.getGain());//设置增益
            initWaveInfo(titles);//5初始化波形配置
        }
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (isAvaiable) {
                    long l1 = System.currentTimeMillis();
                    if (hasData && isBuild) {
                        drawInBackGround();
                    }
                    long l2 = System.currentTimeMillis();
                    long delta = l2 - l1;
                    if (delta >= 30)
                        Log.e("---", "run: " + delta);
                    if (delta < 1000.0f / frame) {
                        try {
                            Thread.sleep((long) (1000.0f / frame - delta));
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
        if (bitmap == null) {
            bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            canvasTmp = new Canvas(bitmap);
            canvasTmp.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        }
        isAvaiable = true;
        thread.start();
    }

    private void drawInBackGround() {
        if (!isBuild) {
            //build notCalled
            return;
        }
        if (needDrawTitle) {
            drawTitles();
            needDrawTitle = false;
        }
        doDiffer(lastIndex, total - 1);
    }

    GestureHelper helper;

    public void setDragEnable(boolean dragEnable) {
        if (helper == null && dragEnable) {
            helper = new GestureHelper(this);
            helper.setEnable(dragEnable);
        } else if (helper != null)
            helper.setEnable(false);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (helper != null && helper.isEnable()) {
            if (getParent() != null) {
                getParent().requestDisallowInterceptTouchEvent(true);
            }
            return helper.process(event);
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (helper != null && helper.isEnable()) {
            canvas.translate(helper.getOffsetX(), helper.getOffsetY());
            canvas.scale(helper.getScale(), helper.getScale(), getWidth() / 2f, getHeight() / 2f);
        }
        if (bitmapBg != null) {
            canvas.drawBitmap(bitmapBg, 0, 0, null);
        }
        if (bitmap != null)
            canvas.drawBitmap(bitmap, 0, 0, null);
    }

    private void drawTitles() {
        for (int i = 0; i < waveInfos.size(); i++) {
            drawTitle(canvasTmp, i, waveInfos.get(i).title);
        }
    }

    private void doDiffer(int before, int now) {
        if (before < 0 || now < 0 || before >= now) {
            return;
        }
        mPathPaint.setColor(color);
        if (before == 0) {
            restore(now);
            lastIndex = now;
            return;
        }
        lastIndex = now;
        if (now % lineNum >= before % lineNum) {
            //没有超过一行
            wipeBitmap(before, now);
            for (int j = 0; j < waveInfos.size(); j++) {
                WaveInfo waveInfo = waveInfos.get(j);
                Path path = waveInfo.getPath();
                path.reset();
                path.moveTo(getStartX(before), getStartY(waveInfo.getMapperData(before), j));
                for (int i = before + 1; i <= now; i++) {
                    path.lineTo(getStartX(i), getStartY(waveInfo.getMapperData(i), j));
                }
                canvasTmp.drawPath(path, mPathPaint);
            }
        } else {
            wipeBitmap(before % lineNum, lineNum);
            wipeBitmap(0, now % lineNum);
            for (int j = 0; j < waveInfos.size(); j++) {
                WaveInfo waveInfo = waveInfos.get(j);
                Path path = waveInfo.getPath();
                path.reset();
                path.moveTo(getStartX(before), getStartY(waveInfo.getMapperData(before), j));
                for (int i = before; i < before + lineNum - before % lineNum; i++) {
                    path.lineTo(getStartX(i), getStartY(waveInfo.getMapperData(i), j));
                }
                canvasTmp.drawPath(path, mPathPaint);
                path.reset();
                path.moveTo(getStartX(0), getStartY(waveInfo.getMapperData(0), j));
                for (int i = now - now % lineNum; i <= now; i++) {
                    path.lineTo(getStartX(i), getStartY(waveInfo.getMapperData(i), j));
                }
                canvasTmp.drawPath(path, mPathPaint);
            }
        }
        postInvalidate();
    }

    public void restore(int lastIndex) {
        bitmap.eraseColor(Color.TRANSPARENT);
        for (int i = 0; i < waveInfos.size(); i++) {
            drawTitle(canvasTmp, i, waveInfos.get(i).title);
            drawOnData(waveInfos.get(i), i, lastIndex);
        }
        postInvalidate();
    }

    private void drawOnData(WaveInfo info, int childIndex, int startIndex) {
//        Log.e("===", "drawOnData:now " + startIndex);
        if (total <= 0) {
            return;
        }
        /**
         * 画前半段
         */
        Path path = info.getPath();
        path.reset();
        int mol = startIndex % lineNum;
        path.moveTo(getStartX(0), getStartY(info.getMapperData(0), childIndex));
        for (int i = 0; i <= mol; i++) {
            path.lineTo(getStartX(i), getStartY(info.getMapperData(i), childIndex));
        }
        canvasTmp.drawPath(path, mPathPaint);

        if (startIndex > lineNum) {//超过一行 画后半段
            path.reset();
            int start = mol + wipeWidth + 1;
            path.moveTo(getStartX(start), getStartY(info.getMapperData(start), childIndex));
            for (int i = start; i < lineNum; i++) {
                path.lineTo(getStartX(i), getStartY(info.getMapperData(i), childIndex));
            }
            canvasTmp.drawPath(path, mPathPaint);
        }
    }

    boolean isBuild;

    public void reBuild(WaveViewConfig config) {
        total = 0;
        pause();
//        setColors(WaveViewConfig.defaultColors);
//        createBitmapBg(titles.length, config.getPagesize() == 1);
//        setGain(config.getGain());
//        setWidthDensity(config.getSpeed(), config.getSecCount());
        lastIndex = 0;
        for (WaveInfo waveInfo : waveInfos) {
            waveInfo.clear();
        }
        bitmap.eraseColor(Color.TRANSPARENT);
        initConfig(config);
        drawTitles();
        needDrawTitle = true;
        isBuild = true;
    }

    public void resume() {
        if (isBuild) {
            initConfig(null);
        }
    }

    public Bitmap getResultBitmap(List<List<Short>> data, WaveViewConfig config, String[] titles) {
        int size = Math.min(data.size(), WaveViewConfig.secCount * 10);//只画10秒数据
        size = (int) Math.max(Math.min(lineNum * 1.5f, data.get(0).size()), size);//10s不够一屏，画1.5屏
        build(config, titles);
        width = (int) (widthDensity * size);
        Bitmap bitmapAll = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        lineNum = (int) ((width - pathLeftMargin - waveBg.getRightSpace()) / widthDensity);
        wipeWidth = 0;
        Canvas canvas1 = new Canvas(bitmapAll);
        waveBg = new WaveBg();
        waveBg.setyNumber(width < height && titles.length == 12 ? 240 : 120);
        waveBg.setBounds(0, 0, width, height);
        gridWidth = waveBg.getGridWidth();
        RectF drawRect = waveBg.getDrawRect();
        paddingLeft = drawRect.left;
        padddingTop = drawRect.top;
        childHeight = drawRect.height() / titles.length;
        waveBg.draw(canvas1);
        for (int i = 0; i < titles.length; i++) {
            drawTitle(canvas1, i, titles[i]);
        }
        drawAll(canvas1, data);
        return bitmapAll;
    }

    private void drawAll(Canvas canvasTmp, List<List<Short>> data) {

        Path path = new Path();
        int size = Math.min(data.size(), WaveViewConfig.secCount * 10);//只画10,秒数据
        size = Math.max(lineNum, size);
        for (int i = 0; i < data.size(); i++) {
            List<Short> shorts = data.get(i);
            path.reset();
            path.moveTo(getStartX(0), getStartY(shorts.get(i), i));
            for (int j = 0; j < size; j++) {
                path.lineTo(getStartX(j), getStartY(shorts.get(j), i));
            }
            canvasTmp.drawPath(path, mPathPaint);
        }
    }

    public void build(WaveViewConfig config, String[] titles) {
        this.titles = titles;
        initConfig(config);
        needDrawTitle = true;
        isBuild = true;
        drawTitles();
        postInvalidate();
    }

    private void initWaveInfo(String[] titles) {
        if (titles != null) {
            setWaveInfos(titles);
        }
    }

    /**
     * @param length
     * @param touchAble 是否支持双击放大
     */
    private void createBitmapBg(int length) {
        if (waveBg == null)
            waveBg = new WaveBg();
        waveBg.setyNumber(width < height && length == 12 ? 240 : 120);
        waveBg.setBounds(0, 0, width, height);
        if (getParent() != null && !touchAble) {
            ((ViewGroup) getParent()).setBackground(waveBg);
        } else if (bitmapBg == null) {
            bitmapBg = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            Canvas canvasBg = new Canvas(bitmapBg);
            waveBg.draw(canvasBg);
            setDragEnable(true);
        }
        gridWidth = waveBg.getGridWidth();
        RectF drawRect = waveBg.getDrawRect();
        paddingLeft = drawRect.left;
        padddingTop = drawRect.top;
        childHeight = drawRect.height() / length;
        waveBg.setColor(colors[0]);
        waveBg.setColors1(colors[1]);
        waveBg.setColors2(colors[2]);
    }


    PrepareListener listener;

    public void setListener(PrepareListener listener) {
        this.listener = listener;
    }

    public List<WaveInfo> getWaveInfos() {
        return waveInfos;
    }

    public interface PrepareListener {
        void onReady();
//        void onDestroy();
    }

    public void pause() {
        if (!isAvaiable) {
            // draw thread has not be created yet
            return;
        }
        try {
            isAvaiable = false;
            thread.join();//等待绘制线程执行完
            lastIndex = 0;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


}
