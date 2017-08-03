package com.bwie.mayungit;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.Region;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by liqy on 2017/8/3.
 */

public class MyView extends View {

    Region region;//圆形区域
    Path path;//圆形路径
    Path rpath;//矩形路径
    Region rregion;
    Paint paint;//画笔
    int x;
    int y;

    int r;
    OnMyClickListener listener;//定义事件监听

    public void setListener(OnMyClickListener listener) {
        this.listener = listener;
    }

    /**
     * 只有一个参数
     *
     * @param context
     */
    public MyView(Context context) {
        super(context);
        init(context, null);
    }

    private void init(Context context, AttributeSet attrs) {

        paint = new Paint();

        //圆形
        region = new Region();
        path = new Path();

        //矩形
        rpath = new Path();
        rregion = new Region();

        //首先在values 中定义attrs.xml ,节点declare-styleable
        if (attrs != null) {//处理自定义属性
            TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.MyView);
            r = (int) array.getFloat(R.styleable.MyView_MYR, 100);
            int color = array.getColor(R.styleable.MyView_MYColor, Color.GREEN);
            paint.setColor(color);
            array.recycle();//一定记得销毁
        }
    }


    /**
     * 两个参数
     *
     * @param context
     * @param attrs
     */
    public MyView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (event.getAction() == MotionEvent.ACTION_DOWN) {

            //获取点击坐标
            int x = (int) event.getX();
            int y = (int) event.getY();

            //判断点击区域
            if (region.contains(x, y)) {
                if (listener != null) {
                    listener.onCircleInnerClick(this);
                }
            } else if (rregion.contains(x, y)) {
                if (listener != null) {
                    listener.onCircleOuterClick(this);
                }
            } else {
                if (listener != null) {
                    listener.onWriteClick(this);
                }
            }
        }

        return super.onTouchEvent(event);

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        //绘制圆的路径

        x = w / 2;
        y = h / 2;

        path.addCircle(x, y, r, Path.Direction.CW);//绘制圆形路径

        rpath.addRect(x - r, y - r, x + r, y + r, Path.Direction.CW);//正方形路径

        //设置圆形区域
        Region region1 = new Region(0, 0, w, h);//整个屏幕区域

        region.setPath(path, region1);//圆形区域

        rregion.setPath(rpath, region1);//矩形区域
    }

    /**
     * 绘制
     *
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Path tempRPath = rpath;//将圆绘制到画布上

        canvas.drawPath(tempRPath, paint);//矩形

        //绘制背景图片
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.bg);
        Rect src = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());//图片大小
        Rect dst = new Rect(x - r, y - r, x + r, y + r);//屏幕区域
        canvas.drawBitmap(bitmap, src, dst, paint);

        Path tempPath = path;//将圆绘制到画布上
        paint.setColor(Color.RED);

        canvas.drawPath(tempPath, paint);

        //绘制文字
        paint.setColor(Color.BLACK);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTextSize(32);
        canvas.drawText("圆心", x, y, paint);
    }

    /**
     * 布局
     *
     * @param changed
     * @param left
     * @param top
     * @param right
     * @param bottom
     */
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    /**
     * 测量
     *
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }


    /**
     * 点击事件监听接口
     */
    public interface OnMyClickListener {
        void onCircleInnerClick(View var1);

        void onCircleOuterClick(View var1);

        void onWriteClick(View var1);
    }

}
