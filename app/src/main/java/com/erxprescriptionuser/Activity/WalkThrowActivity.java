package com.erxprescriptionuser.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.viewpager.widget.ViewPager;

import com.erxprescriptionuser.Adapter.WalkThrowAdapter;
import com.erxprescriptionuser.R;
import com.erxprescriptionuser.SharePrefrences.SPreferenceKey;
import com.erxprescriptionuser.SharePrefrences.SharedPreferenceWriter;
import com.erxprescriptionuser.Utils.CommonUtils;
import com.erxprescriptionuser.databinding.ActivityWalkThrowBinding;

import hari.bounceview.BounceView;

public class WalkThrowActivity extends AppCompatActivity implements View.OnClickListener, ViewPager.OnPageChangeListener {

    ActivityWalkThrowBinding binding;
    int img[]={R.drawable.a,R.drawable.b,R.drawable.takeatour_c};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CommonUtils.setLocale(this);
        binding= DataBindingUtil.setContentView(this, R.layout.activity_walk_throw);
        SharedPreferenceWriter.getInstance(this).writeBooleanValue(SPreferenceKey.TAKE_A_TOUR,true);
        init();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    private void init() {
        binding.viewPager.setAdapter(new WalkThrowAdapter(this));
        binding.tabLayout.setupWithViewPager(binding.viewPager,true);
        binding.viewPager.addOnPageChangeListener(this);
        binding.tvSkip.setOnClickListener(this);
        binding.playText.setOnClickListener(this);
        BounceView.addAnimTo(binding.tvSkip);
        BounceView.addAnimTo(binding.playText);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.tvSkip:
            CommonUtils.startActivity(this,LoginActivity.class);
            break;

            case R.id.playText:
            CommonUtils.startActivity(this,LoginActivity.class);
            break;
        }

    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if(position==2)
        {
            binding.playText.setVisibility(View.VISIBLE);
            binding.tvSkip.setVisibility(View.INVISIBLE);
            binding.tabLayout.setVisibility(View.GONE);
        }
        else if(position!=0)
        {
            binding.playText.setVisibility(View.GONE);
            binding.tvSkip.setVisibility(View.VISIBLE);
            binding.tabLayout.setVisibility(View.VISIBLE);
        }

        else
        {
            binding.playText.setVisibility(View.GONE);
            binding.tabLayout.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onPageSelected(int position) {


    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }
}
