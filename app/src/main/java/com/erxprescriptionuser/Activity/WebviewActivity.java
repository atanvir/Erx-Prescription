package com.erxprescriptionuser.Activity;

import android.net.http.SslError;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.erxprescriptionuser.Model.CommonModel;
import com.erxprescriptionuser.R;
import com.erxprescriptionuser.Retrofit.ServicesConnection;
import com.erxprescriptionuser.Retrofit.ServicesInterface;
import com.erxprescriptionuser.SharePrefrences.SPreferenceKey;
import com.erxprescriptionuser.SharePrefrences.SPrefrenceValues;
import com.erxprescriptionuser.SharePrefrences.SharedPreferenceWriter;
import com.erxprescriptionuser.Utils.CommonUtils;
import com.erxprescriptionuser.Utils.ParamEnum;
import com.erxprescriptionuser.databinding.ActivityWebviewBinding;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WebviewActivity extends AppCompatActivity {

    ActivityWebviewBinding binding;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=DataBindingUtil.setContentView(this,R.layout.activity_webview);
        getStaticContentApi();
    }

    private void getStaticContentApi() {
        try {
            ServicesInterface anInterface = ServicesConnection.getInstance().createService(this,"https://e-rx.cc:2021/api/v1/static/");
            Call<CommonModel> call = anInterface.getStaticContentByType(getIntent().getStringExtra("url"),"User");
            ServicesConnection.getInstance().enqueueWithoutRetry(call, this, true, new Callback<CommonModel>() {
                @Override
                public void onResponse(Call<CommonModel> call, Response<CommonModel> response) {
                    if(response.isSuccessful())
                    {
                        CommonModel serverResponse=response.body();
                        if(serverResponse.getStatus().equalsIgnoreCase(ParamEnum.SUCCESS.theValue()))
                        {
                            binding.tvData.setText(Html.fromHtml(serverResponse.getData().getDescription()).toString());

                        }else if(serverResponse.getStatus().equalsIgnoreCase("401") || serverResponse.getStatus().equalsIgnoreCase(ParamEnum.FAILURE.theValue()))
                        {
                            CommonUtils.showSnackBar(WebviewActivity.this,serverResponse.getMessage());
                        }
                    }
                }

                @Override
                public void onFailure(Call<CommonModel> call, Throwable t) {
                    Log.e("failure",t.getMessage());

                }
            });
        } catch (Exception e) {
            Toast.makeText(this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        CommonUtils.setToolbar(this,getIntent().getStringExtra("Title"));
        CommonUtils.setLocale(this);
    }
}
