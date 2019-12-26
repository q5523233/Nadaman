package com.sm.nadaman.common.ecg;

/**
 * Created by sm on 2018/4/19.
 */

public class Line {
    private float startX;
    private float startY;
    private float endX;
    private float endY;

    public Line() {
    }

    public Line(int sx, int sy, int ex, int ey) {
        startX = sx;
        startY = sy;
        endX = ex;
        endY = ey;
    }

    public float getStartX() {
        return startX;
    }

    public void setStartX(float startX) {
        this.startX = startX;
    }

    public float getStartY() {
        return startY;
    }

    public void setStartY(float startY) {
        this.startY = startY;
    }

    public float getEndX() {
        return endX;
    }

    public void setEndX(float endX) {
        this.endX = endX;
    }

    public float getEndY() {
        return endY;
    }

    public void setEndY(float endY) {
        this.endY = endY;
    }

    @Override
    public String toString() {
        return "Line{" +
                "startX=" + startX +
                ", startY=" + startY +
                ", endX=" + endX +
                ", endY=" + endY +
                '}';
    }
}
