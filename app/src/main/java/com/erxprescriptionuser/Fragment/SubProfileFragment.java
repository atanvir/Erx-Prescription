package com.erxprescriptionuser.Fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.erxprescriptionuser.Activity.AddressPicker;
import com.erxprescriptionuser.Activity.LoginActivity;
import com.erxprescriptionuser.Activity.MainActivity;
import com.erxprescriptionuser.Activity.SignupActivity;
import com.erxprescriptionuser.BuildConfig;
import com.erxprescriptionuser.Model.CommonModel;
import com.erxprescriptionuser.R;
import com.erxprescriptionuser.Retrofit.ServicesConnection;
import com.erxprescriptionuser.Retrofit.ServicesInterface;
import com.erxprescriptionuser.SharePrefrences.SPreferenceKey;
import com.erxprescriptionuser.SharePrefrences.SPrefrenceValues;
import com.erxprescriptionuser.SharePrefrences.SharedPreferenceWriter;
import com.erxprescriptionuser.Utils.CommonUtils;
import com.erxprescriptionuser.Utils.FilePath;
import com.erxprescriptionuser.Utils.ImageGlider;
import com.erxprescriptionuser.Utils.ParamEnum;
import com.erxprescriptionuser.databinding.FragmentSubProfileBinding;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import hari.bounceview.BounceView;
import id.zelory.compressor.Compressor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;

