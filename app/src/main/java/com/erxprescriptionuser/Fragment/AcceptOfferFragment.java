package com.erxprescriptionuser.Fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
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

import hari.bounceview.BounceView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AcceptOfferFragment extends Fragment implements View.OnClickListener {
    FragmentOfferAcceptBinding binding;
    final int GO_SHELL=11;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding=  DataBindingUtil.inflate(inflater, R.layout.fragment_offer_accept,container,false);
        binding.btnDelivery.setOnClickListener(this);
        binding.btnPickUp.setOnClickListener(this);
        BounceView.addAnimTo(binding.btnDelivery);
        BounceView.addAnimTo(binding.btnPickUp);
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
                    chargeApi();
                }else
                {
                    Toast.makeText(getActivity(), R.string.mark_as_agreed, Toast.LENGTH_SHORT).show();
                }
            }
        });
        dialog.show();
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

}
