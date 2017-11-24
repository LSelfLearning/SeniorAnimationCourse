package com.lewish.start.rotateview;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;

import java.util.List;

/**
 * author: sundong
 * created at 2017/11/23 20:29
 */

public class OddView extends View {
    private static final String TAG = "OddView";
    private static final String DEFAULT_RIGHTAREA_COLOR = "#4f4f4f";
    private static final String DEFAULT_MIDDLEAREA_COLOR = "#555555";
    private static final String DEFAULT_LEFTAREA_COLOR = "#4a4a4a";
    private static final String DEFAULT_SCALE_COLOR = "#525252";
    private static final String DEFAULT_SELECTED_COLOR = "#ffd401";
    private static final float DEFAULT_SCALE_MAX_LENGTH = 40;
    private static final int DEFAULT_SCALE_NUM = 10;

    private int rightAreaColor;
    private int middleAreaColor;
    private int leftAreaColor;
    private int scaleColor;
    private int selectedColor;

    private int scaleNum;//一屏有多少刻度
    private float scaleMaxLength;
    private int[] amountArr = {500, 1000, 1500, 2000, 2500};
    private List amountList;
    private Context mContext;
    private Paint mPaint;
    private Paint mAreaPaint;
    private Paint mScalePaint;

    private Path mRightArcPath;
    private Path mMiddleArcPath;
    private Path mLeftArcPath;

    private Path mScalePath;
    private Path mNumPath;
    private PathMeasure mScalePathMeasure;
    private float[] mScalePos;
    private PathMeasure mNumPathMeasure;
    private float[] mNumPos;
    private ValueAnimator mAnimator;
    private float mProgress;
    private boolean mIsCW;

    public OddView(Context context) {
        super(context, null);
    }

