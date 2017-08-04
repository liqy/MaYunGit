package com.bwie.mayungit;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

/**
 * Created by liqy on 2017/8/4.
 */

public class CircleView extends View {

    float x = 200;
    float y = 200;
    float radius = 100;

    float cx = 100;
    float cy = 100;

    boolean isCircle;

    public CircleView(Context context) {
        super(context);
    }

    public CircleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        x = event.getX();
        y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN://只执行一次
                isCircle = !isCircle(x,y)&&isRect(x, y);
                if (isCircle) {
                    Toast.makeText(getContext(), "点击圆", Toast.LENGTH_SHORT).show();
                }
                break;
            case MotionEvent.ACTION_MOVE://连续执行
                if (isCircle) {
                    //跳转圆心的位置
                    cx = x;
                    cy = y;
                    invalidate();//刷新界面，调用onDraw
                }
                break;
            default:
                break;
        }


        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint paint = new Paint();
        paint.setColor(Color.GRAY);

        canvas.drawRect(cx - radius, cy - radius, cx + radius, cy + radius, paint);

        paint.setColor(Color.BLUE);

        canvas.drawCircle(cx, cy, radius, paint);

    }

    /**
     * 判断是否在矩形内
     * @param downX
     * @param downY
     * @return
     */
    private boolean isRect(float downX, float downY) {
        boolean isX=downX<=cx+radius&&downX>=cx-radius;
        boolean isy=downY<=cy+radius&&downY>=cy-radius;
        return isX&&isy;
    }

    //判断当前按下去的位置是否在圆的里面
    private boolean isCircle(float downX, float downY) {
        //运用勾股定理 判断当前点的位置的坐标 计算出来的距离圆心的距离是否大于圆的半径 如果大于圆的半径的话 那么就是大于 否则就是小鱼
        //点击的位置减去原来的位置 算出来的是中间到圆心点之间的距离 进行判断
        float sqrt = (float) Math.sqrt((downX - cx) * (downX - cx) + (downY - cy) * (downY - cy));
        if (sqrt <= radius) {
            return true;
        }
        return false;
    }
}
