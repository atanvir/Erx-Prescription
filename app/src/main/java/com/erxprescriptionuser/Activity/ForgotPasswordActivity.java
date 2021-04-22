package com.erxprescriptionuser.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import com.erxprescriptionuser.Model.CommonDataStringModel;
import com.erxprescriptionuser.Model.CommonModel;
import com.erxprescriptionuser.Model.ResponseBean;
import com.erxprescriptionuser.Model.SignupModel;
import com.erxprescriptionuser.R;
import com.erxprescriptionuser.Retrofit.ServicesConnection;
import com.erxprescriptionuser.Retrofit.ServicesInterface;
import com.erxprescriptionuser.Utils.CommonUtils;
import com.erxprescriptionuser.Utils.ParamEnum;
import com.erxprescriptionuser.databinding.ActivityForgotPasswordBinding;

import hari.bounceview.BounceView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgotPasswordActivity extends AppCompatActivity implements View.OnClickListener {

    ActivityForgotPasswordBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_forgot_password);
        BounceView.addAnimTo(binding.btnSendOtp);
        binding.btnSendOtp.setOnClickListener(this);
        BounceView.addAnimTo(binding.btnSendOtp);
    }

    @Override
    protected void onResume() {
        super.onResume();
        CommonUtils.setToolbar(this,getString(R.string.forgot_password));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.btnSendOtp:
                CommonUtils.hideKeyBoard(this,binding.edPhoneNumber);

                if(checkValidation()) {
                checkEmailForForgotPasswordApi();
            }
            break;
        }
    }

    private void checkEmailForForgotPasswordApi() {
        try {
            ServicesInterface anInterface = ServicesConnection.getInstance().createService(this);
            Call<CommonModel> call = anInterface.checkEmailForForgotPassword(binding.edPhoneNumber.getText().toString().trim());
            ServicesConnection.getInstance().enqueueWithoutRetry(call, this, true, new Callback<CommonModel>() {
                @Override
                public void onResponse(Call<CommonModel> call, Response<CommonModel> response) {
                    if(response.isSuccessful())
                    {
                        CommonModel serverResponse=response.body();
                        if(serverResponse.getStatus().equalsIgnoreCase(ParamEnum.SUCCESS.theValue()))
                        {
                            Intent intent = new Intent(ForgotPasswordActivity.this, OTPVerificationActivity.class);
                            intent.putExtra(ParamEnum.CAME_FROM.theValue(), ForgotPasswordActivity.class.getSimpleName());
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            intent.putExtra("data",getUserData(serverResponse.getData()));
                            intent.putExtra("message",serverResponse.getMessage());
                            startActivity(intent);
                        }else if(serverResponse.getStatus().equalsIgnoreCase(ParamEnum.FAILURE.theValue()))
                        {
                            CommonUtils.showSnackBar(ForgotPasswordActivity.this,serverResponse.getMessage());
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


    private SignupModel getUserData(ResponseBean responseBean) {
        SignupModel model = new SignupModel();
        model.setUserId(responseBean.getUserId());
        model.setOtp(responseBean.getOtp());
        model.setEmail(binding.edPhoneNumber.getText().toString().trim());
        return model;
    }

    private boolean checkValidation() {
        boolean ret=true;
        if(binding.edPhoneNumber.getText().toString().trim().length()==0 || !Patterns.EMAIL_ADDRESS.matcher(binding.edPhoneNumber.getText().toString()).matches())
        {
            ret=false;
            if(binding.edPhoneNumber.getText().toString().trim().length()==0)
                Toast.makeText(this, R.string.enter_email_id, Toast.LENGTH_SHORT).show();
            else if(!Patterns.EMAIL_ADDRESS.matcher(binding.edPhoneNumber.getText().toString()).matches())
                Toast.makeText(this,  R.string.enter_valid_email_id, Toast.LENGTH_SHORT).show();
        }

        return ret;
    }


}
