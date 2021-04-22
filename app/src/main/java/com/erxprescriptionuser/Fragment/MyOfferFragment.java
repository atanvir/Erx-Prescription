package com.erxprescriptionuser.Fragment;

import android.graphics.Canvas;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.erxprescriptionuser.Adapter.RequestOfferAdapter;
import com.erxprescriptionuser.Model.CommonListModel;
import com.erxprescriptionuser.Model.ResponseBean;
import com.erxprescriptionuser.R;
import com.erxprescriptionuser.Retrofit.ServicesConnection;
import com.erxprescriptionuser.Retrofit.ServicesInterface;
import com.erxprescriptionuser.Utils.CommonUtils;
import com.erxprescriptionuser.Utils.ParamEnum;
import com.erxprescriptionuser.Utils.SwitchFragment;
import com.erxprescriptionuser.databinding.AdapterRequestOfferBinding;
import com.erxprescriptionuser.databinding.FragmentMyOfferrBinding;


import java.util.List;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyOfferFragment extends Fragment {
    FragmentMyOfferrBinding binding;
    private List<ResponseBean> list;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding= DataBindingUtil.inflate(inflater, R.layout.fragment_my_offerr,container,false);
        prescriptionOfferListApi();
        return binding.getRoot();
    }

    private void prescriptionOfferListApi() {
        try {
            ServicesInterface anInterface = ServicesConnection.getInstance().createService(getActivity());
            Call<CommonListModel> call = anInterface.prescriptionOfferList();
            ServicesConnection.getInstance().enqueueWithoutRetry(call, getActivity(), true, new Callback<CommonListModel>() {
                @Override
                public void onResponse(Call<CommonListModel> call, Response<CommonListModel> response) {
                    if (response.isSuccessful()) {
                        CommonListModel serverResponse = response.body();
                        if (serverResponse.getStatus().equalsIgnoreCase(ParamEnum.SUCCESS.theValue())) {
                            if (serverResponse.getData().size() > 0) {
                                list = serverResponse.getData();
                                binding.recycleView.setLayoutManager(new LinearLayoutManager(getActivity()));
                                binding.recycleView.setAdapter(new RequestOfferAdapter(getActivity(), serverResponse.getData()));
                            } else {
                                binding.lottieAnimationView.setVisibility(View.VISIBLE);
                            }
                        } else if (serverResponse.getStatus().equalsIgnoreCase(ParamEnum.FAILURE.theValue())) {
                            CommonUtils.showSnackBar(getActivity(), serverResponse.getMessage());
                        }
                    }
                }

                @Override
                public void onFailure(Call<CommonListModel> call, Throwable t) {
                }
            });
        } catch (Exception e) {
            Toast.makeText(getActivity(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}
