package com.erxprescriptionuser.GoShellSdk;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.erxprescriptionuser.R;
import com.erxprescriptionuser.SharePrefrences.SPreferenceKey;
import com.erxprescriptionuser.SharePrefrences.SharedPreferenceWriter;
import com.erxprescriptionuser.databinding.ActivityGoShellBinding;
import com.google.gson.Gson;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;

import company.tap.gosellapi.GoSellSDK;
import company.tap.gosellapi.internal.api.callbacks.GoSellError;
import company.tap.gosellapi.internal.api.models.Authorize;
import company.tap.gosellapi.internal.api.models.Charge;
import company.tap.gosellapi.internal.api.models.PhoneNumber;
import company.tap.gosellapi.internal.api.models.SaveCard;
import company.tap.gosellapi.internal.api.models.SavedCard;
import company.tap.gosellapi.internal.api.models.Token;
import company.tap.gosellapi.open.controllers.SDKSession;
import company.tap.gosellapi.open.controllers.ThemeObject;
import company.tap.gosellapi.open.delegate.SessionDelegate;
import company.tap.gosellapi.open.enums.CardType;
import company.tap.gosellapi.open.enums.TransactionMode;
import company.tap.gosellapi.open.models.CardsList;
import company.tap.gosellapi.open.models.Customer;
import company.tap.gosellapi.open.models.PaymentItem;
import company.tap.gosellapi.open.models.TapCurrency;
import company.tap.gosellapi.open.models.Tax;

