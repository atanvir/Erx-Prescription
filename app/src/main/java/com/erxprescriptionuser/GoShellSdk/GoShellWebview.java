package com.erxprescriptionuser.GoShellSdk;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.erxprescriptionuser.Activity.MainActivity;
import com.erxprescriptionuser.Fragment.MyOfferFragment;
import com.erxprescriptionuser.Model.CommonModel;
import com.erxprescriptionuser.Model.ResponseBean;
import com.erxprescriptionuser.R;
import com.erxprescriptionuser.Retrofit.ServicesConnection;
import com.erxprescriptionuser.Retrofit.ServicesInterface;
import com.erxprescriptionuser.SharePrefrences.SPreferenceKey;
import com.erxprescriptionuser.SharePrefrences.SharedPreferenceWriter;
import com.erxprescriptionuser.Utils.CommonUtils;
import com.erxprescriptionuser.Utils.ParamEnum;
import com.erxprescriptionuser.Utils.SwitchFragment;
import com.erxprescriptionuser.databinding.ActivityPaymentWebviewBinding;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GoShellWebview  extends AppCompatActivity{

    private ActivityPaymentWebviewBinding binding;
    private ResponseBean bean;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=DataBindingUtil.setContentView(this,R.layout.activity_payment_webview);
        CommonUtils.showLoadingDialog(this);
        bean=getIntent().getParcelableExtra("data");
        binding.webPaymentWebView.getSettings().setJavaScriptEnabled(true);
        binding.webPaymentWebView.loadUrl(bean.getChargeData().getTransaction().getUrl());
        binding.webPaymentWebView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                Log.e("URL", ""+url);
                if(url.contains("https://jslib.payments.gosell.io/?mode=page&token=") || url.contains("https://checkout.payments.tap.company/?mode=page&token="))
                {
                    CommonUtils.dismissLoadingDialog();

                }else if(url.contains("https://e-rx.cc/e-Rx-provider/#/"))
                {
                    binding.webPaymentWebView.setVisibility(View.GONE);
                    CommonUtils.showLoadingDialog(GoShellWebview.this);
                    paymentApi();
                }
            }
        });


    }

    private void paymentApi() {
        try {
            String paymentId=createTransactionID();
            ServicesInterface anInterface = ServicesConnection.getInstance().createService(this);
            Call<CommonModel> call = anInterface.payment(getValue("prescriptionOfferId"),bean.getProviderAmount(),bean.getCompanyAmount()!=null?bean.getCompanyAmount():"0",
                                                        bean.getAdminAmount(), ""+bean.getChargeData().getAmount(),getCurrentDate(),bean.getChargeData().getCurrency(),paymentId,"Online",
                                                        bean.getChargeData().getStatus(),bean.getDeliveryToAdminAmount()!=null?bean.getDeliveryToAdminAmount():"0",bean.getChargeData().getId(),getValue("deliveryType"));
            ServicesConnection.getInstance().enqueueWithoutRetry(call, this, false, new Callback<CommonModel>() {
                @Override
                public void onResponse(Call<CommonModel> call, Response<CommonModel> response) {
                    if(response.isSuccessful())
                    {
                        CommonModel serverResponse=response.body();
                        if(serverResponse.getMessage().equalsIgnoreCase(getString(R.string.offer_list_found_sccessfully)))
                        {
                            acceptPrescriptionRequestApi(paymentId,"Online" , bean.getChargeData().getStatus());

                        }else
                        {
                            CommonUtils.dismissLoadingDialog();
                            Intent intent=new Intent();
                            intent.putExtra("status", serverResponse.getMessage());
                            setResult(RESULT_CANCELED,intent);
                            finish();

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

    private void acceptPrescriptionRequestApi(String transactionId,String paymentType,String status) {
        try {
            ServicesInterface anInterface = ServicesConnection.getInstance().createService(this);
            Call<CommonModel> call = anInterface.acceptPrescriptionRequest(getValue("prescriptionOfferId"),
                    Double.parseDouble(getValue(SPreferenceKey.LATITUTE)),
                    Double.parseDouble(getValue(SPreferenceKey.LONGITUTE)),
                    getValue(SPreferenceKey.ADDRESS),getValue("deliveryType"),
                    ""+bean.getChargeData().getAmount(),getCurrentDate(),bean.getChargeData().getCurrency(),transactionId,
                    paymentType,status);
            ServicesConnection.getInstance().enqueueWithoutRetry(call, this, false, new Callback<CommonModel>() {
                @Override
                public void onResponse(Call<CommonModel> call, Response<CommonModel> response) {
                    if(response.isSuccessful())
                    {
                        CommonUtils.dismissLoadingDialog();
                        CommonModel serverResponse=response.body();
                        if(serverResponse.getStatus().equalsIgnoreCase(ParamEnum.SUCCESS.theValue()))
                        {
                            requestAcceptedDailog(serverResponse.getMessage());
                        }else if(serverResponse.getStatus().equalsIgnoreCase("500") || serverResponse.getStatus().equalsIgnoreCase(ParamEnum.FAILURE.theValue()))
                        {
                            Intent intent=new Intent();
                            intent.putExtra("status", serverResponse.getMessage());
                            setResult(RESULT_CANCELED,intent);
                            finish();
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

    private void requestAcceptedDailog(String message) {
        final Dialog dialog=new Dialog(this,R.style.Theme_AppCompat_Light_Dialog_Alert);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.dailog_request_accepted);
        TextView tvPrescription=dialog.findViewById(R.id.tvPrescription);
        tvPrescription.setText(message);
        dialog.setCancelable(true);
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                dialog.dismiss();
                CommonUtils.startActivity(GoShellWebview.this, MainActivity.class);
            }
        });
        dialog.show();
    }

    private String getValue(String key) {
        return getIntent().getStringExtra(key);
    }

    private String getCurrentDate() {
        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);

        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
        return df.format(c);
    }

    public String createTransactionID() throws Exception{
        return "payId"+UUID.randomUUID().toString().replaceAll("-", "").toUpperCase();
    }

    @Override
    public void onBackPressed() {
        Intent intent=new Intent();
        intent.putExtra("status","Cancelled");
        setResult(RESULT_CANCELED,intent);
        finish();
    }
}
