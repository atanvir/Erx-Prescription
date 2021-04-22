package com.erxprescriptionuser.Activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;

import com.erxprescriptionuser.Fragment.HomeFragment;
import com.erxprescriptionuser.GoShellSdk.GoShellWebview;
import com.erxprescriptionuser.Model.CommonModel;
import com.erxprescriptionuser.R;
import com.erxprescriptionuser.Retrofit.ServicesConnection;
import com.erxprescriptionuser.Retrofit.ServicesInterface;
import com.erxprescriptionuser.SharePrefrences.SPreferenceKey;
import com.erxprescriptionuser.SharePrefrences.SharedPreferenceWriter;
import com.erxprescriptionuser.Utils.CommonUtils;
import com.erxprescriptionuser.Utils.MultiTextWatcher;
import com.erxprescriptionuser.Utils.ParamEnum;
import com.erxprescriptionuser.databinding.ActivityChooseAddressBinding;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChooseAddressActivity extends AppCompatActivity implements View.OnClickListener, MultiTextWatcher.TextWatcherWithInstance,GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks, OnCompleteListener<LocationSettingsResponse>, OnSuccessListener<Location>, OnFailureListener {

    private ActivityChooseAddressBinding binding;
    private String address;
    private Double latitue,longitute;
    private final int GO_SHELL=12;

    private GoogleApiClient googleApiClient=null;
    private LocationRequest locationRequest=null;
    private LocationCallback locationCallback = null;
    private FusedLocationProviderClient mFusedLocationClient = null;
    private final int LOCATION_REQ=5;
    private final int PERMISSION_DIALOG_REQ=6;
    private Double currentLat,currentLong;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=DataBindingUtil.setContentView(this,R.layout.activity_choose_address);
        address=SharedPreferenceWriter.getInstance(ChooseAddressActivity.this).getString(SPreferenceKey.ADDRESS);
        latitue= Double.valueOf(SharedPreferenceWriter.getInstance(ChooseAddressActivity.this).getString(SPreferenceKey.LATITUTE));
        longitute= Double.valueOf(SharedPreferenceWriter.getInstance(ChooseAddressActivity.this).getString(SPreferenceKey.LONGITUTE));
        binding.btnProcced.setOnClickListener(this);
        binding.edSelectAddress.setOnClickListener(this);
        new MultiTextWatcher().registerEditText(binding.edEnterAddress).registerEditText(binding.edSelectAddress).setCallback(this);
        getUserDetailsApi();
        binding.yvExisitingAddress.setOnClickListener(this);
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
            }
        }


    }

    private void getUserDetailsApi() {
        try {
            ServicesInterface anInterface = ServicesConnection.getInstance().createService(this);
            Call<CommonModel> call = anInterface.getUserDetails();
            ServicesConnection.getInstance().enqueueWithoutRetry(call, this, true, new Callback<CommonModel>() {
                @Override
                public void onResponse(Call<CommonModel> call, Response<CommonModel> response) {
                    if(response.isSuccessful())
                    {
                        startLocationFunctioning();
                        CommonModel serverResponse=response.body();
                        if(serverResponse.getStatus().equalsIgnoreCase(ParamEnum.SUCCESS.theValue()))
                        { binding.yvExisitingAddress.setText(serverResponse.getData().getAddress());
                        }else if(serverResponse.getStatus().equalsIgnoreCase("500"))
                        {
                            CommonUtils.showSnackBar(ChooseAddressActivity.this,serverResponse.getMessage());
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

    @Override
    protected void onResume() {
        super.onResume();
        CommonUtils.setToolbar(this,getString(R.string.choose_address));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.btnProcced: {refundNotAcceptedPop();} break;
            case R.id.edSelectAddress:
            Intent intent = new Intent(this, AddressPicker.class);
            intent.putExtra("latitue",latitue);
            intent.putExtra("longitute",longitute);
            startActivityForResult(intent, 101);
            break;

            case R.id.yvExisitingAddress:
            address=SharedPreferenceWriter.getInstance(ChooseAddressActivity.this).getString(SPreferenceKey.ADDRESS);
            latitue= Double.valueOf(SharedPreferenceWriter.getInstance(ChooseAddressActivity.this).getString(SPreferenceKey.LATITUTE));
            longitute= Double.valueOf(SharedPreferenceWriter.getInstance(ChooseAddressActivity.this).getString(SPreferenceKey.LONGITUTE));
            ConstraintLayout constraintLayout = binding.mainCl;
            ConstraintSet constraintSet = new ConstraintSet();
            constraintSet.clone(constraintLayout);
            constraintSet.connect(R.id.imageView3,ConstraintSet.TOP,R.id.textView28,ConstraintSet.TOP,0);
            constraintSet.applyTo(constraintLayout);
            break;


        }
    }

    private void refundNotAcceptedPop() {
        final Dialog dialog=new Dialog(this,R.style.Theme_AppCompat_Light_Dialog_Alert);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setContentView(R.layout.dailog_no_refund);
        Button btnContinue=dialog.findViewById(R.id.btnContinue);
        CheckBox checkBox=dialog.findViewById(R.id.checkBox);
        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkBox.isChecked()) {
                    dialog.dismiss();
                    chargeApi();
                }else
                {
                    Toast.makeText(ChooseAddressActivity.this, getString(R.string.mark_as_aggred_to_term), Toast.LENGTH_SHORT).show();
                }
            }
        });
        dialog.show();
    }

    private void chargeApi() {
        try {
            ServicesInterface anInterface = ServicesConnection.getInstance().createService(this);
            Call<CommonModel> call = anInterface.charge(getValue("prescriptionOfferId"),"Delivery");
            ServicesConnection.getInstance().enqueueWithoutRetry(call, this, true, new Callback<CommonModel>() {
                @Override
                public void onResponse(Call<CommonModel> call, Response<CommonModel> response) {
                    if(response.isSuccessful())
                    {
                        CommonModel serverResponse=response.body();
                        if(serverResponse.getMessage().equalsIgnoreCase(getString(R.string.payment_request_places)))
                        {
                            Intent intent= new Intent(ChooseAddressActivity.this, GoShellWebview.class);
                            intent.putExtra("data", serverResponse.getData());
                            intent.putExtra("prescriptionOfferId", getValue("prescriptionOfferId"));
                            intent.putExtra("deliveryType", "Delivery");
                            intent.putExtra(SPreferenceKey.LONGITUTE,""+longitute);
                            intent.putExtra(SPreferenceKey.LATITUTE, ""+latitue);
                            intent.putExtra(SPreferenceKey.ADDRESS, address);
                            startActivityForResult(intent, GO_SHELL);
//                            acceptPrescriptionRequestApi("124223423","ONLINE","INITIATED");

                        }else
                        {
                            CommonUtils.showSnackBar(ChooseAddressActivity.this,serverResponse.getMessage());
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
            Call<CommonModel> call = anInterface.acceptPrescriptionRequest(getValue("prescriptionOfferId"),
                    longitute,
                    longitute,
                    "Nawada New Delhi","Pick Up",
                    "1","02-Apr-2021","AED",transactionId,
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

    @Override
    public void beforeTextChanged(EditText editText, CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(EditText editText, CharSequence s, int start, int before, int count) {
        switch (editText.getId())
        {
            case R.id.edSelectAddress:
            break;

            case R.id.edEnterAddress:
            latitue=currentLat;
            longitute=currentLong;
            address=s.toString();
            ConstraintLayout constraintLayout = binding.mainCl;
            ConstraintSet constraintSet = new ConstraintSet();
            constraintSet.clone(constraintLayout);
            constraintSet.connect(R.id.imageView3,ConstraintSet.TOP,R.id.textView11,ConstraintSet.TOP,0);
            constraintSet.applyTo(constraintLayout);
            break;
        }
    }


    @Override
    public void afterTextChanged(EditText editText, Editable editable) {

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
                    binding.edSelectAddress.setText(address);
                    ConstraintLayout constraintLayout = binding.mainCl;
                    ConstraintSet constraintSet = new ConstraintSet();
                    constraintSet.clone(constraintLayout);
                    constraintSet.connect(R.id.imageView3,ConstraintSet.TOP,R.id.edSelectAddress,ConstraintSet.TOP,0);
                    constraintSet.applyTo(constraintLayout);

                }
                break;

            case PERMISSION_DIALOG_REQ:
                if(resultCode==RESULT_OK)
                {
                  loadCurrentLoc();
                }else if(resultCode==RESULT_CANCELED) {
                    CommonUtils.showSnackBar(this, getString(R.string.please_turn_gps));
                    setUpLocationSettingsTaskStuff();
                }
                break;

            case GO_SHELL:
            if(resultCode==RESULT_CANCELED)
            {
                CommonUtils.showSnackBar(this, data.getStringExtra("status"));
            }
            break;
        }
    }


    public String getValue(String key)
    {
        return getIntent().getStringExtra(key);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        setUpLocationSettingsTaskStuff();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

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

    @SuppressLint("MissingPermission")
    @Override
    public void onSuccess(Location location) {
        if(location!=null) locationCallBack(location);
        else mFusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
    }

    private void locationCallBack(Location location) {
        latitue = location.getLatitude();
        longitute =location.getLongitude();
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


}
