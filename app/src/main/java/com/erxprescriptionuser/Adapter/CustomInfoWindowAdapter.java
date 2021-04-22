package com.erxprescriptionuser.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.databinding.DataBindingUtil;

import com.erxprescriptionuser.Model.ResponseBean;
import com.erxprescriptionuser.R;
import com.erxprescriptionuser.Utils.CommonUtils;
import com.erxprescriptionuser.Utils.ImageGlider;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {
    private Context context;
    private final View view;

public CustomInfoWindowAdapter(Context context) {
        this.context = context;

        this.view= LayoutInflater.from(context).inflate(R.layout.adapter_custom_info, null);
}


    @Override
    public View getInfoWindow(Marker marker) {
        if(marker.getTag()!=null) {
            setView(marker, view);
            return view;
        }else
        {
            return null;
        }
    }

    private void setView(Marker marker, View view) {
       ResponseBean data= (ResponseBean) marker.getTag();
       CircleImageView ciProfilePic=  view.findViewById(R.id.ciProfilePic);
       ProgressBar progressBar=  view.findViewById(R.id.progressBar);
       TextView tvName= view.findViewById(R.id.tvName);
       TextView tvEmail= view.findViewById(R.id.tvEmail);
       TextView tvMobileNo= view.findViewById(R.id.tvMobileNo);
       TextView tvAddress= view.findViewById(R.id.tvAddress);
       ImageGlider.setNormalImage(context,ciProfilePic,progressBar,data.getProfilePic());
       tvName.setText(""+data.getFullName());
       tvEmail.setText(""+data.getEmail());
       tvMobileNo.setText(""+data.getCountryCode()+" "+data.getMobileNumber());
    }

    @Override
    public View getInfoContents(Marker marker) {
        if(marker.getTag()!=null) {
            setView(marker, view);
            return view;

        }else {
            return null;
        }
    }
}
