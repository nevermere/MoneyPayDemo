package com.linyang.pay.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.linyang.pay.R;
import com.linyang.pay.common.AppConfig;
import com.linyang.pay.net.RetrofitHelper;
import com.linyang.pay.pay.unionpay.PayResult;
import com.linyang.pay.pay.unionpay.RSAUtil;
import com.linyang.pay.pay.unionpay.UnionPayOrder;
import com.linyang.pay.util.GsonUtil;
import com.linyang.pay.util.RxSchedulers;
import com.linyang.pay.util.ToastUtil;
import com.unionpay.UPPayAssistEx;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * 描述:银联支付
 * 步骤1：获取交易流水号,即TN
 * 步骤2：通过银联工具类启动支付插件
 * 步骤3：处理银联手机支付控件返回的支付结果
 * Created by fzJiang on 2018-10-12
 */

public class UnionPayActivity extends AppCompatActivity {

    @BindView(R.id.get_tn_btn)
    Button mGetTnBtn;
    @BindView(R.id.union_pay_btn)
    Button mUnionPayBtn;
    @BindView(R.id.tn_text)
    EditText mTnText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_union_pay);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.get_tn_btn, R.id.union_pay_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.get_tn_btn:// 获取交易流水号
                getUnionPayTN();
                break;

            case R.id.union_pay_btn:// 发起银联支付
                String tn = mTnText.getText().toString();
                if (!TextUtils.isEmpty(tn)) {
                    startUnionPay(tn);
                } else {
                    ToastUtil.showToast(getApplicationContext(), "请先获取交易流水号");
                }
                break;
        }
    }

    /**
     * 获取交易流水号  http://101.231.204.84:8091/sim/getacptn
     */

    private void getUnionPayTN() {
        RetrofitHelper.getMPService()
                .getUnionPayTN()
                .compose(RxSchedulers.applySchedulers())
                .subscribe(new Observer<UnionPayOrder>() {

                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(UnionPayOrder order) {
                        if (order != null && !TextUtils.isEmpty(order.getTn())) {
                            mTnText.setText(order.getTn());
                        } else {
                            ToastUtil.showToast(getApplicationContext(), "交易流水号为空,发起支付失败");
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtil.showToast(getApplicationContext(), "获取交易流水号失败");
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    /**
     * 发起银联支付
     *
     * @param tn 交易流水号
     */
    private void startUnionPay(String tn) {
        // 步骤2：通过银联工具类启动支付插件
        int ret = UPPayAssistEx.startPay(UnionPayActivity.this, null, null, tn, AppConfig.UNION_PAY.SANDBOX);
        // 插件需进行更新或者未安装
        if (ret == AppConfig.UNION_PAY.PLUGIN_NEED_UPGRADE || ret == AppConfig.UNION_PAY.PLUGIN_NOT_INSTALLED) {
            AlertDialog.Builder builder = new AlertDialog.Builder(UnionPayActivity.this);
            builder.setTitle("提示");
            builder.setMessage("完成购买需要安装银联支付控件，是否安装？");
            builder.setNegativeButton("确定", (dialog, which) -> {
                // 安装插件
                UPPayAssistEx.installUPPayPlugin(UnionPayActivity.this);
                dialog.dismiss();
            });
            builder.setPositiveButton("取消", (dialog, which) -> dialog.dismiss());
            builder.create().show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // 处理银联手机支付控件返回的支付结果
        if (data == null || data.getExtras() == null) {
            return;
        }

        // 支付控件返回字符串:success、fail、cancel 分别代表支付成功，支付失败，支付取消
        String msg = "";
        String payResult = data.getExtras().getString("pay_result");
        if (TextUtils.isEmpty(payResult)) {
            return;
        }

        switch (payResult) {
            case AppConfig.UNION_PAY.PAY_SUCCESS:// 支付成功
                // 结果数据验签,建议不验签，直接去后台查询交易结果
                String resultData = data.getExtras().getString("result_data");
                if (!TextUtils.isEmpty(resultData)) {
                    PayResult result = GsonUtil.GsonToBean(resultData, PayResult.class);
                    if (result != null) {
                        // 此处的verify建议送去商户后台做验签,如要放在手机端验，则代码必须支持更新证书
                        boolean ret = RSAUtil.verify(result.getData(), result.getSign(), AppConfig.UNION_PAY.SANDBOX);
                        msg = ret ? "支付成功" : "支付失败";
                    } else {
                        // 无支付结果
                        msg = "支付失败！";
                    }
                } else {
                    // 无支付结果
                    msg = "支付失败！";
                }
                break;

            case AppConfig.UNION_PAY.PAY_FAILED:// 支付失败
                msg = "支付失败！";
                break;

            case AppConfig.UNION_PAY.PAY_CANCEL:// 支付取消
                msg = "用户取消了支付";
                break;

            default:
                break;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("支付结果通知");
        builder.setMessage(msg);
        builder.setNegativeButton("确定", (dialog, which) -> dialog.dismiss());
        builder.create().show();
    }
}
