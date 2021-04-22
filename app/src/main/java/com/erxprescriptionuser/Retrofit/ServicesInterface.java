package com.erxprescriptionuser.Retrofit;


import androidx.appcompat.app.AppCompatActivity;

import com.erxprescriptionuser.Model.CommonListModel;
import com.erxprescriptionuser.Model.CommonModel;
import com.erxprescriptionuser.Model.CommonDataStringModel;
import com.erxprescriptionuser.Model.PrescriptionModelList;
import com.erxprescriptionuser.Model.PrescriptionModelObject;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;

public interface ServicesInterface {

    @FormUrlEncoded
    @POST(AppConstants.CHECK_EMAIL_AND_PHONE)
    Call<CommonModel> checkEmailAndMobileAvailability(@Field("email") String email,
                                                      @Field("countryCode") String countryCode,
                                                      @Field("mobileNumber") String mobileNumber);

    @Multipart
    @POST(AppConstants.SIGN_UP)
    Call<CommonModel> userSignup(@Part MultipartBody.Part emiratesIdPicture,
                                 @Part MultipartBody.Part insuranceCardPicture,
                                 @PartMap Map<String,RequestBody> data);

    @FormUrlEncoded
    @POST(AppConstants.LOGIN)
    Call<CommonModel> userLogin(@Field("countryCode") String countryCode,
                                @Field("mobileNumber") String mobileNumber,
                                @Field("password") String password,
                                @Field("deviceType") String deviceType,
                                @Field("deviceToken") String deviceToken);

    @FormUrlEncoded
    @POST(AppConstants.CHECK_PHONE)
    Call<CommonDataStringModel> checkMobileAvailability(@Field("countryCode") String countryCode,
                                                        @Field("mobileNumber") String mobileNumber);

    @FormUrlEncoded
    @POST(AppConstants.RESET_PASSWORD)
    Call<CommonModel> forgotPassword(@Field("userId") String userId,
                                     @Field("password") String password);

    @GET(AppConstants.USER_DETAILS)
    Call<CommonModel> getUserDetails();

    @FormUrlEncoded
    @POST(AppConstants.CHANGE_MOBILE_NUMBER)
    Call<CommonModel> changeMobileNumber(@Field("countryCode") String countryCode,
                                         @Field("mobileNumber") String mobileNumber);

    @Multipart
    @POST(AppConstants.UPDATE_DETAILS)
    Call<CommonModel> userUpdateDetails(@Part MultipartBody.Part profilePic,
                                        @PartMap Map<String,RequestBody> allData);

    @GET(AppConstants.LOGOUT)
    Call<CommonModel> userLogout();

    @GET(AppConstants.NOTIFICATION_LIST)
    Call<CommonListModel> getNotificationList();

    @GET(AppConstants.CLEAR_NOTIFICATION)
    Call<CommonModel> clearNotification();

    @GET(AppConstants.NOTIFICATION_COUNT)
    Call<CommonDataStringModel> getNotificationCount();

    @FormUrlEncoded
    @POST(AppConstants.PRECSCCRIPTION)
    Call<PrescriptionModelList> prescriptionList(@Field("prescriptionType") String prescriptionType);

    @FormUrlEncoded
    @POST(AppConstants.NOTIFICATION_STATUS)
    Call<CommonDataStringModel> changeNotificationStatus(@Field("notificationStatus") boolean notificationStatus);

    @Multipart
    @POST(AppConstants.PRECSCCRIPTION_UPLOAD)
    Call<CommonModel> prescriptionRequest(@Part MultipartBody.Part precriptionImage,
                                          @PartMap Map<String, RequestBody> userData);

    @POST(AppConstants.PRECSCCRIPTION_OFFER)
    Call<CommonListModel> prescriptionOfferList();

    @FormUrlEncoded
    @POST(AppConstants.ACCEPT_PRESCRIPTION)
    Call<CommonModel> acceptPrescriptionRequest(@Field("prescriptionOfferId") String prescriptionOfferId,
                                                @Field("latitude") Double latitude,
                                                @Field("longitude") Double longitude,
                                                @Field("address") String address,
                                                @Field("deliveryType") String deliveryType,
                                                @Field("amount") String amount,
                                                @Field("date") String date,
                                                @Field("currency") String currency,
                                                @Field("paymentId") String paymentId,
                                                @Field("paymentType") String paymentType,
                                                @Field("status") String status);


    @FormUrlEncoded
    @POST(AppConstants.REJECT_PRESCRIPTION)
    Call<CommonModel> prescriptionRequestReject(@Field("prescriptionOfferId") String prescriptionOfferId,
                                                @Field("cancelReason") String reasonOption,
                                                @Field("cancelMessage") String trim);

    @FormUrlEncoded
    @POST(AppConstants.PRECSCCRIPTION_DETAILS)
    Call<PrescriptionModelObject> prescriptionDetail(@Field("prescriptionId") String prescriptionId);

    @FormUrlEncoded
    @POST(AppConstants.CHAT_HISTORY)
    Call<CommonListModel> chatHistory(@Field("roomId") String roomId);

    @FormUrlEncoded
    @POST(AppConstants.NEAR_BY_PROVIDERS)
    Call<CommonListModel> nearByProviderList(@Field("latitude") Double latitude,@Field("longitude") Double longitude);

    @FormUrlEncoded
    @POST(AppConstants.CHARGE)
    Call<CommonModel> charge(@Field("prescriptionOfferId") String prescriptionOfferId , @Field("deliveryType") String deliveryType);

    @FormUrlEncoded
    @POST(AppConstants.PAYMENT)
    Call<CommonModel> payment(@Field("prescriptionOfferId") String prescriptionOfferId,
                              @Field("providerAmount") String providerAmount,
                              @Field("companyAmount") String companyAmount,
                              @Field("adminAmount") String adminAmount,
                              @Field("amount") String amount,
                              @Field("date") String date,
                              @Field("currency") String currency,
                              @Field("paymentId") String paymentId,
                              @Field("paymentType") String paymentType,
                              @Field("status") String status,
                              @Field("deliveryToAdminAmount") String deliveryToAdminAmount,
                              @Field("chargeId") String chargeId,
                              @Field("deliveryType") String deliveryType);

    @GET(AppConstants.PAYMENT_LIST)
    Call<CommonListModel> getPaymentList();

    @FormUrlEncoded
    @POST(AppConstants.CHECK_EMAIL)
    Call<CommonModel> checkEmailForForgotPassword(@Field("email") String email);

    @GET(AppConstants.STATIC_CONTENT)
    Call<CommonModel> getStaticContent();

    @FormUrlEncoded
    @POST(AppConstants.STATIC_CONTENT_BY_TYPE)
    Call<CommonModel> getStaticContentByType(@Field("type") String type, @Field("userType") String userType);
}







