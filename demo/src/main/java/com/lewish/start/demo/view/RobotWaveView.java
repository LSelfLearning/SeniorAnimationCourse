package com.lewish.start.demo.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.view.View;

import com.lewish.start.demo.R;

/** 
 * author: sundong
 * created at 2017/1/19 14:55
 */  
public class RobotWaveView extends View {

    // Xfermode
    private PorterDuffXfermode porterDuffXfermode;
    // 画笔
    private Paint paint;
    // 源图片
    private Bitmap mSrcBitmap;
    // 控件宽高
    private int mViewWidth, mViewHeight;
    // 画贝塞尔曲线Path
    private Path mBezierPath;
    // 在该画布上绘制目标图片
    private Canvas mCanvas;
    // 目标图片
    private Bitmap mDstBitmap;
    // 贝塞尔曲线控制点，使用三阶贝塞尔曲线曲线，需要两个控制点，两个控制点都在该变量基础上生成
    private float controlX, controlY;
    // 上升的高度
    private float mWaveHeight;
    // 用于控制控制点水平移动
    private boolean isIncrease;

    /**
     * @param context
     */
    public RobotWaveView(Context context) {
        this(context, null);
    }

    /**
     * @param context
     * @param attrs
     */
    public RobotWaveView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    /**
     * @param context
     * @param attrs
     * @param defStyle
     */
    public RobotWaveView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    /**
     * 初始化变量
     */
    private void init() {
        // 初始化画笔
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.parseColor("#ffc9394a"));
        // 获得资源文件
        mSrcBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_star_rate_off);
        // 设置宽高为图片的宽高
        mViewWidth = mSrcBitmap.getWidth();
        mViewHeight = mSrcBitmap.getHeight();
        // 初始状态值
        mWaveHeight = 7 / 8F * mViewHeight;
        controlY = 17 / 16F * mViewHeight;
        // 初始化Xfermode
        porterDuffXfermode = new PorterDuffXfermode(PorterDuff.Mode.SRC_IN);
        // 初始化path
        mBezierPath = new Path();
        // 初始化画布
        mCanvas = new Canvas();
        // 创建bitmap
        mDstBitmap = Bitmap.createBitmap(mViewWidth, mViewHeight, Bitmap.Config.ARGB_8888);
        // 将新建的bitmap注入画布
        mCanvas.setBitmap(mDstBitmap);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // 画目标图，存在bg上
        drawTargetBitmap();
        // 将目标图绘制在当前画布上，起点为左边距，上边距的交点
        canvas.drawBitmap(mDstBitmap, getPaddingLeft(), getPaddingTop(), null);
        invalidate();
    }

    private void drawTargetBitmap() {
        // 重置path
        mBezierPath.reset();
        // 擦除像素
        mDstBitmap.eraseColor(Color.parseColor("#00ffffff"));
        // 当控制点的x坐标大于或等于终点x坐标时更改标识值
        if (controlX >= mViewWidth + 1 / 2 * mViewWidth) {
            isIncrease = false;
        }
        // 当控制点的x坐标小于或等于起点x坐标时更改标识值
        else if (controlX <= -1 / 2 * mViewWidth) {
            isIncrease = true;
        }
        // 根据标识值判断当前的控制点x坐标是该加还是减
        controlX = isIncrease ? controlX + 10 : controlX - 10;
        if (controlY >= 0) {
            // 波浪上移
            controlY -= 1;
            mWaveHeight -= 1;
        } else {
            // 超出则重置位置
            mWaveHeight = 7 / 8F * mViewHeight;
            controlY = 17 / 16F * mViewHeight;
        }
        // 贝塞尔曲线的生成
        mBezierPath.moveTo(0, mWaveHeight);
        // 两个控制点通过controlX，controlY生成
        mBezierPath.cubicTo(controlX / 2, mWaveHeight - (controlY - mWaveHeight),
                (controlX + mViewWidth) / 2, controlY, mViewWidth, mWaveHeight);
        // 与下下边界闭合
        mBezierPath.lineTo(mViewWidth, mViewHeight);
        mBezierPath.lineTo(0, mViewHeight);
        // 进行闭合
        mBezierPath.close();
        // 画慕课网logo
        mCanvas.drawBitmap(mSrcBitmap, 0, 0, paint);
        // 设置Xfermode
        paint.setXfermode(porterDuffXfermode);
        // 画三阶贝塞尔曲线
        mCanvas.drawPath(mBezierPath, paint);
        // 重置Xfermode
        paint.setXfermode(null);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // 获得宽高测量模式和大小
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        // 保存测量结果
        int width, height;
        if (widthMode == MeasureSpec.EXACTLY) {
            // 宽度
            width = widthSize;
        } else {
            // 宽度加左右内边距
            width = this.mViewWidth + getPaddingLeft() + getPaddingRight();
            if (widthMode == MeasureSpec.AT_MOST) {
                // 取小的那个
                width = Math.min(width, widthSize);
            }
        }
        if (heightMode == MeasureSpec.EXACTLY) {
            // 高度
            height = heightSize;
        } else {
            // 高度加左右内边距
            height = this.mViewHeight + getPaddingTop() + getPaddingBottom();
            if (heightMode == MeasureSpec.AT_MOST) {
                // 取小的那个
                height = Math.min(height, heightSize);
            }
        }
        // 设置高度宽度为logo宽度和高度,实际开发中应该判断MeasureSpec的模式，进行对应的逻辑处理,这里做了简单的判断测量
        setMeasuredDimension(width, height);
    }
}