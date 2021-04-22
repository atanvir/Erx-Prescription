package com.erxprescriptionuser.SharePrefrences;

import android.content.Context;

import com.erxprescriptionuser.Model.CommonModel;
import com.erxprescriptionuser.Utils.CommonUtils;

public class SPrefrenceValues {

    public static void saveUserData(Context context, CommonModel model)
    {
        SharedPreferenceWriter.getInstance(context).writeBooleanValue(SPreferenceKey.IS_LOGIN,true);
        SharedPreferenceWriter.getInstance(context).writeStringValue(SPreferenceKey.ID,model.getData().getId());
        SharedPreferenceWriter.getInstance(context).writeStringValue(SPreferenceKey.PROFILE_PIC,model.getData().getProfilePic());
        SharedPreferenceWriter.getInstance(context).writeStringValue(SPreferenceKey.USER_TYPE,model.getData().getUserType());
        SharedPreferenceWriter.getInstance(context).writeStringValue(SPreferenceKey.AUTH_TOKEN,model.getData().getJwtToken());
        SharedPreferenceWriter.getInstance(context).writeStringValue(SPreferenceKey.NAME,model.getData().getFullName());
        SharedPreferenceWriter.getInstance(context).writeStringValue(SPreferenceKey.EMAIL,model.getData().getEmail());
        SharedPreferenceWriter.getInstance(context).writeStringValue(SPreferenceKey.COUNTRY_CODE,model.getData().getCountryCode());
        SharedPreferenceWriter.getInstance(context).writeStringValue(SPreferenceKey.MOBILE,model.getData().getMobileNumber());
        SharedPreferenceWriter.getInstance(context).writeStringValue(SPreferenceKey.ADDRESS,model.getData().getAddress());
        SharedPreferenceWriter.getInstance(context).writeStringValue(SPreferenceKey.LATITUTE,""+model.getData().getLatitude());
        SharedPreferenceWriter.getInstance(context).writeStringValue(SPreferenceKey.LONGITUTE,""+model.getData().getLongitude());
        SharedPreferenceWriter.getInstance(context).writeBooleanValue(SPreferenceKey.NOTIFICATION_STATUS,model.getData().getNotificationStatus());
    }

    public static void removeUserData(Context context)
    {
        String countryCode,phoneNumber,password,langcode;
        boolean rememberMe;
        rememberMe=SharedPreferenceWriter.getInstance(context).getBoolean(SPreferenceKey.REMEMBER_ME);
        countryCode=SharedPreferenceWriter.getInstance(context).getString(SPreferenceKey.COUNTRY_CODE);
        phoneNumber=SharedPreferenceWriter.getInstance(context).getString(SPreferenceKey.MOBILE);
        password=SharedPreferenceWriter.getInstance(context).getString(SPreferenceKey.PASSWORD);
        langcode=SharedPreferenceWriter.getInstance(context).getString(SPreferenceKey.LANGUAGE_CODE);
        SharedPreferenceWriter.getInstance(context).clearPreferenceValues();
        if(rememberMe)
        {
            SharedPreferenceWriter.getInstance(context).writeBooleanValue(SPreferenceKey.REMEMBER_ME,rememberMe);
            SharedPreferenceWriter.getInstance(context).writeStringValue(SPreferenceKey.COUNTRY_CODE,countryCode);
            SharedPreferenceWriter.getInstance(context).writeStringValue(SPreferenceKey.MOBILE,phoneNumber);
            SharedPreferenceWriter.getInstance(context).writeStringValue(SPreferenceKey.PASSWORD,password);
        }
        SharedPreferenceWriter.getInstance(context).writeBooleanValue(SPreferenceKey.FIRST_TIME,true);
        SharedPreferenceWriter.getInstance(context).writeBooleanValue(SPreferenceKey.TAKE_A_TOUR,true);
        SharedPreferenceWriter.getInstance(context).writeStringValue(SPreferenceKey.LANGUAGE_CODE,langcode);
        CommonUtils.getDeviceToken(context);
    }




}
