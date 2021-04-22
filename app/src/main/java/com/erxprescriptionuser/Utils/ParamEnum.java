package com.erxprescriptionuser.Utils;

public enum ParamEnum {

    LATITUDE("Latitude"),
    LONGITUDE("Longitude"),
    COUNTRY_CODE("country_code"),
    SUCCESS("200"),
    FAILURE("409"),
    BASE_URL("https://e-rx.cc:2021/api/v1/user/"),
    ANDROID("Android"),
    CAME_FROM("cameFrom"),
    ONGOING("Ongoing"),
    NOTI_PRESCRIPTION_OFFER("prescriptionOffer"),
    SOCKET_EVENT_ROOM_JOIN("room join"),
    SOCKET_EVENT_MESSAGE("message"),
    PAST("Past"),
    ABOUT_US("aboutUs"),
    CONTACT_US("contactUs"),
    ABOUT_US_AR("aboutUsAr"),
    TERMS("termsAndCondition"),
    TERMS_AR("termsAndConditionAr"),
    SUPPORT("http://3.22.227.41/support.html"),
    PRIVACY("privacyPolicy"),
    PRIVACY_AR("privacyPolicyAr"), CONTACT_US_AR("");
    private final String value;

    ParamEnum(String value) {
        this.value=value;
    }
    ParamEnum(){
        this.value=null;
    }
    public String theValue() {
        return this.value;
    }

}
