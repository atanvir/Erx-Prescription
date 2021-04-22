package com.erxprescriptionuser.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.erxprescriptionuser.Activity.AddCardActivity;
import com.erxprescriptionuser.Adapter.PastAdapter;
import com.erxprescriptionuser.Adapter.PaymentAdapter;
import com.erxprescriptionuser.Model.CommonListModel;
import com.erxprescriptionuser.Model.PrescriptionModelList;
import com.erxprescriptionuser.R;
import com.erxprescriptionuser.Retrofit.ServicesConnection;
import com.erxprescriptionuser.Retrofit.ServicesInterface;
import com.erxprescriptionuser.Utils.CommonUtils;
import com.erxprescriptionuser.Utils.ParamEnum;
import com.erxprescriptionuser.databinding.FragmentPaymentBinding;

import java.util.Collections;
import java.util.List;

import hari.bounceview.BounceView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaymentFragment extends Fragment implements View.OnClickListener {
    FragmentPaymentBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding=DataBindingUtil.inflate(inflater, R.layout.fragment_payment,container,false);
        binding.btnAddCard.setOnClickListener(this);
        BounceView.addAnimTo(binding.btnAddCard);
        paymentListApi();
        return binding.getRoot();
    }

    private void paymentListApi() {

        try {
            ServicesInterface anInterface = ServicesConnection.getInstance().createService(getActivity());
            Call<CommonListModel> call = anInterface.getPaymentList();
            ServicesConnection.getInstance().enqueueWithoutRetry(call, getActivity(), true, new Callback<CommonListModel>() {
                @Override
                public void onResponse(Call<CommonListModel> call, Response<CommonListModel> response) {
                    if(response.isSuccessful())
                    {
                        CommonListModel serverResponse=response.body();
                        if(serverResponse.getStatus().equalsIgnoreCase(ParamEnum.SUCCESS.theValue()))
                        {
                            binding.recycleView.setLayoutManager(new LinearLayoutManager(getActivity()));
                            binding.recycleView.setAdapter(new PaymentAdapter(getActivity(),serverResponse.getData()));


                        }else if(serverResponse.getStatus().equalsIgnoreCase(ParamEnum.FAILURE.theValue()))
                        {
                            CommonUtils.showSnackBar(getActivity(),serverResponse.getMessage());
                        }

                    }
                }

                @Override
                public void onFailure(Call<CommonListModel> call, Throwable t) {
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
            case R.id.btnAddCard:
            CommonUtils.startActivity(getActivity(), AddCardActivity.class);
            break;
        }
    }
}
