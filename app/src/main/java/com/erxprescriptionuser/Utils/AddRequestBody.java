package com.erxprescriptionuser.Utils;



import com.erxprescriptionuser.Model.SignupModel;

import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;

public class AddRequestBody<T> {

    private MediaType mediaType = MediaType.parse("text/plain");

    private Map<String, RequestBody> requestBodyMap = new HashMap<>();

    public AddRequestBody(T data)
    {
        if(data instanceof SignupModel)
        {
            requestBodyMap.put("fullName", RequestBody.create(mediaType, ((SignupModel) data).getFullName()));
            requestBodyMap.put("countryCode", RequestBody.create(mediaType, ((SignupModel) data).getCountryCode()));
            requestBodyMap.put("mobileNumber", RequestBody.create(mediaType, ((SignupModel) data).getMobileNumber()));
            requestBodyMap.put("email", RequestBody.create(mediaType, ((SignupModel) data).getEmail()));
            requestBodyMap.put("emiratesId", RequestBody.create(mediaType, ((SignupModel) data).getEmiratesId()));
            requestBodyMap.put("dob", RequestBody.create(mediaType, ((SignupModel) data).getDob()));
            requestBodyMap.put("gender", RequestBody.create(mediaType, ((SignupModel) data).getGender()));
            requestBodyMap.put("address", RequestBody.create(mediaType, ((SignupModel) data).getAddress()));
            requestBodyMap.put("latitude", RequestBody.create(mediaType,""+ ((SignupModel) data).getLatitude()));
            requestBodyMap.put("longitude", RequestBody.create(mediaType,""+ ((SignupModel) data).getLongitude()));
            requestBodyMap.put("additionalMobileNo", RequestBody.create(mediaType,((SignupModel) data).getAdditionalMobileNo()));
            requestBodyMap.put("password", RequestBody.create(mediaType,((SignupModel) data).getPassword()));
            requestBodyMap.put("deviceType", RequestBody.create(mediaType,((SignupModel) data).getDeviceType()));
            requestBodyMap.put("deviceToken", RequestBody.create(mediaType,((SignupModel) data).getDeviceToken()));
        }
    }

    public Map<String, RequestBody> getBody()
    {
        return requestBodyMap;
    }
}
