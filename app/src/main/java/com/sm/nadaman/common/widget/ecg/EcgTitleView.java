package com.sm.nadaman.common.widget.ecg;

import android.graphics.*;
import android.graphics.drawable.Drawable;
import com.scwang.smartrefresh.layout.util.DensityUtil;

public class EcgTitleView extends Drawable {
    Paint paint;
    float bitmapWidth;
    int bitmapHeight;
    String drawText;
    private Rect bounds;
    private float textWidth;
    private int textHeight;

    public EcgTitleView(int bitmapWidth, int bitmapHeight, float gridWidth, String title) {
        locatHeight = gridWidth;
        this.bitmapHeight = bitmapHeight;
        this.bitmapWidth = bitmapWidth;
        this.drawText = title;
        init();
    }

    public void setPaintColor(int color) {
        paint.setColor(color);
    }

    private void init() {
        /**
         *              Path path = new Path();
         *             path.moveTo(bitmapWidth / 8 * 2, (bitmapHeight - locatHeight) / 2 + locatHeight);
         *             path.lineTo(bitmapWidth / 8 * 3, (bitmapHeight - locatHeight) / 2 + locatHeight);
         *             path.lineTo(bitmapWidth / 8 * 3, (bitmapHeight - locatHeight) / 2);
         *             path.lineTo(bitmapWidth / 8 * 5, (bitmapHeight - locatHeight) / 2);
         *             path.lineTo(bitmapWidth / 8 * 5, (bitmapHeight - locatHeight) / 2 + locatHeight);
         *             path.lineTo(bitmapWidth / 8 * 6, (bitmapHeight - locatHeight) / 2 + locatHeight);
         */
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStrokeWidth(DensityUtil.dp2px(1));
        paint.setStyle(Paint.Style.STROKE);
        path = new Path();
        bounds = new Rect();
        paint.getTextBounds(drawText, 0, drawText.length() - 1, bounds);
        textWidth = bounds.right - bounds.left;
        textHeight = bounds.bottom - bounds.top;
        if (bitmapWidth < textWidth) {
            bitmapWidth = textWidth;
        }
    }


    Path path;
    private float locatHeight;

    @Override
    public void draw(Canvas canvas) {
        if (bitmapWidth <= 0 || bitmapHeight <= 0) {
            return;
        }
        path.moveTo(bitmapWidth / 8 * 2, (bitmapHeight - locatHeight) / 2 + locatHeight);
        path.lineTo(bitmapWidth / 8 * 3, (bitmapHeight - locatHeight) / 2 + locatHeight);
        path.lineTo(bitmapWidth / 8 * 3, (bitmapHeight - locatHeight) / 2);
        path.lineTo(bitmapWidth / 8 * 5, (bitmapHeight - locatHeight) / 2);
        path.lineTo(bitmapWidth / 8 * 5, (bitmapHeight - locatHeight) / 2 + locatHeight);
        path.lineTo(bitmapWidth / 8 * 6, (bitmapHeight - locatHeight) / 2 + locatHeight);
        canvas.drawPath(path, paint);

        canvas.drawText(drawText, (bitmapWidth - textWidth) / 2, (bitmapHeight + textHeight) / 2, paint);
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
}