    public OddView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public OddView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        loadAttributeSet(context, attrs, defStyleAttr);
        initVariables();
    }

    private void loadAttributeSet(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs,
                R.styleable.OddView);
        rightAreaColor = typedArray.getColor(R.styleable.OddView_rightAreaColor, Color.parseColor(DEFAULT_RIGHTAREA_COLOR));
        middleAreaColor = typedArray.getColor(R.styleable.OddView_middleAreaColor, Color.parseColor(DEFAULT_MIDDLEAREA_COLOR));
        leftAreaColor = typedArray.getColor(R.styleable.OddView_leftAreaColor, Color.parseColor(DEFAULT_LEFTAREA_COLOR));
        selectedColor = typedArray.getColor(R.styleable.OddView_selectedColor, Color.parseColor(DEFAULT_SELECTED_COLOR));

        scaleColor = typedArray.getColor(R.styleable.OddView_scaleColor, Color.parseColor(DEFAULT_SCALE_COLOR));
        scaleNum = typedArray.getInteger(R.styleable.OddView_scaleNum, DEFAULT_SCALE_NUM);
        scaleMaxLength = typedArray.getDimension(R.styleable.OddView_scaleMaxLength, DEFAULT_SCALE_MAX_LENGTH);
        typedArray.recycle();
    }

    private void initVariables() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(5);

        mAreaPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mAreaPaint.setStyle(Paint.Style.FILL);
        mAreaPaint.setStrokeWidth(5);

        mScalePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mScalePaint.setStyle(Paint.Style.STROKE);
        mScalePaint.setColor(scaleColor);
        mScalePaint.setStrokeWidth(5);
        //Arc
        mRightArcPath = new Path();
        mMiddleArcPath = new Path();
        mLeftArcPath = new Path();
        //刻度
        mScalePath = new Path();
        mScalePathMeasure = new PathMeasure();
        mScalePos = new float[2];
        //数字
        mNumPath = new Path();
        mNumPathMeasure = new PathMeasure();
        mNumPos = new float[2];
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int realWidth = getWidth() - getPaddingLeft() - getPaddingRight();
        int realHeight = getHeight() - getPaddingTop() - getPaddingBottom();
        int controlPointOffset = -250;
        int rightArcOffset = -50;
        int middleArcOffset = -150;
        int leftArcOffset = -200;
        //画最右边的
        int rightStartPointX = realWidth + rightArcOffset;
        int rightControlPointX = realWidth + controlPointOffset;
        mRightArcPath.moveTo(rightStartPointX, realHeight);
        mRightArcPath.lineTo(realWidth, realHeight);
        mRightArcPath.lineTo(realWidth, 0);
        mRightArcPath.lineTo(rightStartPointX, 0);
        mRightArcPath.quadTo(rightControlPointX, realHeight / 2, rightStartPointX, realHeight);
        mRightArcPath.close();
        mAreaPaint.setColor(rightAreaColor);
        canvas.drawPath(mRightArcPath, mAreaPaint);
        //画中间的
        int middleStartPointX = rightStartPointX + middleArcOffset;
        int middleControlPointX = rightControlPointX + middleArcOffset;
        mMiddleArcPath.moveTo(middleStartPointX, realHeight);
        mMiddleArcPath.lineTo(rightStartPointX, realHeight);
        mMiddleArcPath.quadTo(rightControlPointX, realHeight / 2, rightStartPointX, 0);
        mMiddleArcPath.lineTo(middleStartPointX, 0);
        mMiddleArcPath.quadTo(middleControlPointX, realHeight / 2, middleStartPointX, realHeight);
        mMiddleArcPath.close();
        mAreaPaint.setColor(middleAreaColor);
        canvas.drawPath(mMiddleArcPath, mAreaPaint);
        //画最左边的
        int leftStartPointX = middleStartPointX + leftArcOffset;
        int leftControlPointX = middleControlPointX + leftArcOffset;
        mLeftArcPath.moveTo(leftStartPointX, realHeight);
        mLeftArcPath.lineTo(middleStartPointX, realHeight);
        mLeftArcPath.quadTo(middleControlPointX, realHeight / 2, middleStartPointX, 0);
        mLeftArcPath.lineTo(leftStartPointX, 0);
        mLeftArcPath.quadTo(leftControlPointX, realHeight / 2, leftStartPointX, realHeight);
        mLeftArcPath.close();
        mAreaPaint.setColor(leftAreaColor);
        canvas.drawPath(mLeftArcPath, mAreaPaint);
        //画刻度
        float initOffset = mProgress;


        mScalePath.moveTo(middleStartPointX, realHeight);
        mScalePath.quadTo(middleControlPointX, realHeight / 2, middleStartPointX, 0);
        mScalePathMeasure.setPath(mScalePath, false);
        float perPercent = 1f / (scaleNum - 1);//每一段所占屏幕百分比
        if (mIsCW) {
            int a = (int) (2)*scaleNum;
            for (int i = 0; i < a; i++) {

                float perDistance = mScalePathMeasure.getLength() * perPercent;
                float moveDistance = mScalePathMeasure.getLength() * initOffset;
                float linePos = perDistance*(scaleNum-i);
                float movingPos = linePos+moveDistance;

                mScalePathMeasure.getPosTan(movingPos, mScalePos, null);
                mScalePathMeasure.getSegment(0, movingPos, mScalePath, true);

                mScalePaint.setColor(i%2==0?Color.GREEN:Color.RED);

                canvas.drawLine(mScalePos[0],mScalePos[1],mScalePos[0]-40,mScalePos[1],mScalePaint);


                StringBuilder sb = new StringBuilder("onDraw:");

                sb.append("i").append(" = ").append(i).append("\n");
                sb.append("linePos").append(" = ").append(linePos).append("\n");
                sb.append("mScalePos").append(" = ").append(mScalePos).append("\n");
                sb.append("mScalePath").append(" = ").append(mScalePath).append("\n");

                Log.d(TAG, sb.toString());

//                float distance = mScalePathMeasure.getLength() * (perPercent * (i + 1) + initOffset);
//
//                mScalePathMeasure.getPosTan(distance, mScalePos, null);
//                mScalePathMeasure.getSegment(0, distance, mScalePath, true);
//                mScalePaint.setColor(i == scaleNum - 2 ? selectedColor : scaleColor);
//                canvas.drawLine(mScalePos[0], mScalePos[1], mScalePos[0] - (i % 2 != 0 ? scaleMaxLength : scaleMaxLength / 2), mScalePos[1], mScalePaint);
            }
        } else {
            int b = (int) ((2) * scaleNum);
            for (int i = 0; i < b; i++) {
                float perDistance = mScalePathMeasure.getLength() * perPercent;
                float moveDistance = mScalePathMeasure.getLength() * initOffset;
                float linePos = perDistance*i;
                float movingPos = linePos+moveDistance;

                mScalePathMeasure.getPosTan(movingPos, mScalePos, null);
                mScalePathMeasure.getSegment(0, movingPos, mScalePath, true);

                mScalePaint.setColor(i%2==0?Color.GREEN:Color.RED);

                canvas.drawLine(mScalePos[0],mScalePos[1],mScalePos[0]-40,mScalePos[1],mScalePaint);

            }
        }
