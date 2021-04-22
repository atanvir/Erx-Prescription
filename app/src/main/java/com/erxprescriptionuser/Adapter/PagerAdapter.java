package com.erxprescriptionuser.Adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.erxprescriptionuser.Fragment.OnGoingFragment;
import com.erxprescriptionuser.Fragment.PastFragment;
import com.erxprescriptionuser.Fragment.PaymentFragment;
import com.erxprescriptionuser.Fragment.SubProfileFragment;
import com.erxprescriptionuser.R;

public class PagerAdapter extends FragmentStatePagerAdapter {
    private String cameFrom;
    private Context context;

    public PagerAdapter(Context context,String cameFrom, FragmentManager fm) {
        super(fm);
        this.context=context;
        this.cameFrom = cameFrom;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        if (cameFrom.equalsIgnoreCase("Profile")) {
            if (position == 0) return new SubProfileFragment();
            else return new PaymentFragment();
        } else {
            if (position == 0) return new OnGoingFragment();
            else return new PastFragment();
        }

    }


    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        if (cameFrom.equalsIgnoreCase("Profile")) {
            if (position == 0) return context.getString(R.string.profile);
            else return context.getString(R.string.payment);
        } else {
            if (position == 0) return context.getString(R.string.on_going);
            else return context.getString(R.string.past);
        }
    }
}
