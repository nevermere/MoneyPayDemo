package com.linyang.pay.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import com.alipay.sdk.app.AuthTask;
import com.alipay.sdk.app.PayTask;
import com.linyang.pay.R;
import com.linyang.pay.common.AppConfig;
import com.linyang.pay.pay.ailpay.AuthResult;
import com.linyang.pay.pay.ailpay.PayResult;
import com.linyang.pay.pay.ailpay.OrderInfoUtil2_0;
import com.linyang.pay.util.ArmsUtils;
import com.linyang.pay.util.RxSchedulers;
import com.linyang.pay.util.ToastUtil;

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * 描述:支付宝支付
 * Created by fzJiang on 2018-10-12
 */
public class AliPayActivity extends AppCompatActivity {

    @BindView(R.id.h5_auth_btn)
    Button mH5AuthBtn;
    @BindView(R.id.h5_pay_btn)
    Button mH5PayBtn;
    @BindView(R.id.server_pay_btn)
    Button mServerPayBtn;
    @BindView(R.id.get_version_btn)
    Button mGetVersionBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ali_pay);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.h5_auth_btn, R.id.h5_pay_btn, R.id.server_pay_btn, R.id.get_version_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.h5_auth_btn:// 支付宝账户授权:网络获取授权所需的配置信息-->支付宝API进行授权
                final String[] temp = new String[1];
                getAuthInfo()
                        .skipWhile((String authInfo) -> {
                            // 需授权的信息为空，则不继续进行授权业务
                            if (!TextUtils.isEmpty(authInfo)) {
                                temp[0] = authInfo;
                                return false;
                            }
                            return true;
                        })
                        .compose(this.authV2(temp[0]))
                        .compose(RxSchedulers.applySchedulers());

