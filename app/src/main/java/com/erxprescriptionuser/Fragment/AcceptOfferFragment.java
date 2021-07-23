package com.erxprescriptionuser.Fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.erxprescriptionuser.Activity.ChooseAddressActivity;
import com.erxprescriptionuser.Activity.MainActivity;
import com.erxprescriptionuser.GoShellSdk.GoShellHelper;
import com.erxprescriptionuser.GoShellSdk.GoShellWebview;
import com.erxprescriptionuser.Model.CommonModel;
import com.erxprescriptionuser.R;
import com.erxprescriptionuser.Retrofit.ServicesConnection;
import com.erxprescriptionuser.Retrofit.ServicesInterface;
import com.erxprescriptionuser.SharePrefrences.SPreferenceKey;
import com.erxprescriptionuser.SharePrefrences.SharedPreferenceWriter;
import com.erxprescriptionuser.Utils.CommonUtils;
import com.erxprescriptionuser.Utils.ParamEnum;
import com.erxprescriptionuser.Utils.SwitchFragment;
import com.erxprescriptionuser.databinding.FragmentOfferAcceptBinding;
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

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import hari.bounceview.BounceView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AcceptOfferFragment extends Fragment implements View.OnClickListener, OnFailureListener, GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks, OnCompleteListener<LocationSettingsResponse>, OnSuccessListener<Location>{
    FragmentOfferAcceptBinding binding;
    final int GO_SHELL=11;

    private GoogleApiClient googleApiClient=null;
    private LocationRequest locationRequest=null;
    private LocationCallback locationCallback = null;
    private FusedLocationProviderClient mFusedLocationClient = null;
    private final int LOCATION_REQ=5;
    private final int PERMISSION_DIALOG_REQ=6;
    private Double currentLat,currentLong;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding=  DataBindingUtil.inflate(inflater, R.layout.fragment_offer_accept,container,false);
        binding.btnDelivery.setOnClickListener(this);
        binding.btnPickUp.setOnClickListener(this);
        BounceView.addAnimTo(binding.btnDelivery);
        BounceView.addAnimTo(binding.btnPickUp);
        startLocationFunctioning();

       // binding.tvAmount.setText(R.string.tax);
        return binding.getRoot();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.btnDelivery:
            intent(ChooseAddressActivity.class);
            break;

            case R.id.btnPickUp:
            refundNotAcceptedPop();
            break;
        }
    }
    private void refundNotAcceptedPop() {
        final Dialog dialog=new Dialog(getActivity(),R.style.Theme_AppCompat_Light_Dialog_Alert);
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
                    if(getArguments().getString("insuranceType")!=null ){
                        if(getArguments().getString("insuranceType").equalsIgnoreCase("Full insurance coverage,")){
                            acceptPrescriptionRequestApi();
                        }else chargeApi();
                    }
                    else chargeApi();
                }else
                {
                    Toast.makeText(getActivity(), R.string.mark_as_agreed, Toast.LENGTH_SHORT).show();
                }
            }
        });
        dialog.show();
    }

    private void acceptPrescriptionRequestApi() {
        CommonUtils.dismissLoadingDialog();

        try {
            CommonUtils.showLoadingDialog(getActivity());
            ServicesInterface anInterface = ServicesConnection.getInstance().createService(getActivity());
            Call<CommonModel> call = anInterface.acceptPrescriptionRequest(getValue("prescriptionOfferId"),
                    currentLat,
                    currentLong,
                    getAddressFromLatLng(),"Pick Up",
                    "0",/*"02-Apr-2021"*/new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date()),"AED","123",
                    "Full insurance coverage","SUCCESS");
            ServicesConnection.getInstance().enqueueWithoutRetry(call, getActivity(), false, new Callback<CommonModel>() {
                @Override
                public void onResponse(Call<CommonModel> call, Response<CommonModel> response) {
                    CommonUtils.dismissLoadingDialog();
                    if(response.isSuccessful())
                    {
                        CommonUtils.dismissLoadingDialog();
                        CommonModel serverResponse=response.body();
                        if(serverResponse.getStatus().equalsIgnoreCase(ParamEnum.SUCCESS.theValue()))
                        {
                            Toast.makeText(getActivity(), serverResponse.getMessage(), Toast.LENGTH_SHORT).show();
                            getActivity().startActivity(new Intent(getActivity(),MainActivity.class));
                            getActivity().finish();

                        }else if(serverResponse.getStatus().equalsIgnoreCase("500") || serverResponse.getStatus().equalsIgnoreCase(ParamEnum.FAILURE.theValue()))
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

    private String getAddressFromLatLng() {
        Geocoder geocoder;
        List<Address> addresses = null;
        String address="";
        geocoder = new Geocoder(getActivity(),Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(currentLat, currentLong, 1);
            address=addresses.get(0).getAddressLine(0);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return address;
    }

    private void chargeApi() {
        try {
            ServicesInterface anInterface = ServicesConnection.getInstance().createService(getActivity());
            Call<CommonModel> call = anInterface.charge(getValue("prescriptionOfferId"),"Pick Up");
            ServicesConnection.getInstance().enqueueWithoutRetry(call, getActivity(), true, new Callback<CommonModel>() {
                @Override
                public void onResponse(Call<CommonModel> call, Response<CommonModel> response) {
                    if(response.isSuccessful())
                    {
                        CommonModel serverResponse=response.body();
                        if(serverResponse.getMessage().equalsIgnoreCase(getString(R.string.payment_request_places)))
                        {
                            Intent intent= new Intent(getActivity(), GoShellWebview.class);
                            intent.putExtra("data", serverResponse.getData());
                            intent.putExtra("prescriptionOfferId", getValue("prescriptionOfferId"));
                            intent.putExtra("deliveryType", "Pick Up");
                            intent.putExtra(SPreferenceKey.LONGITUTE,""+SharedPreferenceWriter.getInstance(getActivity()).getString(SPreferenceKey.LONGITUTE));
                            intent.putExtra(SPreferenceKey.LATITUTE, ""+SharedPreferenceWriter.getInstance(getActivity()).getString(SPreferenceKey.LATITUTE));
                            intent.putExtra(SPreferenceKey.ADDRESS, SharedPreferenceWriter.getInstance(getActivity()).getString(SPreferenceKey.ADDRESS));
                            startActivityForResult(intent, GO_SHELL);

                        }else
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



    private void intent(Class<? extends Object> className) {
        Intent intent = new Intent(getActivity(),className);
        intent.putExtra("prescriptionOfferId", getValue("prescriptionOfferId"));
        intent.putExtra("currency", getValue("currency"));
        intent.putExtra("amount", getValue("amount"));
        intent.putExtra("insuranceType",getValue("insuranceType"));
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public String getValue(String key)
    {
        return getArguments().getString(key);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode)
        {
            case GO_SHELL:
            if(requestCode==getActivity().RESULT_CANCELED)
            {
                CommonUtils.showSnackBar(getActivity(), data.getStringExtra("status"));
            }
            break;

        }
    }


    private void startLocationFunctioning() {
        if (!CommonUtils.isOnline(getActivity())) { Toast.makeText(getActivity(), getString(R.string.internet_not_available), Toast.LENGTH_SHORT).show();
        } else {
            if (CommonUtils.isGPlayServicesOK(getActivity())) {
                buildGoogleApiClient();
            }
        }
    }

    private void buildGoogleApiClient() {
        try {
            googleApiClient = new GoogleApiClient.Builder(getActivity())
                    .addApi(LocationServices.API)
                    .enableAutoManage(getActivity(), 0, this)
                    .addApi(com.google.android.gms.location.places.Places.GEO_DATA_API)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .build();
            googleApiClient.connect();

        }catch (Exception e){
            e.printStackTrace();
            setUpLocationSettingsTaskStuff();
        }
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
                    for(int i=0;i<grantResults.length;i++){ if(grantResults[i]== PackageManager.PERMISSION_DENIED){ permissionDenied=true;break; } }
                    if (permissionDenied) CommonUtils.showSnackBar(getActivity(), getString(R.string.needs_to_allow_to_use_this_functionality));
                    else startLocationFunctioning();
                    break;
            }
        }


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

            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQ);
                }
            } else {
                mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
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
        currentLat = location.getLatitude();
        currentLong =location.getLongitude();
    }

    @Override
    public void onFailure(@NonNull Exception e) {
        if (e instanceof ResolvableApiException) {
            try { ((ResolvableApiException) e).startResolutionForResult(getActivity(), PERMISSION_DIALOG_REQ); }
            catch (IntentSender.SendIntentException sendEx) { sendEx.printStackTrace(); }
        }
    }



    public  void setUpLocationSettingsTaskStuff() {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(createLocationRequest());
        builder.setAlwaysShow(true);
        SettingsClient client = LocationServices.getSettingsClient(getActivity());
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
