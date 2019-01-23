package com.linyang.pay.ui;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.alipay.sdk.app.PayTask;
import com.linyang.pay.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 描述:网页支付界面
 * Created by fzJiang on 2018-10-10
 */
public class H5PayActivity extends AppCompatActivity {

    @BindView(R.id.h5pay_web_view)
    WebView mH5payWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_h5_pay);
        ButterKnife.bind(this);
        // 初始化支付网页,并拉起支付
        initH5PayView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mH5payWebView != null) {
            mH5payWebView.removeAllViews();
            mH5payWebView.destroy();
            mH5payWebView = null;
        }
    }

    /**
     * 初始化支付网页,并拉起支付
     */
    @SuppressLint("SetJavaScriptEnabled")
    private void initH5PayView() {
        // 可以填写任一支持支付宝支付的网站（如淘宝或一号店），在网站中下订单并唤起支付宝；
        // 或者直接填写由支付宝文档提供的“网站 Demo”生成的订单地址:如 https://mclient.alipay.com/h5Continue.htm?h5_route_token=303ff0894cd4dccf591b089761dexxxx）进行测试
        String url = "http://m.taobao.com";

        WebSettings settings = mH5payWebView.getSettings();
        settings.setRenderPriority(WebSettings.RenderPriority.HIGH);
        settings.setJavaScriptEnabled(true);
        settings.setSavePassword(false);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setMinimumFontSize(settings.getMinimumFontSize() + 8);
        settings.setAllowFileAccess(false);
        settings.setTextSize(WebSettings.TextSize.NORMAL);
        mH5payWebView.setVerticalScrollbarOverlay(true);
        mH5payWebView.setWebViewClient(new MyWebViewClient());
        mH5payWebView.loadUrl(url);
    }

    /**
     * 拦截 URL 唤起支付宝，可以参考它实现自定义的 URL 拦截逻辑。
     */
    private class MyWebViewClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(final WebView view, String url) {
            if (!(url.startsWith("http") || url.startsWith("https"))) {
                return true;
            }

            // 推荐采用的新的二合一接口(payInterceptorWithUrl),只需调用一次
            final PayTask task = new PayTask(H5PayActivity.this);
            boolean isIntercepted = task.payInterceptorWithUrl(url, true, result -> {
                final String url1 = result.getReturnUrl();
                if (!TextUtils.isEmpty(url1)) {
                    H5PayActivity.this.runOnUiThread(() -> view.loadUrl(url1));
                }
            });
            //判断是否成功拦截,若成功拦截，则无需继续加载该URL；否则继续加载
            if (!isIntercepted) {
                view.loadUrl(url);
            }
            return true;
        }
    }


    @Override
    public void onBackPressed() {
        if (mH5payWebView.canGoBack()) {
            mH5payWebView.goBack();
        } else {
            finish();
        }
    }
}