public class GoShellHelper extends Activity implements SessionDelegate {
    private SDKSession sdkSession;
    private ActivityGoShellBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=DataBindingUtil.setContentView(this,R.layout.activity_go_shell);
        startSDK(this);
    }

    public void startSDK(Context context){
       configureApp(context);
       configureSDKThemeObject(context);
       configureSDKSession();
       initPayButton();
   }
    private  void configureApp(Context context){
        GoSellSDK.init(context, "sk_test_hgjFBvp3yNRsMLtPu1lS6qnW","com.erxprescriptionuser");
        GoSellSDK.setLocale("en");
    }

    private void configureSDKThemeObject(Context context) {

        ThemeObject.getInstance()
                .setSdkLanguage("en") //if you dont pass locale then default locale EN will be used
                .setHeaderTextColor(context.getResources().getColor(R.color.black1))  // **Optional**
                .setHeaderTextSize(17) // **Optional**

                // setup header background
                .setHeaderBackgroundColor(context.getResources().getColor(R.color.french_gray_new))//**Optional**

                // setup card form input font type
//                .setCardInputFont(Typeface.createFromAsset(getAssets(),"font/lato_regular.ttf"))//**Optional**

                // setup card input field text color
                .setCardInputTextColor(context.getResources().getColor(R.color.black))//**Optional**

                // setup card input field text color in case of invalid input
                .setCardInputInvalidTextColor(context.getResources().getColor(R.color.red))//**Optional**

                // setup card input hint text color
                .setCardInputPlaceholderTextColor(context.getResources().getColor(R.color.black))//**Optional**

                // setup Switch button Thumb Tint Color in case of Off State
                .setSaveCardSwitchOffThumbTint(context.getResources().getColor(R.color.gray)) // **Optional**

                // setup Switch button Thumb Tint Color in case of On State
                .setSaveCardSwitchOnThumbTint(context.getResources().getColor(R.color.vibrant_green)) // **Optional**

                // setup Switch button Track Tint Color in case of Off State
                .setSaveCardSwitchOffTrackTint(context.getResources().getColor(R.color.gray)) // **Optional**

                // setup Switch button Track Tint Color in case of On State
                .setSaveCardSwitchOnTrackTint(context.getResources().getColor(R.color.green)) // **Optional**

                // change scan icon
                .setScanIconDrawable(context.getResources().getDrawable(R.drawable.btn_card_scanner_normal)) // **Optional**

                // setup pay button selector [ background - round corner ]
                .setPayButtonResourceId(R.drawable.btn_pay_selector)

                // setup pay button font type face
//                .setPayButtonFont(Typeface.createFromAsset(getAssets(),"fonts/roboto_light.ttf")) // **Optional**

                // setup pay button disable title color
                .setPayButtonDisabledTitleColor(context.getResources().getColor(R.color.black)) // **Optional**

                // setup pay button enable title color
                .setPayButtonEnabledTitleColor(context.getResources().getColor(android.R.color.white)) // **Optional**

                //setup pay button text size
                .setPayButtonTextSize(14) // **Optional**

                // show/hide pay button loader
                .setPayButtonLoaderVisible(true) // **Optional**

                // show/hide pay button security icon
                .setPayButtonSecurityIconVisible(true) // **Optional**

                // setup dialog textcolor and textsize
                .setDialogTextColor(context.getResources().getColor(R.color.black1))     // **Optional**
                .setDialogTextSize(17)                // **Optional**

        ;

    }

    private void configureSDKSession() {

        // Instantiate SDK Session
        if(sdkSession==null) sdkSession = new SDKSession();   //** Required **

        // pass your activity as a session delegate to listen to SDK internal payment process follow
        sdkSession.addSessionDelegate(this);    //** Required **

        // initiate PaymentDataSource
        sdkSession.instantiatePaymentDataSource();    //** Required **

        // set transaction currency associated to your account
        sdkSession.setTransactionCurrency(new TapCurrency(getIntent().getStringExtra("currency")));    //** Required **

        // Using static CustomerBuilder method available inside TAP Customer Class you can populate TAP Customer object and pass it to SDK
        sdkSession.setCustomer(getCustomer(this));    //** Required **

        // Set Total Amount. The Total amount will be recalculated according to provided Taxes and Shipping
      //  sdkSession.setAmount(new BigDecimal(getIntent().getStringExtra("amount")));  //** Required **
        sdkSession.setAmount(new BigDecimal(1));
        // Set Payment Items array list
        sdkSession.setPaymentItems(new ArrayList<PaymentItem>());// ** Optional ** you can pass empty array list

        // Set Taxes array list
        sdkSession.setTaxes(new ArrayList<Tax>());// ** Optional ** you can pass empty array list

        // Set Shipping array list
        sdkSession.setShipping(new ArrayList<>());// ** Optional ** you can pass empty array list


        // Post URL
        sdkSession.setPostURL(""); // ** Optional **

        // Payment Description
        sdkSession.setPaymentDescription(""); //** Optional **

        // Payment Extra Info
        sdkSession.setPaymentMetadata(new HashMap<>());// ** Optional ** you can pass empty array hash map

        // Payment Reference
        sdkSession.setPaymentReference(null); // ** Optional ** you can pass null

        // Payment Statement Descriptor
        sdkSession.setPaymentStatementDescriptor(""); // ** Optional **

        // Enable or Disable Saving Card
        sdkSession.isUserAllowedToSaveCard(true); //  ** Required ** you can pass boolean

        // Enable or Disable 3DSecure
        sdkSession.isRequires3DSecure(true);

        //Set Receipt Settings [SMS - Email ]
        sdkSession.setReceiptSettings(null);
        // ** Optional ** you can pass Receipt object or null

        // Set Authorize Action
        sdkSession.setAuthorizeAction(null); // ** Optional ** you can pass AuthorizeAction object or null

        sdkSession.setDestination(null); // ** Optional ** you can pass Destinations object or null

        sdkSession.setMerchantID(null); // ** Optional ** you can pass merchant id or null

        sdkSession.setPaymentType("Online");   //** Merchant can customize payment options [WEB/CARD] for each transaction or it will show all payment options granted to him.

        sdkSession.setCardType(CardType.DEBIT); // ** Optional ** you can pass which cardType[CREDIT/DEBIT] you want.By default it loads all available cards for Merchant.

        sdkSession.setDefaultCardHolderName("Tanvir Ahmed"); // ** Optional ** you can pass default CardHolderName of the user .So you don't need to type it.

        sdkSession.isUserAllowedToEnableCardHolderName(false); //** Optional ** you can enable/ disable  default CardHolderName .

        /**
         * Use this method where ever you want to show TAP SDK Main Screen.
         * This method must be called after you configured SDK as above
         * This method will be used in case of you are not using TAP PayButton in your activity.
         */

       configureSDKMode();
    }

