package com.erxprescriptionuser.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.Dialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.erxprescriptionuser.Model.CommonModel;
import com.erxprescriptionuser.Model.SignupModel;
import com.erxprescriptionuser.R;
import com.erxprescriptionuser.Retrofit.ServicesConnection;
import com.erxprescriptionuser.Retrofit.ServicesInterface;
import com.erxprescriptionuser.SharePrefrences.SPrefrenceValues;
import com.erxprescriptionuser.SharePrefrences.SharedPreferenceWriter;
import com.erxprescriptionuser.Utils.AddRequestBody;
import com.erxprescriptionuser.Utils.CommonUtils;
import com.erxprescriptionuser.Utils.IncomingSms;
import com.erxprescriptionuser.Utils.ParamEnum;
import com.erxprescriptionuser.databinding.ActivityOtpVerificationBinding;
import com.erxprescriptionuser.databinding.DailogImageUploadedBinding;
import com.erxprescriptionuser.databinding.DailogResetPasswordBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.io.File;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import hari.bounceview.BounceView;
import id.zelory.compressor.Compressor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Multipart;

public class OTPVerificationActivity extends AppCompatActivity implements View.OnClickListener, OnCompleteListener<AuthResult> {
    ActivityOtpVerificationBinding binding;
    private SignupModel model;
    private FirebaseAuth mAuth;
    private String verificationId;
    private CountDownTimer countDownTimer;
    private boolean isTimerRunning;
    private IncomingSms reciver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=DataBindingUtil.setContentView(this,R.layout.activity_otp_verification);
        binding.btnSubmit.setOnClickListener(this);
        binding.tvRequestAnotherOtp.setOnClickListener(this);
        BounceView.addAnimTo(binding.btnSubmit);
        BounceView.addAnimTo(binding.tvRequestAnotherOtp);
        model=getIntent().getParcelableExtra("data");
        binding.tvNumber.setText(getString(R.string.enter_the_otp_recived_on_mbile)+getString(R.string.ending_with)+" "+getEncriptedMobileNumber());
        CommonUtils.showSnackBar(this, getIntent().getStringExtra("message"));
//        PhoneAuthProvider.getInstance().verifyPhoneNumber(model.getCountryCode() + model.getMobileNumber(), 60, TimeUnit.SECONDS, TaskExecutors.MAIN_THREAD, mCallbacks);
        startTimer();
    }

    private String getEncriptedMobileNumber() {
        char mobileNumber[]=model.getEmail().split("\\@")[0].toCharArray();
        String number="";
        for(int i=0;i<mobileNumber.length;i++)
        {
            if(i<6)
            {
                number+=mobileNumber[i];
            }
            else
            {
                number+="*";
            }




        }
        for(int j=0;j<model.getEmail().split("\\@")[1].toCharArray().length;j++)
        {
            number+="*";
        }

        return number;
    }

    @Override
    protected void onResume() {
        super.onResume();
        mAuth = FirebaseAuth.getInstance();
        CommonUtils.setToolbar(this,getString(R.string.otp));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.btnSubmit:
                CommonUtils.hideKeyBoard(this,binding.btnSubmit);
                if(checkValidation()) {
                    CommonUtils.showLoadingDialog(this);
                    if(getIntent().getStringExtra(ParamEnum.CAME_FROM.theValue()).equalsIgnoreCase(SignupActivity.class.getSimpleName())) signupApi();
                    else if(getIntent().getStringExtra(ParamEnum.CAME_FROM.theValue()).equalsIgnoreCase(ForgotPasswordActivity.class.getSimpleName())) changePasswordDailog();
                    }
            break;


            case R.id.tvRequestAnotherOtp:
            if(getIntent().getStringExtra(ParamEnum.CAME_FROM.theValue()).equalsIgnoreCase(SignupActivity.class.getSimpleName())) checkEmailAndMobileAvailabilityApi();
            else if(getIntent().getStringExtra(ParamEnum.CAME_FROM.theValue()).equalsIgnoreCase(ForgotPasswordActivity.class.getSimpleName())) checkEmailForForgotPasswordApi();
            break;
        }
    }


    private void checkEmailForForgotPasswordApi() {
        try {
            ServicesInterface anInterface = ServicesConnection.getInstance().createService(this);
            Call<CommonModel> call = anInterface.checkEmailForForgotPassword(model.getEmail());
            ServicesConnection.getInstance().enqueueWithoutRetry(call, this, true, new Callback<CommonModel>() {
                @Override
                public void onResponse(Call<CommonModel> call, Response<CommonModel> response) {
                    if(response.isSuccessful())
                    {
                        CommonModel serverResponse=response.body();
                        if(serverResponse.getStatus().equalsIgnoreCase(ParamEnum.SUCCESS.theValue()))
                        {
                            model.setOtp(serverResponse.getData().getOtp());
                            CommonUtils.showSnackBar(OTPVerificationActivity.this, serverResponse.getMessage());
                            binding.tvRequestAnotherOtp.setVisibility(View.INVISIBLE);
                            binding.tvTimer.setVisibility(View.VISIBLE);
                            startTimer();

                        }else if(serverResponse.getStatus().equalsIgnoreCase(ParamEnum.FAILURE.theValue()))
                        {
                            CommonUtils.showSnackBar(OTPVerificationActivity.this,serverResponse.getMessage());
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
    private void checkEmailAndMobileAvailabilityApi() {
        try {
            ServicesInterface anInterface = ServicesConnection.getInstance().createService(this);
            Call<CommonModel> call = anInterface.checkEmailAndMobileAvailability(model.getEmail(),model.getCountryCode(),model.getMobileNumber());
            ServicesConnection.getInstance().enqueueWithoutRetry(call, this, true, new Callback<CommonModel>() {
                @Override
                public void onResponse(Call<CommonModel> call, Response<CommonModel> response) {
                    if(response.isSuccessful())
                    {
                        CommonModel serverResponse=response.body();
                        if(serverResponse.getStatus().equalsIgnoreCase(ParamEnum.SUCCESS.theValue()))
                        {
                            model.setOtp(serverResponse.getData().getOtp());
                            CommonUtils.showSnackBar(OTPVerificationActivity.this, serverResponse.getMessage());
                            binding.tvRequestAnotherOtp.setVisibility(View.INVISIBLE);
                            binding.tvTimer.setVisibility(View.VISIBLE);
                            startTimer();

                        }else if(serverResponse.getStatus().equalsIgnoreCase(ParamEnum.FAILURE.theValue()))
                        {
                            CommonUtils.showSnackBar(OTPVerificationActivity.this,serverResponse.getMessage());
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
        if(binding.pinview.getText().toString().length()==0 || binding.pinview.getText().toString().length()<4)
        {
            ret=false;
            if(binding.pinview.getText().toString().length()==0) CommonUtils.showSnackBar(this,getString(R.string.enter_otp));
            else if(binding.pinview.getText().toString().length()<6) CommonUtils.showSnackBar(this,getString(R.string.enter_valid_otp));
        }
        else if(!model.getOtp().equalsIgnoreCase(binding.pinview.getText().toString().trim()))
        {
            ret=false;
            CommonUtils.showSnackBar(this, getString(R.string.invalid_code_entered));
        }

        return ret;
    }

    private void changePasswordDailog() {
        CommonUtils.dismissLoadingDialog();
        final Dialog dialog=new Dialog(this,R.style.Theme_AppCompat_Light_Dialog_Alert);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        final DailogResetPasswordBinding binding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.dailog_reset_password, null, false);
        dialog.setContentView(binding.getRoot());
        dialog.setCancelable(true);
        binding.ivPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(((BitmapDrawable)binding.ivPassword.getDrawable()).getBitmap()==((BitmapDrawable)getDrawable(R.drawable.eye)).getBitmap())
                {
                    binding.ivPassword.setImageBitmap(((BitmapDrawable)getDrawable(R.drawable.eye_s)).getBitmap());
                    binding.edPassword.setTransformationMethod(new HideReturnsTransformationMethod());
                }else
                {
                    binding.ivPassword.setImageBitmap(((BitmapDrawable)getDrawable(R.drawable.eye)).getBitmap());
                    binding.edPassword.setTransformationMethod(new PasswordTransformationMethod());
                }
            }
        });

        binding.ivConfirmPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(((BitmapDrawable)binding.ivConfirmPassword.getDrawable()).getBitmap()==((BitmapDrawable)getDrawable(R.drawable.eye)).getBitmap())
                {
                    binding.ivConfirmPassword.setImageBitmap(((BitmapDrawable)getDrawable(R.drawable.eye_s)).getBitmap());
                    binding.edConfirmPassword.setTransformationMethod(new HideReturnsTransformationMethod());
                }else
                {
                    binding.ivConfirmPassword.setImageBitmap(((BitmapDrawable)getDrawable(R.drawable.eye)).getBitmap());
                    binding.edConfirmPassword.setTransformationMethod(new PasswordTransformationMethod());
                }
            }
        });


        binding.btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkValidationResetPassword(binding.edPassword,binding.edConfirmPassword))
                {
                    forgotPasswordApi(dialog,binding.edPassword.getText().toString());
                }

            }
        });
        dialog.show();

    }

    private void forgotPasswordApi(final Dialog dialog, String newPassword) {
        try {
            ServicesInterface anInterface = ServicesConnection.getInstance().createService(this);
            Call<CommonModel> call = anInterface.forgotPassword(model.getUserId(),newPassword);
            ServicesConnection.getInstance().enqueueWithoutRetry(call, this, true, new Callback<CommonModel>() {
                @Override
                public void onResponse(Call<CommonModel> call, Response<CommonModel> response) {
                    if(response.isSuccessful())
                    {
                        CommonModel serverResponse=response.body();
                        if(serverResponse.getStatus().equalsIgnoreCase(ParamEnum.SUCCESS.theValue()))
                        {
                            dialog.dismiss();
                            Intent intent = new Intent(OTPVerificationActivity.this,LoginActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);

                        }else if(serverResponse.getStatus().equalsIgnoreCase(ParamEnum.FAILURE.theValue()))
                        {
                            CommonUtils.showSnackBar(OTPVerificationActivity.this,serverResponse.getMessage());
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

    private boolean checkValidationResetPassword(EditText edPassword, EditText edConfirmPassword) {
        boolean ret=true;

         if(edPassword.getText().toString().trim().length()==0  || edPassword.getText().toString().trim().length()<8)
        {
            ret=false;
            if(edPassword.getText().toString().trim().length()==0) CommonUtils.showSnackBar(this,getString(R.string.enter_your_password));
            else if(edPassword.getText().toString().trim().length()<8) CommonUtils.showSnackBar(this,getString(R.string.password_atleast_of_characters));
        }else if(edConfirmPassword.getText().toString().trim().length()==0  || edConfirmPassword.getText().toString().trim().length()<8)
        {
            ret=false;
            if(edConfirmPassword.getText().toString().trim().length()==0) CommonUtils.showSnackBar(this,getString(R.string.enter_your_confirm_password));
            else if(edConfirmPassword.getText().toString().trim().length()<8) CommonUtils.showSnackBar(this,getString(R.string.enter_your_confirm_pass_atleast_characters));
        }else if(!edPassword.getText().toString().trim().equals(edConfirmPassword.getText().toString().trim()))
        {
            ret=false;
            CommonUtils.showSnackBar(this,getString(R.string.confirm_password_does_not_match));

        }

        return ret;
    }


    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            String code = phoneAuthCredential.getSmsCode();
            if (code != null && verificationId!=null) {
                PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
                mAuth.signInWithCredential(credential).addOnCompleteListener(OTPVerificationActivity.this);
            }
        }

        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            verificationId=s;
            Log.e("onCodeSent",s);
        }


        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            CommonUtils.showSnackBar(OTPVerificationActivity.this,e.getMessage());
        }
    };

    @Override
    public void onComplete(@NonNull Task<AuthResult> task) {
        if(task.isSuccessful())
        {

        }else if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
            CommonUtils.dismissLoadingDialog();
            CommonUtils.showSnackBar(this,getString(R.string.invalid_code_entered));
        }
    }


    private void startTimer() {

        countDownTimer = new CountDownTimer(120000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                isTimerRunning=true;
                binding.tvTimer.setText(""+String.format("%d:%d", TimeUnit.MILLISECONDS.toMinutes( millisUntilFinished),
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));
            }

            @Override
            public void onFinish() {
                if(isTimerRunning) {
                    binding.tvTimer.setVisibility(View.INVISIBLE);
                    isTimerRunning=false;
                    binding.tvRequestAnotherOtp.setVisibility(View.VISIBLE);
                }
            }
        }.start();
    }


    private void signupApi() {
        try {
            ServicesInterface anInterface = ServicesConnection.getInstance().createService(this);
            Call<CommonModel> call = anInterface.userSignup(getEmiratesIdPicture(),getInsuranceCardPicture(),getUserData());
            ServicesConnection.getInstance().enqueueWithoutRetry(call, this, false, new Callback<CommonModel>() {
                @Override
                public void onResponse(Call<CommonModel> call, Response<CommonModel> response) {
                    if(response.isSuccessful())
                    {
                        CommonUtils.dismissLoadingDialog();
                        CommonModel serverResponse=response.body();
                        if(serverResponse.getStatus().equalsIgnoreCase(ParamEnum.SUCCESS.theValue()))
                        {
                            Intent intent = new Intent(OTPVerificationActivity.this,MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                            SPrefrenceValues.saveUserData(OTPVerificationActivity.this,response.body());
                            startActivity(intent);
                        }else
                        {
                            CommonUtils.showSnackBar(OTPVerificationActivity.this,serverResponse.getMessage());
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

    private Map<String, RequestBody> getUserData() {
        AddRequestBody body = new AddRequestBody(model);
        return body.getBody();
    }

    private MultipartBody.Part getInsuranceCardPicture() {
        MultipartBody.Part insuranceCardPicturePart = null;
        try {
            if (model.getInsuranceCardPicture() != null) {
                File file = new File(model.getInsuranceCardPicture());
                RequestBody body = RequestBody.create(MediaType.parse("image/*"),  Compressor.getDefault(this).compressToFile(file));
                insuranceCardPicturePart = MultipartBody.Part.createFormData("insuranceCardPicture", file.getName(), body);
            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }

        return insuranceCardPicturePart;
    }

    private MultipartBody.Part getEmiratesIdPicture() {
        MultipartBody.Part emirateIdPicturePart=null;
        try {
            if (model.getEmiratesIdPicture() != null) {
                File file = new File(model.getEmiratesIdPicture());
                RequestBody body = RequestBody.create(MediaType.parse("image/*"),  Compressor.getDefault(this).compressToFile(file));
                emirateIdPicturePart = MultipartBody.Part.createFormData("emiratesIdPicture", file.getName(), body);
            }

        }catch (Exception e)
        {
            e.printStackTrace();
        }
        return emirateIdPicturePart;
    }


}
