package com.linyang.pay.pay.unionpay;

/**
 * 描述:银联支付结果实体
 * Created by fzJiang on 2018-10-12
 */
public class PayResult {

    private String sign;// 签名
    private String data;// 数据

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
