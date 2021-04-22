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

import com.erxprescriptionuser.Adapter.OnGoingAdapter;
import com.erxprescriptionuser.Model.PrescriptionModelList;
import com.erxprescriptionuser.R;
import com.erxprescriptionuser.Retrofit.ServicesConnection;
import com.erxprescriptionuser.Retrofit.ServicesInterface;
import com.erxprescriptionuser.Utils.CommonUtils;
import com.erxprescriptionuser.Utils.ParamEnum;
import com.erxprescriptionuser.databinding.FragmentOnGoingBinding;

import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OnGoingFragment extends Fragment {

    private FragmentOnGoingBinding binding;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       binding= DataBindingUtil.inflate(inflater, R.layout.fragment_on_going,container,false);
       return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        prescriptionListApi(ParamEnum.ONGOING.theValue());
    }

    private void prescriptionListApi(String title) {
        try {
            ServicesInterface anInterface = ServicesConnection.getInstance().createService(getActivity());
            Call<PrescriptionModelList> call = anInterface.prescriptionList(title);
            ServicesConnection.getInstance().enqueueWithoutRetry(call, getActivity(), true, new Callback<PrescriptionModelList>() {
                @Override
                public void onResponse(Call<PrescriptionModelList> call, Response<PrescriptionModelList> response) {
                    if(response.isSuccessful())
                    {
                            PrescriptionModelList serverResponse = response.body();
                            if (serverResponse.getStatus().equalsIgnoreCase(ParamEnum.SUCCESS.theValue())) {
                                if (serverResponse.getData().size() > 0) {
                                    binding.lottieAnimationView.setVisibility(View.GONE);
                                    binding.recycleView.setLayoutManager(new LinearLayoutManager(getActivity()));
                                    List<PrescriptionModelList.Data> list=serverResponse.getData();
                                    Collections.reverse(list);
                                    binding.recycleView.setAdapter(new OnGoingAdapter(getActivity(), list));
                                } else {
                                    binding.lottieAnimationView.setVisibility(View.VISIBLE);
                                }
                            } else if (serverResponse.getStatus().equalsIgnoreCase(ParamEnum.FAILURE.theValue())) {
                                CommonUtils.showSnackBar(getActivity(), serverResponse.getMessage());
                            }
                    }
                }

                @Override
                public void onFailure(Call<PrescriptionModelList> call, Throwable t) {
                    Log.e("failure",t.getMessage());

                }
            });
        } catch (Exception e) {
            Toast.makeText(getActivity(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}
