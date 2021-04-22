package com.erxprescriptionuser.Fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.erxprescriptionuser.Activity.ChooseAddressActivity;
import com.erxprescriptionuser.Activity.MainActivity;
import com.erxprescriptionuser.Model.CommonModel;
import com.erxprescriptionuser.R;
import com.erxprescriptionuser.Retrofit.ServicesConnection;
import com.erxprescriptionuser.Retrofit.ServicesInterface;
import com.erxprescriptionuser.Utils.CommonUtils;
import com.erxprescriptionuser.Utils.ParamEnum;
import com.erxprescriptionuser.Utils.SwitchFragment;
import com.erxprescriptionuser.databinding.FragmentRejectOfferBinding;

import hari.bounceview.BounceView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RejectOfferFragment extends Fragment implements RadioGroup.OnCheckedChangeListener, View.OnClickListener {
    FragmentRejectOfferBinding binding;
    private String reasonOption;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding=DataBindingUtil.inflate(inflater, R.layout.fragment_reject_offer, container, false);
        binding.radioGroup.setOnCheckedChangeListener(this);
        binding.btnSubmit.setOnClickListener(this);
        BounceView.addAnimTo(binding.btnSubmit);
        return binding.getRoot();
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (group.getCheckedRadioButtonId())
        {
            case R.id.rbReason1: reasonOption=binding.rbReason1.getText().toString(); break;
            case R.id.rbReason2: reasonOption=binding.rbReason2.getText().toString(); break;
            case R.id.rbReason3: reasonOption=binding.rbReason3.getText().toString(); break;
            case R.id.rbOther: reasonOption=binding.rbOther.getText().toString(); break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.btnSubmit:
            if(checkValidation())
            {
                prescriptionRequestRejectApi();
            }
            break;
        }
    }

    private void prescriptionRequestRejectApi() {
        try {
            ServicesInterface anInterface = ServicesConnection.getInstance().createService(getActivity());
            Call<CommonModel> call = anInterface.prescriptionRequestReject(getArguments().getString("prescriptionOfferId"),reasonOption,binding.editText.getText().toString().trim());
            ServicesConnection.getInstance().enqueueWithoutRetry(call, getActivity(), true, new Callback<CommonModel>() {
                @Override
                public void onResponse(Call<CommonModel> call, Response<CommonModel> response) {
                    if(response.isSuccessful())
                    {
                        CommonModel serverResponse=response.body();
                        if(serverResponse.getStatus().equalsIgnoreCase(ParamEnum.SUCCESS.theValue()))
                        {
                            SwitchFragment.changeFragment(getActivity().getSupportFragmentManager(),new MyOfferFragment(),false,true);

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

    private boolean checkValidation() {
        boolean ret=true;

        if(reasonOption==null)
        {
            ret=false;
            CommonUtils.showToast(binding.getRoot().getContext(),"Please Select Option");
        }else if(reasonOption.equalsIgnoreCase(binding.rbOther.getText().toString()) && binding.editText.getText().toString().trim().length()==0)
        {
            ret=false;
            CommonUtils.showToast(binding.getRoot().getContext(),"Please Specify the reason");
        }

        return ret;
    }
}
