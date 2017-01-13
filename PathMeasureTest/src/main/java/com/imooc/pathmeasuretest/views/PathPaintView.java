package com.imooc.pathmeasuretest.views;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.PathMeasure;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

public class PathPaintView extends View {

    private Path mPath;
    private Paint mPaint;
    private float mLength;
    private float mAnimValue;
    private PathEffect mEffect;

    private PathMeasure mPathMeasure;

    public PathPaintView(Context context) {
        super(context);
    }

    public PathPaintView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(5);

        mPath = new Path();

        mPath.moveTo(100, 100);
        mPath.lineTo(100, 500);
        mPath.lineTo(400, 300);
        mPath.close();

        mPathMeasure = new PathMeasure();
        mPathMeasure.setPath(mPath, true);

        mLength = mPathMeasure.getLength();

        ValueAnimator animator = ValueAnimator.ofFloat(1, 0);
        animator.setDuration(2000);
        animator.setInterpolator(new LinearInterpolator());
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                mAnimValue = (float) valueAnimator.getAnimatedValue();
                mEffect = new DashPathEffect(new float[]{mLength, mLength}, mLength * mAnimValue);
                mPaint.setPathEffect(mEffect);
                invalidate();
            }
        });
        animator.start();
    }

    public PathPaintView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawPath(mPath, mPaint);
    }
}
