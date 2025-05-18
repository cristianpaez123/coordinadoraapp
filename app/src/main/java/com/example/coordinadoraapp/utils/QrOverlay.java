package com.example.coordinadoraapp.utils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class QrOverlay extends View {

    private RectF guideRect;
    private Paint paint;

    public QrOverlay(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setStrokeWidth(6);
        paint.setStyle(Paint.Style.STROKE);
        paint.setAntiAlias(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float side = Math.min(getWidth(), getHeight()) * 0.8f;
        float left = (getWidth() - side) / 2f;
        float top = (getHeight() - side) / 2f;
        float right = left + side;
        float bottom = top + side;

        guideRect = new RectF(left, top, right, bottom);
        Log.d("QrOverlay", "GuideRect: " + guideRect.toString());
        canvas.drawRect(guideRect, paint);
    }

    public void setBorderColor(int color) {
        paint.setColor(color);
        invalidate();
    }

    public RectF getGuideRect() {
        return guideRect;
    }
}