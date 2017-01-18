package com.lewish.start.demo.view;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.widget.AppCompatTextView;
import android.text.SpannableString;
import android.text.Spanned;
import android.util.AttributeSet;

import com.lewish.start.demo.R;
import com.lewish.start.demo.utils.JumpingSpan;

/**
 * 本实例是为了让大家了解一个动画的不同实现方式，开拓大家的眼界
 */
public class WaitingDotsView extends AppCompatTextView {

    private JumpingSpan dotOne;
    private JumpingSpan dotTwo;
    private JumpingSpan dotThree;
    private int jumpHeight;
    private boolean autoPlay;
    private int period;
    private AnimatorSet mAnimatorSet = new AnimatorSet();

    public WaitingDotsView(Context context) {
        super(context);
        init(context, null);
    }

    public WaitingDotsView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public WaitingDotsView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        new Handler(Looper.getMainLooper());
        //自定义属性
        if (attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.WaitingDots);
            period = typedArray.getInt(R.styleable.WaitingDots_period, 6000);
            jumpHeight = typedArray.getInt(R.styleable.WaitingDots_jumpHeight, (int) (getTextSize() / 4));
            autoPlay = typedArray.getBoolean(R.styleable.WaitingDots_autoplay, true);
            typedArray.recycle();
        }
        dotOne = new JumpingSpan();
        dotTwo = new JumpingSpan();
        dotThree = new JumpingSpan();
        // 将每个点设置为jumpingSpan类型
        SpannableString spannable = new SpannableString("...");
        spannable.setSpan(dotOne, 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannable.setSpan(dotTwo, 1, 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannable.setSpan(dotThree, 2, 3, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        setText(spannable, BufferType.SPANNABLE);
        getPaint().measureText(".", 0, 1);
        // 一下两个是把updateListener加到点1上，通过它来进行刷新动作
        ObjectAnimator dotOneJumpAnimator = createDotJumpAnimator(dotOne, 0);
        dotOneJumpAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                invalidate();
            }
        });
        // 这里通过animationSet来控制三个点的组合动作
        mAnimatorSet.playTogether(dotOneJumpAnimator, createDotJumpAnimator(dotTwo,
                period / 6), createDotJumpAnimator(dotThree, period * 2 / 6));
        if (autoPlay) {
            start();
        }
    }

    public void start() {
        //一旦开始就INFINITE
        setAllAnimationsRepeatCount(ValueAnimator.INFINITE);
        mAnimatorSet.start();
    }

    /**
     * 动画的实现核心
     *
     * @param jumpingSpan 传入点
     * @delay 动画运行延迟，通过这个参数让三个点进行有时差的运动
     */
    private ObjectAnimator createDotJumpAnimator(JumpingSpan jumpingSpan, long delay) {
        ObjectAnimator jumpAnimator = ObjectAnimator.ofFloat(jumpingSpan, "translationY", 0, -jumpHeight);
        jumpAnimator.setEvaluator(new TypeEvaluator<Number>() {

            @Override
            public Number evaluate(float fraction, Number from, Number to) {
                return Math.max(0, Math.sin(fraction * Math.PI * 2)) * (to.floatValue() - from.floatValue());
            }
        });
        jumpAnimator.setDuration(period);
        jumpAnimator.setStartDelay(delay);
        jumpAnimator.setRepeatCount(ValueAnimator.INFINITE);
        jumpAnimator.setRepeatMode(ValueAnimator.RESTART);
        return jumpAnimator;
    }

    private void setAllAnimationsRepeatCount(int repeatCount) {
        for (Animator animator : mAnimatorSet.getChildAnimations()) {
            if (animator instanceof ObjectAnimator) {
                ((ObjectAnimator) animator).setRepeatCount(repeatCount);
            }
        }
    }

}
