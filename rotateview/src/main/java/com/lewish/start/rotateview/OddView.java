package com.lewish.start.rotateview;

import android.content.Context;
import android.graphics.Canvas;
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
    private Path mPathArc1;
    private Path mPathArc2;
    private Path mPathArc3;
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
        mPathArc1 = new Path();
        mPathArc2 = new Path();
        mPathArc3 = new Path();
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
        int controlPointOffset = -250;
        int firstCircleOffset = -50;
        int secondCircleOffset = -150;
        //画最里边的
        int firstStartPointX = realWidth + firstCircleOffset;
        int firstControlPointX = realWidth + controlPointOffset;
        mPathArc1.moveTo(firstStartPointX, realHeight);
        mPathArc1.lineTo(realWidth, realHeight);
        mPathArc1.lineTo(realWidth, 0);
        mPathArc1.lineTo(firstStartPointX, 0);
        mPathArc1.quadTo(firstControlPointX, realHeight / 2, firstStartPointX, realHeight);
        mPathArc1.close();
        canvas.drawPath(mPathArc1, mPaint);
        //画中间的
        int secondStartPointX = firstStartPointX + secondCircleOffset;
        int secondControlPointX = firstControlPointX + secondCircleOffset;
        mPathArc2.moveTo(secondStartPointX, realHeight);
        mPathArc2.lineTo(firstStartPointX, realHeight);
        mPathArc2.quadTo(firstControlPointX, realHeight / 2, firstStartPointX, 0);
        mPathArc2.lineTo(secondStartPointX, 0);
        mPathArc2.quadTo(secondControlPointX, realHeight / 2, secondStartPointX, realHeight);
        mPathArc2.close();
        canvas.drawPath(mPathArc2, mPaint);
        //画最左边的
//        for (int i = 0; i < 3; i++) {
//            mPaint.setColor(Color.BLACK);
//            int shlValue = i == 2 ? 250 : 200;
//            int pointX = realWidth - 50 - shlValue * i;
//            int pointY = realHeight;
//            int endPointY = 0;
//            int FlagPointX = realWidth - 250 - shlValue * i;
//            int FlagPointY = realHeight / 2;
//            mPathArc1.moveTo(pointX, pointY);
//            mPathArc1.quadTo(FlagPointX, FlagPointY, pointX, endPointY);
//            mPathArc1.lineTo(realWidth, 0);
//            mPathArc1.lineTo(realWidth, realHeight);
//            mPathArc1.lineTo(pointX, pointY);
//            mPathArc1.close();
//            canvas.drawPath(mPathArc1, mPaint);
//            if (i == 1) {
//                //画刻度
//                mPathScale.moveTo(pointX, pointY);
//                mPathScale.quadTo(FlagPointX, FlagPointY, pointX, endPointY);
//                mPathMeasureScale.setPath(mPathScale, false);
//                for (int j = 0; j < 10; j++) {
//                    float distance = mPathMeasureScale.getLength() * 0.1f * (j + 1);
//                    mPathMeasureScale.getPosTan(distance, mScalePos, mScaleTan);
//                    mPathMeasureScale.getSegment(0, distance, mPathScale, true);
//                    canvas.drawLine(mScalePos[0], mScalePos[1], mScalePos[0] - (j % 2 == 0 ? 40 : 20), mScalePos[1], mPaint);
//                }
//                //画数字
//                mPathNum.moveTo(pointX-100, pointY);
//                mPathNum.quadTo(FlagPointX-100, FlagPointY, pointX-100, endPointY);
//                mPathMeasureNum.setPath(mPathNum, false);
//                for (int k = 0;k<5;k++){
//                    float distance = mPathMeasureNum.getLength() * 0.2f * (k + 1);
//                    mPathMeasureNum.getPosTan(distance,mNumPos,mNumTan);
//                    mPathMeasureNum.getSegment(0,distance,mPathNum,true);
//                    mPaint.setTextSize(40);
//                    canvas.drawText(500*(k+1)+"",mNumPos[0]-100,mNumPos[1]+20,mPaint);
//                }
//            }
//        }

    }
}
