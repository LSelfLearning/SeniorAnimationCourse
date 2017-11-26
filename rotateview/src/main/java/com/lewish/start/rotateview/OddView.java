package com.lewish.start.rotateview;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.LinearInterpolator;

/**
 * author: sundong
 * created at 2017/11/23 20:29
 */

public class OddView extends View {
    private static final String TAG = "OddView";
    //颜色相关常量
    private static final String DEFAULT_RIGHTAREA_COLOR = "#4f4f4f";
    private static final String DEFAULT_MIDDLEAREA_COLOR = "#555555";
    private static final String DEFAULT_LEFTAREA_COLOR = "#4a4a4a";
    private static final String DEFAULT_SCALE_COLOR = "#525252";
    private static final String DEFAULT_NUM_COLOR = "#BABABA";
    private static final String DEFAULT_SELECTED_COLOR = "#ffd401";
    //属性配置相关常量
    private static final float DEFAULT_SCALELINE_MAX_LENGTH = 40;
    private static final int DEFAULT_SCALE_NUM = 10;
    //贝塞尔曲线相关常量
    private static final int RIGHT_BEZIER_CONTROL_POINT_OFFSET = -250;
    public static final int RIGHT_BEZIER_OFFSET = -50;
    public static final int MIDDLE_BEZIER_REL_OFFSET = -150;
    public static final int LEFT_BEZIER_REL_OFFSET = -200;

    private Context mContext;

    private int mTouchSlop;//滑动阈值
    //颜色
    private int rightAreaColor;
    private int middleAreaColor;
    private int leftAreaColor;
    private int scaleColor;
    private int textColor;
    private int selectedColor;
    //Paint
    private Paint mAreaPaint;
    private Paint mScalePaint;
    private Paint mTxtPaint;
    //Path
    private Path mRightArcPath;
    private Path mMiddleArcPath;
    private Path mLeftArcPath;
    private Path mScalePath;
    private PathMeasure mScalePathMeasure;
    private float[] mScalePos;
    private Rect mTxtMeasureRect;
    //参数配置
    private float mTxtFontSize;
    private int scaleNum;//一屏有多少刻度
    private float scaleLineMaxLength;
    private int[] amountArr = {500, 1000, 1500, 2000, 2500};
    //逻辑需要
    private int realWidth;
    private int realHeight;
    private float mOffset;
    private boolean mIsCW;
    private int topLineIndex;
    private int bottomLineIndex;
    private amountSelectedListener amountSelectedListener;
    //调试用
    private ValueAnimator mAnimator;
    //交互相关
    float mLastX, mLastY;
    private float downX, downY;

    public OddView(Context context) {
        super(context, null);
    }

