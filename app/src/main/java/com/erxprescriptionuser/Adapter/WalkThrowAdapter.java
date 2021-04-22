package com.erxprescriptionuser.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.erxprescriptionuser.R;
import com.erxprescriptionuser.Utils.CommonUtils;


public class WalkThrowAdapter extends PagerAdapter {

    private Context context;
    private int img[]={R.drawable.a,R.drawable.b,R.drawable.c};
    private String header[];
    private String step1[];
    private String step2[];
    private String step3[];

    public WalkThrowAdapter(Context context)
    {
        this.context=context;
        header= new String[]{context.getString(R.string.how_to_prescription), context.getString(R.string.choosing_pick_up_delivery), context.getString(R.string.my_prescriptions)};
        step1= new String[]{context.getString(R.string.click_on_camera_icon), context.getString(R.string.you_will_receive_notification), context.getString(R.string.you_can_check_prescription_status_from_here)};
        step2= new String[]{context.getString(R.string.choose_the_picture_from_gallery), context.getString(R.string.once_you_accept_the_offer_from_provider_any_pay), ""};
        step3= new String[]{context.getString(R.string.upload_prescription), "", ""};
    }

    @Override
    public int getCount() {
        return img.length;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view=inflater.inflate(R.layout.adapter_walk_throw,container,false);
        ImageView ivBackground=view.findViewById(R.id.ivBackground);
        TextView tvHeading=view.findViewById(R.id.tvHeading);
        TextView tvStep1=view.findViewById(R.id.tvStep1);
        TextView tvStep2=view.findViewById(R.id.tvStep2);
        TextView tvStep3=view.findViewById(R.id.tvStep3);
        tvHeading.setText(header[position]);
        tvStep1.setText(step1[position]);
        tvStep2.setText(step2[position]);
        tvStep3.setText(step3[position]);
        ivBackground.setImageResource(img[position]);
        ViewPager viewPager=(ViewPager) container;
        viewPager.addView(view,0);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
       ViewPager viewPager= (ViewPager)container;
       View view=(View) object;
       viewPager.removeView(view);
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view==object;
    }


}
