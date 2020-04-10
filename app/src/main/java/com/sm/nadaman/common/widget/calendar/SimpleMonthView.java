package com.sm.nadaman.common.widget.calendar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.View;

import com.haibin.calendarview.Calendar;
import com.haibin.calendarview.MonthView;
import com.scwang.smartrefresh.layout.util.DensityUtil;


public class SimpleMonthView extends MonthView {

    protected Paint mPaint = new Paint();

    private int mPadding;

    /**
     * 圆点半径
     */
    private float mPointRadius;

    /**
     * 背景圆点
     */
    private Paint mPointPaint = new Paint();

    public SimpleMonthView(Context context) {
        super(context);
        //兼容硬件加速无效的代码
        setLayerType(View.LAYER_TYPE_SOFTWARE, mSelectedPaint);
        //4.0以上硬件加速会导致无效
//        mSelectedPaint.setMaskFilter(new BlurMaskFilter(25, BlurMaskFilter.Blur.SOLID));
        mPointPaint.setAntiAlias(true);
        mPointPaint.setStyle(Paint.Style.FILL);
        mPointPaint.setTextAlign(Paint.Align.CENTER);
        mPointPaint.setColor(0xFFE50214);

        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setStrokeWidth(2);
        mPaint.setColor(0xFFFFFFFF);
        mPointRadius = dipToPx(getContext(), 3);

        mPadding = dipToPx(getContext(), 2);
    }

    @Override
    protected void onPreviewHook() {
//        mRadius = Math.min(mItemWidth, mItemHeight) / 5 * 2+10;
        mSchemePaint.setStyle(Paint.Style.STROKE);
    }

    @Override
    protected void onLoopStart(int x, int y) {

    }

    @Override
    protected boolean onDrawSelected(Canvas canvas, Calendar calendar, int x, int y, boolean hasScheme) {
        RectF rect = new RectF(x + mPadding, y + mPadding, x + mItemWidth - mPadding, y + mItemHeight - mPadding);
        if (hasScheme) {
            canvas.drawRoundRect(rect, DensityUtil.dp2px(5), DensityUtil.dp2px(5), mSelectedPaint);
        } else {
            canvas.drawRoundRect(rect, DensityUtil.dp2px(5), DensityUtil.dp2px(5), mSelectedPaint);
        }
        return true;
    }

    @Override
    protected void onDrawScheme(Canvas canvas, Calendar calendar, int x, int y) {
    }

    @Override
    protected void onDrawText(Canvas canvas, Calendar calendar, int x, int y, boolean hasScheme, boolean isSelected) {
        float baselineY = mTextBaseLine + y;
        int cx = x + mItemWidth / 2;

        if (!isSelected) {
            RectF rect = new RectF(x + mPadding, y + mPadding, x + mItemWidth - mPadding, y + mItemHeight - mPadding);
            canvas.drawRoundRect(rect, DensityUtil.dp2px(5), DensityUtil.dp2px(5), mPaint);
        }
        if (isSelected) {
            canvas.drawText(String.valueOf(calendar.getDay()),
                    cx,
                    baselineY,
                    mSelectTextPaint);
        } else if (hasScheme) {

            canvas.drawCircle((x + mItemWidth) - (5 * mPadding), y + (5 * mPadding), mPointRadius, mPointPaint);
            canvas.drawText(String.valueOf(calendar.getDay()),
                    cx,
                    baselineY,
                    calendar.isCurrentMonth() ? mCurMonthTextPaint : mOtherMonthTextPaint);

        } else {
            canvas.drawText(String.valueOf(calendar.getDay()), cx, baselineY,
                    calendar.isCurrentMonth() ? mCurMonthTextPaint : mOtherMonthTextPaint);
        }
    }

    /**
     * dp转px
     *
     * @param context context
     * @param dpValue dp
     * @return px
     */
    private static int dipToPx(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
