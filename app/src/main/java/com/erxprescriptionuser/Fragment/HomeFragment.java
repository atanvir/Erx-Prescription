package com.erxprescriptionuser.Fragment;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.erxprescriptionuser.Activity.MainActivity;
import com.erxprescriptionuser.Adapter.CustomInfoWindowAdapter;
import com.erxprescriptionuser.Model.CommonListModel;
import com.erxprescriptionuser.Model.ResponseBean;
import com.erxprescriptionuser.R;
import com.erxprescriptionuser.Retrofit.ServicesConnection;
import com.erxprescriptionuser.Retrofit.ServicesInterface;
import com.erxprescriptionuser.Utils.CommonUtils;
import com.erxprescriptionuser.Utils.ParamEnum;
import com.erxprescriptionuser.databinding.FragmentHomeBinding;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment implements OnMapReadyCallback, MainActivity.IMapListner, AdapterView.OnItemSelectedListener {
    private FragmentHomeBinding binding;
    private GoogleMap mMap;
    private LatLng currentLatLong;
    private ArrayList<Marker> mMarkerArray = new ArrayList<Marker>();
    private List<String> listProvider=new ArrayList<>();
    ImageView ivproviderNameSearch,ivMenu,ivNotification,ivSearch;
    EditText edSearch;
    Spinner spinner;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        CommonUtils.setLocale(getActivity());
        binding=DataBindingUtil.inflate(inflater,R.layout.fragment_home,container,false);
        SupportMapFragment map= (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        map.getMapAsync(this);
        return binding.getRoot();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap=googleMap;
        Log.e("times","times");
        currentLatLong = new LatLng(getArguments().getDouble("latitude"),getArguments().getDouble("longitude"));
        mMap.addMarker(new MarkerOptions().position(currentLatLong).icon(BitmapDescriptorFactory.fromResource(R.drawable.map_pin)));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(currentLatLong));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLong,15));
        getNearByProviderListApi();
    }

    private void getNearByProviderListApi() {
        try {
            ServicesInterface anInterface = ServicesConnection.getInstance().createService(getActivity());
            Call<CommonListModel> call = anInterface.nearByProviderList(currentLatLong.latitude,currentLatLong.longitude);
            ServicesConnection.getInstance().enqueueWithoutRetry(call, getActivity(), true, new Callback<CommonListModel>() {
                @Override
                public void onResponse(Call<CommonListModel> call, Response<CommonListModel> response) {
                    if(response.isSuccessful())
                    {
                        CommonListModel serverResponse= response.body();
                        if (serverResponse.getStatus().equalsIgnoreCase(ParamEnum.SUCCESS.theValue())) {
                            MainActivity.setListner(HomeFragment.this);
                            setMapView(serverResponse.getData());
                        }
                        else if (serverResponse.getStatus().equalsIgnoreCase(ParamEnum.FAILURE.theValue())) {
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

    private void setMapView(List<ResponseBean> data) {
        for(int i=0;i<data.size();i++)
        {
            Marker marker=mMap.addMarker(new MarkerOptions().position(new LatLng(data.get(i).getLatitude(),data.get(i).getLongitude())));
            marker.setTag(data.get(i));
            mMarkerArray.add(marker);
        }
        mMap.setInfoWindowAdapter(new CustomInfoWindowAdapter(getActivity()));
        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
            }
        });
    }


    @Override
    public void onTextChange(String text, View view, ImageView ivSearch, EditText edSearch, ImageView ivproviderNameSearch, ImageView ivMenu, ImageView ivNotification) {
        this.ivSearch=ivSearch;
        this.edSearch=edSearch;
        this.ivproviderNameSearch=ivproviderNameSearch;
        this.ivMenu=ivMenu;
        this.ivNotification=ivNotification;
        this.spinner= (Spinner) view;

        if(!text.equalsIgnoreCase(""))
        {
            new SearchProvider(getActivity(), (Spinner) view,text).execute(mMarkerArray);
        }
        spinner.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId())
        {
            case R.id.spnProvider:
                if(position!=-1 && position!=0) {
                    for(int i=0;i<mMarkerArray.size();i++)
                    {
                        ResponseBean data = (ResponseBean) mMarkerArray.get(i).getTag();
                        if(data.getFullName().equalsIgnoreCase(listProvider.get(position)))
                        {
                            mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(data.getLatitude(), data.getLongitude())));
                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(data.getLatitude(), data.getLongitude()), 15));
                            ivSearch.setVisibility(View.VISIBLE);
                            edSearch.setVisibility(View.GONE);
                            ivproviderNameSearch.setVisibility(View.GONE);
                            ivMenu.setVisibility(View.VISIBLE);
                            ivNotification.setVisibility(View.VISIBLE);
                            spinner.setVisibility(View.GONE);
                            break;
                        }
                    }

                }
            break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public class SearchProvider extends AsyncTask<List<Marker>,Void,List<String>>
    {
        private Context context;
        private Spinner spinner;
        private String searchText;

        public  SearchProvider(Context context,Spinner spinner,String searchText)
        {
            this.context=context;
            this.spinner=spinner;
            this.searchText=searchText;
        }

        @Override
        protected void onPreExecute() {
            CommonUtils.showLoadingDialog((Activity) context);
        }

        @Override
        protected List<String> doInBackground(List<Marker>... lists) {
            List<String> list = new ArrayList<>();
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    for(int i=0;i<lists[0].size();i++)
                    {
                        ResponseBean data= (ResponseBean) lists[0].get(i).getTag();
                        String splitedName[];
                        if(data.getFullName().trim().contains(" "))
                        {
                            splitedName=data.getFullName().split(" ");
                        }else
                        {
                            splitedName= new String[]{"" + data.getFullName()};
                        }

                        for(int y=0;y<splitedName.length;y++)
                        {
                            if((splitedName[y].toLowerCase().contains(searchText.toLowerCase())))
                            {
                                if(!list.contains(data.getFullName())) {
                                    list.add(data.getFullName());
                                }
                            }

                        }
                    }
                    }
                });

            return list;
        }

        @Override
        protected void onPostExecute(List<String> list) {
            CommonUtils.dismissLoadingDialog();
            if(list.size()>0)
            {
             listProvider.clear();
             listProvider=list;
             listProvider.add(0,getString(R.string.select_provider));
             spinner.setVisibility(View.VISIBLE);
            CommonUtils.setSpinner(getActivity(),list,spinner);
            }else
            {
                ivSearch.setVisibility(View.VISIBLE);
                edSearch.setVisibility(View.GONE);
                ivproviderNameSearch.setVisibility(View.GONE);
                ivMenu.setVisibility(View.VISIBLE);
                ivNotification.setVisibility(View.VISIBLE);
                spinner.setVisibility(View.GONE);
                Toast.makeText(context, R.string.no_provider_found, Toast.LENGTH_SHORT).show();
            }
        }
    }

    public interface Listner{
        void hide();
    }

}
