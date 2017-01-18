package com.lewish.start.demo.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.lewish.start.demo.R;
import com.lewish.start.demo.evaluator.BezierEvaluator;

import java.util.Random;
/**
 * author: sundong
 * created at 2017/1/18 10:31
 */
public class DianZanView extends RelativeLayout {

    // 设置不同的插值器，让物体变化的速率具有更大的随机性
    private Interpolator line = new LinearInterpolator();//线性
    private Interpolator acc = new AccelerateInterpolator();//加速
    private Interpolator dce = new DecelerateInterpolator();//减速
    private Interpolator accdec = new AccelerateDecelerateInterpolator();//先加速后减速
    private Interpolator[] interpolators;
    private int mHeight;
    private int mWidth;
    private LayoutParams lp;
    private Drawable[] drawables;
    private Random random = new Random();
    private int dHeight;
    private int dWidth;

    public DianZanView(Context context) {
        super(context);
        init();
    }

    public DianZanView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DianZanView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        // 初始化显示的图片
        drawables = new Drawable[3];
        Drawable red = getResources().getDrawable(R.drawable.pl_red);
        Drawable yellow = getResources().getDrawable(R.drawable.pl_yellow);
        Drawable blue = getResources().getDrawable(R.drawable.pl_blue);
        // 初始化要显示的颜色
        drawables[0] = red;
        drawables[1] = yellow;
        drawables[2] = blue;
        // 获取图的宽高 用于后面的计算 注意 我这里3张图片的大小都是一样的
        dHeight = red.getIntrinsicHeight();
        dWidth = red.getIntrinsicWidth();
        // 底部 并且 水平居中
        lp = new LayoutParams(dWidth, dHeight);
        // 这里的TRUE 要注意 不是true
        lp.addRule(CENTER_HORIZONTAL, TRUE);
        lp.addRule(ALIGN_PARENT_BOTTOM, TRUE);
        // 初始化插补器
        interpolators = new Interpolator[4];
        interpolators[0] = line;
        interpolators[1] = acc;
        interpolators[2] = dce;
        interpolators[3] = accdec;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        // 获取宽高
        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();
    }

    public void addHeart() {
        ImageView imageView = new ImageView(getContext());
        // 随机选一个图像
        imageView.setImageDrawable(drawables[random.nextInt(3)]);
        imageView.setLayoutParams(lp);
        addView(imageView);
        // 执行随机动画
        Animator set = getAnimator(imageView);
        set.addListener(new AnimEndListener(imageView));
        set.start();
    }

    /**
     * 动画生成器，包含两个部分，一个是进入的动画，另一个是移动的动画
     *
     * @param target
     * @return
     */
    private Animator getAnimator(View target) {
        // 进入动画
        AnimatorSet set = getEnterAnimtor(target);
        // 移动动画
        ValueAnimator bezierValueAnimator = getBezierValueAnimator(target);
        AnimatorSet finalSet = new AnimatorSet();
        finalSet.playSequentially(set);
        finalSet.playSequentially(set, bezierValueAnimator);
        finalSet.setInterpolator(interpolators[random.nextInt(4)]);
        finalSet.setTarget(target);
        return finalSet;
    }

    private AnimatorSet getEnterAnimtor(final View target) {
        // 进入动画就是一个透明的+放大的动画
        ObjectAnimator alpha = ObjectAnimator.ofFloat(target, View.ALPHA, 0.2f, 1f);
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(target, View.SCALE_X, 0.2f, 1f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(target, View.SCALE_Y, 0.2f, 1f);
        AnimatorSet enter = new AnimatorSet();
        enter.setDuration(500);
        enter.setInterpolator(new LinearInterpolator());
        enter.playTogether(alpha, scaleX, scaleY);
        enter.setTarget(target);
        return enter;
    }

    private ValueAnimator getBezierValueAnimator(View target) {
        // 初始化一个贝塞尔计算器- - 传入
        BezierEvaluator evaluator = new BezierEvaluator(getPointF(2), getPointF(1));
        // 传入了起点 和 终点，大家可以和视频中的Demo一样，将辅助点和线绘制出来
        ValueAnimator animator = ValueAnimator.ofObject(evaluator, new PointF((mWidth - dWidth) / 2, mHeight - dHeight), new PointF(random.nextInt(getWidth()), 0));
        animator.addUpdateListener(new BezierListener(target));
        animator.setTarget(target);
        animator.setDuration(3000);
        return animator;
    }

    /**
     * 获取中间的两个点
     *
     * @param scale
     */
    private PointF getPointF(int scale) {
        PointF pointF = new PointF();
        // 减去100 是为了控制 x轴活动范围,看效果 随意
        pointF.x = random.nextInt((mWidth - 100));
        // 在Y轴上 为了确保第二个点 在第一个点之上,我把Y分成了上下两半 这样动画效果好一些  也可以用其他方法
        pointF.y = random.nextInt((mHeight - 100)) / scale;
        return pointF;
    }

    private class BezierListener implements ValueAnimator.AnimatorUpdateListener {

        private View target;

        public BezierListener(View target) {
            this.target = target;
        }

        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            // 这里获取到贝塞尔曲线计算出来的的x y值 赋值给view 这样就能让爱心随着曲线走啦
            PointF pointF = (PointF) animation.getAnimatedValue();
            target.setX(pointF.x);
            target.setY(pointF.y);
            // 这里顺便做一个alpha动画
            target.setAlpha(1 - animation.getAnimatedFraction());
        }
    }


    private class AnimEndListener extends AnimatorListenerAdapter {
        private View target;

        public AnimEndListener(View target) {
            this.target = target;
        }

        @Override
        public void onAnimationEnd(Animator animation) {
            super.onAnimationEnd(animation);
            // 因为不停的add 导致子view数量只增不减,所以在view动画结束后remove掉
            removeView((target));
        }
    }
}
