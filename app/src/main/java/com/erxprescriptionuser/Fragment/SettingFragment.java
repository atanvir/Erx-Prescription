package com.erxprescriptionuser.Fragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.erxprescriptionuser.Activity.MainActivity;
import com.erxprescriptionuser.Activity.WebviewActivity;
import com.erxprescriptionuser.Model.CommonDataStringModel;
import com.erxprescriptionuser.R;
import com.erxprescriptionuser.Retrofit.ServicesConnection;
import com.erxprescriptionuser.Retrofit.ServicesInterface;
import com.erxprescriptionuser.SharePrefrences.SPreferenceKey;
import com.erxprescriptionuser.SharePrefrences.SharedPreferenceWriter;
import com.erxprescriptionuser.Utils.CommonUtils;
import com.erxprescriptionuser.Utils.ParamEnum;
import com.erxprescriptionuser.Utils.SwitchFragment;
import com.erxprescriptionuser.databinding.FragmentSettingBinding;

import hari.bounceview.BounceView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SettingFragment extends Fragment implements View.OnClickListener, CompoundButton.OnCheckedChangeListener, RadioGroup.OnCheckedChangeListener {

    FragmentSettingBinding binding;
    private boolean isClickLang=false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       binding= DataBindingUtil.inflate(inflater,R.layout.fragment_setting,container,false);
        binding.tvAboutUs.setOnClickListener(this);
        binding.tvContactUs.setOnClickListener(this);
        binding.tvSupport.setOnClickListener(this);
        binding.tvPrivacy.setOnClickListener(this);
        binding.tvTerm.setOnClickListener(this);
        binding.notiBtn.setChecked(SharedPreferenceWriter.getInstance(getActivity()).getBoolean(SPreferenceKey.NOTIFICATION_STATUS));
        binding.notiBtn.setOnCheckedChangeListener(this);
        binding.radioGroup2.setOnCheckedChangeListener(this);
        binding.rbArabic.setOnClickListener(this);
        binding.rbEnglish.setOnClickListener(this);
        BounceView.addAnimTo(binding.tvAboutUs);
        BounceView.addAnimTo(binding.tvContactUs);
        BounceView.addAnimTo(binding.tvSupport);
        BounceView.addAnimTo(binding.tvPrivacy);
        BounceView.addAnimTo(binding.tvTerm);


       String langCode= SharedPreferenceWriter.getInstance(getActivity()).getString(SPreferenceKey.LANGUAGE_CODE);
       if(langCode.equalsIgnoreCase("en"))
       {
           binding.rbEnglish.setChecked(true);
           isClickLang=true;
       }else if(langCode.equalsIgnoreCase("ar"))
       {
           binding.rbArabic.setChecked(true);
           isClickLang=true;
       }
        return binding.getRoot();
    }


    @Override
    public void onClick(View v) {
        String langcode=SharedPreferenceWriter.getInstance(getActivity()).getString(SPreferenceKey.LANGUAGE_CODE);
        switch (v.getId())
        {

            case R.id.tvAboutUs:
            intent(WebviewActivity.class,getString(R.string.about_us),langcode.equalsIgnoreCase("en")?ParamEnum.ABOUT_US.theValue():ParamEnum.ABOUT_US_AR.theValue());
            break;

            case R.id.tvContactUs:
            intent(WebviewActivity.class,getString(R.string.contact_us),langcode.equalsIgnoreCase("en")?ParamEnum.CONTACT_US.theValue():ParamEnum.CONTACT_US.theValue());
            break;

            case R.id.tvSupport:
            intent(WebviewActivity.class,getString(R.string.support),ParamEnum.SUPPORT.theValue());
            break;

            case R.id.tvPrivacy:
            intent(WebviewActivity.class,getString(R.string.privacy_policy),langcode.equalsIgnoreCase("en")?ParamEnum.PRIVACY.theValue():ParamEnum.PRIVACY_AR.theValue());
            break;
            
            case R.id.tvTerm:
            intent(WebviewActivity.class,getString(R.string.terms_conditions),langcode.equalsIgnoreCase("en")?ParamEnum.TERMS.theValue():ParamEnum.TERMS_AR.theValue());
            break;

            case R.id.rbArabic:
            isClickLang=true;
            break;

            case R.id.rbEnglish:
            isClickLang=true;
            break;
        }
        
    }

    private void intent(Class<? extends Object> className, String title,String url) {
        Intent intent= new Intent(getActivity(),className);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("Title",title);
        intent.putExtra("url",url);
        startActivity(intent);

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        changeNotificationStatusApi(isChecked);
    }

    private void changeNotificationStatusApi(final boolean isChecked) {
        try {
            ServicesInterface anInterface = ServicesConnection.getInstance().createService(getActivity());
            Call<CommonDataStringModel> call = anInterface.changeNotificationStatus(isChecked);
            ServicesConnection.getInstance().enqueueWithoutRetry(call, getActivity(), false, new Callback<CommonDataStringModel>() {
                @Override
                public void onResponse(Call<CommonDataStringModel> call, Response<CommonDataStringModel> response) {
                    if(response.isSuccessful())
                    {
                        CommonDataStringModel serverResponse=response.body();
                        if(serverResponse.getStatus().equalsIgnoreCase(ParamEnum.SUCCESS.theValue()))
                        {
                            SharedPreferenceWriter.getInstance(getActivity()).writeBooleanValue(SPreferenceKey.NOTIFICATION_STATUS,isChecked);
                        }else if(serverResponse.getStatus().equalsIgnoreCase(ParamEnum.FAILURE.theValue()))
                        {
                            CommonUtils.showSnackBar(getActivity(),serverResponse.getMessage());
                        }
                    }
                }

                @Override
                public void onFailure(Call<CommonDataStringModel> call, Throwable t) {
                    Log.e("failure",t.getMessage());
                }
            });
        } catch (Exception e) {
            Toast.makeText(getActivity(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {

        if (isClickLang) {
            switch (group.getCheckedRadioButtonId()) {
                case R.id.rbEnglish:
                    SharedPreferenceWriter.getInstance(getActivity()).writeStringValue(SPreferenceKey.LANGUAGE_CODE, "en");
                    getActivity().finish();
                    startActivity(new Intent(getActivity(),MainActivity.class));
                    break;

                case R.id.rbArabic:
                    SharedPreferenceWriter.getInstance(getActivity()).writeStringValue(SPreferenceKey.LANGUAGE_CODE, "ar");
                    getActivity().finish();
                    startActivity(new Intent(getActivity(),MainActivity.class));
                    break;
            }
        }
    }
}
