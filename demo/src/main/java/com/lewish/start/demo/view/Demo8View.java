package com.lewish.start.demo.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.lewish.start.demo.R;

/**
 * author: sundong
 * created at 2017/1/18 13:57
 */
public class Demo8View extends FrameLayout implements View.OnClickListener {

    // 通过插值器让动画更自然
    private static final DecelerateInterpolator DECCELERATE_INTERPOLATOR = new DecelerateInterpolator();
    private static final AccelerateDecelerateInterpolator ACCELERATE_DECELERATE_INTERPOLATOR = new AccelerateDecelerateInterpolator();
    private static final OvershootInterpolator OVERSHOOT_INTERPOLATOR = new OvershootInterpolator(4);

    private boolean isChecked;
    private AnimatorSet animatorSet;
    private ImageView mIvStar;
    private DotsView mVDotsView;
    private CircleView mVCircle;

    public Demo8View(Context context) {
        super(context);
        init();
    }

    public Demo8View(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public Demo8View(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.demo8_view, this, true);
        mIvStar = (ImageView) view.findViewById(R.id.ivStar);
        mVDotsView = (DotsView) view.findViewById(R.id.vDotsView);
        mVCircle = (CircleView) view.findViewById(R.id.vCircle);
        setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        isChecked = !isChecked;
//        mIvStar.setImageResource(isChecked ? R.drawable.ic_star_rate_on : R.drawable.ic_star_rate_off);
        if (animatorSet != null) {
            animatorSet.cancel();
        }
        if (isChecked) {
            // 给不同的View设置不同的属性动画
            mIvStar.animate().cancel();
            mIvStar.setScaleX(0);
            mIvStar.setScaleY(0);
            mVCircle.setInnerCircleRadiusProgress(0);
            mVCircle.setOuterCircleRadiusProgress(0);
            mVDotsView.setCurrentProgress(0);
            animatorSet = new AnimatorSet();
            ObjectAnimator outerCircleAnimator = ObjectAnimator.ofFloat(mVCircle, CircleView.OUTER_CIRCLE_RADIUS_PROGRESS, 0.1f, 1f);
            outerCircleAnimator.setDuration(250);
            outerCircleAnimator.setInterpolator(DECCELERATE_INTERPOLATOR);
            ObjectAnimator innerCircleAnimator = ObjectAnimator.ofFloat(mVCircle, CircleView.INNER_CIRCLE_RADIUS_PROGRESS, 0.1f, 1f);
            innerCircleAnimator.setDuration(200);
            innerCircleAnimator.setStartDelay(200);
            innerCircleAnimator.setInterpolator(DECCELERATE_INTERPOLATOR);
            ObjectAnimator starScaleYAnimator = ObjectAnimator.ofFloat(mIvStar, ImageView.SCALE_Y, 0.2f, 1f);
            starScaleYAnimator.setDuration(350);
            starScaleYAnimator.setStartDelay(250);
            starScaleYAnimator.setInterpolator(OVERSHOOT_INTERPOLATOR);
            ObjectAnimator starScaleXAnimator = ObjectAnimator.ofFloat(mIvStar, ImageView.SCALE_X, 0.2f, 1f);
            starScaleXAnimator.setDuration(350);
            starScaleXAnimator.setStartDelay(250);
            starScaleXAnimator.setInterpolator(OVERSHOOT_INTERPOLATOR);
            ObjectAnimator dotsAnimator = ObjectAnimator.ofFloat(mVDotsView, DotsView.DOTS_PROGRESS, 0, 1f);
            dotsAnimator.setDuration(900);
            dotsAnimator.setStartDelay(50);
            dotsAnimator.setInterpolator(ACCELERATE_DECELERATE_INTERPOLATOR);
            animatorSet.playTogether(
                    outerCircleAnimator,
                    innerCircleAnimator,
                    starScaleYAnimator,
                    starScaleXAnimator,
                    dotsAnimator
            );
            animatorSet.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationCancel(Animator animation) {
                    mVCircle.setInnerCircleRadiusProgress(0);
                    mVCircle.setOuterCircleRadiusProgress(0);
                    mVDotsView.setCurrentProgress(0);
                    mIvStar.setScaleX(1);
                    mIvStar.setScaleY(1);
                }
            });
            animatorSet.start();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mIvStar.animate().scaleX(0.7f).scaleY(0.7f).setDuration(150).setInterpolator(DECCELERATE_INTERPOLATOR);
                setPressed(true);
                break;
            case MotionEvent.ACTION_MOVE:
                float x = event.getX();
                float y = event.getY();
                boolean isInside = (x > 0 && x < getWidth() && y > 0 && y < getHeight());
                if (isPressed() != isInside) {
                    setPressed(isInside);
                }
                break;
            case MotionEvent.ACTION_UP:
                mIvStar.animate().scaleX(1).scaleY(1).setInterpolator(DECCELERATE_INTERPOLATOR);
                if (isPressed()) {
                    performClick();
                    setPressed(false);
                }
                break;
        }
        return true;
    }
}