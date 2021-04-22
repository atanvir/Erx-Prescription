package com.erxprescriptionuser.Retrofit;

import java.io.Serializable;

public interface AppConstants extends Serializable {
    String CHECK_EMAIL_AND_PHONE = "checkEmailAndMobileAvailability";
    String SIGN_UP = "userSignup";
    String LOGIN = "userLogin";
    String CHECK_PHONE = "checkMobileForForgotPassword";
    String RESET_PASSWORD = "forgotPassword";
    String USER_DETAILS = "getUserDetails";
    String CHANGE_MOBILE_NUMBER ="changeMobileNumber" ;
    String UPDATE_DETAILS = "userUpdateDetails";
    String LOGOUT = "userLogout";
    String NOTIFICATION_LIST = "getNotificationList";
    String CLEAR_NOTIFICATION = "clearNotification";
    String NOTIFICATION_COUNT = "getNotificationCount";
    String PRECSCCRIPTION = "prescriptionList";
    String NOTIFICATION_STATUS = "changeNotificationStatus";
    String PRECSCCRIPTION_UPLOAD = "prescriptionRequest";
    String PRECSCCRIPTION_OFFER = "prescriptionOfferList";
    String ACCEPT_PRESCRIPTION = "acceptPrescriptionRequest";
    String REJECT_PRESCRIPTION = "prescriptionRequestReject";
    String PRECSCCRIPTION_DETAILS = "prescriptionDetail";
    String CHAT_HISTORY = "getChatHistory";
    String NEAR_BY_PROVIDERS ="nearByProviderList" ;
    String CHARGE ="chargeApi" ;
    String PAYMENT = "paymentApi";
    String PAYMENT_LIST ="getPaymentList" ;
    String CHECK_EMAIL = "checkEmailForForgotPassword";
    String STATIC_CONTENT = "getStaticContent";
    String STATIC_CONTENT_BY_TYPE ="getStaticContentByType" ;
}
