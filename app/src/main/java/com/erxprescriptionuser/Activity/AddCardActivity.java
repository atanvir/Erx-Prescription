package com.erxprescriptionuser.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.view.View;

import com.erxprescriptionuser.R;
import com.erxprescriptionuser.Utils.CommonUtils;
import com.erxprescriptionuser.databinding.ActivityAddCardBinding;

import hari.bounceview.BounceView;

public class AddCardActivity extends AppCompatActivity implements View.OnClickListener {

    ActivityAddCardBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=DataBindingUtil.setContentView(this,R.layout.activity_add_card);
        binding.btnPay.setOnClickListener(this);
        BounceView.addAnimTo(binding.btnPay);
    }

    @Override
    protected void onResume() {
        super.onResume();
        CommonUtils.setToolbar(this,getString(R.string.add_new_card));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.btnPay:
            onBackPressed();
            break;
        }
    }
}
