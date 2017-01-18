package com.lewish.start.demo.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

// 这个动画同样是考验动画效果的拆分能力，大家可以通过我在视频中介绍的方式来进行学习如果拆解动画
public class SearchView extends View {

    private static final int STATE_SEARCHING = 0;
    private static final int STATE_WAIT = 1;
    private static final int DEFAULT_LINE_COLOR = Color.BLACK;
    private static final int DEFAULT_LINE_WIDTH = 9;
    private static final int DEFAULT_DOT_SIZE = 3;
    private static final int DEFAULT_DURATION = 3000;
    private Paint mPaint, mArcPaint;
    private float mFraction;
    private int mCurrentState;
    private Path mPath, mArcPath;
    private PathMeasure mPathMeasure;
    private RectF mRectF;
    private float mWidth, mHeight;
    private float mCenterX, mCenterY;
    private float mPathLength, mCircleRadius;
    private float mCurrentPos[], mCurrentTan[];
    private boolean isDotShowing = true;
    private int mDotSize, mDuration;

    public SearchView(Context context) {
        super(context);
    }

    public SearchView(Context context, AttributeSet attrs) {
        super(context, attrs);
        int lineColor = DEFAULT_LINE_COLOR;
        int lineWidth = DEFAULT_LINE_WIDTH;
        int dotSize = DEFAULT_DOT_SIZE;
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(lineWidth);
        mPaint.setColor(lineColor);
        mArcPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mArcPaint.setStyle(Paint.Style.FILL);
        mArcPaint.setColor(Color.WHITE);
        mDotSize = dotSize;
        mDuration = DEFAULT_DURATION;
        mCurrentState = STATE_WAIT;
        mCurrentPos = new float[2];
        mCurrentTan = new float[2];
        mPath = new Path();
        mArcPath = new Path();
        mPathMeasure = new PathMeasure();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
        mCenterX = mWidth / 2;
        mCenterY = mHeight / 2;
        mCircleRadius = mWidth / 4;
        mRectF = new RectF(mCenterX - 0.95f * mCircleRadius, mCenterY - 0.95f * mCircleRadius,
                mCenterX + 0.95f * mCircleRadius, mCenterY + 0.95f * mCircleRadius);
        mPath.moveTo(mCenterX + 2.2f * mCircleRadius / (float) Math.sqrt(2), mCenterY + 2.2f * mCircleRadius / (float) Math.sqrt(2));
        mPath.lineTo(mCenterX, mCenterY);
        mPath.lineTo(mCenterX - 0.45f * mCircleRadius * (float) Math.sqrt(3), mCenterY + 0.45f * mCircleRadius);
        mPath.lineTo(mCenterX - 0.45f * mCircleRadius * (float) Math.sqrt(3), mCenterY - 0.45f * mCircleRadius);
        mPath.lineTo(mCenterX + 0.45f * mCircleRadius * (float) Math.sqrt(3), mCenterY);
        mPath.lineTo(mCenterX, mCenterY);
        mPath.lineTo(mCenterX + 2.2f * mCircleRadius / (float) Math.sqrt(2), mCenterY + 2.2f * mCircleRadius / (float) Math.sqrt(2));
        mPathMeasure.setPath(mPath, false);
        mPathLength = mPathMeasure.getLength();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 通过mFraction来控制动画的时间阶段
        if (mFraction <= 0.2) { // 放大镜手柄收缩
            canvas.drawCircle(mCenterX, mCenterY, mCircleRadius - mCircleRadius * mFraction, mPaint);
            canvas.drawLine(mCenterX + mCircleRadius / (float) Math.sqrt(2) + 1.2f * mCircleRadius / (float) Math.sqrt(2) / 0.2f * mFraction,
                    mCenterY + mCircleRadius / (float) Math.sqrt(2) + 1.2f * mCircleRadius / (float) Math.sqrt(2) / 0.2f * mFraction,
                    mCenterX + 2.2f * mCircleRadius / (float) Math.sqrt(2),
                    mCenterY + 2.2f * mCircleRadius / (float) Math.sqrt(2)
                    , mPaint);
        } else if (mFraction <= 0.8) {
            mPathMeasure.getPosTan(0 + mPathLength / 0.6f * (mFraction - 0.2f), mCurrentPos, mCurrentTan);
            if (mCurrentPos[1] == mCenterY && mCurrentPos[0] <= mCenterX + mCircleRadius / 3 && mCurrentPos[0] >= mCenterX - mCircleRadius / 3) {
                if (isDotShowing) {
                    isDotShowing = false;
                } else {
                    canvas.drawCircle(mCurrentPos[0], mCurrentPos[1], mDotSize, mPaint);    //嗷~ 轨迹中的小球
                    isDotShowing = true;
                }
            } else {
                canvas.drawCircle(mCurrentPos[0], mCurrentPos[1], mDotSize, mPaint);
            }
            if (mFraction <= 0.3) { // 内圈粘性部分
                canvas.drawCircle(mCenterX, mCenterY, 0.8f * mCircleRadius + mCircleRadius * 2 * (mFraction - 0.2f), mPaint);
            } else {
                canvas.drawCircle(mCenterX, mCenterY, mCircleRadius, mPaint);
            }

            if (mFraction <= 0.35 && mFraction > 0.3) {
                canvas.drawArc(mRectF, 45 - 55 / 0.05f * (mFraction - 0.3f), 110 / 0.05f * (mFraction - 0.3f), false, mArcPaint);
            } else if (mFraction <= 0.4 && mFraction > 0.35) {
                canvas.drawArc(mRectF, 45 - 55 / 0.05f * (0.4f - mFraction), 110 / 0.05f * (0.4f - mFraction), false, mArcPaint);
            }

            if (mFraction <= 0.75 && mFraction > 0.7) {  //嗷~ 外圈粘性部分
                mArcPath.reset();
                mArcPath.moveTo(mCenterX + mCircleRadius, mCenterY);
                mArcPath.cubicTo(mCenterX + mCircleRadius + 8 / 0.05f * (mFraction - 0.7f), mCenterY + mCircleRadius / 2 + 8 / 0.05f * (mFraction - 0.7f),
                        mCenterX + mCircleRadius / 2 + 8 / 0.05f * (mFraction - 0.7f), mCenterY + mCircleRadius + 8 / 0.05f * (mFraction - 0.7f),
                        mCenterX, mCenterY + mCircleRadius);
                canvas.drawPath(mArcPath, mPaint);
            } else if (mFraction <= 0.8 && mFraction > 0.75) {
                mArcPath.reset();
                mArcPath.moveTo(mCenterX + mCircleRadius, mCenterY);
                mArcPath.cubicTo(mCenterX + mCircleRadius + 8 / 0.05f * (0.8f - mFraction), mCenterY + mCircleRadius / 2 + 8 / 0.05f * (0.8f - mFraction),
                        mCenterX + mCircleRadius / 2 + 8 / 0.05f * (0.8f - mFraction), mCenterY + mCircleRadius + 8 / 0.05f * (0.8f - mFraction),
                        mCenterX, mCenterY + mCircleRadius);
                canvas.drawPath(mArcPath, mPaint);
            }
        } else {    // 放大镜手柄伸长
            canvas.drawCircle(mCenterX, mCenterY, mCircleRadius, mPaint);
            canvas.drawLine(mCenterX + 2.2f * mCircleRadius / (float) Math.sqrt(2) - 1.2f * mCircleRadius / (float) Math.sqrt(2) / 0.2f * (mFraction - 0.8f),
                    mCenterY + 2.2f * mCircleRadius / (float) Math.sqrt(2) - 1.2f * mCircleRadius / (float) Math.sqrt(2) / 0.2f * (mFraction - 0.8f),
                    mCenterX + 2.2f * mCircleRadius / (float) Math.sqrt(2),
                    mCenterY + 2.2f * mCircleRadius / (float) Math.sqrt(2)
                    , mPaint);
        }
    }

    public void start() {
        if (mCurrentState == STATE_SEARCHING) {
            return;
        }
        mCurrentState = STATE_SEARCHING;
        ValueAnimator valueAnim = ValueAnimator.ofFloat(1.f, 100.f);
        valueAnim.setDuration(mDuration);
        valueAnim.setInterpolator(new LinearInterpolator());
        valueAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                mFraction = valueAnimator.getAnimatedFraction();
                invalidate();
            }
        });
        valueAnim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mCurrentState = STATE_WAIT;
            }
        });
        valueAnim.start();
    }
}