public class SubProfileFragment extends Fragment implements View.OnClickListener {
    private FragmentSubProfileBinding binding;
    private Double latitue,longitute;
    private final int CAMERA=2;
    private String path;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding=DataBindingUtil.inflate(inflater, R.layout.fragment_sub_profile,container,false);
        binding.ciUser.setOnClickListener(this);
        binding.btnEditProfile.setOnClickListener(this);
        binding.edName.setOnClickListener(this);
        binding.edEmailId.setOnClickListener(this);
        binding.edMobileNo.setOnClickListener(this);
        binding.edAddress.setOnClickListener(this);
        BounceView.addAnimTo(binding.btnEditProfile);
        BounceView.addAnimTo(binding.ciUser);
        latitue=getArguments().getDouble("latitude");
        longitute=getArguments().getDouble("longitude");
        getUserDetailsApi();
        return binding.getRoot();
    }

    private void takePhotoIntent(int code) {
        String capture_dir= Environment.getExternalStorageDirectory() + "/ErxPrescription/Images/";
        File file = new File(capture_dir);
        if (!file.exists())
        {
            file.mkdirs();
        }
        path = capture_dir + System.currentTimeMillis() + ".jpg";
        Uri imageFileUri = FileProvider.getUriForFile(requireActivity().getApplicationContext(), BuildConfig.APPLICATION_ID + ".provider", new File(path));
        Intent intent = new CommonUtils().getPickIntent(getActivity(),imageFileUri);
        startActivityForResult(intent, code);
    }


    private void getUserDetailsApi() {
        try {
            ServicesInterface anInterface = ServicesConnection.getInstance().createService(getActivity());
            Call<CommonModel> call = anInterface.getUserDetails();
            ServicesConnection.getInstance().enqueueWithoutRetry(call, getActivity(), true, new Callback<CommonModel>() {
                @Override
                public void onResponse(Call<CommonModel> call, Response<CommonModel> response) {
                    if(response.isSuccessful())
                    {
                        CommonModel serverResponse=response.body();
                        if(serverResponse.getStatus().equalsIgnoreCase(ParamEnum.SUCCESS.theValue()))
                        {
                            binding.ciProgressBar.setVisibility(View.VISIBLE);
                            Glide.with(getActivity()).load(serverResponse.getData().getProfilePic()).listener(new RequestListener<Drawable>() {
                                @Override
                                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                    binding.ciProgressBar.setVisibility(View.GONE);
                                    return false;
                                }

                                @Override
                                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                    binding.ciProgressBar.setVisibility(View.GONE);
                                    return false;
                                }
                            }).placeholder(R.drawable.user_thumbnail).into(binding.ciUser);
                            binding.edName.setText(serverResponse.getData().getFullName());
                            binding.edEmailId.setText(serverResponse.getData().getEmail());
                            binding.edMobileNo.setText(serverResponse.getData().getMobileNumber());
                            binding.edAddress.setText(serverResponse.getData().getAddress());
                            binding.ccode.setText("+971");
                            latitue=serverResponse.getData().getLatitude();
                            longitute=serverResponse.getData().getLongitude();

                        }else if(serverResponse.getStatus().equalsIgnoreCase("500"))
                        {
                            CommonUtils.showSnackBar(getActivity(),serverResponse.getMessage());
                        }
                    }
                }

                @Override
                public void onFailure(Call<CommonModel> call, Throwable t) {
                    Log.e("failure",t.getMessage());

                }
            });
        } catch (Exception e) {
            Toast.makeText(getActivity(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.btnEditProfile:
            if(binding.btnEditProfile.getText().toString().equalsIgnoreCase(getString(R.string.edit_profile)))
            {
                binding.btnEditProfile.setBackgroundTintList(getActivity().getColorStateList(R.color.colorPrimary));
                binding.btnEditProfile.setText(R.string.save);
                setFocusable(true);

            }else
            {
                if(checkValidation()) {
                    CommonUtils.showLoadingDialog(getActivity());
                    changeMobileNumberApi();
                }
            }
            break;

            case R.id.ciUser:
            if(checkButtonAction()) takePhotoIntent(CAMERA);
            break;

            case R.id.edName:
            if(checkButtonAction())
            break;

            case R.id.edEmailId:
            if(checkButtonAction())
            break;

            case R.id.edMobileNo:
            if(checkButtonAction())
            break;

            case R.id.edAddress:
            if(checkButtonAction())
            {
                Intent intent = new Intent(getActivity(), AddressPicker.class);
                intent.putExtra("latitue",latitue);
                intent.putExtra("longitute",longitute);
                startActivityForResult(intent, 101);
            }
            break;
        }
    }

    private void changeMobileNumberApi() {
        try {
            ServicesInterface anInterface = ServicesConnection.getInstance().createService(getActivity());
            Call<CommonModel> call = anInterface.changeMobileNumber("+971",binding.edMobileNo.getText().toString());
            ServicesConnection.getInstance().enqueueWithoutRetry(call, getActivity(), false, new Callback<CommonModel>() {
                @Override
                public void onResponse(Call<CommonModel> call, Response<CommonModel> response) {
                    if(response.isSuccessful())
                    {
                        CommonModel serverResponse=response.body();
                        if(serverResponse.getStatus().equalsIgnoreCase(ParamEnum.SUCCESS.theValue()))
                        {
                            updateUserDetailsApi();

                        }else if(serverResponse.getStatus().equalsIgnoreCase(ParamEnum.FAILURE.theValue()))
                        {
                            CommonUtils.dismissLoadingDialog();
                            CommonUtils.showSnackBar(getActivity(),serverResponse.getMessage());
                        }
                    }
                }

                @Override
                public void onFailure(Call<CommonModel> call, Throwable t) {
                    Log.e("failure",t.getMessage());

                }
            });
        } catch (Exception e) {
            Toast.makeText(getActivity(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

    private void updateUserDetailsApi() {
        try {
            ServicesInterface anInterface = ServicesConnection.getInstance().createService(getActivity());
            Call<CommonModel> call = anInterface.userUpdateDetails(getProfilePic(),getUserData());
            ServicesConnection.getInstance().enqueueWithoutRetry(call, getActivity(), false, new Callback<CommonModel>() {
                @Override
                public void onResponse(Call<CommonModel> call, Response<CommonModel> response) {
                    if(response.isSuccessful())
                    {
                        CommonUtils.dismissLoadingDialog();
                        CommonModel serverResponse=response.body();
                        if(serverResponse.getStatus().equalsIgnoreCase(ParamEnum.SUCCESS.theValue()))
                        {

                            binding.viewAddress.setVisibility(View.GONE);
                            binding.noView.setVisibility(View.GONE);
                            binding.nameView.setVisibility(View.GONE);
                            binding.emailView.setVisibility(View.GONE);
                            binding.btnEditProfile.setText(getString(R.string.edit_profile));
                            binding.btnEditProfile.setBackgroundTintList(getActivity().getColorStateList(R.color.colorPrimaryDark));
                            SPrefrenceValues.saveUserData(getActivity(),response.body());
                            Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_SHORT).show();

                        }else if(serverResponse.getStatus().equalsIgnoreCase("500"))
                        {
                            CommonUtils.showSnackBar(getActivity(),serverResponse.getMessage());
                        }
                    }
                }

                @Override
                public void onFailure(Call<CommonModel> call, Throwable t) {
                    Log.e("failure",t.getMessage());
                }
            });
        } catch (Exception e) {
            Toast.makeText(getActivity(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

    private Map<String, RequestBody> getUserData() {
        Map<String,RequestBody> map = new HashMap<>();
        MediaType mediaType = MediaType.parse("text/plain");
        map.put("fullName",RequestBody.create(mediaType,binding.edName.getText().toString().trim()));
        map.put("email",RequestBody.create(mediaType,binding.edEmailId.getText().toString().trim()));
        map.put("address",RequestBody.create(mediaType,binding.edAddress.getText().toString().trim()));
        map.put("latitude",RequestBody.create(mediaType,""+latitue));
        map.put("longitude",RequestBody.create(mediaType,""+longitute));
        return map;
    }

    private MultipartBody.Part getProfilePic() {
        MultipartBody.Part part = null;

        try {
            if (path != null) {
                File file = new File(path);
                RequestBody body = RequestBody.create(MediaType.parse("image/*"), Compressor.getDefault(getActivity()).compressToFile(file));
                part = MultipartBody.Part.createFormData("profilePic", file.getName(), body);
            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        return part;
    }

    private boolean checkValidation() {
        boolean ret=true;
        if(binding.edName.getText().toString().trim().length()==0)
        {
            ret=false;
            CommonUtils.showSnackBar(getActivity(), getString(R.string.enter_your_name));
        }
        else if(binding.edEmailId.getText().toString().trim().length()==0 || !Patterns.EMAIL_ADDRESS.matcher(binding.edEmailId.getText().toString()).matches())
        {
            ret=false;
            if(binding.edEmailId.getText().toString().trim().length()==0) CommonUtils.showSnackBar(getActivity(),getString(R.string.enter_email_id));
            else if(!Patterns.EMAIL_ADDRESS.matcher(binding.edEmailId.getText().toString()).matches()) CommonUtils.showSnackBar(getActivity(),getString(R.string.enter_valid_email_id));
        }else if(binding.edMobileNo.getText().toString().trim().length()==0 || binding.edMobileNo.getText().toString().trim().length()<9)
        {
            ret=false;
            if(binding.edMobileNo.getText().toString().trim().length()==0) CommonUtils.showSnackBar(getActivity(),getString(R.string.enter_mobile_number));
            else if(binding.edMobileNo.getText().toString().trim().length()<9) CommonUtils.showSnackBar(getActivity(),getString(R.string.enter_valid_mobile_number));
        }else if(binding.edAddress.getText().toString().length()==0)
        {
            ret=false;
            CommonUtils.showSnackBar(getActivity(),getString(R.string.select_your_address));
        }

        return ret;
    }

    private void setFocusable(boolean isFocousable) {
        binding.edName.setFocusableInTouchMode(isFocousable);
        binding.edEmailId.setFocusableInTouchMode(isFocousable);
        binding.edMobileNo.setFocusableInTouchMode(isFocousable);
        binding.edAddress.setFocusableInTouchMode(isFocousable);
        binding.edAddress.setFocusable(false);
        binding.ccode.setFocusableInTouchMode(isFocousable);

        binding.nameView.setVisibility(View.VISIBLE);
        binding.emailView.setVisibility(View.VISIBLE);
        binding.noView.setVisibility(View.VISIBLE);
        binding.viewAddress.setVisibility(View.VISIBLE);
        binding.edName.requestFocus();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode)
        {
            case 101:
                if(resultCode==RESULT_OK)
                {
                    String address= data.getStringExtra("ADDRESS");
                    latitue= data.getDoubleExtra("LAT",0.0);
                    longitute= data.getDoubleExtra("LONG",0.0);
                    binding.edAddress.setText(address);
                }
                break;

            case CAMERA:
                if(resultCode== RESULT_OK) {
                    String originalPath;
                    if (data != null) {
                        path = FilePath.getPath(getActivity(), Uri.parse(data.getDataString()));
                        originalPath=data.getDataString();
                    }
                    else
                    {
                        originalPath=path;
                    }
                    binding.ciProgressBar.setVisibility(View.VISIBLE);
                    ImageGlider.setNormalImage(getActivity(),binding.ciUser,binding.ciProgressBar,originalPath);
                }
                break;
        }
    }


    private boolean checkButtonAction() {
        boolean ret=true;

        if(binding.btnEditProfile.getText().toString().equalsIgnoreCase(getString(R.string.edit_profile)))
        {
            ret=false;
            CommonUtils.showSnackBar(getActivity(),getString(R.string.enable_editing_first));

        }

        return ret;
    }
}
