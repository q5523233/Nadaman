package com.sm.nadaman.common.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

public class BigBitmapLoader extends View {
    private Bitmap bitmap;
    private Paint paint;
    private Rect rect;
    private Rect dst;

    public BigBitmapLoader(Context context) {
        super(context);
    }

    public BigBitmapLoader(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
        rect.set(0, 0, getWidth(), getHeight());
        dst = new Rect(0, 0, getWidth(), getHeight());
        invalidate();
    }

    private void init() {
        paint = new Paint();
        paint.setAntiAlias(true);
        rect = new Rect();
    }

    float dx;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (bitmap == null) {
            return;
        }
        canvas.drawBitmap(bitmap, rect, dst, null);
    }

    float px;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                px = event.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                float x = event.getX();
                float dx = x - px;
                if (Math.abs(dx) > ViewConfiguration.get(getContext()).getScaledTouchSlop()) {
                    rect.left -= dx;
                    rect.left = Math.max(0, rect.left);
                    rect.left = Math.min(bitmap.getWidth() - getWidth(), rect.left);
                    rect.right = rect.left+getWidth();
                    invalidate();
                }
                px = x;
                break;

        }
        return true;
    }
}
