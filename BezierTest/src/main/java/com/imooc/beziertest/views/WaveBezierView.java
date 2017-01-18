package com.imooc.beziertest.views;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

public class WaveBezierView extends View implements View.OnClickListener {

    private float mStartPointX;
    private float mStartPointY;

    private float mEndPointX;
    private float mEndPointY;

    private float mFlagPointOneX;
    private float mFlagPointOneY;
    private float mFlagPointTwoX;
    private float mFlagPointTwoY;

    private Path mPath;

    private Paint mBezierPaint;
    private Paint mFlagPaint;
    private Paint mFlagTextPaint;

    private int mScreenHeight;
    private int mScreenWidth;

    private int mWaveMidHeight;
    private int mWaveLength;
    private int mWaveCount;
    private int mOffset;

    private ValueAnimator mValueAnimator;

    public WaveBezierView(Context context) {
        super(context);
    }

    public WaveBezierView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mBezierPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBezierPaint.setColor(Color.LTGRAY);
        mBezierPaint.setStrokeWidth(8);
        mBezierPaint.setStyle(Paint.Style.FILL_AND_STROKE);

        mWaveLength = 1000;
    }

    public WaveBezierView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mPath = new Path();
        setOnClickListener(this);

        mScreenHeight = h;
        mScreenWidth = w;
        mWaveMidHeight = h / 2;

        mWaveCount = (int) Math.round(mScreenWidth / mWaveLength + 1.5);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPath.reset();
        mPath.moveTo(-mWaveLength + mOffset, mWaveMidHeight);
        for (int i = 0; i < mWaveCount; i++) {
            mPath.quadTo(-mWaveLength * 3 / 4 + i * mWaveLength + mOffset, mWaveMidHeight + 60, -mWaveLength / 2 + i * mWaveLength + mOffset, mWaveMidHeight);
            mPath.quadTo(-mWaveLength / 4 + i * mWaveLength + mOffset, mWaveMidHeight - 60, i * mWaveLength + mOffset, mWaveMidHeight);
        }
        mPath.lineTo(mScreenWidth, mScreenHeight);
        mPath.lineTo(0, mScreenHeight);
        mPath.close();
        canvas.drawPath(mPath, mBezierPaint);
    }

    @Override
    public void onClick(View view) {
        mValueAnimator = ValueAnimator.ofInt(0, mWaveLength);
        mValueAnimator.setDuration(1000);
        mValueAnimator.setRepeatCount(ValueAnimator.INFINITE);
        mValueAnimator.setInterpolator(new LinearInterpolator());
        mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                mOffset = (int) valueAnimator.getAnimatedValue();
                invalidate();
            }
        });
        mValueAnimator.start();
    }
}
