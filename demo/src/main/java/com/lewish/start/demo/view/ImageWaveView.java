package com.lewish.start.demo.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.lewish.start.demo.R;
/**
 * author: sundong
 * created at 2017/1/19 17:11
 */
public class ImageWaveView extends View implements View.OnClickListener {
    private  Bitmap mDstBitmap;
    private PorterDuffXfermode mPorterDuffXfermode;
    private Bitmap mSrcBitmap;
    private Canvas mCanvas;
    private int mFillColor;
    private Path mBezierPath;

    private Paint mBezierPaint;

    private int mViewHeight;
    private int mViewWidth;

    private int mWaveMidHeight;
    private int mWaveHalfRange;
    private int mWaveLength;
    private int mWaveCount;
    private int mWaveOffsetX;
    private int mWaveOffsetY;

    private ValueAnimator mOffsetXValueAnimator;
    private ValueAnimator mOffsetYValueAnimator;

    public ImageWaveView(Context context) {
        this(context,null);
    }

    public ImageWaveView(Context context, AttributeSet attrs) {
        this(context, attrs,-1);
    }

    public ImageWaveView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context,attrs);
        setOnClickListener(this);
    }

    private void initView(Context context,AttributeSet attrs) {
        initPaint();

        mBezierPath = new Path();

        TypedArray mTypedArray = context.obtainStyledAttributes(attrs,
                R.styleable.ImageWaveView);
        BitmapDrawable imageSrc = (BitmapDrawable) mTypedArray.getDrawable(R.styleable.ImageWaveView_imageSrc);
        mFillColor = mTypedArray.getColor(R.styleable.ImageWaveView_fillColor,Color.RED);
        if(imageSrc==null) {
            mSrcBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_star_rate_off);
        }
        mSrcBitmap = imageSrc.getBitmap();
        // 设置宽高为图片的宽高
        mViewWidth = mSrcBitmap.getWidth();
        mViewHeight = mSrcBitmap.getHeight();
        mWaveMidHeight = (int) (7 / 8F * mViewHeight);
        mWaveLength = (int) (mViewWidth*0.8);
        mWaveCount = (int) Math.round(mViewWidth / mWaveLength + 1.5);
        mWaveHalfRange = (int) (mViewHeight*0.1);

        // 初始化Xfermode
        mPorterDuffXfermode = new PorterDuffXfermode(PorterDuff.Mode.SRC_IN);
        // 初始化画布
        mCanvas = new Canvas();
        // 创建bitmap
        mDstBitmap = Bitmap.createBitmap(mViewWidth, mViewHeight, Bitmap.Config.ARGB_8888);
        // 将新建的bitmap注入画布
        mCanvas.setBitmap(mDstBitmap);
        // 擦除像素
        mDstBitmap.eraseColor(Color.parseColor("#00ffffff"));
    }

    private void initPaint() {
        mBezierPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBezierPaint.setDither(true);
        mBezierPaint.setStyle(Paint.Style.FILL);
        mBezierPaint.setColor(mFillColor);
    }

    private void drawTargetBitmap() {
        // 重置path
        mBezierPath.reset();
        // 贝塞尔曲线的生成
        mBezierPath.moveTo(-mWaveLength + mWaveOffsetX, mWaveMidHeight);
        for (int i = 0; i < mWaveCount; i++) {
            mBezierPath.quadTo(-mWaveLength * 3 / 4 + i * mWaveLength + mWaveOffsetX, mWaveMidHeight + mWaveHalfRange, -mWaveLength / 2 + i * mWaveLength + mWaveOffsetX, mWaveMidHeight);
            mBezierPath.quadTo(-mWaveLength / 4 + i * mWaveLength + mWaveOffsetX, mWaveMidHeight - mWaveHalfRange, i * mWaveLength + mWaveOffsetX, mWaveMidHeight);
        }
        mBezierPath.lineTo(mViewWidth, mViewHeight);
        mBezierPath.lineTo(0, mViewHeight);
        mBezierPath.close();

        // 画logo
        mCanvas.drawBitmap(mSrcBitmap, 0, 0, mBezierPaint);
        // 设置Xfermode
        mBezierPaint.setXfermode(mPorterDuffXfermode);
        // 画二阶贝塞尔曲线
        mCanvas.drawPath(mBezierPath, mBezierPaint);
        // 重置Xfermode
        mBezierPaint.setXfermode(null);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawTargetBitmap();
        // 将目标图绘制在当前画布上，起点为左边距，上边距的交点
        canvas.drawBitmap(mDstBitmap, getPaddingLeft(), getPaddingTop(), null);
    }

    @Override
    public void onClick(View view) {
        startAnimation();
    }

    private void startAnimation() {
        mOffsetXValueAnimator = ValueAnimator.ofInt(0, mWaveLength);
        mOffsetXValueAnimator.setDuration(1000);
        mOffsetXValueAnimator.setRepeatCount(ValueAnimator.INFINITE);
        mOffsetXValueAnimator.setInterpolator(new LinearInterpolator());
        mOffsetXValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                mWaveOffsetX = (int) valueAnimator.getAnimatedValue();
                invalidate();
            }
        });
        mOffsetXValueAnimator.start();

        mOffsetYValueAnimator = ValueAnimator.ofInt(mViewHeight,0);
        mOffsetYValueAnimator.setDuration(3000);
        mOffsetYValueAnimator.setRepeatCount(ValueAnimator.INFINITE);
        mOffsetYValueAnimator.setInterpolator(new LinearInterpolator());
        mOffsetYValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                mWaveMidHeight = (int) valueAnimator.getAnimatedValue();
                invalidate();
            }
        });
        mOffsetYValueAnimator.start();
    }
}
