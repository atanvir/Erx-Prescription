package com.erxprescriptionuser.SocketHelper;

import androidx.appcompat.app.AppCompatActivity;
import com.erxprescriptionuser.Utils.ParamEnum;

import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import okhttp3.OkHttpClient;

public class SocketConnection extends AppCompatActivity {
    private static Socket mSocket;
    private static SocketCallbacks listners;

    public static Socket connect(SocketCallbacks listner) throws URISyntaxException {
         listners=listner;
         if(mSocket!=null)
         {
             disconnect();
         }


        HostnameVerifier myHostnameVerifier = new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        };
        TrustManager[] trustAllCerts= new TrustManager[] { new X509TrustManager() {
            public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            }

            public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            }

            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[0];
            }
        }};

        SSLContext mySSLContext = null;
        try {
            mySSLContext = SSLContext.getInstance("TLS");
            try {
                mySSLContext.init(null, trustAllCerts, null);
            } catch (KeyManagementException e) {
                e.printStackTrace();
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        OkHttpClient okHttpClient = new OkHttpClient.Builder().hostnameVerifier(myHostnameVerifier).sslSocketFactory(mySSLContext.getSocketFactory()).build();
        IO.setDefaultOkHttpWebSocketFactory(okHttpClient);
        IO.setDefaultOkHttpCallFactory(okHttpClient);
        IO.Options opts = new IO.Options();
        opts.callFactory = okHttpClient;
        opts.webSocketFactory = okHttpClient;
         mSocket= IO.socket("https://e-rx.cc:2021");
         mSocket.on(Socket.EVENT_CONNECT, onConnect);
         mSocket.on(Socket.EVENT_DISCONNECT, onDisconnect);
         mSocket.on(Socket.EVENT_CONNECT_ERROR, onConnectError);
         mSocket.on(Socket.EVENT_CONNECT_TIMEOUT, onConnectError);
         mSocket.on(ParamEnum.SOCKET_EVENT_ROOM_JOIN.theValue(),onRoomJoin);
         mSocket.on(ParamEnum.SOCKET_EVENT_MESSAGE.theValue(),onMessage);
         mSocket.connect();
        return mSocket;
    }



    private static Emitter.Listener onDisconnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            listners.onDisconnect(args);
        }
    };

    private static Emitter.Listener onConnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            if(listners!=null) {
                listners.onConnect(args);
            }
        }
    };


    private static Emitter.Listener onConnectError = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            if(listners!=null) {
                listners.onConnectError(args);
            }
        }
    };

    private static Emitter.Listener onRoomJoin = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            if(listners!=null) {
                listners.onRoomJoin(args);
            }
        }
    };

    private static Emitter.Listener onMessage = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            if(listners!=null) {
                listners.onMessage(args);
            }
        }
    };


    public static void disconnect()
    {
        if(mSocket!=null) {
            mSocket.disconnect();
            mSocket.off(Socket.EVENT_CONNECT, onConnect);
            mSocket.off(Socket.EVENT_DISCONNECT, onDisconnect);
            mSocket.off(Socket.EVENT_CONNECT_ERROR, onConnectError);
            mSocket.off(Socket.EVENT_CONNECT_TIMEOUT, onConnectError);
            mSocket.off(ParamEnum.SOCKET_EVENT_ROOM_JOIN.theValue(),onRoomJoin);
            mSocket.off(ParamEnum.SOCKET_EVENT_MESSAGE.theValue(),onMessage);
            mSocket=null;
        }
    }



}
