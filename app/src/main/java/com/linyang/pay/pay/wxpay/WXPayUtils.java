package com.linyang.pay.pay.wxpay;

import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;

import java.security.MessageDigest;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;

/**
 * 微信支付工具类
 * Created by chaohx on 2017/7/5.
 */
public class WXPayUtils {

    private WXPayBuilder builder;

    private WXPayUtils(WXPayBuilder builder) {
        this.builder = builder;
    }

    /**
     * 调起微信支付的方法,不需要在客户端签名
     **/
    public void toWXPayNotSign() {
        //这里注意要放在子线程
        Runnable payRunnable = () -> {
            PayReq request = new PayReq(); //调起微信APP的对象
            // 下面是设置必要的参数，也就是前面说的参数,这几个参数从何而来请看上面说明
            request.appId = builder.getAppId();
            request.partnerId = builder.getPartnerId();
            request.prepayId = builder.getPrepayId();
            request.packageValue = builder.getPackageValue();
            request.nonceStr = builder.getNonceStr();
            request.timeStamp = builder.getTimeStamp();
            request.sign = builder.getSign();
            // 发送调起微信的请求
            builder.getApi().sendReq(request);
        };
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    /**
     * 调起微信支付的方法,需要在客户端签名
     *
     * @param key 加密秘钥
     */
    public void toWXPayAndSign(String key) {
        // 这里注意要放在子线程
        Runnable payRunnable = () -> {
            PayReq request = new PayReq(); //调起微信APP的对象
            // 下面是设置必要的参数，也就是前面说的参数,这几个参数从何而来请看上面说明
            request.appId = builder.getAppId();
            request.partnerId = builder.getPartnerId();
            request.prepayId = builder.getPrepayId();
            request.packageValue = builder.getPackageValue();
            request.nonceStr = builder.getNonceStr();
            request.timeStamp = builder.getTimeStamp();
            request.sign = builder.getSign();
            // 签名
            LinkedHashMap<String, String> signParams = new LinkedHashMap<>();
            signParams.put("appid", request.appId);
            signParams.put("noncestr", request.nonceStr);
            signParams.put("package", request.packageValue);
            signParams.put("partnerid", request.partnerId);
            signParams.put("prepayid", request.prepayId);
            signParams.put("timestamp", request.timeStamp);
            request.sign = genPackageSign(signParams, key);
            // 发送调起微信的请求
            builder.getApi().sendReq(request);
        };
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    /**
     * 调起微信APP支付，生成签名
     *
     * @param params 参数
     * @param key    秘钥
     * @return
     */
    private String genPackageSign(LinkedHashMap<String, String> params, String key) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            sb.append(entry.getKey());
            sb.append('=');
            sb.append(entry.getValue());
            sb.append('&');
        }
        sb.append("key=");
        sb.append(key);
        return getMessageDigest(sb.toString().getBytes());
    }

    /**
     * md5加密
     *
     * @param buffer 加密的数组
     * @return
     */
    private String getMessageDigest(byte[] buffer) {
        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        try {
            MessageDigest mdTemp = MessageDigest.getInstance("MD5");
            mdTemp.update(buffer);
            byte[] md = mdTemp.digest();
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (byte byte0 : md) {
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str).toUpperCase();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 获取随机数
     *
     * @return
     */
    public String genNonceStr() {
        Random random = new Random();
        return getMessageDigest(String.valueOf(random.nextInt(10000)).getBytes());
    }


    /**
     * 获取时间戳
     *
     * @return
     */
    public long genTimeStamp() {
        return System.currentTimeMillis() / 1000;
    }

    public static class WXPayBuilder {
        private IWXAPI api;
        private String appId;
        private String partnerId;
        private String prepayId;
        private String packageValue;
        private String nonceStr;
        private String timeStamp;
        private String sign;

        public WXPayUtils build() {
            return new WXPayUtils(this);
        }

        public IWXAPI getApi() {
            return api;
        }

        public WXPayBuilder setApi(IWXAPI api) {
            this.api = api;
            return this;

        }

        public String getAppId() {
            return appId;
        }

        public WXPayBuilder setAppId(String appId) {
            this.appId = appId;
            return this;
        }

        public String getPartnerId() {
            return partnerId;
        }

        public WXPayBuilder setPartnerId(String partnerId) {
            this.partnerId = partnerId;
            return this;
        }

        public String getPrepayId() {
            return prepayId;
        }

        public WXPayBuilder setPrepayId(String prepayId) {
            this.prepayId = prepayId;
            return this;
        }

        public String getPackageValue() {
            return packageValue;
        }

        public WXPayBuilder setPackageValue(String packageValue) {
            this.packageValue = packageValue;
            return this;
        }

        public String getNonceStr() {
            return nonceStr;
        }

        public WXPayBuilder setNonceStr(String nonceStr) {
            this.nonceStr = nonceStr;
            return this;
        }

        public String getTimeStamp() {
            return timeStamp;
        }

        public WXPayBuilder setTimeStamp(String timeStamp) {
            this.timeStamp = timeStamp;
            return this;
        }

        public String getSign() {
            return sign;
        }

        public WXPayBuilder setSign(String sign) {
            this.sign = sign;
            return this;
        }
    }
}
