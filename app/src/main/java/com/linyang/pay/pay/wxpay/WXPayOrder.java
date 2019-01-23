package com.linyang.pay.pay.wxpay;

import com.google.gson.annotations.SerializedName;

/**
 * 描述:微信预支付订单实体类
 * Created by fzJiang on 2018-10-11
 */
public class WXPayOrder {

    @SerializedName("appid")
    private String appId;// 微信开放平台审核通过的应用APPID

    @SerializedName("partnerid")
    private String partnerId;// 微信支付分配的商户号

    @SerializedName("prepayid")
    private String prepayId;// 微信返回的支付交易会话ID

    @SerializedName("package")
    private String packageValue;// 暂填写固定值Sign=WXPay

    @SerializedName("noncestr")
    private String nonceStr;// 随机字符串，不长于32位。推荐随机数生成算法

    @SerializedName("timestamp")
    private String timeStamp;// 时间戳，请见接口规则-参数规定

    private String sign;// 签名，详见签名生成算法

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(String partnerId) {
        this.partnerId = partnerId;
    }

    public String getPrepayId() {
        return prepayId;
    }

    public void setPrepayId(String prepayId) {
        this.prepayId = prepayId;
    }

    public String getPackageValue() {
        return packageValue;
    }

    public void setPackageValue(String packageValue) {
        this.packageValue = packageValue;
    }

    public String getNonceStr() {
        return nonceStr;
    }

    public void setNonceStr(String nonceStr) {
        this.nonceStr = nonceStr;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    @Override
    public String toString() {
        return "appId:" + appId
                + ",partnerId:" + partnerId
                + ",prepayId:" + prepayId
                + ",packageValue:" + packageValue
                + ",nonceStr:" + nonceStr
                + ",timeStamp:" + timeStamp
                + ",sign:" + sign;
    }
}
