package com.erxprescriptionuser.Retrofit;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.erxprescriptionuser.Activity.LoginActivity;
import com.erxprescriptionuser.Activity.MainActivity;
import com.erxprescriptionuser.Model.CommonDataStringModel;
import com.erxprescriptionuser.Model.CommonListModel;
import com.erxprescriptionuser.Model.CommonModel;
import com.erxprescriptionuser.R;
import com.erxprescriptionuser.SharePrefrences.SPreferenceKey;
import com.erxprescriptionuser.SharePrefrences.SPrefrenceValues;
import com.erxprescriptionuser.SharePrefrences.SharedPreferenceWriter;
import com.erxprescriptionuser.Utils.CommonUtils;
import com.erxprescriptionuser.Utils.ParamEnum;

import java.io.IOException;
import java.io.InputStream;
import java.net.SocketTimeoutException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServicesConnection {
    private static ServicesConnection connect;
    private ServicesInterface clientService;
    public static final int DEFAULT_RETRIES = 0;
    boolean isInValidToken=false;

    public static synchronized ServicesConnection getInstance() {
        if (connect == null) {
            connect = new ServicesConnection();
        }
        return connect;
    }

    public ServicesInterface createService(final Context context) throws Exception {

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(ParamEnum.BASE_URL.theValue())
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(getUnsafeOkHttpClient(context).build())
                    .build();

            clientService = retrofit.create(ServicesInterface.class);

        return clientService;
    }

    public static OkHttpClient.Builder getUnsafeOkHttpClient(Context context) {
        try {
            // Create a trust manager that does not validate certificate chains
            final TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return new java.security.cert.X509Certificate[]{};
                        }
                    }
            };

            // Install the all-trusting trust manager
            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());

            // Create an ssl socket factory with our all-trusting manager
            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);//
            OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
            httpClient.sslSocketFactory(sslSocketFactory, (X509TrustManager) trustAllCerts[0]);
            httpClient.readTimeout(1, TimeUnit.MINUTES);
            httpClient.readTimeout(1, TimeUnit.MINUTES);
            httpClient.addInterceptor(new Interceptor() {
                @Override
                public okhttp3.Response intercept(Chain chain) throws IOException {
                    Request original = chain.request();
                    Request request;
                    request = original.newBuilder()
                            .header("Authorization", SharedPreferenceWriter.getInstance(context).getString(SPreferenceKey.AUTH_TOKEN))
                            .header("langCode", SharedPreferenceWriter.getInstance(context).getString(SPreferenceKey.LANGUAGE_CODE))
                            .method(original.method(),original.body())
                            .build();
                    return chain.proceed(request);
                }

            });
            httpClient.addInterceptor(logging);

            httpClient.hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });
            return httpClient;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public ServicesInterface createService(Context context,String BASE_URL) throws Exception {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);//
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.readTimeout(1, TimeUnit.MINUTES);
        httpClient.readTimeout(1, TimeUnit.MINUTES);
        httpClient.addInterceptor(logging);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(getUnsafeOkHttpClient(context).build())
                .build();

        clientService = retrofit.create(ServicesInterface.class);

        return clientService;
    }


    //    enqueue
    public <T> boolean enqueueWithRetry(Call<T> call, final Activity activity, final boolean isLoader, final int retryCount, final Callback<T> callback) {
        if (CommonUtils.networkConnectionCheck(activity)) {
            if(isLoader)
            {
                if(activity!=null){
                    CommonUtils.showLoadingDialog(activity);
                }
            }
            call.enqueue(new ServicesRetryableCallback<T>(call, retryCount)
            {
                @Override
                public void onFinalResponse(Call<T> call, Response<T> response)
                {
                    if(CommonUtils.customLoader !=null)
                    {
                        if(isLoader) {
                            CommonUtils.dismissLoadingDialog();
                        }
                    }
                    checkValidUser(activity,callback,call,response);
                }

                @Override
                public void onFinalFailure(Call<T> call, Throwable t)
                {
                    if(CommonUtils.customLoader !=null)
                    {

                        CommonUtils.dismissLoadingDialog();
                    }
                    if(t instanceof SocketTimeoutException)
                    {


                    }
                    CommonUtils.showSnackBar(activity,""+t.getMessage());
                    callback.onFailure(call, t);
                }
            });
           return true;
        } else {
            CommonUtils.showSnackBar(activity,activity.getString(R.string.no_internet));
            return false;
        }
    }

    private <T> void checkValidUser(Context context,Callback<T> callback, Call<T> call, Response<T> response) {
        boolean ret=true;
        if(response.body() instanceof CommonModel)
        {
            if(((CommonModel) response.body()).getMessage().equalsIgnoreCase("Invalid Token"))
            {
               ret=false;
            }
        }else if(response.body() instanceof CommonDataStringModel)
        {
            if(((CommonDataStringModel) response.body()).getMessage().equalsIgnoreCase("Invalid Token"))
            {
                ret=false;
            }
        }else if(response.body() instanceof CommonListModel)
        {
            if(((CommonListModel) response.body()).getMessage().equalsIgnoreCase("Invalid Token"))
            {
                ret=false;
            }
        }

        if(ret) {
            callback.onResponse(call, response);
        }else
        {
            Toast.makeText(context, R.string.login_expire, Toast.LENGTH_SHORT).show();
            SPrefrenceValues.removeUserData(context);
            CommonUtils.startActivity(context,LoginActivity.class);

        }
    }


    public  <T> boolean  enqueueWithoutRetry(Call<T> call, Activity activity, boolean isLoader, final Callback<T> callback) {
        return enqueueWithRetry(call, activity,isLoader, DEFAULT_RETRIES, callback);
    }
}
