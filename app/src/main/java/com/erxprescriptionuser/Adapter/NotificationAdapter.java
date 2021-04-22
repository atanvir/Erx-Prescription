package com.erxprescriptionuser.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.erxprescriptionuser.Fragment.MyOfferFragment;
import com.erxprescriptionuser.Model.ResponseBean;
import com.erxprescriptionuser.R;
import com.erxprescriptionuser.SharePrefrences.SPreferenceKey;
import com.erxprescriptionuser.SharePrefrences.SharedPreferenceWriter;
import com.erxprescriptionuser.Utils.CommonUtils;
import com.erxprescriptionuser.Utils.SwitchFragment;
import com.erxprescriptionuser.databinding.AdapterNotificationBinding;

import java.util.List;

import okhttp3.Response;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.MyHolder> {
    private Context context;
    private List<ResponseBean> list;

    public NotificationAdapter(Context context, List<ResponseBean> list) {
        this.context = context;
        this.list=list;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
     AdapterNotificationBinding binding= DataBindingUtil.inflate(LayoutInflater.from(context),R.layout.adapter_notification,parent,false);
     return new NotificationAdapter.MyHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        holder.binding.tvTitle.setText(list.get(position).getBody());
        holder.binding.tvTime.setText(CommonUtils.getTimeAgo(list.get(position).getCreatedAt()));

        if(!list.get(position).isSeen())
        {
            holder.binding.clMain.setBackgroundColor(ContextCompat.getColor(context,R.color.blur_blue));
            holder.binding.tvTitle.setTextColor(ContextCompat.getColor(context,R.color.colorPrimary));
            holder.binding.tvTime.setTextColor(ContextCompat.getColor(context,R.color.colorPrimary));

        }
    }

    @Override
    public int getItemCount() {
        return list!=null?list.size():0;
    }

    public class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        AdapterNotificationBinding binding;

        public MyHolder(@NonNull AdapterNotificationBinding itemView) {
            super(itemView.getRoot());
            binding=itemView;
            CommonUtils.showAnimation(binding.clMain);
            binding.clMain.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId())
            {
                case R.id.clMain:
                    if(list.get(getAdapterPosition()).getTitle().equalsIgnoreCase("Hi! New offer available."))
                    {
                        SharedPreferenceWriter.getInstance(context).writeBooleanValue(SPreferenceKey.NOTI_OFFER, false);
                        SwitchFragment.changeFragment(((AppCompatActivity)context).getSupportFragmentManager(),new MyOfferFragment(),false,true);
                    }

                break;
            }

        }
    }
}