    public OddView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public OddView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
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
        textColor = typedArray.getColor(R.styleable.OddView_numColor, Color.parseColor(DEFAULT_NUM_COLOR));
        scaleNum = typedArray.getInteger(R.styleable.OddView_scaleNum, DEFAULT_SCALE_NUM);
        scaleLineMaxLength = typedArray.getDimension(R.styleable.OddView_scaleLineMaxLength, DEFAULT_SCALELINE_MAX_LENGTH);
        mTxtFontSize = typedArray.getDimension(R.styleable.OddView_txtFontSize, 40);
        typedArray.recycle();
    }

    private void initVariables() {
        mAreaPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mAreaPaint.setStyle(Paint.Style.FILL);
        mAreaPaint.setStrokeWidth(5);

        mScalePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mScalePaint.setStyle(Paint.Style.STROKE);
        mScalePaint.setColor(scaleColor);
        mScalePaint.setStrokeWidth(5);

        mTxtPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTxtPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mTxtPaint.setTextSize(mTxtFontSize);
        mTxtPaint.setTextAlign(Paint.Align.LEFT);
        mTxtPaint.setColor(textColor);
        mTxtPaint.setTypeface(Typeface.DEFAULT);
        mTxtPaint.setStrokeWidth(2);

        //Arc
        mRightArcPath = new Path();
        mMiddleArcPath = new Path();
        mLeftArcPath = new Path();
        //刻度
        mScalePath = new Path();
        mScalePathMeasure = new PathMeasure();
        mScalePos = new float[2];

        mTxtMeasureRect = new Rect();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        realWidth = getWidth() - getPaddingLeft() - getPaddingRight();
        realHeight = getHeight() - getPaddingTop() - getPaddingBottom();
        drawArcArea(canvas);
        //画刻度
        int scaleBezierStartX = realWidth + RIGHT_BEZIER_OFFSET + MIDDLE_BEZIER_REL_OFFSET;
        int scaleBezierControlX = realWidth + RIGHT_BEZIER_CONTROL_POINT_OFFSET + MIDDLE_BEZIER_REL_OFFSET;
        mIsCW = mOffset > 0;
        mScalePath.moveTo(scaleBezierStartX, realHeight);
        mScalePath.quadTo(scaleBezierControlX, realHeight / 2, scaleBezierStartX, 0);
        mScalePathMeasure.setPath(mScalePath, false);
        float perArcPercent = 1f / (scaleNum - 1);//每一段所占屏幕百分比
        float perArcLength = mScalePathMeasure.getLength() * perArcPercent;//每一段的弧长
        float moveDistance = mScalePathMeasure.getLength() * mOffset;//要移动的距离
        int lineCount = scaleNum * 30;
        float scaleLineLength;//刻度线长度
        float movingPos;//要移动的位置
        float movingNextPos;
        float linePos;//刻度线在曲线弧中的位置
        float nextLinePos;
        for (int i = 0; i < lineCount; i++) {
            if (mIsCW) {
                //顺时针转->从上向下画
                linePos = perArcLength * (scaleNum - i);
                nextLinePos = perArcLength * (scaleNum - i + 1);
            } else {
                //逆时针转->从下向上画
                linePos = perArcLength * i;
                nextLinePos = perArcLength * (i - 1);
            }
            movingPos = linePos + moveDistance;
            movingNextPos = nextLinePos + moveDistance;
            if(mIsCW) {
                if (movingPos < mScalePathMeasure.getLength() && movingNextPos >= mScalePathMeasure.getLength()) {
                    topLineIndex = i;
                    bottomLineIndex = topLineIndex + 7;
                    Log.d(TAG, "顺时针 topLineIndex=" + topLineIndex + "bottomLineIndex=" + bottomLineIndex);
                }
            }else {
                if(movingPos>0&&movingNextPos<=0) {
                    bottomLineIndex = i;
                    topLineIndex = bottomLineIndex + 7;
                    Log.d(TAG, "逆时针 topLineIndex=" + topLineIndex + "bottomLineIndex=" + bottomLineIndex);
                }
            }

            mScalePathMeasure.getPosTan(movingPos, mScalePos, null);
            mScalePathMeasure.getSegment(0, movingPos, mScalePath, true);
            mScalePaint.setColor(i % 2 == 0 ? Color.GREEN : Color.RED);
            if (i % 2 == 0) {
                scaleLineLength = scaleLineMaxLength / 2;
            } else {
                scaleLineLength = scaleLineMaxLength;
                String text = i > 6 ? "500" : "2000";
                mTxtPaint.getTextBounds(text, 0, text.length(), mTxtMeasureRect);
                if ((mScalePos[1] >= 0 + perArcLength) && (mScalePos[1] <= realHeight - perArcLength)) {
                    canvas.drawText(text, mScalePos[0] - mTxtMeasureRect.width() - 2 * scaleLineMaxLength, mScalePos[1] + mTxtMeasureRect.height() / 2f, mTxtPaint);
                }
            }
            canvas.drawLine(mScalePos[0], mScalePos[1], mScalePos[0] - scaleLineLength, mScalePos[1], mScalePaint);
        }
    }

    private void drawTxt(){

    }

    private void drawLine(){

    }
    /**
     * 画弧形区域
     */
    private void drawArcArea(Canvas canvas) {
        //画最右边的
        int rightStartPointX = realWidth + RIGHT_BEZIER_OFFSET;
        int rightControlPointX = realWidth + RIGHT_BEZIER_CONTROL_POINT_OFFSET;
        mRightArcPath.moveTo(rightStartPointX, realHeight);
        mRightArcPath.lineTo(realWidth, realHeight);
        mRightArcPath.lineTo(realWidth, 0);
        mRightArcPath.lineTo(rightStartPointX, 0);
        mRightArcPath.quadTo(rightControlPointX, realHeight / 2, rightStartPointX, realHeight);
        mRightArcPath.close();
        mAreaPaint.setColor(rightAreaColor);
        canvas.drawPath(mRightArcPath, mAreaPaint);
        //画中间的
        int middleStartPointX = rightStartPointX + MIDDLE_BEZIER_REL_OFFSET;
        int middleControlPointX = rightControlPointX + MIDDLE_BEZIER_REL_OFFSET;
        mMiddleArcPath.moveTo(middleStartPointX, realHeight);
        mMiddleArcPath.lineTo(rightStartPointX, realHeight);
        mMiddleArcPath.quadTo(rightControlPointX, realHeight / 2, rightStartPointX, 0);
        mMiddleArcPath.lineTo(middleStartPointX, 0);
        mMiddleArcPath.quadTo(middleControlPointX, realHeight / 2, middleStartPointX, realHeight);
        mMiddleArcPath.close();
        mAreaPaint.setColor(middleAreaColor);
        canvas.drawPath(mMiddleArcPath, mAreaPaint);
        //画最左边的
        int leftStartPointX = middleStartPointX + LEFT_BEZIER_REL_OFFSET;
        int leftControlPointX = middleControlPointX + LEFT_BEZIER_REL_OFFSET;
        mLeftArcPath.moveTo(leftStartPointX, realHeight);
        mLeftArcPath.lineTo(middleStartPointX, realHeight);
        mLeftArcPath.quadTo(middleControlPointX, realHeight / 2, middleStartPointX, 0);
        mLeftArcPath.lineTo(leftStartPointX, 0);
        mLeftArcPath.quadTo(leftControlPointX, realHeight / 2, leftStartPointX, realHeight);
        mLeftArcPath.close();
        mAreaPaint.setColor(leftAreaColor);
        canvas.drawPath(mLeftArcPath, mAreaPaint);
    }

    /**
     * @param offset 0~100
     */
    public void setOffset(float offset, int time) {
        mIsCW = offset > 0;
        mAnimator = ValueAnimator.ofFloat(0, offset);
        mAnimator.setDuration(time);
        mAnimator.setInterpolator(new LinearInterpolator());
        mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                mOffset = (float) valueAnimator.getAnimatedValue();
                invalidate();
            }
        });
        mAnimator.start();
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        float x = event.getRawX();
        float y = event.getRawY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = x;
                downY = y;
                break;
            case MotionEvent.ACTION_MOVE:
                float deltaX = downX - x;
                float deltaY = downY - y;
                boolean isScrollY = Math.abs(deltaY) > Math.abs(deltaX) && Math.abs(deltaY) > mTouchSlop;
                if (isScrollY) {
                    //Y方向上的移动
                    mOffset = deltaY / realHeight;
                    invalidate();
                }
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                break;
        }
        mLastX = x;
        mLastY = y;
        return true;
    }

    interface amountSelectedListener {
        void onAmountSelected(String amount);
    }

    public void setAmountSelectedListener(amountSelectedListener amountSelectedListener) {
        this.amountSelectedListener = amountSelectedListener;
    }
}
