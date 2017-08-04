package com.bwie.mayungit;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by liqy on 2017/8/4.
 */

public class TitleBar extends RelativeLayout {

    TextView title;
    TextView back;

    public BarClickListener listener;

    public void setListener(BarClickListener listener) {
        this.listener = listener;
    }

    public TitleBar(@NonNull Context context) {
        super(context);
        initView(null);
    }

    public TitleBar(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(attrs);
    }

    private void initView(AttributeSet attrs) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.bar_title, this);
        back = (TextView) view.findViewById(R.id.bar_back);
        title = (TextView) view.findViewById(R.id.bar_title);

        back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener!=null){
                    listener.backListener(v);
                }
            }
        });

        title.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener!=null){
                    listener.titleListener(v);
                }
            }
        });

        if (attrs != null) {
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.TitleBar);

            String str = a.getString(R.styleable.TitleBar_BarTitle);

            if (TextUtils.isEmpty(str)) {
                title.setText(getResources().getString(R.string.app_name));
            } else {
                title.setText(str);
            }

            a.recycle();
        }


    }

    public interface BarClickListener{
        public void backListener(View view);
        public void titleListener(View view);
    }

}
