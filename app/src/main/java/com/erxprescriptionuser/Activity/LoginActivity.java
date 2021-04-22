package com.erxprescriptionuser.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.erxprescriptionuser.Model.CommonModel;
import com.erxprescriptionuser.R;
import com.erxprescriptionuser.Retrofit.ServicesConnection;
import com.erxprescriptionuser.Retrofit.ServicesInterface;
import com.erxprescriptionuser.SharePrefrences.SPreferenceKey;
import com.erxprescriptionuser.SharePrefrences.SPrefrenceValues;
import com.erxprescriptionuser.SharePrefrences.SharedPreferenceWriter;
import com.erxprescriptionuser.Utils.CommonUtils;
import com.erxprescriptionuser.Utils.IncomingSms;
import com.erxprescriptionuser.Utils.ParamEnum;
import com.erxprescriptionuser.databinding.ActivityLoginBinding;

import hari.bounceview.BounceView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CommonUtils.setLocale(this);
        binding=DataBindingUtil.setContentView(this,R.layout.activity_login);
        init();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void init() {
        binding.btnLogin.setOnClickListener(this);
        binding.btnSingup.setOnClickListener(this);
        binding.tvForgotPassword.setOnClickListener(this);
        binding.ivPassword.setOnClickListener(this);
        BounceView.addAnimTo(binding.btnLogin);
        BounceView.addAnimTo(binding.btnSingup);
        BounceView.addAnimTo(binding.tvForgotPassword);
        BounceView.addAnimTo(binding.ivPassword);

        if(SharedPreferenceWriter.getInstance(this).getBoolean(SPreferenceKey.REMEMBER_ME))
        {
            binding.edPhoneNumber.setBackground(ContextCompat.getDrawable(this,R.drawable.drawable_black_stroke));
            binding.edPassword.setBackground(ContextCompat.getDrawable(this,R.drawable.drawable_black_stroke));
            binding.edPhoneNumber.setTextColor(ContextCompat.getColor(this,R.color.black));
            binding.edPassword.setTextColor(ContextCompat.getColor(this,R.color.black));
            binding.edPhoneNumber.setText(SharedPreferenceWriter.getInstance(this).getString(SPreferenceKey.MOBILE));
            binding.edPassword.setText(SharedPreferenceWriter.getInstance(this).getString(SPreferenceKey.PASSWORD));
            binding.ccode.setText(SPreferenceKey.COUNTRY_CODE_API);
            binding.cbRememberMe.setChecked(true);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.btnLogin:
            if(checkValidation()) loginApi();
            break;


            case R.id.btnSingup:
            CommonUtils.startActivity(this, SignupActivity.class);
            break;

            case R.id.tvForgotPassword:
            CommonUtils.startActivity(this,ForgotPasswordActivity.class);
            break;

            case R.id.ivPassword:
             if(((BitmapDrawable)binding.ivPassword.getDrawable()).getBitmap()==((BitmapDrawable)getDrawable(R.drawable.eye)).getBitmap())
             {
                 binding.ivPassword.setImageBitmap(((BitmapDrawable)getDrawable(R.drawable.eye_s)).getBitmap());
                 binding.edPassword.setTransformationMethod(new HideReturnsTransformationMethod());
             }else
             {
                 binding.ivPassword.setImageBitmap(((BitmapDrawable)getDrawable(R.drawable.eye)).getBitmap());
                 binding.edPassword.setTransformationMethod(new PasswordTransformationMethod());
             }
            break;

        }
    }

    private void loginApi() {
    try {
        ServicesInterface anInterface = ServicesConnection.getInstance().createService(this);
        Call<CommonModel> call = anInterface.userLogin(SPreferenceKey.COUNTRY_CODE_API,
                                                       binding.edPhoneNumber.getText().toString(),
                                                       binding.edPassword.getText().toString().trim(),
                                                       ParamEnum.ANDROID.theValue(),
                                                        SharedPreferenceWriter.getInstance(LoginActivity.this).getString(SPreferenceKey.TOKEN));
        ServicesConnection.getInstance().enqueueWithoutRetry(call, this, true, new Callback<CommonModel>() {
            @Override
            public void onResponse(Call<CommonModel> call, Response<CommonModel> response) {
                if(response.isSuccessful())
                {
                    CommonModel serverResponse=response.body();
                    if(serverResponse.getStatus().equalsIgnoreCase(ParamEnum.SUCCESS.theValue()))
                    {
                        SharedPreferenceWriter.getInstance(LoginActivity.this).writeBooleanValue(SPreferenceKey.REMEMBER_ME,binding.cbRememberMe.isChecked());
                        CommonUtils.startActivity(LoginActivity.this,MainActivity.class);
                        SPrefrenceValues.saveUserData(LoginActivity.this,response.body());
                        if(binding.cbRememberMe.isChecked()) {
                            SharedPreferenceWriter.getInstance(LoginActivity.this).writeStringValue(SPreferenceKey.PASSWORD, binding.edPassword.getText().toString());
                            SharedPreferenceWriter.getInstance(LoginActivity.this).writeBooleanValue(SPreferenceKey.REMEMBER_ME,true);
                        }
                    }else if(serverResponse.getStatus().equalsIgnoreCase("401"))
                    {
                        CommonUtils.showSnackBar(LoginActivity.this,serverResponse.getMessage());
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

    private boolean checkValidation() {
        boolean ret=true;
        if(binding.edPhoneNumber.getText().toString().length()==0 || binding.edPhoneNumber.getText().toString().length()<9)
        {
            ret=false;
            if(binding.edPhoneNumber.getText().toString().length()==0) CommonUtils.showSnackBar(this,getString(R.string.enter_phone_number));
            else if(binding.edPhoneNumber.getText().toString().length()<9) CommonUtils.showSnackBar(this,getString(R.string.enter_valid_phone_number));
        }else if(binding.edPassword.getText().toString().length()==0)
        {
            ret=false;
            CommonUtils.showSnackBar(this,getString(R.string.enter_your_password));
        }
        return ret;
    }


    @Override
    public void onBackPressed() {
        finishAffinity();
    }
}
