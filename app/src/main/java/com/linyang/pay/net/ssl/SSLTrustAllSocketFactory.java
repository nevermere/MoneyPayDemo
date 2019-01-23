//package com.linyang.pay.net.ssl;
//
//
//import android.annotation.SuppressLint;
//
//import com.linyang.pay.util.LogUtil;
//
//import java.io.IOException;
//import java.net.InetAddress;
//import java.net.Socket;
//import java.security.KeyStore;
//import java.security.cert.CertificateException;
//import java.security.cert.X509Certificate;
//
//import javax.net.ssl.SSLContext;
//import javax.net.ssl.SSLSocketFactory;
//import javax.net.ssl.TrustManager;
//import javax.net.ssl.X509TrustManager;
//
///**
// * 描述:自定义SSLSocketFactory里跳过校验
// * Created by fzJiang on 2018-10-10
// */
//public class SSLTrustAllSocketFactory extends SSLSocketFactory {
//
//    private SSLContext mSSLContext;
//
//    @Override
//    public String[] getDefaultCipherSuites() {
//        return new String[0];
//    }
//
//    @Override
//    public String[] getSupportedCipherSuites() {
//        return new String[0];
//    }
//
//    @Override
//    public Socket createSocket() throws IOException {
//        return mSSLContext.getSocketFactory().createSocket();
//    }
//
//    @Override
//    public Socket createSocket(Socket socket, String host, int port, boolean autoClose) throws IOException {
//        return mSSLContext.getSocketFactory().createSocket(socket, host, port, autoClose);
//    }
//
//    @Override
//    public Socket createSocket(String host, int port) throws IOException {
//        return mSSLContext.getSocketFactory().createSocket(host, port);
//    }
//
//    @Override
//    public Socket createSocket(String host, int port, InetAddress localHost, int localPort) throws IOException {
//        return mSSLContext.getSocketFactory().createSocket(host, port, localHost, localPort);
//    }
//
//    @Override
//    public Socket createSocket(InetAddress host, int port) throws IOException {
//        return mSSLContext.getSocketFactory().createSocket(host, port);
//    }
//
//    @Override
//    public Socket createSocket(InetAddress address, int port, InetAddress localAddress, int localPort) throws IOException {
//        return mSSLContext.getSocketFactory().createSocket(address, port, localAddress, localPort);
//    }
//
//    public static SSLSocketFactory getSocketFactory() {
//        try {
//            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
//            trustStore.load(null, null);
//            return new SSLTrustAllSocketFactory(trustStore);
//        } catch (Throwable e) {
//            LogUtil.e(e.getMessage());
//        }
//        return null;
//    }
//
//    private SSLTrustAllSocketFactory(KeyStore keyStore) throws Throwable {
//        super();
//        try {
//            mSSLContext = SSLContext.getInstance("SSL");
//            mSSLContext.init(null, new TrustManager[]{new SSLTrustAllManager()}, new java.security.SecureRandom());
//            final SSLSocketFactory sslSocketFactory = mSSLContext.getSocketFactory();
//            return mSSLContext.getSocketFactory();
//            setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//    }
//
//
//    //
//    @SuppressLint("TrustAllX509TrustManager")
//    private class SSLTrustAllManager implements X509TrustManager {
//
//
//        @Override
//        public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
//
//        }
//
//        @Override
//        public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
//
//        }
//
//        @Override
//        public X509Certificate[] getAcceptedIssuers() {
//            return new java.security.cert.X509Certificate[]{};
//        }
//    }
//}
