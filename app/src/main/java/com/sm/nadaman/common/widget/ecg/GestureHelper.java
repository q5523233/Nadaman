package com.sm.nadaman.common.widget.ecg;

import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.widget.OverScroller;

import androidx.core.view.GestureDetectorCompat;

public class GestureHelper implements GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener, Runnable, ScaleGestureDetector.OnScaleGestureListener {
    private GestureDetectorCompat gestureDetectorCompat;
    private WaveTView waveTView;
    private boolean enable = true;
    private float maxScale = 2;
    private float smallScale = 1;
    private float currScale = 1;
    private boolean isBig;
    private float offsetX;
    private float offsetY;
    private float maxX;
    private float maxY;
    private OverScroller overScroller;
    private ScaleGestureDetector scaleGestureDetector;

    GestureHelper(WaveTView waveTView) {
        this.waveTView = waveTView;
        gestureDetectorCompat = new GestureDetectorCompat(waveTView.getContext(), this);
        overScroller = new OverScroller(waveTView.getContext());
        scaleGestureDetector = new ScaleGestureDetector(waveTView.getContext(), this);
    }

    float getOffsetX() {
        return offsetX;
    }

    float getOffsetY() {
        return offsetY;
    }

    float getScale() {
        return currScale;
    }


    void setEnable(boolean enable) {
        this.enable = enable;
    }

    boolean isEnable() {
        return enable;
    }

    public boolean process(MotionEvent event) {
        if (!enable) {
            return false;
        }
        maxX = (waveTView.getWidth() * maxScale - waveTView.getWidth()) / 2f;
        maxY = (waveTView.getHeight() * maxScale - waveTView.getHeight()) / 2f;
//        boolean a=scaleGestureDetector.onTouchEvent(event);
//        if (!scaleGestureDetector.isInProgress()) {
            return gestureDetectorCompat.onTouchEvent(event);
//        }
//        return a;
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return true;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        if (isBig) {
            offsetX -= distanceX;
            offsetX = Math.min(maxX, offsetX);
            offsetX = Math.max(-maxX, offsetX);
            offsetY -= distanceY;
            offsetY = Math.min(maxY, offsetY);
            offsetY = Math.max(-maxY, offsetY);
            waveTView.invalidate();
        }
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        if (!isBig) {
            return false;
        }
        overScroller.fling(0, 0, (int) velocityX, (int) velocityY, (int) -maxX, (int) maxX, (int) -maxY, (int) maxY);
        waveTView.postOnAnimation(this);
        return false;
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onDoubleTap(MotionEvent e) {
        isBig = !isBig;
        currScale = isBig ? maxScale : smallScale;
        offsetX = isBig ? maxX : 0;
        offsetY = isBig ? maxY : 0;
        waveTView.invalidate();
        return false;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent e) {
        return false;
    }

    @Override
    public void run() {
        if (overScroller.computeScrollOffset()) {
            int currX = overScroller.getCurrX();
            int currY = overScroller.getCurrY();
            offsetX += currX;
            offsetY += currY;
            this.offsetX = Math.min(offsetX, maxX);
            this.offsetX = Math.max(offsetX, -maxX);
            this.offsetY = Math.min(offsetY, maxY);
            this.offsetY = Math.max(offsetY, -maxY);
            waveTView.postOnAnimation(this);
            waveTView.invalidate();

        }
    }

    float lastScale;

    @Override
    public boolean onScale(ScaleGestureDetector detector) {
        currScale = lastScale * detector.getScaleFactor();
        currScale=Math.max(smallScale,currScale);
        currScale=Math.min(maxScale,currScale);
        waveTView.invalidate();
        return true;
    }

    @Override
    public boolean onScaleBegin(ScaleGestureDetector detector) {
        lastScale = currScale;
        return true;
    }

    @Override
    public void onScaleEnd(ScaleGestureDetector detector) {

    }
}
