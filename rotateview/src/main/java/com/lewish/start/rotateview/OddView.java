package com.lewish.start.rotateview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * author: sundong
 * created at 2017/11/23 20:29
 */

public class OddView extends View {
    private Paint mPaint;
    private Path mPathArc;
    private Path mPathScale;
    private Path mPathNum;
    private PathMeasure mPathMeasureScale;
    private float[] mScalePos;
    private float[] mScaleTan;
    private PathMeasure mPathMeasureNum;
    private float[] mNumPos;
    private float[] mNumTan;

    public OddView(Context context) {
        super(context, null);
    }

    public OddView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public OddView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(5);
        //Arc
        mPathArc = new Path();
        //刻度
        mPathScale = new Path();
        mPathMeasureScale = new PathMeasure();
        mScalePos = new float[2];
        mScaleTan = new float[2];
        //数字
        mPathNum = new Path();
        mPathMeasureNum = new PathMeasure();
        mNumPos = new float[2];
        mNumTan = new float[2];
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int realWidth = getWidth() - getPaddingLeft() - getPaddingRight();
        int realHeight = getHeight() - getPaddingTop() - getPaddingBottom();
        for (int i = 0; i < 3; i++) {
            mPaint.setColor(Color.BLACK);
            int shlValue = i == 2 ? 250 : 200;
            int pointX = realWidth - 50 - shlValue * i;
            int startPointY = realHeight;
            int endPointY = 0;
            int FlagPointX = realWidth - 250 - shlValue * i;
            int FlagPointY = realHeight / 2;
            mPathArc.moveTo(pointX, startPointY);
            mPathArc.quadTo(FlagPointX, FlagPointY, pointX, endPointY);
            mPathArc.lineTo(realWidth, 0);
            mPathArc.lineTo(realWidth, realHeight);
            mPathArc.lineTo(pointX, startPointY);
            mPathArc.close();
            canvas.drawPath(mPathArc, mPaint);
            if (i == 1) {
                //画刻度
                mPathScale.moveTo(pointX, startPointY);
                mPathScale.quadTo(FlagPointX, FlagPointY, pointX, endPointY);
                mPathMeasureScale.setPath(mPathScale, false);
                for (int j = 0; j < 10; j++) {
                    float distance = mPathMeasureScale.getLength() * 0.1f * (j + 1);
                    mPathMeasureScale.getPosTan(distance, mScalePos, mScaleTan);
                    mPathMeasureScale.getSegment(0, distance, mPathScale, true);
                    canvas.drawLine(mScalePos[0], mScalePos[1], mScalePos[0] - (j % 2 == 0 ? 40 : 20), mScalePos[1], mPaint);
                }
                //画数字
                mPathNum.moveTo(pointX-100, startPointY);
                mPathNum.quadTo(FlagPointX-100, FlagPointY, pointX-100, endPointY);
                mPathMeasureNum.setPath(mPathNum, false);
                for (int k = 0;k<5;k++){
                    float distance = mPathMeasureNum.getLength() * 0.2f * (k + 1);
                    mPathMeasureNum.getPosTan(distance,mNumPos,mNumTan);
                    mPathMeasureNum.getSegment(0,distance,mPathNum,true);
                    mPaint.setTextSize(40);
                    canvas.drawText(500*(k+1)+"",mNumPos[0]-100,mNumPos[1]+20,mPaint);
                }
            }
        }

    }
}
