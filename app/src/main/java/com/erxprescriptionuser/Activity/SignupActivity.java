package com.erxprescriptionuser.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.databinding.DataBindingUtil;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Looper;
import android.text.Editable;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.erxprescriptionuser.BuildConfig;
import com.erxprescriptionuser.Model.CommonModel;
import com.erxprescriptionuser.Model.SignupModel;
import com.erxprescriptionuser.R;
import com.erxprescriptionuser.Retrofit.ServicesConnection;
import com.erxprescriptionuser.Retrofit.ServicesInterface;
import com.erxprescriptionuser.SharePrefrences.SPreferenceKey;
import com.erxprescriptionuser.SharePrefrences.SharedPreferenceWriter;
import com.erxprescriptionuser.Utils.CommonUtils;
import com.erxprescriptionuser.Utils.FilePath;
import com.erxprescriptionuser.Utils.ImageGlider;
import com.erxprescriptionuser.Utils.MultiTextWatcher;
import com.erxprescriptionuser.Utils.ParamEnum;
import com.erxprescriptionuser.databinding.ActivitySignupBinding;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

import hari.bounceview.BounceView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignupActivity extends AppCompatActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener, RadioGroup.OnCheckedChangeListener, CompoundButton.OnCheckedChangeListener, MultiTextWatcher.TextWatcherWithInstance, GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks, OnCompleteListener<LocationSettingsResponse>, OnFailureListener, OnSuccessListener<Location> {
    ActivitySignupBinding binding;
    private final int INSURANCE_CARD=1,EMIRATES=2,CAMERA_PERMISSION=3;
    private String path = null;
    private String emirateImagePath,insurancCardPath;
    boolean isEmirate,isInsrance;
    private String gender;
    private String address;
    private Double latitue,longitute;
    private Double currentLat,currentLong;
    String cameFrom;
    private GoogleApiClient googleApiClient=null;
    private LocationRequest locationRequest=null;
    private LocationCallback locationCallback = null;
    private FusedLocationProviderClient mFusedLocationClient = null;
    private final int LOCATION_REQ=5;
    private final int PERMISSION_DIALOG_REQ=6;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_signup);
        cameFrom="onCreate";
        intit();
        startLocationFunctioning();

    }

    private void startLocationFunctioning() {
        if (!CommonUtils.isOnline(this)) { Toast.makeText(this, getString(R.string.internet_not_available), Toast.LENGTH_SHORT).show();
        } else {
            if (CommonUtils.isGPlayServicesOK(this)) {
                buildGoogleApiClient();
            }
        }
    }

    private void buildGoogleApiClient() {
        googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .enableAutoManage(this, 0, this)
                .addApi(com.google.android.gms.location.places.Places.GEO_DATA_API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        googleApiClient.connect();
    }



    private void intit() {
        binding.edDob.setOnClickListener(this);
        binding.edLocation.setOnClickListener(this);
        binding.btnSingup.setOnClickListener(this);
        binding.llBack.setOnClickListener(this);
        binding.ivEmitrateNumber.setOnClickListener(this);
        binding.ivInsuranceCamera.setOnClickListener(this);
        binding.radioGroup.setOnCheckedChangeListener(this);
        binding.cbNotAvailable.setOnCheckedChangeListener(this);
        binding.ivPassword.setOnClickListener(this);
        binding.ivConfirmPassword.setOnClickListener(this);
        binding.tvTermCondtion.setOnClickListener(this);
        new MultiTextWatcher().registerEditText(binding.edLocation).registerEditText(binding.edEnterAddress).setCallback(this);
        BounceView.addAnimTo(binding.btnSingup);
        BounceView.addAnimTo(binding.llBack);
        BounceView.addAnimTo(binding.ivEmitrateNumber);
        BounceView.addAnimTo(binding.ivInsuranceCamera);
        BounceView.addAnimTo(binding.cbNotAvailable);
        BounceView.addAnimTo(binding.rbMale);
        BounceView.addAnimTo(binding.rbFemale);
        BounceView.addAnimTo(binding.ivPassword);
        BounceView.addAnimTo(binding.ivConfirmPassword);
        BounceView.addAnimTo(binding.tvTermCondtion);
    }

    @Override
    public void onClick(View v) {
        String langcode=SharedPreferenceWriter.getInstance(SignupActivity.this).getString(SPreferenceKey.LANGUAGE_CODE);
        switch (v.getId())
        {
            case R.id.edDob:
            Date date= Calendar.getInstance().getTime();
            DatePickerDialog dialog=new DatePickerDialog(this, android.app.AlertDialog.THEME_HOLO_LIGHT,this,
                    Integer.parseInt(new SimpleDateFormat("yyyy").format(date)),
                    Integer.parseInt(new SimpleDateFormat("MM").format(date))-1,
                    Integer.parseInt(new SimpleDateFormat("dd").format(date)));
            dialog.getDatePicker().setMaxDate(new Date().getTime());
            dialog.show();
            break;

            case R.id.tvTermCondtion:
            intent(WebviewActivity.class,getString(R.string.terms_conditions),langcode.equalsIgnoreCase("en")?ParamEnum.TERMS.theValue():ParamEnum.TERMS_AR.theValue());
            break;

            case R.id.edLocation:
            Intent intent = new Intent(this, AddressPicker.class);
            intent.putExtra("latitue",latitue);
            intent.putExtra("longitute",longitute);
            startActivityForResult(intent, 101);
            break;

            case R.id.btnSingup:
                if(checkValidation()) {
                checkEmailAndMobileAvailabilityApi();
            }
            break;

            case R.id.llBack:
            onBackPressed();
            break;

            case R.id.ivInsuranceCamera:
            if(checkPermission())
            {
                isInsrance=true;
                isEmirate=false;
                takePhotoIntent(INSURANCE_CARD);
            }
            break;

            case R.id.ivEmitrateNumber:
            if(checkPermission()) {
                isInsrance=false;
                isEmirate=true;
                takePhotoIntent(EMIRATES);
            }
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

            case R.id.ivConfirmPassword:
            if(((BitmapDrawable)binding.ivConfirmPassword.getDrawable()).getBitmap()==((BitmapDrawable)getDrawable(R.drawable.eye)).getBitmap())
            {
                binding.ivConfirmPassword.setImageBitmap(((BitmapDrawable)getDrawable(R.drawable.eye_s)).getBitmap());
                binding.edConfirmPassword.setTransformationMethod(new HideReturnsTransformationMethod());
            }else
            {
                binding.ivConfirmPassword.setImageBitmap(((BitmapDrawable)getDrawable(R.drawable.eye)).getBitmap());
                binding.edConfirmPassword.setTransformationMethod(new PasswordTransformationMethod());
            }
            break;
        }
    }

    private void intent(Class<? extends Object> className, String title,String url) {
        Intent intent= new Intent(this,className);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("Title",title);
        intent.putExtra("url",url);
        startActivity(intent);

    }

    private void checkEmailAndMobileAvailabilityApi() {
    try {
//        "+971"
        ServicesInterface anInterface = ServicesConnection.getInstance().createService(this);
        Call<CommonModel> call = anInterface.checkEmailAndMobileAvailability(binding.edEmailId.getText().toString(),SPreferenceKey.COUNTRY_CODE_API,binding.edMobileNo.getText().toString().trim());
        ServicesConnection.getInstance().enqueueWithoutRetry(call, this, true, new Callback<CommonModel>() {
            @Override
            public void onResponse(Call<CommonModel> call, Response<CommonModel> response) {
                if(response.isSuccessful())
                {
                    CommonModel serverResponse=response.body();
                    if(serverResponse.getStatus().equalsIgnoreCase(ParamEnum.SUCCESS.theValue()))
                    {
                        Intent intent = new Intent(SignupActivity.this,OTPVerificationActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra(ParamEnum.CAME_FROM.theValue(), SignupActivity.class.getSimpleName());
                        intent.putExtra("data", setUserData(serverResponse.getData().getOtp()));
                        intent.putExtra("message", serverResponse.getMessage());
                        startActivity(intent);
                    }else if(serverResponse.getStatus().equalsIgnoreCase(ParamEnum.FAILURE.theValue()))
                    {
                        CommonUtils.showSnackBar(SignupActivity.this,serverResponse.getMessage());
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

    private SignupModel setUserData(String otp) {
        SignupModel model = new SignupModel();
        model.setFullName(binding.edFullName.getText().toString().trim());
        model.setCountryCode("+971");
        model.setMobileNumber(binding.edMobileNo.getText().toString().trim());
        model.setEmail(binding.edEmailId.getText().toString().trim());
        model.setEmiratesId(binding.edEmiratesId.getText().toString().trim());
        model.setEmiratesIdPicture(emirateImagePath);
        if(!binding.cbNotAvailable.isChecked())
        {
            model.setInsuranceCardPicture(insurancCardPath);
        }
        model.setDob(binding.edDob.getTag().toString().trim());
        model.setGender(gender);
        model.setAddress(address);
        model.setLongitude(longitute);
        model.setLatitude(latitue);
        model.setAdditionalMobileNo(binding.edAddtionalMobileNo.getText().toString().trim());
        model.setPassword(binding.edPassword.getText().toString().trim());
        model.setDeviceToken(SharedPreferenceWriter.getInstance(SignupActivity.this).getString(SPreferenceKey.TOKEN));
        model.setDeviceType(ParamEnum.ANDROID.theValue());
        model.setOtp(otp);
        return model;
    }

    private boolean checkValidation() {
        boolean ret=true;
        if(binding.edFullName.getText().toString().trim().length()==0)
        {
            ret=false;
            CommonUtils.showSnackBar(this,getString(R.string.enter_full_name));
        }else if(binding.edMobileNo.getText().toString().trim().length()==0 || binding.edMobileNo.getText().toString().trim().length()<9)
        {
            ret=false;
            if(binding.edMobileNo.getText().toString().trim().length()==0) CommonUtils.showSnackBar(this, getString(R.string.enter_mobile_number));
            else if( binding.edMobileNo.getText().toString().trim().length()<9) CommonUtils.showSnackBar(this,getString(R.string.valid_phone_number));
        }else if(binding.edEmailId.getText().toString().trim().length()==0 || !Patterns.EMAIL_ADDRESS.matcher(binding.edEmailId.getText().toString()).matches())
        {
            ret=false;
            if(binding.edEmailId.getText().toString().trim().length()==0) CommonUtils.showSnackBar(this,getString(R.string.enter_email_id));
            else if(!Patterns.EMAIL_ADDRESS.matcher(binding.edEmailId.getText().toString()).matches()) CommonUtils.showSnackBar(this,getString(R.string.enter_valid_email_id));
        }else if(binding.edEmiratesId.getText().toString().trim().length()==0)
        {
            ret=false;
            CommonUtils.showSnackBar(this,getString(R.string.enter_emirates_id_number));
        }
        else if(emirateImagePath==null)
        {
            ret=false;
            CommonUtils.showSnackBar(this,getString(R.string.attach_the_photo_of_emirates_id_number));
        }else if(!binding.cbNotAvailable.isChecked() && insurancCardPath==null)
        {
            ret=false;
            CommonUtils.showSnackBar(this,getString(R.string.attach_the_photo_of_insrance_card));

        }else if(binding.edDob.getText().toString().trim().length()==0)
        {
            ret=false;
            CommonUtils.showSnackBar(this,getString(R.string.select_your_dob));
        }else if(gender==null)
        {
            ret=false;
            CommonUtils.showSnackBar(this,getString(R.string.select_gender));
        }else if(address==null)
        {
            ret=false;
            CommonUtils.showSnackBar(this,getString(R.string.add_your_address));
        }else if(binding.edAddtionalMobileNo.getText().length()>0 && binding.edAddtionalMobileNo.getText().toString().length()<9)
        {
            ret=false;
            CommonUtils.showSnackBar(this,getString(R.string.valid_additional_mobile_number));
        }else if(binding.edAddtionalMobileNo.getText().toString().trim().equalsIgnoreCase(binding.edMobileNo.getText().toString().trim()))
        {
            ret=false;
            CommonUtils.showSnackBar(this,getString(R.string.add_differ_additional_number));
        }

        else if(binding.edPassword.getText().toString().trim().length()==0  || binding.edPassword.getText().toString().trim().length()<8)
        {
            ret=false;
            if(binding.edPassword.getText().toString().trim().length()==0) CommonUtils.showSnackBar(this,getString(R.string.enter_your_password));
            else if(binding.edPassword.getText().toString().trim().length()<8) CommonUtils.showSnackBar(this,getString(R.string.password_atleast_of_characters));
        }else if(binding.edConfirmPassword.getText().toString().trim().length()==0  || binding.edConfirmPassword.getText().toString().trim().length()<8)
        {
            ret=false;
            if(binding.edConfirmPassword.getText().toString().trim().length()==0) CommonUtils.showSnackBar(this,getString(R.string.enter_your_confirm_password));
            else if(binding.edConfirmPassword.getText().toString().trim().length()<8) CommonUtils.showSnackBar(this,getString(R.string.enter_your_confirm_password_characters));
        }else if(!binding.edPassword.getText().toString().trim().equals(binding.edConfirmPassword.getText().toString().trim()))
        {
          ret=false;
          CommonUtils.showSnackBar(this,getString(R.string.confirm_password_does_not_match));

        }else if(!binding.cbTerms.isChecked()){
            ret=false;
            CommonUtils.showSnackBar(this,getString(R.string.mark_as_aggred_to_term));
        }

        return ret;
    }

    private boolean checkPermission() {
        boolean ret = true;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ){
                ret = false;
                requestPermissions(new String[]{Manifest.permission.CAMERA,Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE}, CAMERA_PERMISSION);

            }
        }

        return ret;
    }

    private void takePhotoIntent(int code) {
        String capture_dir= Environment.getExternalStorageDirectory() + "/ErxPrescription/Images/";
        File file = new File(capture_dir);
        if (!file.exists())
        {
            file.mkdirs();
        }
        path = capture_dir + System.currentTimeMillis() + ".jpg";
        Uri imageFileUri = FileProvider.getUriForFile(Objects.requireNonNull(getApplicationContext()), BuildConfig.APPLICATION_ID + ".provider", new File(path));
        Intent intent = new CommonUtils().getPickIntent(this,imageFileUri);
        startActivityForResult(intent, code);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults.length>0)
        {
            switch (requestCode)
            {
                case LOCATION_REQ:
                boolean permissionDenied=false;
                for(int i=0;i<grantResults.length;i++){ if(grantResults[i]==PackageManager.PERMISSION_DENIED){ permissionDenied=true;break; } }
                if (permissionDenied) CommonUtils.showSnackBar(this, getString(R.string.needs_to_allow_to_use_this_functionality));
                else startLocationFunctioning();
                break;
                case CAMERA_PERMISSION:
                boolean camera = grantResults[0]==PackageManager.PERMISSION_GRANTED;
                boolean read=grantResults[1]==PackageManager.PERMISSION_GRANTED;
                boolean write=grantResults[2]==PackageManager.PERMISSION_GRANTED;
                if(camera && read && write){
                    if(isInsrance) takePhotoIntent(INSURANCE_CARD);
                    else if(isEmirate) takePhotoIntent(EMIRATES);
                }
                else { CommonUtils.showSnackBar(this,getString(R.string.needs_to_allow_permission)); isInsrance=false; isEmirate=false; };
                break;

            }



        }


    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");
        Date date= null;
        try {
            date = simpleDateFormat.parse(year+"-"+(month+1)+"-"+dayOfMonth);
            StringBuilder builder=new StringBuilder();
            builder.append(simpleDateFormat.format(date));
            String newDate=new SimpleDateFormat("dd MMMM yyyy").format(date);
            binding.edDob.setTag(builder);
            binding.edDob.setText(newDate);

        } catch (ParseException e) {
            e.printStackTrace();
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode)
        {
            case 101:
            if(resultCode==RESULT_OK)
            {
               address= data.getStringExtra("ADDRESS");
               latitue= data.getDoubleExtra("LAT",0.0);
               longitute= data.getDoubleExtra("LONG",0.0);
               binding.edLocation.setText(address);

            }
            break;

            case PERMISSION_DIALOG_REQ:
            if(resultCode==Activity.RESULT_OK) { loadCurrentLoc(); }
            else if(resultCode==RESULT_CANCELED) {
                CommonUtils.showSnackBar(this, getString(R.string.please_turn_gps));
                setUpLocationSettingsTaskStuff();
            }
            break;


            case INSURANCE_CARD:
            if(resultCode== Activity.RESULT_OK) {
                if (data != null) {
                    path = FilePath.getPath(this, Uri.parse(data.getDataString()));
                    insurancCardPath = path;
                    binding.ivInsuranceCamera.setPadding(0, 0, 0, 0);
                    ImageGlider.setRoundImage(this, binding.ivInsuranceCamera, null, "" + Uri.parse(data.getDataString()));
                } else {
                    insurancCardPath = path;
                    binding.ivInsuranceCamera.setPadding(0, 0, 0, 0);
                    ImageGlider.setRoundImage(this, binding.ivInsuranceCamera, null, "" + Uri.parse(path));
                }
            }else if(resultCode==Activity.RESULT_CANCELED)
            {
//                if(insurancCardPath==null){
//                    binding.ivInsuranceCamera.setPadding(15, 15, 15, 15);
//                    binding.ivInsuranceCamera.setBackground(ContextCompat.getDrawable(this,R.drawable.cam_white));
//                }
            }
            break;

            case EMIRATES:
            if(resultCode==Activity.RESULT_OK){
            if(data!=null) {
                path= FilePath.getPath(this,Uri.parse(data.getDataString())); emirateImagePath=path;
                binding.ivEmitrateNumber.setPadding(0, 0,0,0);
                ImageGlider.setRoundImage(this, binding.ivEmitrateNumber,null, ""+Uri.parse(data.getDataString()));
            }else
            {
                emirateImagePath=path;
                binding.ivEmitrateNumber.setPadding(0, 0,0,0);
                ImageGlider.setRoundImage(this, binding.ivEmitrateNumber,null, ""+Uri.parse(path));

            }}else if(resultCode==Activity.RESULT_CANCELED)
            {
//                if(emirateImagePath==null){
//                    binding.ivEmitrateNumber.setPadding(12, 12, 12, 12);
//                    binding.ivEmitrateNumber.setBackground(ContextCompat.getDrawable(this,R.drawable.cam_gray));
//                }

            }
            break;
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId)
        {
            case R.id.rbMale:
                gender="Male";
                binding.rbMale.setTextColor(ContextCompat.getColor(this,R.color.colorPrimary));
                binding.rbFemale.setTextColor(ContextCompat.getColor(this,R.color.grey));
                break;
            case R.id.rbFemale:
                gender="Female";
                binding.rbFemale.setTextColor(ContextCompat.getColor(this,R.color.colorPrimary));
                binding.rbMale.setTextColor(ContextCompat.getColor(this,R.color.grey));
                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId())
        {
            case R.id.cbNotAvailable:
            if(isChecked)
            {
                binding.clInsurance.setVisibility(View.GONE);
                binding.tvInsuranceCardPicture.setVisibility(View.GONE);
            }else
            {
                binding.clInsurance.setVisibility(View.VISIBLE);
                binding.tvInsuranceCardPicture.setVisibility(View.VISIBLE);
            }
            break;
        }
    }

    @Override
    public void beforeTextChanged(EditText editText, CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(EditText editText, CharSequence s, int start, int before, int count) {
        switch (editText.getId())
        {
            case R.id.edEnterAddress:
            address=s.toString();
            latitue=currentLat;
            longitute=currentLong;
            break;
        }

    }

    @Override
    public void afterTextChanged(EditText editText, Editable editable) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        setUpLocationSettingsTaskStuff();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    public  void setUpLocationSettingsTaskStuff() {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(createLocationRequest());
        builder.setAlwaysShow(true);
        SettingsClient client = LocationServices.getSettingsClient(this);
        Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());
        task.addOnCompleteListener(this);
        task.addOnFailureListener(this);
    }

    private LocationRequest createLocationRequest() {
        locationRequest = LocationRequest.create();
        locationRequest.setInterval(2000);
        locationRequest.setFastestInterval(10 * 1000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        return locationRequest;
    }

    @Override
    public void onComplete(@NonNull Task<LocationSettingsResponse> task) {
        loadCurrentLoc();
    }


    private void loadCurrentLoc() {
        try {
            locationCallback=new LocationCallback(){
                @Override
                public void onLocationResult(LocationResult locationResult) {
                    for (Location location : locationResult.getLocations()) {
                        if (location != null) {
                            locationCallBack(location);
                            mFusedLocationClient.removeLocationUpdates(locationCallback);
                            break;
                        } } }};

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQ);
                }
            } else {
                mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
                mFusedLocationClient.getLastLocation().addOnSuccessListener(this);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void locationCallBack(Location location) {
        latitue = location.getLatitude();
        longitute=location.getLongitude();
        currentLat=latitue;
        currentLong=longitute;
    }


    @Override
    public void onFailure(@NonNull Exception e) {
        if (e instanceof ResolvableApiException) {
            try { ((ResolvableApiException) e).startResolutionForResult(this, PERMISSION_DIALOG_REQ); }
            catch (IntentSender.SendIntentException sendEx) { sendEx.printStackTrace(); }
        }

    }

    @SuppressLint("MissingPermission")
    @Override
    public void onSuccess(Location location) {
        if(location!=null) locationCallBack(location);
        else mFusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
    }
}
