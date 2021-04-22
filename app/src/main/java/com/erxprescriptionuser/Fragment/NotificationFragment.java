package com.erxprescriptionuser.Fragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.erxprescriptionuser.Activity.LoginActivity;
import com.erxprescriptionuser.Activity.MainActivity;
import com.erxprescriptionuser.Adapter.NotificationAdapter;
import com.erxprescriptionuser.Model.CommonListModel;
import com.erxprescriptionuser.Model.CommonModel;
import com.erxprescriptionuser.R;
import com.erxprescriptionuser.Retrofit.ServicesConnection;
import com.erxprescriptionuser.Retrofit.ServicesInterface;
import com.erxprescriptionuser.SharePrefrences.SPreferenceKey;
import com.erxprescriptionuser.SharePrefrences.SPrefrenceValues;
import com.erxprescriptionuser.SharePrefrences.SharedPreferenceWriter;
import com.erxprescriptionuser.Utils.CommonUtils;
import com.erxprescriptionuser.Utils.ParamEnum;
import com.erxprescriptionuser.databinding.FragmentNotificationBinding;

import hari.bounceview.BounceView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationFragment extends Fragment implements View.OnClickListener {
   private FragmentNotificationBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       binding= DataBindingUtil.inflate(inflater,R.layout.fragment_notification,container,false);
       binding.btnClearAll.setOnClickListener(this);
       BounceView.addAnimTo(binding.btnClearAll);
       CommonUtils.showLoadingDialog(getActivity());
       notificationListApi();
       return binding.getRoot();
    }

    private void notificationListApi() {
        try {
            ServicesInterface anInterface = ServicesConnection.getInstance().createService(getActivity());
            Call<CommonListModel> call = anInterface.getNotificationList();
            ServicesConnection.getInstance().enqueueWithoutRetry(call, getActivity(), false, new Callback<CommonListModel>() {
                @Override
                public void onResponse(Call<CommonListModel> call, Response<CommonListModel> response) {
                    if(response.isSuccessful())
                    {
                        CommonUtils.dismissLoadingDialog();
                        CommonListModel serverResponse=response.body();
                        if(serverResponse.getStatus().equalsIgnoreCase(ParamEnum.SUCCESS.theValue()))
                        {
                            if(serverResponse.getData().size()>0) {
                                binding.lottieAnimationView.setVisibility(View.GONE);
                                binding.btnClearAll.setVisibility(View.VISIBLE);
                                binding.recycleView.setVisibility(View.VISIBLE);
                                binding.recycleView.setLayoutManager(new LinearLayoutManager(getActivity()));
                                binding.recycleView.setAdapter(new NotificationAdapter(getActivity(), serverResponse.getData()));
                            }else
                            {
                                binding.lottieAnimationView.setVisibility(View.VISIBLE);
                                binding.btnClearAll.setVisibility(View.GONE);
                                binding.recycleView.setVisibility(View.GONE);
                            }
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
            case R.id.btnClearAll:
            CommonUtils.showLoadingDialog(getActivity());
            clearNotificationApi();
            break;
        }
    }

    private void clearNotificationApi() {
        try {
            ServicesInterface anInterface = ServicesConnection.getInstance().createService(getActivity());
            Call<CommonModel> call = anInterface.clearNotification();
            ServicesConnection.getInstance().enqueueWithoutRetry(call, getActivity(), false, new Callback<CommonModel>() {
                @Override
                public void onResponse(Call<CommonModel> call, Response<CommonModel> response) {
                    if(response.isSuccessful())
                    {
                        CommonModel serverResponse=response.body();
                        if(serverResponse.getStatus().equalsIgnoreCase(ParamEnum.SUCCESS.theValue()))
                        {
                            CommonUtils.showSnackBar(getActivity(),serverResponse.getMessage());
                            notificationListApi();
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
}
