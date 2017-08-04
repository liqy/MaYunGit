package com.bwie.mayungit;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.support.v4.view.MotionEventCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * 简单触摸图片放大、位移、旋转
 */
public class ZoomImageView extends View {

    private Bitmap mPicture;//加载操作图片
    private Matrix matrix;

    private int mTouchX, mTouchY;
    private int mTouchMode;

    private double mLastDistance;
    private double mLastRotate;

    private static final int SINGLE_TOUCH_MODE = 1;//单点模式
    private static final int MUL_TOUCH_MODE = 2;//多点模式

    public ZoomImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public ZoomImageView(Context context) {
        this(context, null);
    }

    private void init(AttributeSet attrs) {
        matrix = new Matrix();
        if (attrs != null) {
            TypedArray array = getContext().obtainStyledAttributes(attrs, R.styleable.ZoomImageView);
            Drawable drawable = array.getDrawable(R.styleable.ZoomImageView_zoom_src);
            if (drawable != null) {
                mPicture = drawableToBitmap(drawable);
            }
            array.recycle();
        }
    }

    public void setPicture(Bitmap mPicture) {
        this.mPicture = mPicture;
        invalidate();
    }

    /**
     * 自定义控件待实现方法:测量
     *
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    /**
     * 自定义控件待实现方法：绘制
     *
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mPicture == null) {
            return;
        }
        canvas.drawBitmap(mPicture, matrix, null);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = MotionEventCompat.getActionMasked(event);
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                //获取坐标值
                mTouchX = (int) event.getX();
                mTouchY = (int) event.getY();
                //单点模式
                mTouchMode = SINGLE_TOUCH_MODE;
                break;
            case MotionEvent.ACTION_MOVE:
                //获取跟随手指移动的坐标点
                float moveX = (int) event.getX();
                float moveY = (int) event.getY();

                if (mTouchMode == SINGLE_TOUCH_MODE) {
                    //位移
                    int distanceX = (int) (moveX - mTouchX);
                    int distanceY = (int) (moveY - mTouchY);
                    matrix.postTranslate(distanceX, distanceY);
                    invalidate();
                    mTouchX = (int) moveX;
                    mTouchY = (int) moveY;
                } else if (mTouchMode == MUL_TOUCH_MODE) {
                    //中心点
                    float centerX = getPointCenterX(event);
                    float centerY = getPointCenterY(event);
                    //旋转
                    double currentRotate = getRotation(event);
                    matrix.postRotate((float) (currentRotate - mLastRotate), centerX, centerY);

                    double currentDistance = getDistance(event);

                    if (mLastDistance > 0) {
                        //缩放
                        float scale = (float) (currentDistance / mLastDistance);
                        if (Math.abs(scale) > 0) {
                            matrix.postScale(scale, scale, centerX, centerY);
                            invalidate();
                        }
                    }

                    mLastDistance = currentDistance;
                    mLastRotate = currentRotate;
                }
                break;
            case MotionEvent.ACTION_POINTER_DOWN://多指触摸屏幕的时候会触发这个事件
                if (event.getActionIndex() > 1) {
                    break;
                }

                mTouchMode = MUL_TOUCH_MODE;//多点模式

                mLastDistance = getDistance(event);
                mLastRotate = getRotation(event);
                break;
            case MotionEvent.ACTION_POINTER_UP://与ACTION_POINTER_DOWN，成对存在，一个结束，一个开始
                mTouchMode = 0;
                break;
            case MotionEvent.ACTION_UP://触发事件结束的时候必定触发
            case MotionEvent.ACTION_CANCEL:
                mTouchX = 0;
                mTouchY = 0;
                break;
        }
        return true;
    }

    /**
     * 两点间距离
     */
    private double getDistance(MotionEvent event) {
        float dx = event.getX(1) - event.getX(0);
        float dy = event.getY(1) - event.getY(0);
        return Math.sqrt(dx * dx + dy * dy);//求距离
    }

    /**
     * 两点角度
     */
    private double getRotation(MotionEvent event) {
        float delta_x = (event.getX(0) - event.getX(1));
        float delta_y = (event.getY(0) - event.getY(1));
        double radians = Math.atan2(delta_y, delta_x);
        return Math.toDegrees(radians);
    }

    /**
     * 两点X中心点
     */
    private float getPointCenterX(MotionEvent event) {
        return Math.abs((event.getX(1) + event.getX(0)) / 2);
    }

    /**
     * 两点Y中心点
     */
    private float getPointCenterY(MotionEvent event) {
        return Math.abs((event.getY(1) + event.getY(0)) / 2);
    }

    public Bitmap drawableToBitmap(Drawable drawable) {
        Bitmap bitmap = Bitmap.createBitmap(

                drawable.getIntrinsicWidth(),

                drawable.getIntrinsicHeight(),

                drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888

                        : Bitmap.Config.RGB_565);

        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);

        return bitmap;

    }

}
