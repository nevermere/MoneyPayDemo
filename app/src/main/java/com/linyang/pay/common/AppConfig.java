package com.linyang.pay.common;

/**
 * 描述:app配置信息
 * Created by fzJiang on 2018-10-10
 */
public class AppConfig {

    /**
     * 服务器地址
     */
//    public static final String BASE_URL = "https://wxpay.wxutil.com/";

    public static final String BASE_URL = "http://www.wanandroid.com/";

    /**
     * 支付宝支付配置信息
     */
    public interface ALI_PAY {
        String APP_ID = "sdadafea5445rea4g582ds4f";// 支付宝支付业务：入参app_id
        String PID = "12124523548";// 支付宝账户登录授权业务：入参pid值
        String TARGET_ID = "45732548583545";// 支付宝账户登录授权业务：入参target_id值
        String RSA2_PRIVATE = "MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQCSX/Ye2Fvyk4DK6wPD2pZCxwDFcKHpoMeMOKIdckVh2GKKVY8lremmcLp+7K+UOz/NuhiVUkZ/0sPa1pkvq3BRYOTWRB3q3HRjp4ATUqNfcB0TgPMl5sXVVn8urMcgGaC6BUgWJvtUdMhaWI/e/MbIX3FjqVBJKifEpt+6qmY8LgIzbU6elNt4w+87EJ2cfXVTTAeF0vjslHflHY5EmWow+jIO7RnfGcQ4pIenambfCnY+ttXJ2oBVIexiAwUJWHbiKiqyfbAd7/uZpTd+e+ykZLYKdNPB+uefDdNfkQRYf13FQFMheYEM00Cu4sT7mVZfyoHoxa6vXIHyN/ewts1BAgMBAAECggEAEuW6s77BGC886HWw9b8puRNSIStqa+wq9wp0u5J+LKSwDCYBph6u1KMnIK3T+TIxCZFmDRkjKut+B6m++y2KAaedw5/lsOWnTRpZ6aS4nOCvkE4Mli8WCXXyU4dQ6e8FFh8KcHm15vBw0WEvqd0+mK3hyLkaOWfs3HGi3smTC1++zWnV1uBz85BeASorzhzs+wuP6834R7/l0LtLqo/ro5vG+JH87e46VD31GgoQ9OkBNos0en9HAvn6QzTYNY3rKbPLkLA0Ud/kLxiOA1OKcVrzq5rvg4u8dut9vFoY1T1BSsngmzppgBKl4GkANxeSzdETv3NqJ26KApK7bGJB9QKBgQDdsXn1aSlx/iECIgdE2XP9n34+Qs78VzsCpW9095d56mL6Y3NOh3fqJ/zXR+bdOyk7xyDNvebdnlNjnvNA674UbeePmLhSP0BeTJc2dE7NA9pB450LGPHzgMJ5qzFXF7hnv9AOvyIyoduteEGFraeqS5BfmxQiAJbUrfxPG2yJpwKBgQCpBrJZlJj7xMFRFE+p5q/vlFIZR2+ydqn7z3xYXA67IR7etI3PGLlp65zJRjbeLW1IKB6zATfRe6AdEtg7nds7lgcVUnqDDXyCbwKnkzDpn1lRAdInC8EYGos7c+65SPGbR+BqeU4d126bBI8ijtm+Ed7kxq8r1h1oP4VVSJl+1wKBgBvd6USt+WW42hFYka1X4DqrR8UvxLD3jhVhEkeTr9jFW7e5tI7UfIBUknXFNgHtI9u9xiPWkSSeeED3hABPIc+TmR31xoWgKckwu3k+2YDv0QmjQORaOF+xecCtgkA+XRG9jLutzvCqY+DEUwgosgC4CufZqIg1psAJNsQC4HvVAoGAXmZpEXLWhjZ7KUdIvsEiHZIe0BN/pJJ1mTdmYQr5BzYWQZdhY/qRA0EdzaOzXR7N2/DcRGk3vX1oDRTLQ3CEUqEVz6jC15KeqVJKj0C7np7qiT2VG02LSGT6wfebqku89FaOVajuIeiB034WWtyha3AbpnX4u2jX/7MKSnlDkP0CgYBN1aJ8KdwsbziAKLW5h4LmmHvOFhxCcKXRNpMkHWiH6g+exYPgvW28S0HeOfoHD34JZC7WUXowVzrIsnV/crfkmdv4tkUjeEpNPJo0wwl4sqyORza1J3kkHOipQj9Jz2cOs4VFFt9le8thySCkdSiugy2LmrSKkJlTmZ2RnGZSrQ==";// 商户私钥，pkcs8格式,可以保证商户交易在更加安全的环境下进行

        int SDK_PAY_FLAG = 1;
        int SDK_AUTH_FLAG = 2;
    }

    /**
     * 微信支付配置信息
     */
    public interface WX_PAY {
        String APP_ID = "wx398fea4bd5ad6c0c";// APP_ID
        String APP_SECRET = "49d58cedee5aa89f29889c7c8b8c2b8a";// 秘钥
    }

    /**
     * 银联支付配置信息
     */
    public interface UNION_PAY {
        String SANDBOX = "01";// 01 沙盒环境
        String FORMAL = "00";// 00 正式环境

        int PLUGIN_VALID = 0;// 插件不可用
        int PLUGIN_NOT_INSTALLED = -1;// 未安装插件
        int PLUGIN_NEED_UPGRADE = 2;// 插件需进行更新

        String PAY_SUCCESS = "success";// 支付成功
        String PAY_FAILED = "fail";// 支付失败
        String PAY_CANCEL = "cancel";// 支付取消
    }
}
