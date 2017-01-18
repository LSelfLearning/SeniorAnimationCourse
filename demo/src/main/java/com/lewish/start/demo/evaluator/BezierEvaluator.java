package com.lewish.start.demo.evaluator;

import android.animation.TypeEvaluator;
import android.graphics.PointF;

/**
 * author: sundong
 * created at 2017/1/18 10:30
 */
public class BezierEvaluator implements TypeEvaluator<PointF> {

    private PointF pointF1;
    private PointF pointF2;

    public BezierEvaluator(PointF pointF1, PointF pointF2) {
        this.pointF1 = pointF1;
        this.pointF2 = pointF2;
    }

    /**
     * 利用公式计算三阶贝塞尔曲线，具体的参数可以参考视频中的讲解
     *
     * @param time
     * @param startValue
     * @param endValue
     * @return
     */
    @Override
    public PointF evaluate(float time, PointF startValue, PointF endValue) {
        float timeLeft = 1.0f - time;
        PointF point = new PointF();
        point.x = timeLeft * timeLeft * timeLeft * (startValue.x)
                + 3 * timeLeft * timeLeft * time * (pointF1.x)
                + 3 * timeLeft * time * time * (pointF2.x)
                + time * time * time * (endValue.x);
        point.y = timeLeft * timeLeft * timeLeft * (startValue.y)
                + 3 * timeLeft * timeLeft * time * (pointF1.y)
                + 3 * timeLeft * time * time * (pointF2.y)
                + time * time * time * (endValue.y);
        return point;
    }
}