//        for (int j = 0; j < scaleNum; j++) {
//            float distance = mScalePathMeasure.getLength() * (initOffset + 1f / scaleNum * (j + 1));
//            mScalePathMeasure.getPosTan(distance, mScalePos, null);
//            mScalePathMeasure.getSegment(0, distance, mScalePath, true);
//            mScalePaint.setColor(j == scaleNum - 2 ? selectedColor : scaleColor);
//            canvas.drawLine(mScalePos[0], mScalePos[1], mScalePos[0] - (j % 2 != 0 ? scaleMaxLength : scaleMaxLength / 2), mScalePos[1], mScalePaint);
//        }

//        canvas.drawPath(mLeftArcPath,mPaint);
//        for (int i = 0; i < 3; i++) {
//            mPaint.setColor(Color.BLACK);
//            int shlValue = i == 2 ? 250 : 200;
//            int pointX = realWidth - 50 - shlValue * i;
//            int pointY = realHeight;
//            int endPointY = 0;
//            int FlagPointX = realWidth - 250 - shlValue * i;
//            int FlagPointY = realHeight / 2;
//            mRightArcPath.moveTo(pointX, pointY);
//            mRightArcPath.quadTo(FlagPointX, FlagPointY, pointX, endPointY);
//            mRightArcPath.lineTo(realWidth, 0);
//            mRightArcPath.lineTo(realWidth, realHeight);
//            mRightArcPath.lineTo(pointX, pointY);
//            mRightArcPath.close();
//            canvas.drawPath(mRightArcPath, mPaint);
//            if (i == 1) {
//                //画刻度
//                mScalePath.moveTo(pointX, pointY);
//                mScalePath.quadTo(FlagPointX, FlagPointY, pointX, endPointY);
//                mScalePathMeasure.setPath(mScalePath, false);
//                for (int j = 0; j < 10; j++) {
//                    float distance = mScalePathMeasure.getLength() * 0.1f * (j + 1);
//                    mScalePathMeasure.getPosTan(distance, mScalePos, mScaleTan);
//                    mScalePathMeasure.getSegment(0, distance, mScalePath, true);
//                    canvas.drawLine(mScalePos[0], mScalePos[1], mScalePos[0] - (j % 2 == 0 ? 40 : 20), mScalePos[1], mPaint);
//                }
//                //画数字
//                mNumPath.moveTo(pointX-100, pointY);
//                mNumPath.quadTo(FlagPointX-100, FlagPointY, pointX-100, endPointY);
//                mNumPathMeasure.setPath(mNumPath, false);
//                for (int k = 0;k<5;k++){
//                    float distance = mNumPathMeasure.getLength() * 0.2f * (k + 1);
//                    mNumPathMeasure.getPosTan(distance,mNumPos,mNumTan);
//                    mNumPathMeasure.getSegment(0,distance,mNumPath,true);
//                    mPaint.setTextSize(40);
//                    canvas.drawText(500*(k+1)+"",mNumPos[0]-100,mNumPos[1]+20,mPaint);
//                }
//            }
//        }

    }

    /**
     * @param progress 0~100
     */
    public void setProgress(float progress, int time) {
        mIsCW = progress>0;
        mAnimator = ValueAnimator.ofFloat(0, progress);
        mAnimator.setDuration(time);
        mAnimator.setInterpolator(new LinearInterpolator());
        mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                mProgress = (float) valueAnimator.getAnimatedValue();
                invalidate();
            }
        });
        mAnimator.start();
    }
}
