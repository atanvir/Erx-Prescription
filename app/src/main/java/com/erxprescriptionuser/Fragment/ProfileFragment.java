package com.erxprescriptionuser.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.erxprescriptionuser.Adapter.PagerAdapter;
import com.erxprescriptionuser.R;
import com.erxprescriptionuser.databinding.FragmentProfileBinding;

public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding= DataBindingUtil.inflate(inflater, R.layout.fragment_profile,container,false);
        PagerAdapter pagerAdapter=new PagerAdapter(getActivity(),"Profile",getActivity().getSupportFragmentManager());
        binding.vpMain.setAdapter(pagerAdapter);
        binding.tabLayout.setupWithViewPager(binding.vpMain);
        return binding.getRoot();
    }
}
