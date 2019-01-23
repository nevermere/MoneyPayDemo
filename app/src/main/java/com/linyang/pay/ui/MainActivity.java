package com.linyang.pay.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.linyang.pay.R;
import com.linyang.pay.util.ArmsUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 描述:主页
 * Created by fzJiang on 2018-10-10
 */
public class MainActivity extends AppCompatActivity {

    @BindView(R.id.wx_pay_btn)
    Button mWxPayBtn;
    @BindView(R.id.ali_pay_btn)
    Button mAliPayBtn;
    @BindView(R.id.union_pay_btn)
    Button mUnionPayBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.ali_pay_btn, R.id.wx_pay_btn, R.id.union_pay_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ali_pay_btn:// 支付宝支付
                ArmsUtils.startActivity(this, AliPayActivity.class);
                break;
            case R.id.wx_pay_btn:// 微信支付
                ArmsUtils.startActivity(this, WxPayActivity.class);
                break;
            case R.id.union_pay_btn:// 银联支付
                ArmsUtils.startActivity(this, UnionPayActivity.class);
                break;
        }
    }
}
