
package com.erxprescriptionuser.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.erxprescriptionuser.Model.PrescriptionModelList;
import com.erxprescriptionuser.Model.PrescriptionModelObject;
import com.erxprescriptionuser.R;
import com.erxprescriptionuser.Retrofit.ServicesConnection;
import com.erxprescriptionuser.Retrofit.ServicesInterface;
import com.erxprescriptionuser.SharePrefrences.SPreferenceKey;
import com.erxprescriptionuser.SharePrefrences.SharedPreferenceWriter;
import com.erxprescriptionuser.Utils.CommonUtils;
import com.erxprescriptionuser.Utils.ImageGlider;
import com.erxprescriptionuser.Utils.ParamEnum;
import com.erxprescriptionuser.databinding.ActivityBookingDetailBinding;
import com.erxprescriptionuser.databinding.DailogRateOurServiceBinding;

import java.util.List;

import hari.bounceview.BounceView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookingDetailActivity extends AppCompatActivity implements View.OnClickListener {

    ActivityBookingDetailBinding binding;
    PrescriptionModelList.Data data;
    private String providerId,deliveryBoyId;
    private String providerRoomId,deliveryRoomId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=DataBindingUtil.setContentView(this,R.layout.activity_booking_detail);
        binding.tvDeliveryBoy.setOnClickListener(this);
        binding.tvProvider.setOnClickListener(this);
        data=getIntent().getParcelableExtra("data");
        binding.ivPrescription.setOnClickListener(this);

        BounceView.addAnimTo( binding.tvDeliveryBoy);
        BounceView.addAnimTo( binding.tvProvider);
//        rateOurServicePopup();
        prescriptionDetailApi();
    }

    private void prescriptionDetailApi() {
        try {
            ServicesInterface anInterface = ServicesConnection.getInstance().createService(this);
            Call<PrescriptionModelObject> call = anInterface.prescriptionDetail(data.getId());
            ServicesConnection.getInstance().enqueueWithoutRetry(call, BookingDetailActivity.this, false, new Callback<PrescriptionModelObject>() {
                @Override
                public void onResponse(Call<PrescriptionModelObject> call, Response<PrescriptionModelObject> response) {
                    if(response.isSuccessful())
                    {
                        PrescriptionModelObject serverResponse = response.body();
                        if (serverResponse.getStatus().equalsIgnoreCase(ParamEnum.SUCCESS.theValue())) {
                            providerRoomId=serverResponse.getData().getProviderRoomId();
                            providerId=serverResponse.getData().getProviderId().getId();
                            deliveryRoomId=serverResponse.getData().getDeliveryRoomId();
                            if(serverResponse.getData().getDeliveryId()!=null) {
                                deliveryBoyId = serverResponse.getData().getDeliveryId().getId();
                                binding.linearLayout.setVisibility(View.VISIBLE);
                            }else
                            {
                                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
                                params.weight = 2;
                                binding.tvDeliveryBoy.setVisibility(View.GONE);
                                binding.tvProvider.setLayoutParams(params);
//                                binding.tvProvider.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                                binding.viewProvider.setVisibility(View.GONE);
                                binding.linearLayout.setVisibility(View.VISIBLE);
                            }
                            setUpDate(serverResponse.getData());
                        } else if (serverResponse.getStatus().equalsIgnoreCase(ParamEnum.FAILURE.theValue())) {
                            CommonUtils.showSnackBar(BookingDetailActivity.this, serverResponse.getMessage());
                        }
                    }
                }

                @Override
                public void onFailure(Call<PrescriptionModelObject> call, Throwable t) {
                    Log.e("failure",t.getMessage());
                }
            });
        } catch (Exception e) {
            Toast.makeText(BookingDetailActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void setUpDate(PrescriptionModelObject.Data data) {
        ImageGlider.setNormalImage(this,binding.ciProvider,binding.progressBar,data.getProviderId().getProfilePic());
        binding.tvName.setText(data.getProviderId().getFullName());
        binding.tvMobileNo.setText(data.getProviderId().getCountryCode()+getEncriptedMobileNumber(data.getProviderId().getMobileNumber()));
        binding.tvRating.setText(data.getProviderId().getAvgRating());
        ImageGlider.setRoundImage(this,binding.ivPrescription,binding.pbPrescription,data.getPrescriptionImage());
        binding.tvDetails.setText(getString(R.string.booking_date)+" "+CommonUtils.getDate(data.getCreatedAt())+getString(R.string.delivery_type)+" "+data.getDeliveryType()+getString(R.string.address)+" "+data.getAddress());
        binding.button.setText(data.getPrescriptionStatus());
    }

    private String getEncriptedMobileNumber(String pNo) {
        char mobileNumber[]=pNo.toCharArray();
        String number="";
        for(int i=0;i<mobileNumber.length;i++)
        {
            if(mobileNumber.length-1==i)
            {
                number+=mobileNumber[i];
            }else if(mobileNumber.length-2==i)
            {
                number+=mobileNumber[i];
            }else if(mobileNumber.length-3==i)
            {
                number+=mobileNumber[i];
            }else if(mobileNumber.length-4==i)
            {
                number+=mobileNumber[i];
            }

            else
            {
                number+="*";
            }


        }

        return number;
    }

    private void rateOurServicePopup() {
        final Dialog dialog=new Dialog(this,android.R.style.Theme_Black);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        DailogRateOurServiceBinding binding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.dailog_rate_our_service, null, false);
        dialog.setContentView(binding.getRoot());
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        binding.btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        CommonUtils.setToolbar(this,getString(R.string.booking_detail));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.tvDeliveryBoy:
            intent(ChatActivity.class,getString(R.string.delivery_boy));
            break;

            case R.id.tvProvider:
            intent(ChatActivity.class,getString(R.string.provider));
            break;

            case R.id.ivPrescription:
            intent(ZoomImageActivity.class,"");
            break;
        }
    }

    private void intent(Class<? extends Object> className, String type) {
        Intent intent = new Intent(this,className);
        if(type.equalsIgnoreCase(getString(R.string.delivery_boy)))
        {
            intent.putExtra("roomId", deliveryRoomId);
            intent.putExtra("receiverId", deliveryBoyId);
        }else if(type.equalsIgnoreCase(getString(R.string.provider)))
        {
            intent.putExtra("roomId", providerRoomId);
            intent.putExtra("receiverId", providerId);
        }
        intent.putExtra("title", type);
        intent.putExtra("image", data.getPrescriptionImage());
        if(data.getDeliveryId()!=null) {
            intent.putExtra("token", data.getDeliveryId().getDeviceToken());
        }
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
