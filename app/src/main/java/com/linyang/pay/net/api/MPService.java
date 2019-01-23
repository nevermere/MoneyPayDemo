package com.linyang.pay.net.api;

import com.linyang.pay.pay.unionpay.UnionPayOrder;
import com.linyang.pay.pay.wxpay.WXPayOrder;

import io.reactivex.Observable;
import retrofit2.http.GET;

/**
 * 描述:API接口
 * Created by fzJiang on 2018-10-10
 */
public interface MPService {

    /**
     * 获取微信预支付订单
     */
//    @GET("pub_v2/app/app_pay.php")
    @GET("tools/mockapi/1224/getWXPayOrder")
    Observable<WXPayOrder> getWXPayOrder();

    /**
     * 获取银联支付交易流水号
     */
    @GET("tools/mockapi/1224/getUnionPayTN")
    Observable<UnionPayOrder> getUnionPayTN();

}
