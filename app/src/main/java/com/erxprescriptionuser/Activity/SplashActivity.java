package com.erxprescriptionuser.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import android.Manifest;
import android.app.Dialog;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.erxprescriptionuser.R;
import com.erxprescriptionuser.SharePrefrences.SPreferenceKey;
import com.erxprescriptionuser.SharePrefrences.SharedPreferenceWriter;
import com.erxprescriptionuser.Utils.CommonUtils;
import com.erxprescriptionuser.databinding.DailogLanguageSelectionBinding;

import hari.bounceview.BounceView;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DataBindingUtil.setContentView(this,R.layout.activity_splash);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        CommonUtils.getDeviceToken(SplashActivity.this);
        if(!SharedPreferenceWriter.getInstance(this).getBoolean(SPreferenceKey.FIRST_TIME)) {
            intentCall();
        }else
        {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if(!SharedPreferenceWriter.getInstance(SplashActivity.this).getBoolean(SPreferenceKey.TAKE_A_TOUR)) CommonUtils.startActivity(SplashActivity.this, WalkThrowActivity.class);
                    else if(SharedPreferenceWriter.getInstance(SplashActivity.this).getBoolean(SPreferenceKey.IS_LOGIN)) CommonUtils.startActivity(SplashActivity.this,MainActivity.class);
                    else CommonUtils.startActivity(SplashActivity.this, LoginActivity.class);
                }
            }, 2500);
        }
    }

    private void intentCall() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                languageSelectionDailog();
            }},2500);
    }

    private void languageSelectionDailog() {
        final Dialog dialog=new Dialog(this,R.style.Theme_AppCompat_Light_Dialog_Alert);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        final DailogLanguageSelectionBinding binding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.dailog_language_selection, null, false);
        dialog.setContentView(binding.getRoot());
        dialog.setCancelable(false);
        binding.tvNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if(checkPermission()){
                    SharedPreferenceWriter.getInstance(SplashActivity.this).writeBooleanValue(SPreferenceKey.FIRST_TIME,true);
                    if(!SharedPreferenceWriter.getInstance(SplashActivity.this).getBoolean(SPreferenceKey.TAKE_A_TOUR)) CommonUtils.startActivity(SplashActivity.this, WalkThrowActivity.class);
                    else if(SharedPreferenceWriter.getInstance(SplashActivity.this).getBoolean(SPreferenceKey.IS_LOGIN)) CommonUtils.startActivity(SplashActivity.this,MainActivity.class);
                    else CommonUtils.startActivity(SplashActivity.this, LoginActivity.class);
                }
            }
        });

        binding.btnEnglish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferenceWriter.getInstance(SplashActivity.this).writeStringValue(SPreferenceKey.LANGUAGE_CODE,"en");
                binding.btnArabic.setBackground(ContextCompat.getDrawable(SplashActivity.this,R.drawable.round_corners));
                binding.btnArabic.setBackgroundTintList(getColorStateList(R.color.grey2));
                binding.btnArabic.setTextColor(ContextCompat.getColor(SplashActivity.this,android.R.color.black));
                binding.btnEnglish.setBackground(ContextCompat.getDrawable(SplashActivity.this,R.drawable.round_corners));
                binding.btnEnglish.setBackgroundTintList(getColorStateList(R.color.colorPrimary));
                binding.btnEnglish.setTextColor(ContextCompat.getColor(SplashActivity.this,android.R.color.white));
            }
        });

        binding.btnArabic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferenceWriter.getInstance(SplashActivity.this).writeStringValue(SPreferenceKey.LANGUAGE_CODE,"ar");
                binding.btnEnglish.setBackground(ContextCompat.getDrawable(SplashActivity.this,R.drawable.round_corners));
                binding.btnEnglish.setBackgroundTintList(getColorStateList(R.color.grey2));
                binding.btnEnglish.setTextColor(ContextCompat.getColor(SplashActivity.this,android.R.color.black));
                binding.btnArabic.setBackground(ContextCompat.getDrawable(SplashActivity.this,R.drawable.round_corners));
                binding.btnArabic.setBackgroundTintList(getColorStateList(R.color.colorPrimary));
                binding.btnArabic.setTextColor(ContextCompat.getColor(SplashActivity.this,android.R.color.white));
            }
        });

        BounceView.addAnimTo(binding.btnEnglish);
        BounceView.addAnimTo(binding.btnArabic);
        BounceView.addAnimTo(binding.tvNext);
        dialog.show();
    }


    @Override
    protected void onResume() {
        super.onResume();
        if(SharedPreferenceWriter.getInstance(this).getString(SPreferenceKey.LANGUAGE_CODE).equalsIgnoreCase(""))
        {
            SharedPreferenceWriter.getInstance(this).writeStringValue(SPreferenceKey.LANGUAGE_CODE,"en");
        }
        CommonUtils.setLocale(this);
    }

    private boolean checkPermission() {
        boolean ret = true;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                ret = false;
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.CAMERA,Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE}, 11);

            }
        }

        return ret;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults.length>0)
        {
            switch (requestCode)
            {
                case 11:
                    boolean coarseLocation= grantResults[0]==PackageManager.PERMISSION_GRANTED;
                    boolean fineLocation= grantResults[1]==PackageManager.PERMISSION_GRANTED;
                    boolean camera= grantResults[2]==PackageManager.PERMISSION_GRANTED;
                    boolean readExternal= grantResults[3]==PackageManager.PERMISSION_GRANTED;
                    boolean writeExternal= grantResults[4]==PackageManager.PERMISSION_GRANTED;

                    if(coarseLocation && fineLocation && camera && readExternal && writeExternal)
                    {
                        SharedPreferenceWriter.getInstance(SplashActivity.this).writeBooleanValue(SPreferenceKey.FIRST_TIME,true);
                        if(!SharedPreferenceWriter.getInstance(SplashActivity.this).getBoolean(SPreferenceKey.TAKE_A_TOUR)) CommonUtils.startActivity(SplashActivity.this, WalkThrowActivity.class);
                        else if(SharedPreferenceWriter.getInstance(SplashActivity.this).getBoolean(SPreferenceKey.IS_LOGIN)) CommonUtils.startActivity(SplashActivity.this,MainActivity.class);
                        else CommonUtils.startActivity(SplashActivity.this, LoginActivity.class);

                    }else
                    {
                        CommonUtils.showSnackBar(this,getString(R.string.needs_to_allow_permission));
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                checkPermission();
                            }
                        },2000);

                    }
                    break;

            }
        }
    }



}