//                authV2().subscribe(new Observer<AuthResult>() {
//
//                    @Override
//                    public void onSubscribe(Disposable d) {
//
//                    }
//
//                    @Override
//                    public void onNext(AuthResult authResult) {
//                        LogUtil.i("支付宝账户授权结果:" + authResult);
//                        // 判断resultStatus 为“9000”且result_code
//                        // 为“200”则代表授权成功，具体状态码代表含义可参考授权接口文档
//                        if (TextUtils.equals(authResult.getResultStatus(), "9000") && TextUtils.equals(authResult.getResultCode(), "200")) {
//                            // 获取alipay_open_id，调支付时作为参数extern_token 的value传入，则支付账户为该授权账户
//                            ToastUtil.showToast(getApplicationContext(), "授权成功\n" + String.format("authCode:%s", authResult.getAuthCode()));
//                        } else {
//                            // 其他状态值则为授权失败
//                            ToastUtil.showToast(getApplicationContext(), "授权失败\n" + String.format("authCode:%s", authResult.getAuthCode()));
//                        }
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        // 授权出现错误
//                        ToastUtil.showToast(getApplicationContext(), "授权出现错误：" + e.getMessage());
//                    }
//
//                    @Override
//                    public void onComplete() {
//
//                    }
//                });
                break;

            case R.id.h5_pay_btn:// 网页支付,原生的H5（手机网页版支付切本地支付） 【对应页面网页支付按钮】
                ArmsUtils.startActivity(this, H5PayActivity.class);
                break;

            case R.id.server_pay_btn:// 申请后台支付
                // 展示支付宝的整个支付流程；加签过程直接放在客户端完成；privateKey等数据严禁放在客户端，加签过程务必要放在服务端完成；
                // 防止商户私密数据泄露，造成不必要的资金损失，及面临各种安全风险；
                payV2().subscribe(new Observer<PayResult>() {

                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(PayResult payResult) {
                        //  对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。

                        // 同步返回需要验证的信息
                        String resultInfo = payResult.getResult();
                        String resultStatus = payResult.getResultStatus();

                        // 判断resultStatus 为9000则代表支付成功
                        if (TextUtils.equals(resultStatus, "9000")) {
                            // 支付成功
                            ToastUtil.showToast(getApplicationContext(), "支付成功");
                        } else {
                            // 支付失败
                            ToastUtil.showToast(getApplicationContext(), "支付失败");
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        // 支付失败
                        ToastUtil.showToast(getApplicationContext(), "支付失败：" + e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
                break;

            case R.id.get_version_btn:// 获取SDK版本号
                getSDKVersion();
                break;
        }
    }

    /**
     * 网络获取授权所需的配置信息
     *
     * @return
     */
    private Observable<String> getAuthInfo() {
        return Observable.create((ObservableOnSubscribe<String>) emitter -> {
            // 检查配置
            if (!TextUtils.isEmpty(AppConfig.ALI_PAY.PID)
                    && !TextUtils.isEmpty(AppConfig.ALI_PAY.APP_ID)
                    && !TextUtils.isEmpty(AppConfig.ALI_PAY.RSA2_PRIVATE)
                    && !TextUtils.isEmpty(AppConfig.ALI_PAY.TARGET_ID)) {
                // 这里只是为了方便直接向商户展示支付宝的整个支付流程；所以Demo中加签过程直接放在客户端完成；
                // 真实App里，privateKey等数据严禁放在客户端，加签过程务必要放在服务端完成；
                // 防止商户私密数据泄露，造成不必要的资金损失，及面临各种安全风险；
                // authInfo的获取必须来自服务端；
                boolean rsa2 = (AppConfig.ALI_PAY.RSA2_PRIVATE.length() > 0);
                Map<String, String> authInfoMap =
                        OrderInfoUtil2_0.buildAuthInfoMap(AppConfig.ALI_PAY.PID, AppConfig.ALI_PAY.APP_ID, AppConfig.ALI_PAY.TARGET_ID, rsa2);
                String info = OrderInfoUtil2_0.buildOrderParam(authInfoMap);

                String sign = OrderInfoUtil2_0.getSign(authInfoMap, AppConfig.ALI_PAY.RSA2_PRIVATE, rsa2);
                String authInfo = info + "&" + sign;
                emitter.onNext(authInfo);
            } else {
                emitter.onError(new Throwable("需要配置PARTNER |APP_ID| RSA_PRIVATE| TARGET_ID"));
            }
            emitter.onComplete();
        }).subscribeOn(Schedulers.io());
    }

    /**
     * 支付宝账户授权业务
     *
     * @param authInfo 加密的授权信息
     * @return {@link AuthResult}
     */
    private <T> ObservableTransformer<T, AuthResult> authV2(String authInfo) {
        return upstream -> upstream.flatMap((Function<T, ObservableSource<? extends AuthResult>>) t -> {
            return Observable.create((ObservableOnSubscribe<AuthResult>) emitter -> {
                // 构造AuthTask 对象
                AuthTask authTask = new AuthTask(AliPayActivity.this);
                // 调用授权接口，获取授权结果
                Map<String, String> result = authTask.authV2(authInfo, true);
                AuthResult authResult = null;
                if (result != null) {
                    authResult = new AuthResult(result, true);
                } else {
                    emitter.onError(new Throwable("授权失败,授权结果为空"));
                }

                if (authResult != null) {
                    emitter.onNext(authResult);
                } else {
                    emitter.onError(new Throwable("授权失败,授权结果为空"));
                }

                emitter.onNext(authResult);
                emitter.onComplete();
            }).subscribeOn(Schedulers.io());
        });
    }

    /**
     * 订单信息orderInfo
     * 申请后台支付
     *
     * @return {@link PayResult}
     */
    private Observable<PayResult> payV2() {
        return Observable.create((ObservableOnSubscribe<PayResult>) emitter -> {
            if (!TextUtils.isEmpty(AppConfig.ALI_PAY.APP_ID) && !TextUtils.isEmpty(AppConfig.ALI_PAY.RSA2_PRIVATE)) {
                // 获取订单信息orderInfo
                boolean rsa2 = (AppConfig.ALI_PAY.RSA2_PRIVATE.length() > 0);
                Map<String, String> params = OrderInfoUtil2_0.buildOrderParamMap(AppConfig.ALI_PAY.APP_ID, rsa2);
                String orderParam = OrderInfoUtil2_0.buildOrderParam(params);

                String sign = OrderInfoUtil2_0.getSign(params, AppConfig.ALI_PAY.RSA2_PRIVATE, rsa2);

                //  注：订单信息orderInfo的获取必须来自服务端
                String orderInfo = orderParam + "&" + sign;

                PayTask alipay = new PayTask(AliPayActivity.this);
                Map<String, String> result = alipay.payV2(orderInfo, true);
                PayResult payResult = null;

                if (result != null) {
                    payResult = new PayResult(result);
                } else {
                    emitter.onError(new Throwable("支付失败,支付结果为空"));
                }

                if (payResult != null) {
                    emitter.onNext(payResult);
                } else {
                    emitter.onError(new Throwable("支付失败,支付结果为空"));
                }
            } else {
                emitter.onError(new Throwable("需要配置 |APP_ID| RSA_PRIVATE"));
            }
        }).compose(RxSchedulers.applySchedulers());
    }

    /**
     * 获取SDK版本号
     */
    public void getSDKVersion() {
        PayTask payTask = new PayTask(this);
        String version = payTask.getVersion();
        ToastUtil.showToast(getApplicationContext(), version);
    }
}
