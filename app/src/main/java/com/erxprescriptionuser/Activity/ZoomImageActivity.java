package com.erxprescriptionuser.Activity;

import android.os.Bundle;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.erxprescriptionuser.R;
import com.erxprescriptionuser.Utils.ImageGlider;
import com.erxprescriptionuser.databinding.ActivityZoomImageBinding;

public class ZoomImageActivity extends AppCompatActivity {

    ActivityZoomImageBinding binding;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=DataBindingUtil.setContentView(this,R.layout.activity_zoom_image);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        ImageGlider.setRoundImage(this,binding.ivPrescription,binding.progressBar,getIntent().getStringExtra("image"));
    }
}
