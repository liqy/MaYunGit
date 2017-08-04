package com.bwie.mayungit;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 *
 * 查阅资料：https://github.com/GcsSloop/AndroidNote
 *
 *
 *
 *
 */
public class MainActivity extends AppCompatActivity implements MyView.OnMyClickListener {

    MyView myView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myView = (MyView) findViewById(R.id.myView);
        myView.setListener(this);// 注册点击事件


    }


    @Override
    public void onCircleInnerClick(View var1) {
        Toast.makeText(this, "圆内", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCircleOuterClick(View var1) {//TODO 点击圆外

    }

    @Override
    public void onWriteClick(View var1) {//TODO 点击空白区域

    }


}
