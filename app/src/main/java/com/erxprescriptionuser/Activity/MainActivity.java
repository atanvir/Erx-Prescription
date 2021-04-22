package com.erxprescriptionuser.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.erxprescriptionuser.BuildConfig;
import com.erxprescriptionuser.Firebase.MyFirebaseMessageService;
import com.erxprescriptionuser.Fragment.HomeFragment;
import com.erxprescriptionuser.Fragment.NotificationFragment;
import com.erxprescriptionuser.Fragment.PrescriptionFragment;
import com.erxprescriptionuser.Fragment.MyOfferFragment;
import com.erxprescriptionuser.Fragment.SettingFragment;
import com.erxprescriptionuser.Fragment.SubProfileFragment;
import com.erxprescriptionuser.Model.CommonDataStringModel;
import com.erxprescriptionuser.Model.CommonModel;
import com.erxprescriptionuser.R;


import com.erxprescriptionuser.Retrofit.ServicesConnection;
import com.erxprescriptionuser.Retrofit.ServicesInterface;
import com.erxprescriptionuser.SharePrefrences.SPreferenceKey;
import com.erxprescriptionuser.SharePrefrences.SPrefrenceValues;
import com.erxprescriptionuser.SharePrefrences.SharedPreferenceWriter;
import com.erxprescriptionuser.Utils.CommonUtils;
import com.erxprescriptionuser.Utils.FilePath;
import com.erxprescriptionuser.Utils.ParamEnum;
import com.erxprescriptionuser.Utils.SwitchFragment;
import com.erxprescriptionuser.databinding.ActivityMainBinding;
import com.erxprescriptionuser.databinding.DailogImageUploadedBinding;
import com.erxprescriptionuser.databinding.DailogRateUsBinding;
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
import java.util.HashMap;
import java.util.Map;

