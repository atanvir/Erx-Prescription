package com.erxprescriptionuser.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.erxprescriptionuser.R;
import com.erxprescriptionuser.Utils.CommonUtils;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;


public class AddressPicker extends AppCompatActivity implements OnMapReadyCallback, View.OnClickListener
{
    private GoogleMap mMap;
    private TextView detectLocationTextView;
    private Button submitButton;
    private TextView searchTextView;
    private ArrayList markerPoints;
    private final int PLACE_REQ_CODE=12,GETTING_ADDRESS=1,NOT_SERVE_THIS_AREA=2,HIDE_INFO_WINDOW=3,GETTING_SELECTED_CITY=4,CHECKING_CITY=5;
    Marker marker=null ;
    Geocoder geocoder;
    private Handler handler;
    private boolean isInsideCity=false;
    private GetAddress getAddress;
    private LatLng homeLatLong;
    String km;

    @SuppressLint("HandlerLeak")
    private void init()
    {
        submitButton=findViewById(R.id.submitButton);
        searchTextView=findViewById(R.id.searchTextView);
        detectLocationTextView=findViewById(R.id.detectLocationTextView);
        markerPoints= new ArrayList();
        submitButton.setOnClickListener(this);
        searchTextView.setOnClickListener(this);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_picker);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        init();
    }

    @Override
    protected void onResume() {
        super.onResume();
        CommonUtils.setToolbar(this,getString(R.string.select_location));
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        getAddress=new GetAddress();
                homeLatLong = new LatLng(getIntent().getDoubleExtra("latitue",0.0), getIntent().getDoubleExtra("longitute",0.0));
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(homeLatLong, 12.0f));
                marker = mMap.addMarker(new MarkerOptions().position(mMap.getCameraPosition().target).title("Marker").icon(BitmapDescriptorFactory.fromResource(R.drawable.map_pin)));

                handler=new Handler(){
                    @Override
                    public void handleMessage(Message msg) {
                        super.handleMessage(msg);

                        if(msg.what==GETTING_ADDRESS)
                        {
                            marker.setTitle(getString(R.string.getting_address));
                            marker.showInfoWindow();
                        }else if(msg.what==NOT_SERVE_THIS_AREA)
                        {
                            marker.setTitle(getString(R.string.we_do_not_serve_here));
                            marker.showInfoWindow();
                        }else if(msg.what==HIDE_INFO_WINDOW)
                        {
                            marker.hideInfoWindow();
                        }

                    }
                };

                geocoder = new Geocoder(AddressPicker.this, Locale.getDefault());
                getAddress.execute(GETTING_SELECTED_CITY);

                GetAddress getSelectedCityAddress=new GetAddress();
                getSelectedCityAddress.execute();

                GetSelectedAddress getSelectedAddress=new GetSelectedAddress();
                getSelectedAddress.execute();
                markerPoints.add(homeLatLong);


            mMap.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {
            @Override
            public void onCameraMove() {


                if (marker == null) {
                    marker = mMap.addMarker(new MarkerOptions().position(mMap.getCameraPosition().target).title("Marker").icon(BitmapDescriptorFactory.fromResource(R.drawable.map_pin)));


                } else {
                    marker.setPosition(mMap.getCameraPosition().target);
                    handler.sendEmptyMessage(HIDE_INFO_WINDOW);
                }
            }


        });



        mMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {
                if(getAddress.getStatus()!= AsyncTask.Status.RUNNING)
                {
                    getAddress=new GetAddress();
                    getAddress.execute(CHECKING_CITY);
                }else
                {
                    getAddress.cancel(true);
                    getAddress=new GetAddress();
                    getAddress.execute(CHECKING_CITY);
                }
            }
        });
      }

    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            Log.e("url",strUrl);
            URL url = new URL(strUrl);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.connect();
            iStream = urlConnection.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));
            StringBuffer sb = new StringBuffer();
            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            data = sb.toString();
            br.close();
        } catch (Exception e) {
            Log.d("Exception", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    @Override
    public void onClick(View v)
    { switch (v.getId())
        {
            case R.id.submitButton:
            onSubmit();
            break;

            case R.id.detectLocationTextView:
            LatLng latLng = new LatLng(getIntent().getDoubleExtra("latitue",0.0), getIntent().getDoubleExtra("longitute",0.0));
            marker.setPosition(latLng);
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12.0f));
            markerPoints.add(latLng);
            break;

            case R.id.searchTextView:
            Places.initialize(AddressPicker.this,getResources().getString(R.string.google_map_api_key));
            List<Place.Field> fields1 = Arrays.asList(Place.Field.LAT_LNG);
            Intent intent1 = new Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields1)
                                                 .setTypeFilter(TypeFilter.ADDRESS)
                                                 .build(AddressPicker.this);

            startActivityForResult(intent1, PLACE_REQ_CODE);
            break;
        }

    }

    private void onSubmit()
    {
        LatLng selectedLocation =  marker.getPosition();
        Intent intent=new Intent();
        intent.putExtra("ADDRESS",detectLocationTextView.getText().toString());
        intent.putExtra("LAT",selectedLocation.latitude);
        intent.putExtra("LONG",selectedLocation.longitude);
        setResult(RESULT_OK,intent);
        finish();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PLACE_REQ_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);
                markerPoints.clear();
                LatLng selectedPos = place.getLatLng();
                markerPoints.add(selectedPos);
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(selectedPos, 12.0f));
            }
        }
    }

    private class GetAddress extends AsyncTask<Integer, Void, String>
    {

        LatLng latLng;
        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            isInsideCity=false;
            latLng=marker.getPosition();
            handler.sendEmptyMessage(GETTING_ADDRESS);
        }

        @Override
        protected String doInBackground(Integer... AddressType) {
            String address="";

            try
            {

                List<Address> addressesList=geocoder.getFromLocation(latLng.latitude,latLng.longitude,1);

                if(addressesList!=null&&!addressesList.isEmpty())
                {
                    Address addresses = addressesList.get(0);
                       address=addresses.getAddressLine(0);
                        isInsideCity=true;
                        handler.sendEmptyMessage(HIDE_INFO_WINDOW);
                }else
                {
                    handler.sendEmptyMessage(NOT_SERVE_THIS_AREA);
                    address="";
                }











            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                if(e.getMessage().equalsIgnoreCase("grpc failed"))
                {
                CommonUtils.showSnackBar(AddressPicker.this,getString(R.string.enable_gps));
                }


            }

           /* if(AddressType[0]==GETTING_SELECTED_CITY)
            {
                selectedCity=address;
            }*/

            return address;
        }



        @Override
        protected void onPostExecute(String s)
        {
            super.onPostExecute(s);
            Log.e("updated address",s);
            detectLocationTextView.setText(s);
        }
    }

    private class GetSelectedAddress extends AsyncTask<Integer, Void, String>
    {

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Integer... AddressType) {
            String address="";

            try
            {

                List<Address> addressesList=geocoder.getFromLocation(homeLatLong.latitude,homeLatLong.longitude,1);
                if(addressesList.size()>0) {
                    address = addressesList.get(0).getAddressLine(0);
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();

            }

           /* if(AddressType[0]==GETTING_SELECTED_CITY)
            {
                selectedCity=address;
            }*/

            return address;
        }



        @Override
        protected void onPostExecute(String s)
        {
            super.onPostExecute(s);
            Log.e("addresss",s);
            detectLocationTextView.setText(s);
        }
    }


}
