package com.erxprescriptionuser.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.erxprescriptionuser.Activity.BookingDetailActivity;
import com.erxprescriptionuser.Activity.PrescriptionDetailActivity;
import com.erxprescriptionuser.Model.PrescriptionModelList;
import com.erxprescriptionuser.R;
import com.erxprescriptionuser.Utils.CommonUtils;
import com.erxprescriptionuser.databinding.AdapterPastBinding;

import java.util.List;

import hari.bounceview.BounceView;

public class PastAdapter extends RecyclerView.Adapter<PastAdapter.MyViewHolder> {
    private Context context;
    private List<PrescriptionModelList.Data> list;
    public PastAdapter(Context context, List<PrescriptionModelList.Data> list)
    {
        this.context=context;
        this.list=list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        AdapterPastBinding binding=DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.adapter_past,parent,false);
        return new PastAdapter.MyViewHolder(binding);
    }


    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.binding.tvBookingId.setText(list.get(position).getPrescriptionNumber());
        holder.binding.tvDetails.setText(context.getString(R.string.booking_date)+" "+CommonUtils.getDate(list.get(position).getCreatedAt())+context.getString(R.string.delivery_date)+" "+CommonUtils.getDate(list.get(position).getUpdatedAt())+context.getString(R.string.type)+" "+list.get(position).getDeliveryType()+context.getString(R.string.status)+" " +list.get(position).getPrescriptionStatus() +context.getString(R.string.address)+" "+list.get(position).getAddress());
    }

    @Override
    public int getItemCount() {
        return list != null ? list.size(): 0;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        AdapterPastBinding binding;
        public MyViewHolder(@NonNull AdapterPastBinding itemView) {
            super(itemView.getRoot());
            binding=itemView;
            CommonUtils.showAnimation(binding.clMain);
            BounceView.addAnimTo(binding.clMain);
            binding.clMain.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId())
            {
                case R.id.clMain:
                Intent intent = new Intent(context, PrescriptionDetailActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("data",list.get(getAdapterPosition()));
                context.startActivity(intent);
                break;
            }
        }
    }
}