import company.tap.gosellapi.open.controllers.SDKSession;
import de.hdodenhof.circleimageview.CircleImageView;
import hari.bounceview.BounceView;
import id.zelory.compressor.Compressor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, MyFirebaseMessageService.INotificationCount, TextWatcher, TextView.OnEditorActionListener, GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks, OnCompleteListener<LocationSettingsResponse>, OnSuccessListener<Location>, OnFailureListener {
    // Main layout
    // Sub Main Toolbar
    ConstraintLayout clSubMain;
    ImageView ivMenu,ivSearch,ivNotification,ivproviderNameSearch;
    Toolbar subMainToolbar;
    EditText edSearch;
    private SDKSession sdkSession;

    // Main Toolbar
    Toolbar toolbar;
    ConstraintLayout clMain;
    LinearLayout llBack;
    TextView tvTitle,tvCount;
    ImageView ivLogout;
    private static IMapListner listner;

    ActivityMainBinding binding;
    private boolean isBack=false;

    // Navigation Drawer
    ConstraintLayout clProfile,clHome,clNotification,clMyOffers,clPrescription,clShareApp,clSetting,clRateUs;
    TextView tvHome,tvNotifications,tvMyOffers,tvPrescription,tvShare,tvRateUs,tvSettings;
    ImageView ivHome,ivNotifications,ivMyOffers,ivPrescription,ivShare,ivRateUs,ivSettings;
    CircleImageView ciUser;
    TextView tvUserName,tvMobileNo;
    ProgressBar ciProgressBar;
    private final int SELFIE=2,GALLERY=3;
    private Double longitude,latitude;
    private String path;
    private ImageView ivDot;
    private GoogleApiClient googleApiClient=null;
    private LocationRequest locationRequest=null;
    private LocationCallback locationCallback = null;
    private FusedLocationProviderClient mFusedLocationClient = null;
    private final int LOCATION_REQ=5;
    private final int PERMISSION_DIALOG_REQ=6;
    boolean isCalled;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CommonUtils.setLocale(this);
        binding=DataBindingUtil.setContentView(this,R.layout.activity_main);
        binding.drwer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        init(binding.getRoot());
        if(SharedPreferenceWriter.getInstance(MainActivity.this).getBoolean(SPreferenceKey.NOTI_OFFER))
        {
            ivDot.setVisibility(View.VISIBLE);
        }
        MyFirebaseMessageService.setListner(this);
        getNoficiationCountApi();
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

    @Override
    protected void onResume() {
        super.onResume();

        //Set Drawer Values
        Glide.with(MainActivity.this).load(SharedPreferenceWriter.getInstance(MainActivity.this).getString(SPreferenceKey.PROFILE_PIC)).placeholder(R.drawable.user_thumbnail).listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                ciProgressBar.setVisibility(View.GONE);
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                ciProgressBar.setVisibility(View.GONE);
                return false;
            }
        }).into(ciUser);
    }


    private void getNoficiationCountApi() {
        try {
            ServicesInterface anInterface = ServicesConnection.getInstance().createService(this);
            Call<CommonDataStringModel> call = anInterface.getNotificationCount();
            ServicesConnection.getInstance().enqueueWithoutRetry(call, this, false, new Callback<CommonDataStringModel>() {
                @Override
                public void onResponse(Call<CommonDataStringModel> call, Response<CommonDataStringModel> response) {
                    if(response.isSuccessful())
                    {
                        CommonDataStringModel serverResponse=response.body();
                        if(serverResponse.getStatus().equalsIgnoreCase(ParamEnum.SUCCESS.theValue()))
                        {
                            long count= Long.parseLong(serverResponse.getData());
                            if(count>0)
                            {
                                tvCount.setVisibility(View.VISIBLE);
                                tvCount.setText(serverResponse.getData());
                            }

                        }else if(serverResponse.getStatus().equalsIgnoreCase(ParamEnum.FAILURE.theValue()))
                        {
                            CommonUtils.showSnackBar(MainActivity.this,serverResponse.getMessage());
                        }
                    }
                }

                @Override
                public void onFailure(Call<CommonDataStringModel> call, Throwable t) {
                    Log.e("failure",t.getMessage());
                }
            });
        } catch (Exception e) {
            Toast.makeText(this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
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

                case 12:
                boolean camera= grantResults[0]==PackageManager.PERMISSION_GRANTED;
                boolean readExternal= grantResults[1]==PackageManager.PERMISSION_GRANTED;
                boolean writeExternal= grantResults[2]==PackageManager.PERMISSION_GRANTED;

                if(camera && readExternal && writeExternal)
                {
                    cameraDailog();
                }

                break;
            }
        }
    }

    private void init(final View view) {
     binding.mainCl.setOnClickListener(this);
     binding.ivCamera.setOnClickListener(this);
     BounceView.addAnimTo(binding.ivCamera);

     // Main Toolbar
     clMain=view.findViewById(R.id.clMain);
     toolbar=view.findViewById(R.id.toolbar);
     tvTitle=view.findViewById(R.id.tvTitle);
     tvCount=view.findViewById(R.id.tvCount);
     llBack=view.findViewById(R.id.llBack);
     ivLogout=view.findViewById(R.id.ivLogout);

     llBack.setOnClickListener(this);
     ivLogout.setOnClickListener(this);
     BounceView.addAnimTo(llBack);
     BounceView.addAnimTo(ivLogout);

     // Sub Main Toolbar
     subMainToolbar=view.findViewById(R.id.subMainToolbar);
     clSubMain=view.findViewById(R.id.include4);
     setSupportActionBar(subMainToolbar);
     ivMenu=view.findViewById(R.id.ivMenu);
     ivSearch=view.findViewById(R.id.ivSearch);
     edSearch=view.findViewById(R.id.edSearch);
     ivproviderNameSearch=view.findViewById(R.id.ivproviderNameSearch);
     ivNotification=view.findViewById(R.id.ivNotification);

     ivMenu.setOnClickListener(this);
     ivSearch.setOnClickListener(this);
     ivNotification.setOnClickListener(this);
     edSearch.addTextChangedListener(this);
     ivproviderNameSearch.setOnClickListener(this);
     edSearch.setOnEditorActionListener(this);

     BounceView.addAnimTo(ivMenu);
     BounceView.addAnimTo(ivSearch);
     BounceView.addAnimTo(ivNotification);
     BounceView.addAnimTo(ivproviderNameSearch);

    //Navigation Drawer
    clProfile=view.findViewById(R.id.clProfile);
    clHome=view.findViewById(R.id.clHome);
    clNotification=view.findViewById(R.id.clNotification);
    clMyOffers=view.findViewById(R.id.clMyOffers);
    clPrescription=view.findViewById(R.id.clPrescription);
    clShareApp=view.findViewById(R.id.clShareApp);
    clRateUs=view.findViewById(R.id.clRateUs);

    clSetting=view.findViewById(R.id.clSetting);
    ivHome=view.findViewById(R.id.ivHome);
    tvHome=view.findViewById(R.id.tvHome);
    ivNotifications=view.findViewById(R.id.ivNotifications);
    tvNotifications=view.findViewById(R.id.tvNotifications);
    ivMyOffers=view.findViewById(R.id.ivMyOffers);
    tvMyOffers=view.findViewById(R.id.tvMyOffers);
    ivPrescription=view.findViewById(R.id.ivPrescription);
    tvPrescription=view.findViewById(R.id.tvPrescription);
    ivShare=view.findViewById(R.id.ivShare);
    tvShare=view.findViewById(R.id.tvShare);
    ivRateUs=view.findViewById(R.id.ivRateUs);
    tvRateUs=view.findViewById(R.id.tvRateUs);
    ivSettings=view.findViewById(R.id.ivSettings);
    tvSettings=view.findViewById(R.id.tvSettings);
    tvUserName=view.findViewById(R.id.tvUserName);
    tvMobileNo=view.findViewById(R.id.tvMobileNo);
    ciUser=view.findViewById(R.id.ciUser);
    ciProgressBar=view.findViewById(R.id.ciProgressBar);
    ivDot=view.findViewById(R.id.ivDot);

    clProfile.setOnClickListener(this);
    clHome.setOnClickListener(this);
    clNotification.setOnClickListener(this);
    clMyOffers.setOnClickListener(this);
    clPrescription.setOnClickListener(this);
    clShareApp.setOnClickListener(this);
    clRateUs.setOnClickListener(this);
    clSetting.setOnClickListener(this);

    BounceView.addAnimTo(clProfile);
    BounceView.addAnimTo(clHome);
    BounceView.addAnimTo(clNotification);
    BounceView.addAnimTo(clMyOffers);
    BounceView.addAnimTo(clPrescription);
    BounceView.addAnimTo(clShareApp);
    BounceView.addAnimTo(clRateUs);
    BounceView.addAnimTo(clSetting);

    tvUserName.setText(SharedPreferenceWriter.getInstance(this).getString(SPreferenceKey.NAME));
    tvMobileNo.setText(SharedPreferenceWriter.getInstance(this).getString(SPreferenceKey.MOBILE));
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){

            //Main Layout
            case R.id.ivCamera:
            if( checkCameraPermission()) {
                cameraDailog();
            }

//                paymentApi();
            break;

            case R.id.mainCl:
            break;

            case R.id.ivproviderNameSearch:
            CommonUtils.hideKeyBoard(this,edSearch);
            if(edSearch.getText().toString().length()>0) {
                if (listner != null) {
                    listner.onTextChange(edSearch.getText().toString(), binding.spnProvider, ivSearch, edSearch, ivproviderNameSearch, ivMenu, ivNotification);
                }
            }
            else {
                Toast.makeText(this, R.string.enter_provider_name, Toast.LENGTH_LONG).show();
            }
            break;

            //Sub Main Toolbar
            case R.id.ivMenu:
            settingBackground(tvHome.getText().toString());
            binding.drwer.openDrawer(GravityCompat.START);
            break;

            case R.id.ivNotification:
            tvCount.setVisibility(View.GONE);
            setToolbarVisibility("Main",getString(R.string.notification));
            loadFragment(new NotificationFragment());
            break;

            case R.id.ivSearch:
            showKeyBoard(this);
            ivMenu.setVisibility(View.GONE);
            tvCount.setVisibility(View.GONE);
            ivNotification.setVisibility(View.GONE);
            ivSearch.setVisibility(View.GONE);
            edSearch.setVisibility(View.VISIBLE);
            ivproviderNameSearch.setVisibility(View.VISIBLE);
            break;

            //Main Toolbar
            case R.id.llBack:
            onBackPressed();
            break;

            case R.id.ivLogout:
            logoutDailog();
            break;

            // Navigation Drawer
            case R.id.clProfile:
            setToolbarVisibility("Main",getString(R.string.my_profile));
            SubProfileFragment fragment=new SubProfileFragment();
            fragment.setArguments(getBundle());
            loadFragment(fragment);
            binding.drwer.closeDrawers();
            break;

            case R.id.clNotification:
            tvCount.setVisibility(View.GONE);
            setToolbarVisibility("Main",getString(R.string.notification));
            settingBackground(tvNotifications.getText().toString());
            loadFragment(new NotificationFragment());
            binding.drwer.closeDrawers();
            break;

            case R.id.clMyOffers:
            SharedPreferenceWriter.getInstance(MainActivity.this).writeBooleanValue(SPreferenceKey.NOTI_OFFER,false);
            ivDot.setVisibility(View.GONE);
            setToolbarVisibility("Main",getString(R.string.my_offers));
            settingBackground(tvMyOffers.getText().toString());
            loadFragment(new MyOfferFragment());
            binding.drwer.closeDrawers();
            break;

            case R.id.clHome:
            setToolbarVisibility("Sub Main","");
            settingBackground(tvHome.getText().toString());
            HomeFragment homeFragment=new HomeFragment();
            homeFragment.setArguments(getBundle());
            loadFragment(homeFragment);
            binding.drwer.closeDrawers();
            break;

            case R.id.clPrescription:
            setToolbarVisibility("Main",getString(R.string.my_prescriptions));
            settingBackground(tvPrescription.getText().toString());
            loadFragment(new PrescriptionFragment());
            binding.drwer.closeDrawers();
            break;

            case R.id.clShareApp:
            settingBackground(tvShare.getText().toString());
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, "Hey check out my app at: https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID);
            sendIntent.setType("text/plain");
            startActivity(sendIntent);
            binding.drwer.closeDrawers();
            break;

            case R.id.clRateUs:
            settingBackground(tvRateUs.getText().toString());
            binding.drwer.closeDrawers();
            try {
                Uri marketUri = Uri.parse("market://details?id=" + getPackageName());
                Intent marketIntent = new Intent(Intent.ACTION_VIEW, marketUri);
                startActivity(marketIntent);
            }catch(ActivityNotFoundException e) {
                Uri marketUri = Uri.parse("https://play.google.com/store/apps/details?id=" + getPackageName());
                Intent marketIntent = new Intent(Intent.ACTION_VIEW, marketUri);
                startActivity(marketIntent);
            }
            break;

            case R.id.clSetting:
            setToolbarVisibility("Main",getString(R.string.settings));
            settingBackground(tvSettings.getText().toString());
            loadFragment(new SettingFragment());
            binding.drwer.closeDrawers();
            break;
        }
    }


    private void paymentApi() {
        try {
            String paymentId="payIdC0A34C116A5649AA8A11E4B8E2D85250";
            ServicesInterface anInterface = ServicesConnection.getInstance().createService(this);
            Call<CommonModel> call = anInterface.payment("60656e051bd8971370f60a70","1","0",
                   "0", "1.0","01-Apr-2021","AED",paymentId,"Online",
                    "INITIATED","0","chg_LV063120210955Fq2l0104861","Pick Up");
            ServicesConnection.getInstance().enqueueWithoutRetry(call, this, false, new Callback<CommonModel>() {
                @Override
                public void onResponse(Call<CommonModel> call, Response<CommonModel> response) {
                    if(response.isSuccessful())
                    {
                        CommonModel serverResponse=response.body();
                        if(serverResponse.getMessage().equalsIgnoreCase(getString(R.string.offer_list_found_sccessfully)))
                        {
                            acceptPrescriptionRequestApi(paymentId,"Online" , "INITIATED");

                        }else
                        {
                            CommonUtils.dismissLoadingDialog();
                            Intent intent=new Intent();
                            intent.putExtra("status", serverResponse.getMessage());
                            setResult(RESULT_CANCELED,intent);
                            finish();

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


    private void acceptPrescriptionRequestApi(String transactionId,String paymentType,String status) {
        try {
            ServicesInterface anInterface = ServicesConnection.getInstance().createService(this);
            Call<CommonModel> call = anInterface.acceptPrescriptionRequest("60656e051bd8971370f60a70",
                    Double.parseDouble("28.0"),
                    Double.parseDouble("72.0"),
                    "Noida Electronic Cityskdjkdsfdkjjkadsf","Pick Up",
                    "1","01-Apr-2021","AED",transactionId,
                    paymentType,status);
            ServicesConnection.getInstance().enqueueWithoutRetry(call, this, false, new Callback<CommonModel>() {
                @Override
                public void onResponse(Call<CommonModel> call, Response<CommonModel> response) {
                    if(response.isSuccessful())
                    {
                        CommonUtils.dismissLoadingDialog();
                        CommonModel serverResponse=response.body();
                        if(serverResponse.getStatus().equalsIgnoreCase(ParamEnum.SUCCESS.theValue()))
                        {
//                            requestAcceptedDailog(serverResponse.getMessage());
                        }else if(serverResponse.getStatus().equalsIgnoreCase("500") || serverResponse.getStatus().equalsIgnoreCase(ParamEnum.FAILURE.theValue()))
                        {
                            Intent intent=new Intent();
                            intent.putExtra("status", serverResponse.getMessage());
                            setResult(RESULT_CANCELED,intent);
                            finish();
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

    public Bundle getBundle(){
        Bundle bundle=new Bundle();
        bundle.putDouble("latitude",latitude);
        bundle.putDouble("longitude",longitude);
        return bundle;
    }

    private void logoutDailog() {
        final Dialog dialog = new Dialog(this, R.style.Theme_AppCompat_Light_Dialog_Alert);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_logout);
        dialog.setCancelable(true);
        TextView tvAllow=dialog.findViewById(R.id.tvAllow);
        TextView tvDontAllow=dialog.findViewById(R.id.tvDontAllow);
        BounceView.addAnimTo(tvAllow);
        BounceView.addAnimTo(tvDontAllow);
        tvAllow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                logoutApi(dialog);
            }
        });
        tvDontAllow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void logoutApi(final Dialog dialog) {
    try {
        ServicesInterface anInterface = ServicesConnection.getInstance().createService(this);
        Call<CommonModel> call = anInterface.userLogout();
        ServicesConnection.getInstance().enqueueWithoutRetry(call, this, true, new Callback<CommonModel>() {
            @Override
            public void onResponse(Call<CommonModel> call, Response<CommonModel> response) {
                if(response.isSuccessful())
                {
                    CommonUtils.dismissLoadingDialog();
                    CommonModel serverResponse=response.body();
                    if(serverResponse.getStatus().equalsIgnoreCase(ParamEnum.SUCCESS.theValue()))
                    {
                        SPrefrenceValues.removeUserData(MainActivity.this);
                        CommonUtils.startActivity(MainActivity.this,LoginActivity.class);

                    }else if(serverResponse.getStatus().equalsIgnoreCase(ParamEnum.FAILURE.theValue()))
                    {
                        CommonUtils.showSnackBar(MainActivity.this,serverResponse.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<CommonModel> call, Throwable t) {
                Log.e("failure",t.getMessage());
            }
        });
    }
    catch (Exception e) {
        Toast.makeText(this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
    }
    }

    private void settingBackground(String view) {
        if(view.equalsIgnoreCase(getString(R.string.home)))
        {
            ivHome.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.home_s));
            tvHome.setTextColor(ContextCompat.getColor(this,R.color.colorPrimary));
            ivNotifications.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.notoficatios));
            tvNotifications.setTextColor(ContextCompat.getColor(this,android.R.color.black));
            ivPrescription.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.pres));
            tvPrescription.setTextColor(ContextCompat.getColor(this,android.R.color.black));
            ivShare.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.share));
            tvShare.setTextColor(ContextCompat.getColor(this,android.R.color.black));
            ivRateUs.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.rate));
            tvRateUs.setTextColor(ContextCompat.getColor(this,android.R.color.black));
            ivSettings.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.settings));
            tvSettings.setTextColor(ContextCompat.getColor(this,android.R.color.black));

        }else if(view.equalsIgnoreCase(getString(R.string.notifications)))
        {
            ivHome.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.home));
            tvHome.setTextColor(ContextCompat.getColor(this,android.R.color.black));
            ivNotifications.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.notoficatios_s));
            tvNotifications.setTextColor(ContextCompat.getColor(this,R.color.colorPrimary));
            ivPrescription.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.pres));
            tvPrescription.setTextColor(ContextCompat.getColor(this,android.R.color.black));
            ivShare.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.share));
            tvShare.setTextColor(ContextCompat.getColor(this,android.R.color.black));
            ivRateUs.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.rate));
            tvRateUs.setTextColor(ContextCompat.getColor(this,android.R.color.black));
            ivSettings.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.settings));
            tvSettings.setTextColor(ContextCompat.getColor(this,android.R.color.black));
        }
        else if(view.equalsIgnoreCase(getString(R.string.my_prescription)))
        {
            ivHome.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.home));
            tvHome.setTextColor(ContextCompat.getColor(this,android.R.color.black));
            ivNotifications.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.notoficatios));
            tvNotifications.setTextColor(ContextCompat.getColor(this,android.R.color.black));
            ivPrescription.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.pres_s));
            tvPrescription.setTextColor(ContextCompat.getColor(this,R.color.colorPrimary));
            ivShare.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.share));
            tvShare.setTextColor(ContextCompat.getColor(this,android.R.color.black));
            ivRateUs.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.rate));
            tvRateUs.setTextColor(ContextCompat.getColor(this,android.R.color.black));
            ivSettings.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.settings));
            tvSettings.setTextColor(ContextCompat.getColor(this,android.R.color.black));
        }
        else if(view.equalsIgnoreCase(getString(R.string.share_app)))
        {
            ivHome.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.home));
            tvHome.setTextColor(ContextCompat.getColor(this,android.R.color.black));
            ivNotifications.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.notoficatios));
            tvNotifications.setTextColor(ContextCompat.getColor(this,android.R.color.black));
            ivPrescription.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.pres));
            tvPrescription.setTextColor(ContextCompat.getColor(this,android.R.color.black));
            ivShare.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.share_s));
            tvShare.setTextColor(ContextCompat.getColor(this,R.color.colorPrimary));
            ivRateUs.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.rate));
            tvRateUs.setTextColor(ContextCompat.getColor(this,android.R.color.black));
            ivSettings.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.settings));
            tvSettings.setTextColor(ContextCompat.getColor(this,android.R.color.black));
        }
        else if(view.equalsIgnoreCase(getString(R.string.rate_us)))
        {
            ivHome.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.home));
            tvHome.setTextColor(ContextCompat.getColor(this,android.R.color.black));
            ivNotifications.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.notoficatios));
            tvNotifications.setTextColor(ContextCompat.getColor(this,android.R.color.black));
            ivPrescription.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.pres));
            tvPrescription.setTextColor(ContextCompat.getColor(this,android.R.color.black));
            ivShare.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.share));
            tvShare.setTextColor(ContextCompat.getColor(this,android.R.color.black));
            ivRateUs.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.rate_s));
            tvRateUs.setTextColor(ContextCompat.getColor(this,R.color.colorPrimary));
            ivSettings.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.settings));
            tvSettings.setTextColor(ContextCompat.getColor(this,android.R.color.black));
        }
        else if(view.equalsIgnoreCase(getString(R.string.setting)))
        {
            ivHome.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.home));
            tvHome.setTextColor(ContextCompat.getColor(this,android.R.color.black));
            ivNotifications.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.notoficatios));
            tvNotifications.setTextColor(ContextCompat.getColor(this,android.R.color.black));
            ivPrescription.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.pres));
            tvPrescription.setTextColor(ContextCompat.getColor(this,android.R.color.black));
            ivShare.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.share));
            tvShare.setTextColor(ContextCompat.getColor(this,android.R.color.black));
            ivRateUs.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.rate));
            tvRateUs.setTextColor(ContextCompat.getColor(this,android.R.color.black));
            ivSettings.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.settings_s));
            tvSettings.setTextColor(ContextCompat.getColor(this,R.color.colorPrimary));

        }
        else if(view.equalsIgnoreCase(tvMyOffers.getText().toString()))
        {
            tvMyOffers.setTextAlignment(ContextCompat.getColor(this,R.color.colorPrimary));
        }
    }

    private boolean checkCameraPermission() {
        boolean ret = true;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ret = false;
                requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE}, 12);
            }
        }

        return ret;
    }

    private void cameraDailog() {
        final Dialog dialog=new Dialog(this,R.style.Theme_AppCompat_Light_Dialog_Alert);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setContentView(R.layout.dailog_camera);
        ImageView ivCamera= dialog.getWindow().findViewById(R.id.ivCamera);
        ImageView ivGallery= dialog.getWindow().findViewById(R.id.ivGallery);
        dialog.setCanceledOnTouchOutside(true);
        ivCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Intent takePictureIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(takePictureIntent, SELFIE);
            }
        });


        ivGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent , GALLERY);
            }
        });
        dialog.show();
    }

    private void imageUploadedDialog(String message) {
        final Dialog dialog=new Dialog(this,R.style.Theme_AppCompat_Light_Dialog_Alert);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        final DailogImageUploadedBinding binding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.dailog_image_uploaded, null, false);
        binding.tvPrescription.setText(message);
        dialog.setContentView(binding.getRoot());
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
        getNoficiationCountApi();
    }

    private void rateUsDaiog() {
        final Dialog dialog=new Dialog(this,R.style.Theme_AppCompat_Light_Dialog_Alert);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        DailogRateUsBinding binding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.dailog_rate_us, null, false);
        dialog.setContentView(binding.getRoot());
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        binding.btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void setToolbarVisibility(String type, String tite) {
        if(type.equalsIgnoreCase("Sub Main"))
        {
            clMain.setVisibility(View.GONE);
            clSubMain.setVisibility(View.VISIBLE);
            binding.ivCamera.setVisibility(View.VISIBLE);
            setSupportActionBar(subMainToolbar);

        }else if(type.equalsIgnoreCase("Main"))
        {
            clMain.setVisibility(View.VISIBLE);
            clSubMain.setVisibility(View.GONE);
            binding.ivCamera.setVisibility(View.GONE);
            setSupportActionBar(toolbar);
            tvTitle.setText(tite);
            if (tite.equalsIgnoreCase(getString(R.string.my_profile))) ivLogout.setVisibility(View.VISIBLE);
            else ivLogout.setVisibility(View.GONE);
        }

        //
    }

    private void loadFragment(Fragment fragment) {
        boolean saveInstance=true;
        if(fragment instanceof HomeFragment)
        {
            saveInstance=false;
        }
        SwitchFragment.changeFragment(getSupportFragmentManager(),fragment,saveInstance,true);
    }

    @Override
    public void onBackPressed() {
        CommonUtils.setLocale(this);
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            startActivity(new Intent(this,MainActivity.class));
//                getSupportFragmentManager().popBackStack();
//                isBack = false;
//                setToolbarVisibility("Sub Main", "");
//                settingBackground(tvHome.getText().toString());
            return;
        }
        else
        {
            if(ivMenu.getVisibility()==View.GONE)
            {
                edSearch.setText("");
                ivSearch.setVisibility(View.VISIBLE);
                edSearch.setVisibility(View.GONE);
                ivproviderNameSearch.setVisibility(View.GONE);
                ivMenu.setVisibility(View.VISIBLE);
                ivNotification.setVisibility(View.VISIBLE);
                HomeFragment homeFragment=new HomeFragment();
                homeFragment.setArguments(getBundle());
                loadFragment(homeFragment);
                binding.drwer.closeDrawers();
                setToolbarVisibility("Sub Main", "");
                settingBackground(tvHome.getText().toString());
            }

            else if(!isBack) {
                isBack = true;
                Toast.makeText(this, R.string.press_again_to_exit, Toast.LENGTH_SHORT).show();
            }
            else
            {
                finishAffinity();
            }
        }

}

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode== Activity.RESULT_OK)
        {
            switch (requestCode)
            {
                case SELFIE:
                try{
                    Uri uri=CommonUtils.getImageUri(this,(Bitmap)data.getExtras().get("data"));
                    path= FilePath.getPath(this,uri);
                    prescriptionRequestApi(path);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

                case GALLERY:
                Uri uri=data.getData();
                path= FilePath.getPath(this,uri);
                prescriptionRequestApi(path);
                break;

                case PERMISSION_DIALOG_REQ:
                loadCurrentLoc();
                break;
            }

        }
        else if(resultCode==RESULT_CANCELED) {
            switch (requestCode) {

            case PERMISSION_DIALOG_REQ:
            CommonUtils.showSnackBar(this, getString(R.string.please_turn_gps));
            setUpLocationSettingsTaskStuff();
            break;

            }
        }

    }

    private void prescriptionRequestApi(String path) {
        try {
            ServicesInterface anInterface = ServicesConnection.getInstance().createService(this);
            Call<CommonModel> call = anInterface.prescriptionRequest(getPrecriptionImage(path),getUserData());
            ServicesConnection.getInstance().enqueueWithoutRetry(call, this, true, new Callback<CommonModel>() {
                @Override
                public void onResponse(Call<CommonModel> call, Response<CommonModel> response) {
                    if(response.isSuccessful())
                    {
                        CommonModel serverResponse=response.body();
                        if(serverResponse.getStatus().equalsIgnoreCase(ParamEnum.SUCCESS.theValue()))
                        {
                            imageUploadedDialog(serverResponse.getMessage());

                        }else if(serverResponse.getStatus().equalsIgnoreCase(ParamEnum.FAILURE.theValue()))
                        {
                            CommonUtils.showSnackBar(MainActivity.this,serverResponse.getMessage());
                        }
                    }
                }

                @Override
                public void onFailure(Call<CommonModel> call, Throwable t) {
                    Log.e("failure",t.getMessage());

                }
            });
        } catch (Exception e) {
            Toast.makeText(MainActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private MultipartBody.Part getPrecriptionImage(String path) {
        MultipartBody.Part part = null;

        try {
            if (path != null) {
                File file = new File(path);
                RequestBody body = RequestBody.create(MediaType.parse("image/*"),  Compressor.getDefault(this).compressToFile(file));
                part = MultipartBody.Part.createFormData("prescriptionImage", file.getName(), body);

            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        return part;
    }

    private Map<String, RequestBody> getUserData() {
        Map<String,RequestBody> map = new HashMap<>();
        MediaType mediaType = MediaType.parse("text/plain");
        map.put("longitude",RequestBody.create(mediaType,""+longitude));
        map.put("latitude",RequestBody.create(mediaType,""+latitude));
        return map;
    }



    @Override
    public void onMessageReceived(String type) {
        if(type.equalsIgnoreCase(ParamEnum.NOTI_PRESCRIPTION_OFFER.theValue()))
        {
            runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    SharedPreferenceWriter.getInstance(MainActivity.this).writeBooleanValue(SPreferenceKey.NOTI_OFFER,true);
                    ivDot.setVisibility(View.VISIBLE);
                    clMyOffers.performClick();
                }
            });

        }else
        {
            getNoficiationCountApi();
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public void afterTextChanged(Editable s) {
    }

    public static void setListner(IMapListner lisen)
    {
        listner=lisen;
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
            ivproviderNameSearch.performClick();
        }
        return false;
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

    @Override
    public void onComplete(@NonNull Task<LocationSettingsResponse> task) {
        loadCurrentLoc();
    }


        @SuppressLint("MissingPermission")
        private void loadCurrentLoc() {
        try {
            locationCallback=new LocationCallback(){
                @Override
                public void onLocationResult(LocationResult locationResult) {
                for (Location location : locationResult.getLocations()) {
                if (location != null) {
                    mFusedLocationClient.removeLocationUpdates(locationCallback);
                    locationCallBack(location);
                    break;
                } } }};

            if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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

    @SuppressLint("MissingPermission")
    @Override
    public void onSuccess(Location location) {
        if(location!=null) locationCallBack(location);
        else mFusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
    }

    private void locationCallBack(Location location) {
        latitude = location.getLatitude();
        longitude=location.getLongitude();
        if(!isCalled) {
            isCalled = true;
            HomeFragment homeFragment = new HomeFragment();
            homeFragment.setArguments(getBundle());
            loadFragment(homeFragment);
        }
    }

    @Override
    public void onFailure(@NonNull Exception e) {
        if (e instanceof ResolvableApiException) {
            try { ((ResolvableApiException) e).startResolutionForResult(this, PERMISSION_DIALOG_REQ); }
            catch (IntentSender.SendIntentException sendEx) { sendEx.printStackTrace(); }
        }
    }



    public interface IMapListner{
        void onTextChange(String text,View view,ImageView ivSearch,EditText edSearch,ImageView ivproviderNameSearch,ImageView ivMenu,ImageView ivNotification);
    }

    public static void showKeyBoard(Activity mActivity) {
        try {
            View view = mActivity.getCurrentFocus();
            if (view != null) {
                InputMethodManager keyboard = (InputMethodManager) mActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
                keyboard.showSoftInputFromInputMethod(view.getWindowToken(), InputMethodManager.SHOW_FORCED);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showDialog(String chargeID, String msg, int icon) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        PopupWindow popupWindow;
        try {
            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if (inflater != null) {

                View layout = inflater.inflate(company.tap.gosellapi.R.layout.charge_status_layout, findViewById(company.tap.gosellapi.R.id.popup_element));
                popupWindow = new PopupWindow(layout, width, 250, true);
                ImageView status_icon = layout.findViewById(company.tap.gosellapi.R.id.status_icon);
                TextView statusText = layout.findViewById(company.tap.gosellapi.R.id.status_text);
                TextView chargeText = layout.findViewById(company.tap.gosellapi.R.id.charge_id_txt);
                status_icon.setImageResource(icon);
//                status_icon.setVisibility(View.INVISIBLE);
                chargeText.setText(chargeID);
                statusText.setText((msg != null && msg.length() > 30) ? msg.substring(0, 29) :msg);
                popupWindow.showAtLocation(layout, Gravity.TOP, 0, 50);
                popupWindow.getContentView().startAnimation(AnimationUtils.loadAnimation(this, R.anim.popup_show));
                setupTimer(popupWindow);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setupTimer(PopupWindow popupWindow) {
        // Hide after some seconds
        final Handler handler = new Handler();
        final Runnable runnable = () -> {
            if (popupWindow.isShowing()) {
                popupWindow.dismiss();
            }
        };
        popupWindow.setOnDismissListener(() -> handler.removeCallbacks(runnable));
        handler.postDelayed(runnable, 4000);
    }


}
