package com.linyang.pay.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.linyang.pay.R;
import com.linyang.pay.common.AppConfig;
import com.linyang.pay.net.RetrofitHelper;
import com.linyang.pay.pay.wxpay.WXPayOrder;
import com.linyang.pay.pay.wxpay.WXPayUtils;
import com.linyang.pay.util.LogUtil;
import com.linyang.pay.util.RxSchedulers;
import com.linyang.pay.util.ToastUtil;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * 描述:微信支付
 * Created by fzJiang on 2018-10-10
 */
public class WxPayActivity extends AppCompatActivity implements IWXAPIEventHandler {

    @BindView(R.id.wx_pay_and_sign_on_server_btn)
    Button mWxPayAndSignOnServerBtn;
    @BindView(R.id.wx_pay_and_sign_on_client_btn)
    Button mWxPayAndSignOnClientBtn;

    private IWXAPI mIWXAPI;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wx_pay);
        ButterKnife.bind(this);
        // 初始化微信API
        initWXAPI();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        mIWXAPI.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq req) {
        // 申请支付
        LogUtil.i("-------onReq--------" + req.openId + "," + req.transaction);
    }

    @Override
    public void onResp(BaseResp resp) {
        // 处理支付结果
        LogUtil.i("---------onResp-------------" + resp.getType() + "," + resp.openId + "," + resp.transaction + "," + resp.errCode + "," + resp.errStr);

        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            if (resp.errCode == 0) {
                ToastUtil.showToast(getApplicationContext(), "支付成功");
            } else {
                ToastUtil.showToast(getApplicationContext(), "支付失败:" + resp.errCode);
            }
        }
    }

    @OnClick({R.id.wx_pay_and_sign_on_server_btn, R.id.wx_pay_and_sign_on_client_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.wx_pay_and_sign_on_server_btn:
                toWXPayNotSign();
                break;

            case R.id.wx_pay_and_sign_on_client_btn:
                toWXPayAndSign();
                break;
        }
    }

    /**
     * 初始化微信API
     */
    private void initWXAPI() {
        mIWXAPI = WXAPIFactory.createWXAPI(this, null);
        mIWXAPI.registerApp(AppConfig.WX_PAY.APP_ID);
        mIWXAPI.handleIntent(getIntent(), this);
    }

    /**
     * 调起微信支付的方法,在服务端进行签名
     */
    private void toWXPayNotSign() {
//        商户系统和微信支付系统主要交互说明：
//
//        步骤1：用户在商户APP中选择商品，提交订单，选择微信支付。
//
//        步骤2：商户后台收到用户支付单，调用微信支付统一下单接口。参见【统一下单API】。
//
//        步骤3：统一下单接口返回正常的prepay_id，再按签名规范重新生成签名后，将数据传输给APP。参与签名的字段名为appId，partnerId，prepayId，nonceStr，timeStamp，package。注意：package的值格式为Sign=WXPay
//
//        步骤4：商户APP调起微信支付。api参见本章节【app端开发步骤说明】
//
//        步骤5：商户后台接收支付通知。api参见【支付结果通知API】
//
//        步骤6：商户后台查询支付结果。，api参见【查询订单API】

        // 先请求服务器 获取订单信息等参数,注意参数不能少
        RetrofitHelper.getMPService()
                .getWXPayOrder()
                .compose(RxSchedulers.applySchedulers())
                .subscribe(new Observer<WXPayOrder>() {

                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(WXPayOrder wxPayOrder) {
                        if (wxPayOrder != null) {
                            WXPayUtils.WXPayBuilder builder = new WXPayUtils.WXPayBuilder();
                            builder.setApi(mIWXAPI)
                                    .setAppId(wxPayOrder.getAppId())
                                    .setPartnerId(wxPayOrder.getPartnerId())
                                    .setPrepayId(wxPayOrder.getPrepayId())
                                    .setPackageValue(wxPayOrder.getPackageValue())
                                    .setNonceStr(wxPayOrder.getNonceStr())
                                    .setTimeStamp(wxPayOrder.getTimeStamp())
                                    .setSign(wxPayOrder.getSign())
                                    .build().toWXPayNotSign();
                        } else {
                            ToastUtil.showToast(getApplicationContext(), "获取订单信息失败");
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtil.showToast(getApplicationContext(), "获取订单信息失败:" + e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    /**
     * 调起微信支付的方法,在客户端进行签名
     */
    private void toWXPayAndSign() {
        // 先请求服务器 获取订单信息等参数,注意参数不能少
        RetrofitHelper.getMPService().getWXPayOrder()
                .compose(RxSchedulers.applySchedulers())
                .subscribe(new Observer<WXPayOrder>() {

                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(WXPayOrder wxPayOrder) {
                        if (wxPayOrder != null) {
                            WXPayUtils.WXPayBuilder builder = new WXPayUtils.WXPayBuilder();
                            builder.setApi(mIWXAPI)
                                    .setAppId(wxPayOrder.getAppId())
                                    .setPartnerId(wxPayOrder.getPartnerId())
                                    .setPrepayId(wxPayOrder.getPrepayId())
                                    .setPackageValue(wxPayOrder.getPackageValue())
                                    .build()
                                    .toWXPayAndSign("key");
                        } else {
                            ToastUtil.showToast(getApplicationContext(), "获取订单信息失败");
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        // 获取订单信息失败
                        ToastUtil.showToast(getApplicationContext(), "获取订单信息失败:" + e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