//    private void listSavedCards(){
//        if(sdkSession!=null)
//            sdkSession.listAllCards("CUSTOMER_ID",this);
//    }
    private Customer getCustomer(Context context) {
       String name= SharedPreferenceWriter.getInstance(context).getString(SPreferenceKey.NAME).trim();
       String firstName="",lastName="",middleName="";
       if(name.contains(" "))
       {
           String fullName[]=name.split(" ");
           if(fullName.length>2)
           {
               firstName=fullName[0];
               middleName=fullName[1];
               lastName=fullName[2];
           }else if(fullName.length==2)
           {

               firstName=fullName[0];
               lastName=fullName[1];
           }else
           {
               firstName=fullName[0];
           }
       }
        return new Customer.CustomerBuilder(null).email(SharedPreferenceWriter.getInstance(context).getString(SPreferenceKey.EMAIL)).firstName(firstName)
                .lastName(lastName).metadata("").phone(new PhoneNumber(SharedPreferenceWriter.getInstance(context).getString(SPreferenceKey.COUNTRY_CODE),SharedPreferenceWriter.getInstance(context).getString(SPreferenceKey.MOBILE)))
                .middleName(middleName).build();
    }
    private void configureSDKMode() {
        startSDKWithUI();

    }

    private void startSDKWithUI() {
        if (sdkSession != null) {
            sdkSession.setTransactionMode(TransactionMode.PURCHASE);    //** Required **
        }
    }
    @Override
    public void paymentSucceed(@NonNull Charge charge) {
        String object=new Gson().toJson(charge);
        Log.e("response", object);
        Intent intent=new Intent();
        intent.putExtra("chargeId",charge.getId());
        intent.putExtra("status",charge.getStatus());
        intent.putExtra("paymentType", charge.getSource().getPaymentMethodStringValue()+" "+charge.getSource().getPaymentType()+" CARD ");
        intent.putExtra("payemntId", charge.getSource().getId());
        setResult(RESULT_OK,intent);
        finish();
    }


    @Override
    public void paymentFailed(@Nullable Charge charge) {
        Intent intent = new Intent();
        intent.putExtra("status", charge.getStatus());
        intent.putExtra("message",charge.getResponse().getMessage());
        setResult(RESULT_CANCELED,intent);
        finish();
    }

    @Override
    public void authorizationSucceed(@NonNull Authorize authorize) {
        System.out.println("Authorize Succeeded : " + authorize.getStatus());
        System.out.println("Authorize Succeeded : " + authorize.getResponse().getMessage());

        if (authorize.getCard() != null) {
            System.out.println("Payment Authorized Succeeded : first six : " + authorize.getCard().getFirstSix());
            System.out.println("Payment Authorized Succeeded : last four: " + authorize.getCard().getLast4());
            System.out.println("Payment Authorized Succeeded : card object : " + authorize.getCard().getObject());
        }

        System.out.println("##############################################################################");
        if (authorize.getAcquirer() != null) {
            System.out.println("Payment Authorized Succeeded : acquirer id : " + authorize.getAcquirer().getId());
            System.out.println("Payment Authorized Succeeded : acquirer response code : " + authorize.getAcquirer().getResponse().getCode());
            System.out.println("Payment Authorized Succeeded : acquirer response message: " + authorize.getAcquirer().getResponse().getMessage());
        }
        System.out.println("##############################################################################");
        if (authorize.getSource() != null) {
            System.out.println("Payment Authorized Succeeded : source id: " + authorize.getSource().getId());
            System.out.println("Payment Authorized Succeeded : source channel: " + authorize.getSource().getChannel());
            System.out.println("Payment Authorized Succeeded : source object: " + authorize.getSource().getObject());
            System.out.println("Payment Authorized Succeeded : source payment method: " + authorize.getSource().getPaymentMethodStringValue());
            System.out.println("Payment Authorized Succeeded : source payment type: " + authorize.getSource().getPaymentType());
            System.out.println("Payment Authorized Succeeded : source type: " + authorize.getSource().getType());
        }

        System.out.println("##############################################################################");
        if (authorize.getExpiry() != null) {
            System.out.println("Payment Authorized Succeeded : expiry type :" + authorize.getExpiry().getType());
            System.out.println("Payment Authorized Succeeded : expiry period :" + authorize.getExpiry().getPeriod());
        }
    }

    @Override
    public void authorizationFailed(Authorize authorize) {
        System.out.println("Authorize Failed : " + authorize.getStatus());
        System.out.println("Authorize Failed : " + authorize.getDescription());
        System.out.println("Authorize Failed : " + authorize.getResponse().getMessage());
    }

    @Override
    public void cardSaved(@NonNull Charge charge) {
        if (charge instanceof SaveCard) {
            System.out.println("Card Saved Succeeded : first six digits : " + ((SaveCard) charge).getCard().getFirstSix() + "  last four :" + ((SaveCard) charge).getCard().getLast4());
        }
        System.out.println("Card Saved Succeeded : " + charge.getStatus());
        System.out.println("Card Saved Succeeded : " + charge.getCard().getBrand());
        System.out.println("Card Saved Succeeded : " + charge.getDescription());
        System.out.println("Card Saved Succeeded : " + charge.getResponse().getMessage());


    }

    @Override
    public void cardSavingFailed(@NonNull Charge charge) {
        System.out.println("Card Saved Failed : " + charge.getStatus());
        System.out.println("Card Saved Failed : " + charge.getDescription());
        System.out.println("Card Saved Failed : " + charge.getResponse().getMessage());
    }

    @Override
    public void cardTokenizedSuccessfully(@NonNull Token token) {
        System.out.println("Card Tokenized Succeeded : ");
        System.out.println("Token card : " + token.getCard().getFirstSix() + " **** " + token.getCard().getLastFour());
        System.out.println("Token card : " + token.getCard().getFingerprint() + " **** " + token.getCard().getFunding());
        System.out.println("Token card : " + token.getCard().getId() + " ****** " + token.getCard().getName());
        System.out.println("Token card : " + token.getCard().getAddress() + " ****** " + token.getCard().getObject());
        System.out.println("Token card : " + token.getCard().getExpirationMonth() + " ****** " + token.getCard().getExpirationYear());

    }

    @Override
    public void savedCardsList(@NonNull CardsList cardsList) {
        if (cardsList != null && cardsList.getCards() != null) {
            savedCardsList(cardsList);
            System.out.println(" Card List Response Code : " + cardsList.getResponseCode());
            System.out.println(" Card List Top 10 : " + cardsList.getCards().size());
            System.out.println(" Card List Has More : " + cardsList.isHas_more());
            System.out.println("----------------------------------------------");
            for (SavedCard sc : cardsList.getCards()) {
                System.out.println(sc.getBrandName());
            }
            System.out.println("----------------------------------------------");
        }
    }

    @Override
    public void sdkError(@Nullable GoSellError goSellError) {
        Intent intent= new Intent();
        intent.putExtra("status", "GO SELL ERROR");
        intent.putExtra("message", "Error in initializing");
        setResult(RESULT_CANCELED,intent);
        finish();

    }

    @Override
    public void sessionIsStarting() {
        System.out.println(" Session Is Starting.....");
    }

    @Override
    public void sessionHasStarted() {
        System.out.println(" Session Has Started .......");
    }

    @Override
    public void sessionCancelled() {
        Intent intent= new Intent();
        intent.putExtra("status", "CANCEL");
        intent.putExtra("message", "cancelled");
        setResult(RESULT_CANCELED,intent);
        finish();
    }

    @Override
    public void sessionFailedToStart() {
        Log.d("MainActivity", "Session Failed to start.........");
    }

    @Override
    public void invalidCardDetails() {
        System.out.println(" Card details are invalid....");
    }

    @Override
    public void backendUnknownError(String message) {
        System.out.println("Backend Un-Known error.... : " + message);
    }

    @Override
    public void invalidTransactionMode() {
        System.out.println(" invalidTransactionMode  ......");
    }

    @Override
    public void invalidCustomerID() {
        System.out.println("Invalid Customer ID .......");
    }

    @Override
    public void userEnabledSaveCardOption(boolean saveCardEnabled) {
        Log.e("saveCardEnabled", ""+saveCardEnabled);

    }


    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }


    private void initPayButton() {
        binding.payButtonId.setupFontTypeFace(ThemeObject.getInstance().getPayButtonFont());
        binding.payButtonId.setupTextColor(ThemeObject.getInstance().getPayButtonEnabledTitleColor(), ThemeObject.getInstance().getPayButtonDisabledTitleColor());
        binding.payButtonId.getPayButton().setTextSize(ThemeObject.getInstance().getPayButtonTextSize());
        binding.payButtonId.getSecurityIconView().setVisibility(ThemeObject.getInstance().isPayButtSecurityIconVisible()?View.VISIBLE:View.INVISIBLE);
        binding.payButtonId.setBackgroundSelector(ThemeObject.getInstance().getPayButtonResourceId());
        if(sdkSession!=null){
            TransactionMode trx_mode = sdkSession.getTransactionMode();
            if(trx_mode!=null){

                if (TransactionMode.SAVE_CARD == trx_mode  || TransactionMode.SAVE_CARD_NO_UI ==trx_mode) {
                    binding.payButtonId.getPayButton().setText(getString(company.tap.gosellapi.R.string.save_card));
                }
                else if(TransactionMode.TOKENIZE_CARD == trx_mode || TransactionMode.TOKENIZE_CARD_NO_UI == trx_mode){
                    binding.payButtonId.getPayButton().setText(getString(company.tap.gosellapi.R.string.tokenize));
                }
                else {
                    binding.payButtonId.getPayButton().setText(getString(company.tap.gosellapi.R.string.pay)+" "+getIntent().getStringExtra("amount") +" "+getIntent().getStringExtra("currency"));
                }
            }else{
//                sdkSession.start(this);
                configureSDKMode();
            }
            sdkSession.setButtonView(binding.payButtonId, this, 111);
        }


    }
}
