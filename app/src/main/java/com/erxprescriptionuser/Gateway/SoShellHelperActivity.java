package com.erxprescriptionuser.Gateway;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.erxprescriptionuser.R;
import com.erxprescriptionuser.databinding.ActivityGoShellHelperBinding;

public class SoShellHelperActivity extends AppCompatActivity {
    ActivityGoShellHelperBinding binding;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_go_shell_helper);
    }
}
